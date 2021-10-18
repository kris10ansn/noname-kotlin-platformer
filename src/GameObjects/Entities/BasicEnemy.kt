package GameObjects.Entities

import GameObjects.Enums.Type
import GameObjects.GameObject
import Main.game
import Other.Key_CTRL
import Other.Vector
import Sprites.Animation
import Sprites.Spritesheet
import Window.Handler
import java.awt.Graphics
import java.awt.Rectangle
import kotlin.math.abs
import java.util.concurrent.TimeUnit
import java.util.concurrent.ScheduledThreadPoolExecutor



class BasicEnemy(x:Float, y:Float, spritesheet:Spritesheet, handler: Handler, override var layer:Int = 4) : Entity(79, 65, handler) {
    override var type: Type = Type.BasicEnemy
    override var pos = Vector(x, y)

    private val spriteWidth = 16
    private val spriteHeight = 13
    private val spritePaddingTop = 3

    enum class State { Running, Dead }

    private var state = State.Running

    override var boundsTop = Rectangle()
        get() = Rectangle(xx, yy, width, height/2)
    override var boundsLeft = Rectangle()
        get() = Rectangle(xx, yy + height/2, width/5, height/2 - height/3)
    override var boundsRight = Rectangle()
        get() = Rectangle(xx + width-width/5, yy + height/2, width/5, height/2 - height/3)


    init {
        velocity.x = 2f
    }

    override fun tick() {
        walkAnimationRight.tick()
        walkAnimationLeft.tick()

        velocity.y += gravity
        pos.addVec(velocity)

        collision()
    }

    override fun render(g: Graphics) {
        when {
            state == State.Running && abs(velocity.x) == velocity.x -> g.drawImage(walkAnimationRight.currentFrame(), xx, yy, width, height, game)
            state == State.Running && abs(velocity.x) != velocity.x -> g.drawImage(walkAnimationLeft.currentFrame(), xx, yy, width, height, game)
            state == State.Dead -> g.drawImage(deadAnimation.currentFrame(), xx, yy, width, height, game)
        }
        if(Key_CTRL) drawBounds(g)
    }

    override fun collisionRight(it: GameObject) {
        velocity.x = abs(velocity.x)*-1
        x = it.x-width
    }

    override fun collideBorderRight() {
        velocity.x = abs(velocity.x)*-1
    }

    override fun collisionLeft(it: GameObject) {
        velocity.x = abs(velocity.x)
        x = it.x + it.bounds.width
    }

    override fun collideBorderLeft() {
        velocity.x = abs(velocity.x)
    }

    fun kill() {
        state = State.Dead
        velocity.x = 0f

        type = Type.Decoration

        val exec = ScheduledThreadPoolExecutor(1)
        exec.schedule({ handler.removeObject(this) }, 500, TimeUnit.MILLISECONDS)
    }

    private var walkAnimationLeft = Animation(175f, 0,
            arrayOf(
                    spritesheet.grab(spriteWidth*1, spritePaddingTop, spriteWidth, spriteHeight),
                    spritesheet.grab(spriteWidth*2, spritePaddingTop, spriteWidth, spriteHeight),
                    spritesheet.grab(spriteWidth*3, spritePaddingTop, spriteWidth, spriteHeight)
                    ))
    private var walkAnimationRight = Animation(175f, 0,
            arrayOf(
                    spritesheet.grab(spriteWidth*3, spriteHeight + spritePaddingTop*2, spriteWidth, spriteHeight),
                    spritesheet.grab(spriteWidth*2, spriteHeight + spritePaddingTop*2, spriteWidth, spriteHeight),
                    spritesheet.grab(spriteWidth*1, spriteHeight + spritePaddingTop*2, spriteWidth, spriteHeight)
                    ))

    private var deadAnimation = Animation(1000f, 0, arrayOf( spritesheet.grab(0, spriteHeight*2 + spritePaddingTop*3, spriteWidth, spriteHeight) ))
}