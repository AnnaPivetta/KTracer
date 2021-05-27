import java.lang.Math.floor
import kotlin.math.floor

/*** A checkered pigment
 * This class (which is derived from Pigment) represents a checkered pigment.
 * You can choose the number of rows and columns, but the number or squares along the u direction and the v direction must be the same
 */

class CheckeredPigment (val color1 : Color = WHITE.copy(), val color2 : Color = BLACK.copy(), val numOfSteps : Int = 5) : Pigment () {
    override fun getColor(vec: Vector2d): Color {
        val intU = floor(vec.u * numOfSteps)
        val intV = floor(vec.v * numOfSteps)
        return if (intU%2 == intV%2) color1 else color2
    }
}