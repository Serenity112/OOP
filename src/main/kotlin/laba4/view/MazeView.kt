package laba4.view
import laba4.model.MazeModel
import laba4.model.ModelChangeListener

class MazeView(private val model: MazeModel) {
    init {
        val listener = object : ModelChangeListener {
            override fun onModelChanged() {
                repaint()
            }
        }
        model.addModelChangeListener(listener)

        repaint()
    }

    private fun repaint() {
       // Runtime.getRuntime().exec("clear")
        println("Maze:")
        println(model)
    }
}