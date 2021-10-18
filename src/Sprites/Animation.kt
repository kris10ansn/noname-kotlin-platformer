package Sprites

import java.awt.image.BufferedImage

class Animation(private var delay:Float, var speed:Long, private var frames: Array<BufferedImage>) {
    private var index = 0
    private var timer:Long = 0
    private var lastTime = System.currentTimeMillis()

    fun tick() {
        timer += System.currentTimeMillis() - lastTime + speed
        lastTime = System.currentTimeMillis()

        if(timer >= delay) {
            index++
            timer = 0
            if(index >= frames.size) index = 0
        }
    }

    fun reset() {
        index = 0
        timer = 0
        lastTime = System.currentTimeMillis()
    }

    fun currentFrame() : BufferedImage = frames[index]
}