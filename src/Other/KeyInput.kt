package Other

import Window.Handler
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

var Key_UP = false
var Key_LEFT = false
var Key_DOWN = false
var Key_RIGHT = false
var Key_SPACE = false
var Key_CTRL = false

class KeyInput(private var handler:Handler) : KeyAdapter() {
    override fun keyPressed(e:KeyEvent) {
        val key = e.keyCode

        when (key) {
            KeyEvent.VK_W, KeyEvent.VK_UP -> Key_UP = true
            KeyEvent.VK_A, KeyEvent.VK_LEFT -> Key_LEFT = true
            KeyEvent.VK_S, KeyEvent.VK_DOWN -> Key_DOWN = true
            KeyEvent.VK_D, KeyEvent.VK_RIGHT -> Key_RIGHT = true
            KeyEvent.VK_SPACE -> Key_SPACE = true
            KeyEvent.VK_ESCAPE -> System.exit(0)
            KeyEvent.VK_CONTROL -> Key_CTRL = true
        }
    }

    override fun keyReleased(e:KeyEvent) {
        val key = e.keyCode

        when (key) {
            KeyEvent.VK_W, KeyEvent.VK_UP -> Key_UP = false
            KeyEvent.VK_A, KeyEvent.VK_LEFT -> Key_LEFT = false
            KeyEvent.VK_S, KeyEvent.VK_DOWN -> Key_DOWN = false
            KeyEvent.VK_D, KeyEvent.VK_RIGHT -> Key_RIGHT = false
            KeyEvent.VK_SPACE -> Key_SPACE = false
            KeyEvent.VK_CONTROL -> Key_CTRL = false
        }
    }
}