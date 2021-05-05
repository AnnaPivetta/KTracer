import kotlin.math.abs

/**
 * 2D vector to represent a point on a surface
 */

class Vec2d (
    var u : Float = 0.0F,
    var v : Float = 0.0F){
    /**
     * checks if two Vec2d are the same whithin an *epsilon*
     */
    fun isClose (other: Vec2d, epsilon : Float = 1e-5F) : Boolean {
        return (abs(u-other.u) < epsilon && abs(v-other.v) < epsilon)
    }
}