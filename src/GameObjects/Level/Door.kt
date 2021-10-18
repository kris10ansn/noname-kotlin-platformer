package GameObjects.Level

import GameObjects.Enums.Type
import GameObjects.GameObject
import Main.game
import Other.Vector
import Sprites.Spritesheet
import Window.Handler
import java.awt.Graphics
import java.awt.Rectangle
import kotlin.math.roundToInt

class Door(x:Float, y:Float, private var width:Int, private var height:Int, spritesheet: Spritesheet, private val handler: Handler, private val color:Int, private val opn:() -> Unit = {}, override var layer:Int = 15) : GameObject() {
    companion object {
        const val COLOR_BLUE = 6
        const val COLOR_YELLOW = 7
        const val COLOR_GREEN = 8
        const val COLOR_RED = 9
    }

    override var pos = Vector(x, y)
    override var type = Type.Door

    private enum class State { Open, Closed }
    private var state = State.Closed

    private val closed = spritesheet.grabImage(color, 5, 64, 128)
    private val opened = spritesheet.grabImage(5, 5, 64, 128)

    override var bounds = Rectangle(x.roundToInt(), y.roundToInt(), width, height)

    private var imageAdded = false

    override fun render(g: Graphics) {
        when (state) {
            State.Open -> {
                g.drawImage(opened, x.roundToInt(), y.roundToInt(), width, height, game)

                val xx = x + width/2
                val yy = y

                if(!imageAdded) {
                    handler.addToForegroundObjects(
                            object : GameObject() {
                                private val sprite = Spritesheet(opened, opened.width / 2).grabImage(1, 0, opened.width / 2, opened.height)
                                override var pos: Vector = Vector(xx, yy)
                                override var layer: Int = 10
                                override var bounds: Rectangle = Rectangle(0, 0, 0, 0);
                                override var type: Type = Type.Decoration
                                override fun tick() {}
                                override fun render(g: Graphics) {
                                    g.drawImage(sprite, x.roundToInt(), y.roundToInt(), width / 2, height, game)
                                }
                            }
                    )
                    imageAdded = true
                }
            }
            State.Closed -> g.drawImage(closed, x.roundToInt(), y.roundToInt(), width, height, game)
        }
    }
    override fun tick() {}

    fun open() {
        state = State.Open
        handler.objects.forEach {
            if(it.type == Type.House) it.type = Type.Decoration
        }
        opn()
    }

    fun isOpen() : Boolean = (state == State.Open)
}