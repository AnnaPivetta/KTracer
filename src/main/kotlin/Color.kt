data class Color(val r: Float = 0.0F, var g: Float = 0.0F, var b: Float = 0.0F) {
    /* RGB Color
    params:
        r --> Level of Red
        g --> Level of Green
        b --> Level of Blue
    functions:
        overload of operator "+" --> sum of respective params of two colors
        is_close                 --> verifies the equality of
     */
    operator fun plus(other: Color): Color {
        return Color(r + other.r, g + other.g, b + other.b)
    }

}