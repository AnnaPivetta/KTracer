import kotlin.math.floor
import kotlin.time.measureTime

/** A checkered pigment
 *
 * This class inherits from [Pigment] and represents a checkered pigment.
 *
 * Number of rows and columns is customizable,
 * but the number of squares must be the same along both directions (u and v)
 *
 * @param color1 One color of the chessboard. Default is White
 * @param color2 The other color of the chessboard. Default is Black
 * @param numOfSteps The number of squares along one axis. MUST be a even number. If not so,
 * init block will increase it by 1. Default is 10
 *
 * @see Pigment
 */

class CheckeredPigment (val color1 : Color = WHITE.copy(), val color2 : Color = BLACK.copy(), var numOfSteps : Int = 4) : Pigment () {
    init {
        if (numOfSteps%2 != 0) {numOfSteps++}
    }
    override fun getColor(vec: Vector2d): Color {
        val intU = floor(vec.u * numOfSteps)
        val intV = floor(vec.v * numOfSteps)
        return if (intU%2 == intV%2) color1 else color2
    }
}