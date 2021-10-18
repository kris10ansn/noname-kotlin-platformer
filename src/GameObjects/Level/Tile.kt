package GameObjects.Level

import GameObjects.Enums.Traits
import GameObjects.Enums.Type
import GameObjects.GameObject
import Main.game
import Other.Key_CTRL
import Other.Vector
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

open class Tile(x:Float, y:Float, var size:Int = 32, protected var sprite:BufferedImage, override var type:Type = Type.Tile, override var layer:Int = 1, traits:ArrayList<Traits> = arrayListOf()) : GameObject() {
    init { this.traits = traits }

    override var pos = Vector(x, y)

    override var bounds = Rectangle(x.roundToInt(), y.roundToInt(), size, size)

    override fun tick() {}

    override fun render(g: Graphics) {
        g.drawImage(sprite, x.roundToInt(), y.roundToInt(), size, size, game)
        if(Key_CTRL) drawBounds(g)
    }

    protected fun drawBounds(g:Graphics) {
        g.color = Color.blue
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height)
    }
}