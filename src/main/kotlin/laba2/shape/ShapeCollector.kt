package laba2.shape

class ShapeCollector<T : ColoredShape2d>(
    private val shapesList: MutableList<T> = mutableListOf()
) {

    // Took from this:
    // https://www.bezkoder.com/kotlin-comparator-example/
    class AreaComparator {
        companion object : Comparator<ColoredShape2d> {
            override fun compare(a: ColoredShape2d, b: ColoredShape2d): Int = when {
                a.calcArea() > b.calcArea() -> 1
                a.calcArea() < b.calcArea() -> -1
                else -> 0
            }
        }
    }

    fun addCollection(collection: Collection<T>) {
        shapesList.addAll(collection)
    }

    fun getSorted(comparator: Comparator<in T>): List<T> {
        return shapesList.sortedWith(comparator).toMutableList()
    }

    fun getShapesList(): List<T> {
        return shapesList
    }

    fun addShape(shape: T) {
        shapesList.add(shape)
    }

    fun findMinimumArea(): List<T> {
        val minimumArea = shapesList.minOfOrNull { it.calcArea() }

        return shapesList.filter { it.calcArea() == minimumArea }
    }

    fun findMaximumArea(): List<T> {
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

    fun findShapesByBorderColor(color: Color): List<T> {
        return shapesList.filter { it.borderColor == color }
    }

    fun findShapesByFillColor(color: Color): List<T> {
        return shapesList.filter { it.fillColor == color }
    }

    // Source: https://stackoverflow.com/questions/68526191/how-to-group-and-merge-list-in-kotlin
    fun sortShapesByBorderColor(): Map<Color, List<T>> {
        return shapesList.groupBy { it.borderColor }
    }

    fun sortShapesByFillColor(): Map<Color, List<T>> {
        return shapesList.groupBy { it.fillColor }
    }

    inline fun <reified T : ColoredShape2d> getShapesByType(): List<T> {
        return this.getShapesList().filterIsInstance<T>()
    }
}