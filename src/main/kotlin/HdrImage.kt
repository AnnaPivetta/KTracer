class HdrImage(
    private val width: Int = 0,
    private val height: Int = 0,
    var pixels: Array<Color> = Array(width * height) { Color(0F, 0F, 0F) }
) {
    fun validCoordinates(x: Int, y: Int): Boolean {
        assert(0 <= x && x < width && 0 <= y && y < height)
        return true

    }

    fun pixelOffset(x: Int, y: Int): Int {
        assert(validCoordinates(x, y))
        return y * width + x
    }

    fun getPixel(x: Int, y: Int) : Color {
        return pixels[pixelOffset(x,y)]
    }
}

