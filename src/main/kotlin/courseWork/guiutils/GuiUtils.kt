package courseWork.guiutils

import javax.swing.JComponent

object GuiUtils {
    enum class Colors(val hex: Int) {
        GREY(0x878a9a),
        LIGHT_GREY(0xbfbfbf),
        BLUE(0x3366ff),
        GREEN(0x33cc33),
        RED(0xff0000),
        DARK_BLUE(0x000066),
        DARK_RED(0x800000),
        DARK_PURPLE(0x660033),
        PURPLE(0x990099),
        BLACK(0x000000),
        WHITE( 0xFFFFFF),
    }

    fun updateFont(component: JComponent, newFontSize: Float) {
        val font = component.font
        val derivedFont = font.deriveFont(newFontSize)
        component.font = derivedFont
    }
}