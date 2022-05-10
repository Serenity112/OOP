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
        println("Maze:")
        println(model)
    }
}