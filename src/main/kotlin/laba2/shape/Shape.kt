package laba2.shape

import kotlin.math.*

data class RgbColor(val red: Float, val green: Float, val blue: Float, val alpha: Float) {
    init {
        if (!((red in 0f..255f) && (green in 0f..255f) && (blue in 0f..255f) && (alpha in 0f..255f))) {
            throw IllegalArgumentException("Incorrect color")
        }
    }
}

enum class Color(val rgbColor: RgbColor) {
    RED(RgbColor(255f, 0f, 0f, 1f)),
    GREEN(RgbColor(0f, 255f, 0f, 1f)),
    BLUE(RgbColor(0f, 0f, 255f, 1f)),
    WHITE(RgbColor(255f, 255f, 255f, 1f)),
    BLACK(RgbColor(0f, 0f, 0f, 1f)),
}

interface Shape2d {
    fun calcArea(): Double
}

interface ColoredShape2d : Shape2d {
    val borderColor: Color
    val fillColor: Color
}

data class Circle(val radius: Double, override val borderColor: Color, override val fillColor: Color) : ColoredShape2d {
    init {
        if (radius < 0) {
            throw IllegalArgumentException("Negative radius input: $radius")
        }
    }

    override fun calcArea(): Double {
        return PI * radius * radius
    }

    override fun toString(): String {
        return "Circle with radius $radius"
    }
}

data class Rectangle(
    val firstSide: Double,
    val secondSide: Double,
    override val borderColor: Color,
    override val fillColor: Color
) : ColoredShape2d {

    init {
        if (firstSide < 0) {
            throw IllegalArgumentException("Negative side input: $firstSide")
        }
        if (secondSide < 0) {
            throw IllegalArgumentException("Negative side input: $secondSide")
        }
    }

    override fun calcArea(): Double {
        return firstSide * secondSide
    }

    override fun toString(): String {
        return "Rectangle with sides $firstSide and $secondSide"
    }
}

data class Square(val side: Double, override val borderColor: Color, override val fillColor: Color) : ColoredShape2d {
    init {
        if (side < 0) {
            throw IllegalArgumentException("Negative side input: $side")
        }
    }

    override fun calcArea(): Double {
        return side * side
    }

    override fun toString(): String {
        return "Square with side $side"
    }
}

data class Triangle(
    val firstSide: Double,
    val secondSide: Double,
    val thirdSide: Double,
    override val borderColor: Color,
    override val fillColor: Color
) : ColoredShape2d {
    init {
        if (firstSide + secondSide < thirdSide || firstSide + thirdSide < secondSide || secondSide + thirdSide < firstSide) {
            throw IllegalArgumentException("Sum of two sides can't be less than third")
        }
    }

    override fun calcArea(): Double {
        val halfPerimeter = (firstSide + secondSide + thirdSide) / 2

        return sqrt(halfPerimeter * (halfPerimeter - firstSide) * (halfPerimeter - secondSide) * (halfPerimeter - thirdSide))
    }

    override fun toString(): String {
        return "Triangle with sides: $firstSide, $secondSide, $thirdSide"
    }
}