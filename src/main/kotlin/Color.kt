import kotlin.math.abs

/**
 * RGB Color
 *
 * Class properties:
 * - [r] - Level of red component
 * - [g] - Level of green component
 * - [b] - Level of blue component
 */
data class Color(var r: Float = 0.0F, var g: Float = 0.0F, var b: Float = 0.0F) {
    /* RGB Color
    params:
        r --> Level of Red
        g --> Level of Green
        b --> Level of Blue
    functions:
        overload of operator "+" --> sum of respective params of two colors
        overload of operator "*" --> (scalar) multiplication of params for the same scalar float
                                     (other)  scalar product between RGB vectors
        is_close                 --> verifies the equality of two colors within an epsilon
        luminosity               --> computes the luminosity of the pixel
     */

    override fun toString(): String {
        return "Color ($r, $g, $b)"
    }
    /**
     * Sum two colors
     */
    operator fun plus(other: Color): Color {
        return Color(r + other.r, g + other.g, b + other.b)
    }

    /**
     * Multiply each component of a color with a scalar
     */

    operator fun times(scalar: Float): Color {
        return Color(r * scalar, g * scalar, b * scalar)
    }

    /**
     * Multiply with another color
     */

    operator fun times(other: Color): Color {
        return Color(r * other.r, g * other.g, b * other.b)
    }

    /**
     * Check equality within a range
     * @return true if the r, g and b component of two colors are closer than epsilon
     */

    fun isClose(other: Color, epsilon: Float = 1e-10F): Boolean {
        return abs(r - other.r) < epsilon && abs(g - other.g) < epsilon && abs(b - other.b) < epsilon
    }

    /***
     * returns a measure of the luminosity associated to the color
     */
    fun luminosity() : Float {
        return (maxOf(r,g,b) + minOf(r,g,b))/2
    }

}