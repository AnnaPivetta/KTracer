/** Camera implementing Orthogonal Projection
 *
 * This class inherit from abstract class Camera and implements the orthogonal projection onto the screen
 * for the observer
 *
 * @see Camera
 *
 */
class OrthogonalCamera (
    AR : Float,
    T : Transformation = Transformation())
    : Camera (AR= AR, T = T) {

    override fun fireRay(u: Float, v: Float): Ray {
        val origin = Point(-1.0F, AR * (1.0F - 2.0F*u), 2.0F*v - 1.0F)
        val direction = VECX
        return T*Ray(origin= origin, dir = direction, tmin=1.0F)
    }
}