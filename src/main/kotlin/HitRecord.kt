/** Informations about an intersection
 * This type is meant to record all the informations about an intersection when it occurs.
 * It contains:
 *  - worldPoint : the 3D point where the intersection occurs
 *  - normal : the surface normal in *worldPoint*
 *  - surfacePoint : the intersection coordinates with respect to the surface
 *  - t : the ray parameter associated with the intersection (it specifies the distance between the origin of the ray and the hit point)
 *  - ray : the ray which hit the surface
 *
 * @See Point
 * @See Normal
 * @See Vec2d
 * @See Ray
 *
 */


class HitRecord (
    var worldPoint : Point = Point(0.0F, 0.0F, 0.0F),
    var normal : Normal = Normal(0.0F, 0.0F, 0.0F),
    var surfacePonit : Vec2d = Vec2d(0.0F, 0.0F),
    var t : Float = 0.0F,
    var ray : Ray = Ray()){

}