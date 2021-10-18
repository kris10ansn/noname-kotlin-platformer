package GameObjects.Entities

import GameObjects.Level.Door
import GameObjects.Enums.Traits
import GameObjects.Enums.Type
import GameObjects.Level.InvisibleMysteryBox
import GameObjects.Level.MysteryBox
import GameObjects.Level.Portal
import Main.game
import Other.*
import Window.*
import Other.Vector
import Sprites.Animation
import Sprites.Spritesheet
import java.awt.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToLong

class Player(x:Float, y:Float, handler:Handler, spritesheet: Spritesheet, width:Int, height:Int = width, override var layer:Int = 5) : Entity(width, height, handler) {
    enum class State { Idle, Running, Jumping, Falling, Dead }
    private var state = State.Idle

    private var speed = 1.5f
    private var friction = 0.8f
    private var dir = 1
    override var pos = Vector(x, y)

    override var type = Type.Player

    private var door = false

    private var jumping = false
    private var wallJumping = false

    override fun render(g: Graphics) {
        // Kjører de forskjellige animasjonene ut fra statusen
        when(state) {
            State.Running -> {
                if(dir == 1) g.drawImage(runAnimationRight.currentFrame(), xx, yy, width, height, game)
                        else g.drawImage(runAnimationLeft.currentFrame(),  xx, yy, width, height, game)
            }
            State.Idle -> {
                if(dir == 1) g.drawImage(idleAnimationRight.currentFrame(), xx, yy, width, height, game)
                        else g.drawImage(idleAnimationLeft.currentFrame(),  xx, yy, width, height, game)
            }
            State.Jumping -> {
                if(dir == 1) g.drawImage(jumpingAnimationRight.currentFrame(), xx, yy, width, height, game)
                        else g.drawImage(jumpingAnimationLeft.currentFrame(),  xx, yy, width, height, game)
            }

            State.Falling -> {
                if(dir == 1) g.drawImage(fallingAnimationRight.currentFrame(), xx, yy, width, height, game)
                        else g.drawImage(fallingAnimationLeft.currentFrame(),  xx, yy, width, height, game)
            }

            State.Dead -> {
                g.drawImage(dead, xx, yy, width+10, height+10, game)
            }
        }

        if(Key_CTRL) drawBounds(g)
    }

    override fun tick() {
        if(Math.abs(velocity.y) < 0.5)
            velocity.y = 0f

        physics(gravity)
        movement()
        pos.addVec(velocity)

        if(yy > game.width) handler.restartLevel()

        collision()

        runAnimationRight.tick()
        runAnimationLeft.tick()
        idleAnimationRight.tick()
        idleAnimationLeft.tick()

        velocity.x *= friction

        if(abs(velocity.x) <= 1) wallJumping = false

    }


    private fun physics(GRAVITY:Float) {
        velocity.y += GRAVITY
    }

    private fun movement() {
        if(state == State.Dead) return

        if(Key_LEFT && !wallJumping) {
            velocity.x -= speed
            state = State.Running
        }
        if(Key_RIGHT && !wallJumping) {
            velocity.x += speed
            state = State.Running
        }

        runAnimationRight.speed = Math.abs(velocity.x).roundToLong() * 25
        runAnimationLeft.speed = Math.abs(velocity.x).roundToLong() * 25

        if(velocity.xx == 0) {
            if(state != State.Idle) {
                state = State.Idle
                idleAnimationLeft.reset()
                idleAnimationRight.reset()
            }
        } else {
            dir = (velocity.x / abs(velocity.x)).toInt()
        }

        if(velocity.y < -1f) state = State.Jumping
        else if(velocity.y > 1) { state = State.Falling; wallJumping = false }
    }

    private fun die() {
        jump()
        state = State.Dead
    }

    override fun collision() {
        if(x < 0) {
            velocity.x = 0f
            x = 0f
            state = State.Idle
        }

        if(x + width > game.level.width * game.tileSize) {
            velocity.x = 0f
            x = game.level.width * game.tileSize - width
        }

        handler.objects.forEach {
            when(it.type) {
                Type.Coin -> if(it.bounds.intersects(this.bounds)) {
                    handler.removeObject(it)
                    ++game.coins
                }

                Type.Door -> door = if(it.bounds.intersects(this.bounds)) { (it as? Door)!!.open(); true } else { false }

                Type.Portal -> if(it.bounds.intersects(this.bounds) && (it as? Portal)!!.isActive()) { it.use(handler) }

                Type.BasicEnemy -> {
                    /* Just to ensure the player cant kill enemies when dead himself */ if(state == State.Dead) {} else
                    if((it as? BasicEnemy)!!.boundsRight.intersects(this.bounds) || it.boundsLeft.intersects(this.bounds)) {
                        this.die()
                    } else if(it.boundsTop.intersects(this.boundsBottom)) {
                        it.kill()
                        this.jump()
                    }
                }

                Type.SpikeEnemy -> {
                    /* Just to ensure the player cant kill enemies when dead himself */ if(state == State.Dead) {} else
                        if((it as? SpikeEnemy)!!.boundsTop.intersects(this.boundsBottom)) {
                            this.die()
                        } else if(it.boundsRight.intersects(this.boundsLeft) || it.boundsBottom.intersects(this.boundsTop) || it.boundsLeft.intersects(this.boundsRight)) {
                            this.die()
                        }
                }

                Type.Tile, Type.Player, Type.MysteryBox -> {
                    var canjump = false

                    if(handler.objects.indexOf(this) != handler.objects.indexOf(it) && state != State.Dead) {

                        if (it.bounds.intersects(this.boundsBottom)) {
                            velocity.y = 0f
                            y = it.y - height
                            jumping = false
                            canjump = true

                            wallJumping = false
                        }

                        if (it.bounds.intersects(this.boundsRight)) {
                            velocity.x = 0f
                            x = it.x - width
                            wallJumping = false

                            if ((Key_UP || Key_SPACE) && Key_RIGHT && jumping && it.traits.contains(Traits.WallJump))
                                wallJump(-1)
                        }
                        if (it.bounds.intersects(this.boundsLeft)) {
                            velocity.x = 0f
                            x = it.x + it.bounds.width
                            wallJumping = false

                            if ((Key_UP || Key_SPACE) && Key_LEFT && jumping && it.traits.contains(Traits.WallJump))
                                wallJump(1)
                        }

                        if (it.bounds.intersects(this.boundsTop)) {
                            velocity.y = 0f
                            y = it.y + it.bounds.height

                            wallJumping = false

                            try {
                                if (it.type == Type.MysteryBox && (it as? MysteryBox)!!.isActive()) {
                                    it.popup()
                                    it.deactivate()
                                }
                            } catch(e:NullPointerException) {
                                if (it.type == Type.MysteryBox && (it as? InvisibleMysteryBox)!!.isActive()) {
                                    it.popup()
                                    it.deactivate()
                                }
                            }
                        }

                        if (canjump && (Key_SPACE || Key_UP) && state != State.Dead && !door) jump()
                    }
                }

                else -> {}
            }
        }
    }

    private fun jump() { println("jump"); velocity.y = -16f; jumping = true; }
    private fun wallJump(dir:Int) { jump(); velocity.x = 20f * dir; wallJumping = true; }

    private var runAnimationRight = Animation(1000f, 0,
            arrayOf(
                    spritesheet.grabImage(0,1, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(1,1, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(2,1, 14, 14, 2, 1, 1)
            ))
    private var runAnimationLeft = Animation(1000f, 0,
            arrayOf(
                    spritesheet.grabImage(2,2, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(1,2, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(0,2, 14, 14, 2, 1, 1)
            ))

    private var idleAnimationRight = Animation(1500f, 0,
            arrayOf(
                    spritesheet.grabImage(0,0, 14, 14, 2, 2, 1),
                    spritesheet.grabImage(1,0, 14, 14, 2, 2, 1)
            ))

    private var idleAnimationLeft = Animation(1100f, 0,
            arrayOf(
                    spritesheet.grabImage(2,0, 14, 14, 2, 2, 1),
                    spritesheet.grabImage(3,0, 14, 14, 2, 2, 1)
            ))

    private var jumpingAnimationRight = Animation(1000f, 0,
            arrayOf(
                    spritesheet.grabImage(0,3, 14, 14, 1, 1, 0, 1)
            ))
    private var fallingAnimationRight = Animation(1000f, 0,
            arrayOf(
                    spritesheet.grabImage(1,3, 14, 14, 2, 1, 1)
            ))

    private var jumpingAnimationLeft = Animation(1000f, 0,
            arrayOf(
                    spritesheet.grabImage(3,3, 14, 14, 1, 1, 0, 1)
            ))

    private var fallingAnimationLeft = Animation(1000f, 0,
            arrayOf(
                    spritesheet.grabImage(2,3, 14, 14, 2, 1, 1)
            ))

    private var punchAnimationRight = Animation(100f, 0,
            arrayOf(
                    spritesheet.grabImage(2,6, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(1,6, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(0,6, 14, 14, 2, 1, 1)
            ))

    private var punchAnimationLeft = Animation(100f, 0,
            arrayOf(
                    spritesheet.grabImage(2,7, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(1,7, 14, 14, 2, 1, 1),
                    spritesheet.grabImage(0,7, 14, 14, 2, 1, 1)
            ))

    private var dead = spritesheet.grabImage(0, 4, 14, 15, 0, 1, 0, 1)
}
