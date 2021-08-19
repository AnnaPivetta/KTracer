import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Wood like pigment
 *
 * This class inherits from [Pigment] and using a 2D [Perlin] noise, generates a wood-like pigment. There are
 * a few parameters that can be set to obtain different features for woos
 * A sine-like pattern and the circle equation is used to generate veining.
 *
 * For more reference about [Perlin] noise, please see this [paper](https://mrl.cs.nyu.edu/~perlin/paper445.pdf)
 * For more reference about wood-like textures see this [site](https://lodev.org/cgtutor/randomnoise.html)
 *
 * Class properties:
 *  - [c1] The background color of wood
 *  - [c2] The color of wood's veining
 *  - [xyPeriod] Number of circles
 *  - [turbPower] This parameter sets the intensity of twists in the lines. The higher the power, the bigger the twists
 *  in the veining
 *  - [octaves] Parameter that sets the number of fractal octaves in the sum. Must be a power of 2. The higher the #
 *  of octaves, the blurrier will result the noise image
 *
 * @see LavaPigment
 * @see MarblePigment
 */

class WoodPigment(
    val c1: Color = DARKBROWN.copy(),
    val c2: Color = BLACK.copy(),
    val xyPeriod: Float = 8.0F,
    val turbPower: Float = 0.1F,
    val octaves: Int = 128
) : Pigment() {

    override fun getColor(vec: Vector2d): Color {
        val x = vec.u
        val y = vec.v

        val xValue = x - 0.5F
        val yValue = y - 0.5F
        val distValue = sqrt(xValue * xValue + yValue * yValue) +
                    turbPower * Perlin.turbulence(x, y, octaves.toFloat() )
        val sineValue = abs(sin(2.0F * xyPeriod * distValue * PI.toFloat()))
        return Color(
            c2.r + c1.r * sineValue,
            c2.g + c1.g * sineValue,
            c2.b + c1.b * sineValue
        )
    }
}
