package Other

import kotlin.math.pow

class Vector {

    var x:Float
    var y:Float

    var xx = 0
        get() = x.toInt()
    var yy = 0
        get() = y.toInt()

    constructor(x:Int, y:Int) {
        this.x = x.toFloat()
        this.y = y.toFloat()
    }

    constructor(x:Float, y:Float) {
        this.x = x
        this.y = y
    }

    fun dist(vector:Vector) : Float = Math.sqrt(( (x-vector.x).pow(2) + (y-vector.y).pow(2) ).toDouble()).toFloat()

    fun add(vector:Vector) : Vector { return Vector(this.x+vector.x, this.y+vector.y); }
    fun add(num:Float) : Vector { return this.add(Vector(num, num)) }

    fun sub(vector:Vector) : Vector { return Vector(this.x-vector.x, this.y-vector.y); }
    fun sub(num:Float) : Vector { return this.sub(Vector(num, num)) }

    fun mult(vector: Vector) : Vector { return Vector(this.x*vector.x, this.y*vector.y); }
    fun mult(num:Float) : Vector { return this.mult(Vector(num, num)) }

    fun div(vector: Vector) : Vector { return Vector(this.x/vector.x, this.y/vector.y); }
    fun div(num:Float) : Vector { return this.div(Vector(num, num)) }

    fun addVec(vector: Vector) {
        val added = this.add(vector)
        x = added.x
        y = added.y
    }
    fun addVec(num:Float) { addVec(Vector(num, num)) }

    fun subVec(vector: Vector) {
        val subbed = this.sub(vector)
        x = subbed.x
        y = subbed.y
    }
    fun subVec(num:Float) { subVec(Vector(num, num)) }

    fun multBy(vector: Vector) {
        val multed = this.mult(vector)
        x = multed.x
        y = multed.y
    }
    fun multBy(num:Float) { multBy(Vector(num, num)) }

    fun divBy(vector: Vector) {
        val divided = this.div(vector)
        x = divided.x
        y = divided.y
    }
    fun divBy(num:Float) { divBy(Vector(num, num)) }
}