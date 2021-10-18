package Window

import GameObjects.GameObject
import Main.game
import Other.Vector

class Camera(x:Float, y: Float) {
    var pos = Vector(x, y)
    var velocity = Vector(-1, 0)

    fun tick(player: GameObject) {
        val goal = -player.pos.x + game.width/2

        velocity.x = -(pos.x - goal)/7
        velocity.x *= 0.5f

        pos.addVec(velocity)
        if(pos.x > 0) pos.x = 0f
        if(pos.x - game.width < -(game.level.width * game.tileSize)) pos.x = -(game.level.width * game.tileSize) + game.width
    }
}