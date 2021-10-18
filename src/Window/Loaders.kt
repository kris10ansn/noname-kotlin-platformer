package Window

import GameObjects.Entities.Coin
import GameObjects.Entities.BasicEnemy
import GameObjects.Entities.Player
import GameObjects.Entities.SpikeEnemy
import GameObjects.Enums.Traits
import GameObjects.Enums.Type
import GameObjects.Level.*
import Main.game
import Other.Vector
import Sprites.Spritesheet
import java.awt.Color
import java.awt.Font
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Paths
import javax.imageio.ImageIO

// (Using window class to get to Window package)
fun loadImage(path:String) : BufferedImage {
    return ImageIO.read(ImageIO.createImageInputStream(File("${Paths.get("").toAbsolutePath()}/src/res/$path")))
}

fun loadLevel(image:BufferedImage, handler: Handler, tileSize:Float) {
    val spritesheet = Spritesheet(loadImage("Tiles/platformpack_spritesheet.png"), 64)
    val characterSheet = Spritesheet(loadImage("sprites/charactersheet.png"), 14)
    val enemySheet = Spritesheet(loadImage("sprites/enemysheet.png"), 15)
    val coinsheet = Spritesheet(loadImage("sprites/spritesheet_coin.png"), 96, 24)

    when (game.levelNo) {
        0 -> handler.addObject(Text(50f, 850f, "Arial", 50, "Use W, A, S, D to move.", Color.white, 4, Font.BOLD))
    }

    for(y in 0 until image.height) {
        for(x in 0 until image.width) {
            val pixel = image.getRGB(x, y)

            // Henter rød-, grønn- og blåverdien fra pikselen
            val r = pixel shr 16 and 0xff
            val g = pixel shr 8 and 0xff
            val b = pixel and 0xff
            //

            if(x == 0 && y == 0) {
                game.backgroundColor = Color(r, g, b)
            }

            val xx = x*tileSize
            val yy = y*tileSize
            val ts = tileSize.toInt()

            when {
                r in 1..10 -> handler.addObject(Tile(xx, yy, ts, spritesheet.grabImage(g / 20, b / 20), Type.Decoration, layer = r))
                r == 20 -> handler.addObject(Tile(xx, yy, ts, spritesheet.grabImage(g / 20, b / 20), Type.Tile, 4))
                r == 25 -> handler.addObject(Tile(xx, yy, ts, spritesheet.grabImage(g / 20, b / 20), Type.Tile, 4, arrayListOf(Traits.WallJump)))

                // Portal to normal level
                r in 40..69 -> handler.addObject(Portal(xx, yy, ts, ts, spritesheet.grabImage(g/20, b/20), 4, r - 41))

                r == 255 && g == 255 && b == 0 -> handler.addObject(Coin(xx, yy, coinsheet, handler, ts, ts))

                r == 200 && g == 255 && b in 0..4 -> {
                    handler.addObject(Door(xx, yy - tileSize, ts, ts * 2, spritesheet, handler, when (b) {
                        1 -> Door.COLOR_BLUE
                        2 -> Door.COLOR_YELLOW
                        3 -> Door.COLOR_GREEN
                        4 -> Door.COLOR_RED
                        else -> Door.COLOR_BLUE
                    }, {}, 4))
                }
                r in 100..140 && g in 100..140 && b == 255 -> {
                    val p = Player(xx, yy, handler, characterSheet, 60, 70)
                    handler.addObject(p)

                    p.velocity = Vector(r-120,g-120)

                    game.cam.pos = Vector(-p.pos.x + game.width/2, 0f)
                    game.cam.velocity.x = p.velocity.x

                }

                r == 150 -> {
                    handler.addObject(BasicEnemy(xx, yy, enemySheet, handler))
                }

                r == 155 -> {
                    handler.addObject(SpikeEnemy(xx, yy, enemySheet, handler))
                }

                r == 200 && g == 120 && b == 0 -> handler.addObject(MysteryBox(xx, yy, ts, { handler.addObject(Coin(xx, yy-ts, coinsheet, handler, ts, ts, 9f)) }, handler, spritesheet.grabImage(4, 2), spritesheet.grabImage(4,5), 4, arrayListOf(Traits.WallJump)))
                r == 210 && g == 120 && b == 0 -> handler.addObject(InvisibleMysteryBox(xx, yy, ts, { handler.addObject(Coin(xx, yy-ts, coinsheet, handler, ts, ts, 9f)) }, handler, spritesheet.grabImage(4,5), 4, arrayListOf(Traits.WallJump)))
            }
        }
    }
}