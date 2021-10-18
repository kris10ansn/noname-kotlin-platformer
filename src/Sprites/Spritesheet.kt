package Sprites

import java.awt.image.BufferedImage

class Spritesheet(private val image:BufferedImage, private val scale:Int = 32, private val spacingX:Int = 0, private val spacingY:Int = 0) {
    fun grabImage(col:Int, row:Int, width:Int = scale, height:Int = scale, paddingLeft:Int = 0, paddingRight:Int = 0, paddingTop:Int = 0, paddingBottom:Int = 0): BufferedImage {
        return if(spacingX + spacingY == 0) {
            val c = col + 1
            val r = row + 1
            image.getSubimage(c * scale - scale + paddingLeft, r * scale - scale + paddingTop, width - paddingLeft - paddingRight, height - paddingTop - paddingBottom)
        } else {
            image.getSubimage(col*(scale + spacingX), row*(scale + spacingY), width, height)
        }
    }

    fun grab(x:Int, y:Int, width:Int, height:Int) : BufferedImage {
        return image.getSubimage(x,y,width,height)
    }
}