data class Color(val r: Float = 0.0F, var g: Float = 0.0F, var b: Float = 0.0F) {
    operator fun times(other: Color): Color {
        return Color(r * other.r, g * other.g, b * other.b)
    }

    operator fun times(scalar: Float): Color {
        return Color(r * scalar, g * scalar, b * scalar)
    }
}