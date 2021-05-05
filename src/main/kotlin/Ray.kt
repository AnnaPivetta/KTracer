/**
 * This class represents a light ray in three dimensions.
 *
 * Class properties:
 * - [origin] - The ray origin
 * - [dir] - The ray direction
 * - [tmin] - Specifies the minimum distance at which an object can be seen, in terms of the direction vector
 * - [tmax] - Specifies the maximum distance at which an object can be seen, in terms of the direction vector
 * - [depth] - Specifies the number of reflections that the ray undergoes
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
    fun transform (transformation : Transformation) : Ray{
        return Ray(transformation*origin, transformation*dir, tmin, tmax, depth)
    }
}