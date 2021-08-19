import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

@ExperimentalUnsignedTypes
/**
 * Marble like pigment
 *
 * This class inherits from [Pigment] and using a random noise, generates a marble-like pigment. There are
 * a few parameters that can be set to obtain different marbles style.
 * The random values are generated when the object is allocated. They are stored in a grid which then is
 * bilinerarly interpolated to obtain the random value associated to each pixel of the UV map.
 * A sine-like pattern is used to generate veining.
 *
 * Class properties:
 *  - [c1] The background color of marble
 *  - [c2] The color of marble's veining
 *  - [noiseWidth] Number of different random values generated along x direction of the random grid
 *  - [noiseHeight] Number of different random values generated along y direction of the random grid
 *  - [xPeriod] Number of line repetition along x direction in the sine pattern. 0.0 means only horizontal lines
 *  - [yPeriod] Number of line repetition along y direction in the sine pattern. 1.0 means only 1 line. Together with
 *  [xPeriod] defines the angle of the lines.
 *  - [turbPower] This parameter sets the intensity of twists in the lines. The higher the power, the bigger the twists
 *  in the veining of the marble
 *
 *  There are also 2 value-parameters that make possible to obtain different marble patterns with the same features,
 *  as they change the random grid
 *  @param initState Initial state of the [PCG]
 *  @param initSeq Initial sequence of the [PCG]
 */
class MarblePigment(
    val c1: Color = WHITE.copy(),
    val c2: Color = BLACK.copy(),
    val xPeriod : Float = 0.0F,
    val yPeriod : Float = 0.15F,
    val turbPower : Float = 4.0F,
    noiseWidth: Int = 256,
    noiseHeight: Int = 256,
    initState: ULong = 42UL,
    initSeq: ULong = 54UL,
) : ProceduralPigment(noiseWidth, noiseHeight, initState, initSeq) {

    override fun getColor(vec: Vector2d): Color {
        var x = (vec.u * noiseWidth).toInt()
        var y = (vec.v * noiseHeight).toInt()

        if (x >= noiseWidth) {
            x = noiseWidth - 1
        }
        if (y >= noiseHeight) {
            y = noiseHeight - 1
        }

        val turbSize = 64.0F //initial size of the turbulence
        val xyValue = x * xPeriod / noiseWidth + y * yPeriod / noiseHeight +
                turbPower * turbulence(x.toFloat(), y.toFloat(), turbSize) / 256.0F
        val sineValue = abs(sin(xyValue * PI.toFloat()))
        return Color(
            c2.r + c1.r * sineValue,
            c2.g + c1.g * sineValue,
            c2.b + c1.b * sineValue
        )
    }
}
