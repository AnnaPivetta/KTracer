/** Camera implementing Orthogonal Projection
 *
 * This class inherits from abstract class Camera and implements the orthogonal projection onto the screen
 * for the observer
 *
 * Class properties:
 * - [AR] - The aspect ratio (width/height) of the screen (default is 1.6...MacBook standard)
 * - [T] (optional) - The Transformation applied to the default camera position  (see [Camera] for default position)
 *
 * @see Camera
 *
 */
class OrthogonalCamera (
    private val AR : Float = 1.6F,
    private val T : Transformation = Transformation())
    : Camera () {

    override fun fireRay(u: Float, v: Float): Ray {
        val origin = Point(-1.0F, AR * (1.0F - 2.0F*u), 2.0F*v - 1.0F)
        val direction = VECX
        return T*Ray(origin= origin, dir = direction, tmin=1.0F)
    }
}