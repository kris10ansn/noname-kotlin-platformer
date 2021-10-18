package GameObjects.Entities

import GameObjects.Enums.Type
import Main.game
import Other.*
import Sprites.Animation
import Sprites.Spritesheet
import java.awt.Graphics
import Window.Handler

class Coin(x:Float, y:Float, spritesheet: Spritesheet, handler:Handler, width:Int, height:Int = width, jump:Float = 0f, override var layer:Int = 4) : Entity(width, height, handler) {
    override var pos = Vector(x,y)
    override var type = Type.Coin

    private val animation = Animation(80f, 1, arrayOf(
            spritesheet.grabImage(0,0),
            spritesheet.grabImage(1,0),
            spritesheet.grabImage(2,0),
            spritesheet.grabImage(3,0),
            spritesheet.grabImage(4,0),
            spritesheet.grabImage(5,0)
            ))

    init {
        velocity.y = -jump
    }

    override fun tick() {
        animation.tick()

        velocity.y += gravity
        pos.addVec(velocity)

        collision()
    }

    override fun render(g: Graphics) {
        g.drawImage(animation.currentFrame(), xx, yy, width, height, game)
        if(Key_CTRL) drawBounds(g)
    }

}