package GameObjects

import GameObjects.Enums.Traits
import GameObjects.Enums.Type
import java.awt.Graphics
import Other.Vector
import java.awt.Rectangle
import kotlin.math.roundToInt

// Abstrakt klasse GameObject - Betyr at den kun kan bli brukt om du EXTENDER den.
// Du kan ikke bare lage et GameObject(x, y, type), men du kan lage en Player(x,y,type),
// siden den EXTENDER GameObject
abstract class GameObject {
    abstract var pos:Vector
    var velocity = Vector(0, 0)

    var traits = ArrayList<Traits>()

    abstract var layer:Int

    abstract var bounds:Rectangle

    abstract var type: Type

    var x = 0f
        get() = pos.x
        set(value) { field = value; pos.x = value }

    var y = 0f
        get() = pos.y
        set(value) { field = value; pos.y = value }

    var xx = 0
        get() = pos.x.roundToInt()
    var yy = 0
        get() = pos.y.roundToInt()

    abstract fun tick()
    abstract fun render(g: Graphics)
}