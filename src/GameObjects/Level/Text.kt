package GameObjects.Level

import GameObjects.Enums.Type
import GameObjects.GameObject
import Other.Vector
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.Rectangle

class Text(x:Float, y:Float, font: String, fontSize: Int, private val text:String, private val color:Color = Color.white, override var layer: Int = 6, fontStyle:Int = Font.PLAIN) : GameObject() {
    override var type = Type.Decoration
    override var pos = Vector(x, y)
    override var bounds = Rectangle(0,0,0,0)

    private val font = Font(font, fontStyle, fontSize)

    override fun tick() {}

    override fun render(g: Graphics) {
        g.color = this.color
        g.font = this.font
        g.drawString(text, xx, yy)
    }
}