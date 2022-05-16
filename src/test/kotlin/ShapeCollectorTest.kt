import laba2.shape.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ShapeCollectorTest {
    @Test
    fun addShape() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        assertTrue(shapeCollector.getShapesList().isEmpty())

        shapeCollector.addShape(Circle(5.0, Color.RED, Color.GREEN))

        assertFalse(shapeCollector.getShapesList().isEmpty())

        assertEquals(Circle(5.0, Color.RED, Color.GREEN), shapeCollector.getShapesList()[0])
    }

    @Test
    fun findMinimumArea() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Circle(5.0, Color.RED, Color.GREEN))
        shapeCollector.addShape(Circle(5.5, Color.RED, Color.GREEN))
        shapeCollector.addShape(Square(10.0, Color.RED, Color.GREEN))
        shapeCollector.addShape(Square(3.0, Color.RED, Color.BLUE))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.GREEN))
        shapeCollector.addShape(Rectangle(5.0, 10.0, Color.WHITE, Color.BLACK))
        shapeCollector.addShape(Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN))
        shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

        val foundShapesList = shapeCollector.findMinimumArea()

        val actualFoundShapesList = listOf(
            Rectangle(2.0, 3.0, Color.RED, Color.GREEN),
            Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN)
        )

        assertEquals(actualFoundShapesList, foundShapesList)
    }

    @Test
    fun findMaximumArea() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Circle(5.0, Color.RED, Color.GREEN))
        shapeCollector.addShape(Circle(5.5, Color.RED, Color.GREEN))
        shapeCollector.addShape(Square(10.0, Color.RED, Color.GREEN))
        shapeCollector.addShape(Square(3.0, Color.RED, Color.BLUE))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.GREEN))
        shapeCollector.addShape(Rectangle(5.0, 10.0, Color.WHITE, Color.BLACK))
        shapeCollector.addShape(Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN))
        shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

        val foundShapesList = shapeCollector.findMaximumArea()

        val actualFoundShapesList = listOf(
            Square(10.0, Color.RED, Color.GREEN),
            Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN)
        )

        assertEquals(actualFoundShapesList, foundShapesList)
    }

    @Test
    fun findSummaryArea() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Square(10.0, Color.RED, Color.GREEN))
        shapeCollector.addShape(Square(3.0, Color.RED, Color.BLUE))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.GREEN))

        assertEquals(115.0, shapeCollector.findSummaryArea())
    }

    @Test
    fun findFiguresByBorderColor() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Circle(5.0, Color.RED, Color.WHITE))
        shapeCollector.addShape(Circle(5.5, Color.RED, Color.GREEN))
        shapeCollector.addShape(Square(10.0, Color.RED, Color.RED))
        shapeCollector.addShape(Square(3.0, Color.RED, Color.BLUE))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.WHITE))
        shapeCollector.addShape(Rectangle(5.0, 10.0, Color.WHITE, Color.BLACK))
        shapeCollector.addShape(Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN))
        shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

        val foundShapesList = shapeCollector.findShapesByBorderColor(Color.WHITE)

        val actualFoundShapesList = listOf(
            Rectangle(5.0, 10.0, Color.WHITE, Color.BLACK),
            Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN)
        )

        assertEquals(actualFoundShapesList, foundShapesList)
    }

    @Test
    fun findFiguresByFillColor() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Circle(5.0, Color.RED, Color.WHITE))
        shapeCollector.addShape(Circle(5.5, Color.RED, Color.GREEN))
        shapeCollector.addShape(Square(10.0, Color.RED, Color.RED))
        shapeCollector.addShape(Square(3.0, Color.RED, Color.BLUE))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.WHITE))
        shapeCollector.addShape(Rectangle(5.0, 10.0, Color.WHITE, Color.BLACK))
        shapeCollector.addShape(Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN))
        shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

        val foundShapesList = shapeCollector.findShapesByFillColor(Color.GREEN)

        val actualFoundShapesList = listOf(
            Circle(5.5, Color.RED, Color.GREEN),
            Rectangle(4.0, 25.0, Color.WHITE, Color.GREEN),
            Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN)
        )

        assertEquals(actualFoundShapesList, foundShapesList)
    }

    @Test
    fun sortShapesByBorderColor() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Circle(5.0, Color.WHITE, Color.WHITE))
        shapeCollector.addShape(Circle(5.5, Color.WHITE, Color.GREEN))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.WHITE))
        shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

        val foundShapesMap = shapeCollector.sortShapesByBorderColor()

        val listOfRedShapes = listOf(
            Rectangle(2.0, 3.0, Color.RED, Color.WHITE),
            Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN)
        )
        val listOfWhiteShapes = listOf(
            Circle(5.0, Color.WHITE, Color.WHITE),
            Circle(5.5, Color.WHITE, Color.GREEN)
        )

        val actualFoundShapesMap = mapOf(Color.RED to listOfRedShapes, Color.WHITE to listOfWhiteShapes)

        assertEquals(actualFoundShapesMap, foundShapesMap)
    }

    @Test
    fun sortShapesByFillColor() {
        val shapeCollector =ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Circle(5.0, Color.WHITE, Color.WHITE))
        shapeCollector.addShape(Circle(5.5, Color.WHITE, Color.GREEN))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.WHITE))
        shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

        val foundShapesMap = shapeCollector.sortShapesByFillColor()

        val listOfGreenShapes = listOf(
            Circle(5.5, Color.WHITE, Color.GREEN),
            Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN)
        )
        val listOfWhiteShapes = listOf(
            Circle(5.0, Color.WHITE, Color.WHITE),
            Rectangle(2.0, 3.0, Color.RED, Color.WHITE)
        )

        val actualFoundShapesMap = mapOf(Color.WHITE to listOfWhiteShapes, Color.GREEN to listOfGreenShapes)

        assertEquals(actualFoundShapesMap, foundShapesMap)
    }

    @Test
    fun getShapesByType() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        shapeCollector.addShape(Circle(5.0, Color.WHITE, Color.WHITE))
        shapeCollector.addShape(Circle(5.5, Color.WHITE, Color.GREEN))
        shapeCollector.addShape(Rectangle(2.0, 3.0, Color.RED, Color.WHITE))
        shapeCollector.addShape(Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN))

        val foundCirclesList = shapeCollector.getShapesByType<Circle>()
        val foundRectanglesList = shapeCollector.getShapesByType<Rectangle>()
        val foundTrianglesList = shapeCollector.getShapesByType<Triangle>()

        val actualCirclesList = listOf(
            Circle(5.0, Color.WHITE, Color.WHITE),
            Circle(5.5, Color.WHITE, Color.GREEN)
        )

        val actualRectanglesList = listOf(
            Rectangle(2.0, 3.0, Color.RED, Color.WHITE)
        )
        val actualTrianglesList = listOf(
            Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN)
        )

        assertEquals(actualCirclesList, foundCirclesList)
        assertEquals(actualRectanglesList, foundRectanglesList)
        assertEquals(actualTrianglesList, foundTrianglesList)
    }

    @Test
    fun addCollection() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()
        val circleCollection = mutableListOf(
            Circle(5.0, Color.RED, Color.GREEN),
            Circle(5.5, Color.RED, Color.GREEN),
        )
        shapeCollector.addCollection((circleCollection))

        val actualShapesList = listOf(
            Circle(5.0, Color.RED, Color.GREEN),
            Circle(5.5, Color.RED, Color.GREEN),
        )

        assertEquals(actualShapesList, shapeCollector.getShapesList())
    }

    @Test
    fun getSorted() {
        val shapeCollector = ShapeCollector<ColoredShape2d>()

        // Areas: 4, 9, 6
        val shapesCollection = mutableListOf(
            Square(2.0, Color.RED, Color.GREEN),
            Square(3.0, Color.RED, Color.GREEN),
            Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN),
        )
        shapeCollector.addCollection((shapesCollection))

        val actualShapesList = listOf(
            Square(2.0, Color.RED, Color.GREEN),
            Triangle(3.0, 4.0, 5.0, Color.RED, Color.GREEN),
            Square(3.0, Color.RED, Color.GREEN),
        )

        val maxAreaComparator = ShapeCollector.AreaComparator
        assertEquals(actualShapesList, shapeCollector.getSorted(maxAreaComparator))
    }
}