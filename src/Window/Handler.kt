package Window

import GameObjects.GameObject
import Main.game
import Other.Vector
import java.awt.Graphics
import kotlin.collections.ArrayList


class Handler {
    var objects = ArrayList<GameObject>()
    private var foregroundObjects = ArrayList<GameObject>()

    private var objectsToRemove = ArrayList<GameObject>()
    private var objectsToAdd = ArrayList<GameObject>()
    private var foregroundObjectsToRemove = ArrayList<GameObject>()
    private var foregroundObjectsToAdd = ArrayList<GameObject>()

    private var functionsToRun = ArrayList<()->Unit> ()

    fun tick() {
        objects.forEach { it.tick() }

        functionsToRun.forEach { it(); }
        functionsToRun.clear()

        objects.removeAll(objectsToRemove)
        objects.addAll(objectsToAdd)
        foregroundObjects.removeAll(foregroundObjectsToRemove)
        foregroundObjects.addAll(foregroundObjectsToAdd)

        objectsToRemove.clear()
        objectsToAdd.clear()
        foregroundObjectsToRemove.clear()
        foregroundObjectsToAdd.clear()

        sortObjects()
    }

    fun render(g: Graphics) {
        objects.forEach { it.render(g) }
        foregroundObjects.forEach { it.render(g) }
    }

    fun restart() {
        game = Game("Kotlin Game Template")
    }

    fun restartLevel() {
        setLevel(game.levelNo)
    }

    private fun reset() {
        objects.clear()
        foregroundObjects.clear()
    }

    private fun reset(level:Int) {
        if(game.levelNo == level) reset()
    }

    fun setLevel(n:Int, resetCameraPosition:Boolean = true) {
        val set = {
            reset()
            game.setLevel(n)

            if(resetCameraPosition) {
                game.cam.pos = Vector(0,0)
            }
        }

        if(!functionsToRun.contains(set)) functionsToRun.add(set)
    }

    fun nextLevel(resetCameraPosition: Boolean) {
        if(!functionsToRun.isEmpty()) return
        else setLevel(game.levelNo+1, resetCameraPosition)
    }

    fun addObject(obj:GameObject) {
        objectsToAdd.add(obj)
    }

    fun removeObject(obj:GameObject) {
        objectsToRemove.add(obj)
    }

    fun addToForegroundObjects(obj:GameObject) {
        foregroundObjectsToAdd.add(obj)
    }

    fun removeFromForegroundObjects(obj:GameObject) {
        foregroundObjectsToRemove.add(obj)
    }

    private fun sortObjects() {
        objects.sortWith(compareBy( { it.layer } ))
    }
}