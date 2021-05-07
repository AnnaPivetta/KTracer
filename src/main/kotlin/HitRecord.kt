import kotlin.math.abs

/** Information about an intersection
 * This type is meant to record all the information about an intersection when it occurs.
 *
 * Class properties:
 *  - [worldPoint] - The 3D point where the intersection occurs
 *  - [normal] - The normal to the surface in [worldPoint]
 *  - [surfacePoint] - The intersection coordinates with respect to the surface
 *  - [t] - The ray parameter associated with the intersection (it specifies the distance between the origin of the ray and the hit point)
 *  - [ray] - The ray which hit the surface
 *
 * @See Point
 * @See Normal
 * @See Vector2d
 * @See Ray
 *
 */

class HitRecord (
    var worldPoint : Point = Point(0.0F, 0.0F, 0.0F),
    var normal : Normal = Normal(0.0F, 0.0F, 0.0F),
    var surfacePoint : Vector2d = Vector2d(0.0F, 0.0F),
    var t : Float = 0.0F,
    var ray : Ray = Ray()){

    /**
     * Checks if two [HitRecord] represent the same hit event
     */
    fun isClose (other : HitRecord?, epsilon : Float = 1e-5F) : Boolean {
        if (other == null) {return false}
        return (worldPoint.isClose(other.worldPoint) &&
                normal.isClose(other.normal) &&
                surfacePoint.isClose(other.surfacePoint) &&
                (abs(t - other.t) < epsilon) &&
                ray.isClose(other.ray))
    }

}