import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin
import kotlin.math.sqrt

@ExperimentalUnsignedTypes
/**
 * Wood like pigment
 *
 * This class inherits from [ProceduralPigment] and generates a wood-like pigment. There are
 * a few parameters that can be set to obtain different marbles style.
 * A sine-like pattern is used to generate veining.
 *
 * Class properties:
 *  - [c1] The background color of marble
 *  - [c2] The color of marble's veining
 *  - [xyPeriod] Number of rings in the pattern
 *  - [turbPower] This parameter sets the intensity of twists in the lines. The higher the power, the bigger the twists
 *  in the veining. We recommend to use small values in order to appreciate the wood veining
 *
 *  There are also 4 value-parameters that make possible to obtain different marble patterns with the same features,
 *  as they change the random grid.
 *  They are directly used by the constructor of the father: [ProceduralPigment]
 *  @param initState Initial state of the [PCG]
 *  @param initSeq Initial sequence of the [PCG]
 *  @param noiseWidth Number of different random values generated along x direction of the random grid
 *  @param noiseHeight Number of different random values generated along y direction of the random grid
 *
 *  @see ProceduralPigment
 *  @see MarblePigment
 */

class WoodPigment(
    val c1: Color = DARKBROWN.copy(),
    val c2: Color = SADDLEBROWN.copy(),
    val xyPeriod: Float = 8.0F,
    val turbPower: Float = 0.1F,
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


        val turbSize = 32.0F //initial size of the turbulence

        val xValue = (x - noiseWidth * 0.5F) / noiseWidth
        val yValue = (y - noiseHeight * 0.5F) / noiseHeight
        val distValue = sqrt(xValue * xValue + yValue * yValue) +
                turbPower * turbulence(x.toFloat(), y.toFloat(), turbSize) / 256.0F
        val sineValue = abs(sin(2.0F * xyPeriod * distValue * PI.toFloat()))
        return Color(
            c2.r + c1.r * sineValue,
            c2.g + c1.g * sineValue,
            c2.b + c1.b * sineValue
        )
    }
}
