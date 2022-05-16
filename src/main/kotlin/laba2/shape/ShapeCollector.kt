package laba2.shape

class ShapeCollector {
    private val shapesList = mutableListOf<ColoredShape2d>()

    fun getShapesList(): List<ColoredShape2d> {
        return shapesList
    }

    fun addShape(shape: ColoredShape2d) {
        shapesList.add(shape)
    }

    fun findMinimumArea(): List<ColoredShape2d> {
        val minimumArea = shapesList.minOfOrNull { it.calcArea() }

        return shapesList.filter { it.calcArea() == minimumArea }
    }

    fun findMaximumArea(): List<ColoredShape2d> {
        val maximumArea = shapesList.maxOfOrNull { it.calcArea() }

        return shapesList.filter { it.calcArea() == maximumArea }
    }

    fun findSummaryArea(): Double {
        var sum = 0.0

        shapesList.forEach {
            sum += it.calcArea()
        }

        return sum
    }

    fun findShapesByBorderColor(color: Color): List<ColoredShape2d> {
        return shapesList.filter { it.borderColor == color }
    }

    fun findShapesByFillColor(color: Color): List<ColoredShape2d> {
        return shapesList.filter { it.fillColor == color }
    }

    // Source: https://stackoverflow.com/questions/68526191/how-to-group-and-merge-list-in-kotlin
    fun sortShapesByBorderColor(): Map<Color, List<ColoredShape2d>> {
        return shapesList.groupBy { it.borderColor }
    }

    fun sortShapesByFillColor(): Map<Color, List<ColoredShape2d>> {
        return shapesList.groupBy { it.fillColor }
    }

    // Source: https://stackoverflow.com/questions/13154463/how-can-i-check-for-generic-type-in-kotlin
    inline fun <reified T> getShapesByType(): List<ColoredShape2d> {
        return getShapesList().filter { it is T }
    }
}