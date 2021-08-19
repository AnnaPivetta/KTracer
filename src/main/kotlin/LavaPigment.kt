/**
 * Lava like pigment
 *
 * This class inherits from [Pigment] and using a 2D [Perlin] noise, generates a lava-like pigment, by means of
 * procedural texturing.
 * The noise is not modified by a function, only the sum of octaves is used.
 *
 * For more reference about [Perlin] noise, please see this [paper](https://mrl.cs.nyu.edu/~perlin/paper445.pdf)
 *
 * Class properties:
 * - [scale] Parameter that sets the value that multiplies the coordinate in the generation of Perlin noise. Higher
 * values reflect in a more detailed noise.
 * - [octaves] Parameter that sets the number of fractal octaves in the sum. Must be a power of 2. The higher the #
 *  of octaves, the blurrier will result the noise image
 *
 * @see MarblePigment
 * @see WoodPigment
 */

class LavaPigment(
    val scale: Float = 4.0F,
    val octaves: Int = 1024
) : Pigment() {

    val c1 = BLACK.copy()
    val c2 = Color(1.0F, 0.0F, 0.0F)
    val c3 = Color(0.9F, 1.0F, 0.0F)
    val c4 = WHITE.copy()
    val r1 = 0.4F
    override fun getColor(vec: Vector2d): Color {
        val t = 0.5F + 0.5F * Perlin.turbulence(vec.u * scale, vec.v * scale, octaves.toFloat())
        when {
            t < r1 -> return Color(
                lerp(t, c1.r, c2.r),
                lerp(t, c1.g, c2.g),
                lerp(t, c1.b, c2.b)
            )
            (t > r1 && t < 0.85F) -> return Color(
                lerp(t, c2.r, c3.r),
                lerp(t, c2.g, c3.g),
                lerp(t, c2.b, c3.b)
            )
            else -> return Color(
                lerp(t, c3.r, c4.r),
                lerp(t, c3.g, c4.g),
                lerp(t, c3.b, c4.b)
            )
        }
    }

    private fun lerp(t: Float, a: Float, b: Float): Float {
        return a + t * (b - a)
    }
}