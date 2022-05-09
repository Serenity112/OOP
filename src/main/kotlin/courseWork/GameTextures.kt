package courseWork

import java.awt.Image
import javax.imageio.ImageIO

object GameTextures {
    class Textures(
        val MineTex: Image,
        val FieldTex: Image,
        val MarkTex: Image,
        val EmptyTex: Image,
        val logoTex: Image,
        val arrowLeft: Image,
        val arrowRight: Image,
        val bottomLeft: Image,
        val bottomRight: Image,
        val goButton: Image,
        val settingsButton: Image,
    )

    private fun loadTextures(): Textures {
        return Textures(

            ImageIO.read(javaClass.getResource("/images/mine.png")),
            ImageIO.read(javaClass.getResource("/images/field.png")),
            ImageIO.read(javaClass.getResource("/images/flag.png")),
            ImageIO.read(javaClass.getResource("/images/empty.png")),

            ImageIO.read(javaClass.getResource("/images/logo.png")),
            ImageIO.read(javaClass.getResource("/images/arrowleft.png")),
            ImageIO.read(javaClass.getResource("/images/arrowright.png")),
            ImageIO.read(javaClass.getResource("/images/bottomleft.png")),
            ImageIO.read(javaClass.getResource("/images/bottomright.png")),

            ImageIO.read(javaClass.getResource("/images/gobutton.png")),
            ImageIO.read(javaClass.getResource("/images/button.png")), // 250:50
        )
    }

    val textures = loadTextures()
}