import kotlin.math.abs

/** Information about an intersection
 * This type is meant to record all the information about an intersection when it occurs.
 * It contains:
 *  - worldPoint : the 3D point where the intersection occurs
 *  - normal : the surface normal in *worldPoint*
 *  - surfacePoint : the intersection coordinates with respect to the surface
 *  - t : the ray parameter associated with the intersection (it specifies the distance between the origin of the ray and the hit point)
 *  - ray : the ray which hit the surface
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
     * checks if two *HitRecord* represent the same hit event
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