package laba2

import laba2.shape.*

fun main() {
    // Warning "Parameter 'rgbColor' is never used" can be ignored. We don't really need exact RGBA params of colors and only use their ENUMs
    // But theoretically we will need information of particular RGBA data, if we expand our program

    val shapeCollector = ShapeCollector()

    shapeCollector.addShape(Circle(5.0, Color.RED, Color.GREEN))
    shapeCollector.addShape(Circle(5.5, Color.RED, Color.GREEN))

    shapeCollector.addShape(Square(10.0, Color.RED, Color.GREEN))
    shapeCollector.addShape(Square(3.0, Color.RED, Color.BLUE))

    shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.GREEN))
    shapeCollector.addShape(Rectangle(5.0, 10.0, Color.WHITE, Color.BLACK))
    shapeCollector.addShape(Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN))

    shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

    try {
        shapeCollector.addShape(Square(-333.0, Color.RED, Color.BLUE))
    } catch (e: IllegalArgumentException) {
        println(e.message)
    }

    println("\nShapes with minimal area:")
    shapeCollector.findMinimumArea().forEach { println(it) }

    println("\nShapes with maximum area:")
    shapeCollector.findMaximumArea().forEach { println(it) }

    println("\nSummary area:")
    println(shapeCollector.findSummaryArea())

    println("\nShapes with border color RED:")
    shapeCollector.findShapesByBorderColor(Color.RED).forEach { println(it) }

    println("\nShapes with fill color GREEN:")
    shapeCollector.findShapesByFillColor(Color.GREEN).forEach { println(it) }

    println("\nAll shapes:")
    shapeCollector.getShapesList().forEach { println(it) }

    println("\nShapes count:")
    println(shapeCollector.getShapesList().size)

    println("\nShapes grouped by border color:")
    val sortedShapes1 = shapeCollector.sortShapesByBorderColor()
    for ((key, value) in sortedShapes1) {
        println("$key = $value")
    }

    println("\nShapes grouped by fill color:")
    val sortedShapes2 = shapeCollector.sortShapesByFillColor()
    for ((key, value) in sortedShapes2) {
        println("$key = $value")
    }

    println("\nAll squares:")
    println(shapeCollector.getShapesByType<Square>())
}