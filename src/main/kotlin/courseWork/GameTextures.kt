package courseWork

import java.awt.Image
import javax.imageio.ImageIO
import javax.swing.ImageIcon

object GameTextures {
    class Textures(
        val MineTex: Image,
        val FieldTex: Image,
        val MarkTex: Image,
        val EmptyWaterTex: Image,
        val crossTex: Image,

        val restart: Image,
        val logoTex: Image,
        val arrowLeft: Image,
        val arrowRight: Image,
        val bottomLeft: Image,
        val bottomRight: Image,
        val goButton: Image,
        val backButton: Image,
    )

    private fun loadTextures(): Textures {
        return Textures(

            ImageIcon("src/main/resources/images/mine.png").image,
            ImageIcon("src/main/resources/images/field.png").image,
            ImageIcon("src/main/resources/images/flag.png").image,
            ImageIcon("src/main/resources/images/water.png").image,
            ImageIcon("src/main/resources/images/cross.png").image,

            ImageIcon("src/main/resources/images/restart.png").image,
            ImageIcon("src/main/resources/images/logo.png").image,
            ImageIcon("src/main/resources/images/arrowleft.png").image,
            ImageIcon("src/main/resources/images/arrowright.png").image,
            ImageIcon("src/main/resources/images/bottomleft.png").image,
            ImageIcon("src/main/resources/images/bottomright.png").image,

            ImageIcon("src/main/resources/images/gobutton.png").image,
            ImageIcon("src/main/resources/images/backbutton.png").image,
        )
    }

    val textures = loadTextures()
}