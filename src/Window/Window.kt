package Window

import java.awt.Dimension
import javax.swing.JFrame

class Window(width: Int, height: Int, title: String, game: Game) {
    init {
        val frame = JFrame()
        frame.title = title

        val size = Dimension(width, height)
        frame.preferredSize = size
        frame.minimumSize = size
        frame.maximumSize = size


        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setLocationRelativeTo(null)
        frame.isResizable = false
        // Setter vinduet til fullskjerm
        frame.extendedState = JFrame.MAXIMIZED_BOTH
        frame.isUndecorated = true
        //

        frame.isVisible = true

        frame.add(game)
        game.start()
    }
}