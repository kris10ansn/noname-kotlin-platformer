package GameObjects.Level

import GameObjects.Enums.Type
import GameObjects.GameObject
import Main.game
import Other.Key_CTRL
import Other.Vector
import Window.Handler
import java.awt.Graphics
import java.awt.Rectangle
import java.awt.image.BufferedImage
import kotlin.math.roundToInt

class Portal(x:Float, y:Float, private var width:Int, private var height:Int, private val sprite: BufferedImage, override var layer:Int = 15, private var level:Int = -1, private val secret:Boolean = false, private val resetCameraPosition:Boolean = true) : GameObject() {
    override var pos = Vector(x,y)
    override var bounds = Rectangle(x.roundToInt(), y.roundToInt(), width, height)
    override var type = Type.Portal

    private var active = true

    override fun tick() {}

    override fun render(g: Graphics) { g.drawImage(sprite, x.roundToInt(), y.roundToInt(), width, height, game); if(Key_CTRL) g.drawRect(xx, yy, width, height) }

    fun use(handler:Handler) {
        when (level) {
            -1 -> {
                println("level: $level")
                handler.nextLevel(resetCameraPosition)
            }
            else -> {
                println("level: $level")
                handler.setLevel(level, resetCameraPosition)
            }
        }
        deactivate()
    }
    fun isActive() : Boolean = active
    private fun deactivate() { active = false }
}