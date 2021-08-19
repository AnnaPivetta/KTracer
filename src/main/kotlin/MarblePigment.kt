import kotlin.math.PI
import kotlin.math.sin

/**
 * Marble like pigment
 *
 * This class inherits from [Pigment] and using a 2D [Perlin] noise, generates a marble-like pigment. There are
 * a few parameters that can be set to obtain different marbles style.
 * A sine-like pattern is used to generate veining.
 * For more reference about [Perlin] noise, please see this [paper](https://mrl.cs.nyu.edu/~perlin/paper445.pdf)
 * For more reference about marble-like textures see this [paper](https://www.researchgate.net/profile/Man-Gon-Park/publication/264020475_Combination_Algorithm_of_a_Material_for_Marble_Solid_Effects/links/54b7a87e0cf2bd04be33b673/Combination-Algorithm-of-a-Material-for-Marble-Solid-Effects.pdf)
 *
 * Class properties:
 *  - [c1] The background color of marble
 *  - [c2] The color of marble's veining
 *  - [xPeriod] Number of vertical lines. 0.0 means only horizontal lines
 *  - [yPeriod] Number of horizontal lines. Together with
 *  [xPeriod] defines the angle of the lines.
 *  - [turbPower] This parameter sets the intensity of twists in the lines. The higher the power, the bigger the twists
 *  in the veining of the marble
 *  - [octaves] Parameter that sets the number of fractal octaves in the sum. Must be a power of 2. The higher the #
 *  of octaves, the blurrier will result the noise image
 *
 * @see LavaPigment
 * @see WoodPigment
 */
class MarblePigment(
    val c1: Color = WHITE.copy(),
    val c2: Color = BLACK.copy(),
    val xPeriod: Float = 0.0F,
    val yPeriod: Float = 1.0F,
    val turbPower: Float = 4.0F,
    val octaves: Int = 128
) : Pigment() {

    override fun getColor(vec: Vector2d): Color {
        val x = vec.u
        val y = vec.v

        val xyValue =
                x * xPeriod * 2.0F * PI.toFloat() +
                y * yPeriod * 2.0F * PI.toFloat() +
                turbPower * Perlin.turbulence(x, y, octaves.toFloat())

        val c = 0.5F + 0.5F * (sin(xyValue * PI.toFloat()))
        return Color(
            c2.r + c1.r * c,
            c2.g + c1.g * c,
            c2.b + c1.b * c
        )


    }

}
