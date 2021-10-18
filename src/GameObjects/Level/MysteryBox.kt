package GameObjects.Level

import GameObjects.Enums.Traits
import GameObjects.Enums.Type
import Main.game
import Other.Key_CTRL
import Window.Handler
import java.awt.Graphics
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class MysteryBox(x:Float, y:Float, size:Int, val popup:() -> Unit, handler: Handler, sprite:BufferedImage, private val used:BufferedImage, layer:Int, traits:ArrayList<Traits> = arrayListOf()) : Tile(x, y, size, sprite, Type.MysteryBox, layer, traits) {
    private var active = true

    override fun render(g: Graphics) {
        g.drawImage(if(active) sprite else used, x.roundToInt(), y.roundToInt(), size, size, game)
        if(Key_CTRL) drawBounds(g)
    }

    fun isActive() : Boolean = active
    fun deactivate() { active = false }
}