/** Ray
 * this class represents a light ray in three dimensions. The members of this class are:
 * - the ray origin
 * - the ray direction
 * - a parameter which specifies the minimum distance at which an object can be seen, in terms of the direction vector
 * - a parameter which specifies the maximum distance at which an object can be seen, in terms of the direction vector
 * - a parameter wich specifies the number of reflections that the ray undergoes
 *
 * @see Point
 * @see Vector
 */

class Ray (
        var origin : Point = Point(0.0F, 0.0F, 0.0F),
        var dir : Vector = Vector(0.0F, 0.0F, 0.0F),
        var tmin : Float = 1e-5F,
        var tmax : Float = Float.POSITIVE_INFINITY,
        var depth : Int = 0
        ) {
    fun isClose (other : Ray, epsilon : Float = 1e-5F) : Boolean {
        return (origin.isClose(other.origin, epsilon) and
                dir.isClose(other.dir, epsilon))
    }

    fun at(t : Float) : Point {
        return origin+dir*t
    }
}