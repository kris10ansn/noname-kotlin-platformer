package Window

import Other.*
import GameObjects.Enums.Type
import Main.game
import java.awt.*
import java.awt.image.BufferedImage

class Game(title:String, var levelNo:Int = 0) : Canvas(), Runnable {

    // Vinduets bredde og høyde
    private val HEIGHT:Int = 1920
    private val WIDTH:Int = (HEIGHT.toFloat()*1.778).toInt()
    //

    var backgroundColor = Color(0x6bbaff)

    val tileSize = 48f

    // Thread (Kode som kjører for seg selv, separat fra annen kode)
    private lateinit var thread:Thread
    //

    lateinit var level:BufferedImage

    private lateinit var handler:Handler
    lateinit var cam:Camera

    var coins = 0

    private var running:Boolean = false

    private var fps = 60.0

    // Init kjøres når en forekomst av denne klassen blir lagd
    init { Window(WIDTH, HEIGHT, title, this) }

    // Funksjonen init er en funksjon som blir kalt på lengre nede (litt forvirrende med to ting kalt init, men ja)
    private fun init() {
        this.background = Color(107, 186, 255)

        handler = Handler()

        this.addKeyListener(KeyInput(handler))

        cam = Camera(0f, 0f)

        setLevel(levelNo)
    }

    fun setLevel(n:Int) {
        levelNo = n
        level = loadImage("levels/level_$n.png")
        loadLevel(level, handler, tileSize)
    }


    // Funksjon til all rendering
    private fun render() {
        // Lager buffer(e)
        val bs = this.bufferStrategy
        if(bs == null) { this.createBufferStrategy(3); return }
        //

        //////// Draw ////////

        val g: Graphics = bs.drawGraphics

        // For translate funksjonen
        val g2d = g as Graphics2D

        // Tegner alt som om kameraets x og y var 0, 0 (hvis du har noe på 5, 7 tegnes det på cam.x+5, cam.y+7)
        g2d.translate(cam.pos.x.toInt(), cam.pos.y.toInt())

        // Bakgrunn
        g.color = this.backgroundColor
        g.fillRect(0, 0, Math.abs(cam.pos.x.toInt()) + width, Math.abs(cam.pos.y.toInt()) + height)
        //

        handler.render(g)

        g.font = Font("SansSerif", Font.BOLD, 100)

        g.color = Color.white
        g.drawString(coins.toString(), game.width/2 - 25 - game.cam.pos.xx, 100)

        g.font = Font("SansSerif", Font.PLAIN, 30)
        g.color = Color.green
        g.drawString(fps.toString(), -game.cam.pos.xx + 5, 30)


        g.dispose()
        bs.show()

        //////////////////////
}

    private fun tick() {
        handler.objects.forEach { if(it.type == Type.Player) cam.tick(it); }
        handler.tick()
    }

    @Synchronized
    // Start funksjon som starter spillet (WOW)
    fun start() {
        // Initialiserer thread
        thread = Thread(this)
        // Starter thread
        thread.start()
        running = true
    }

    @Synchronized
    // Stop funksjon som STOPPER spillet (WHAT?)
    fun stop() {
        // Try/catch = Prøv / Hvis du feiler:
        try {
            thread.join()
            running = false
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        init()
        requestFocus()

        this.fps = 60.0
        val ns = 1000000000/fps
        var lastTime:Long = System.nanoTime()
        var delta= 0.0
        var timer:Long = System.currentTimeMillis()
        var frames = 0

        // Spillets hoved-loop
        while(running) {
            val now:Long = System.nanoTime()
            delta += (now-lastTime) / ns
            lastTime = now

            while(delta >= 1) {
                tick()
                delta--
            }

            if(running)
                render()

            frames++

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000
                fps = frames.toDouble()
                println("FPS: $fps")

                frames = 0
            }
        }
    }

}
