/** Camera implementing Orthogonal Projection
 *
 * This class inherit from abstract class Camera and implements the orthogonal projection onto the screen
 * for the observer
 *
 * @see Camera
 *
 */
class OrthogonalCamera (
    a : Float,
    T : Transformation)
    : Camera (a= a, T = T) {

    override fun fireRay(u: Float, v: Float): Ray {
        val origin = Point(1.0F, a * (0.5F - u), v - 0.5F)
        val direction = VECX
        return T*Ray(origin= origin, dir = direction, tmin=1.0F)
    }
}