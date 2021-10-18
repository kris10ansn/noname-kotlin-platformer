package GameObjects.Entities

import GameObjects.Enums.Type
import GameObjects.GameObject
import Main.game
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import Window.Handler

abstract class Entity(protected val width:Int, protected val height:Int, protected val handler:Handler) : GameObject() {

    var gravity = 0.7f

    open fun collision() {
        if(x < 0) {
            velocity.x = 0f
            x = 0f
            collideBorderLeft()
        }

        if(x + width > game.level.width * game.tileSize) {
            x = game.level.width * game.tileSize - width
            collideBorderRight()
        }

        handler.objects.forEach {
            when(it.type) {
                Type.Tile, Type.MysteryBox -> {
                    if(handler.objects.indexOf(this) != handler.objects.indexOf(it)) {

                        if (it.bounds.intersects(this.boundsBottom)) {
                            collisionBottom(it)
                        }

                        if (it.bounds.intersects(this.boundsRight)) {
                            collisionRight(it)
                        }
                        if (it.bounds.intersects(this.boundsLeft)) {
                            collisionLeft(it)
                        }

                        if (it.bounds.intersects(this.boundsTop)) {
                            collisionTop(it)
                        }
                    }
                }
                else -> {}
            }
        }
    }

    open fun collideBorderRight() {}
    open fun collideBorderLeft() {}

    open fun collisionRight(it:GameObject) {
        velocity.x = 0f
        x = it.x - width
    }

    open fun collisionLeft(it:GameObject) {
        velocity.x = 0f
        x = it.x + it.bounds.width
    }

    open fun collisionTop(it:GameObject) {
        velocity.y = 0f
        y = it.y + it.bounds.height
    }
    open fun collisionBottom(it:GameObject) {
        velocity.y = 0f
        y = it.y - height
    }

    protected fun drawBounds(g: Graphics) {
        g.color = Color(255, 0, 255, 255/2)
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height)

        g.color = Color(255, 0, 0, (255/4)*3)
        g.fillRect(boundsTop.x, boundsTop.y, boundsTop.width, boundsTop.height)

        g.color = Color(0, 255, 0, (255/4)*3)
        g.fillRect(boundsLeft.x, boundsLeft.y, boundsLeft.width, boundsLeft.height)

        g.color = Color(0, 0, 255, (255/4)*3)
        g.fillRect(boundsBottom.x, boundsBottom.y, boundsBottom.width, boundsBottom.height)

        g.color = Color(255,255,0, (255/4)*3)
        g.fillRect(boundsRight.x, boundsRight.y, boundsRight.width, boundsRight.height)
    }

    override var bounds = Rectangle()
    get() = Rectangle(xx, yy, width, height)

    open var boundsTop = Rectangle()
        get() = Rectangle(xx + width/5, yy, width - (width/5)*2, height/6)

    open var boundsLeft = Rectangle()
        get() = Rectangle(xx, yy + height/6, width/5, height - height/3)

    open var boundsBottom = Rectangle()
        get() = Rectangle(xx + width/5, yy + height - height/6, width - (width/5)*2, height/6)

    open var boundsRight = Rectangle()
        get() = Rectangle(xx + width-width/5, yy + height/6, width/5, height - height/3)
}