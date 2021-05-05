/** Camera implementing Perspective Projection
 *
 * This class inherit from abstract class Camera and implements the perspective projection onto the screen
 * for the observer
 *
 *
 * @param dist The distance of the Camera from the screen (default is 1.0)
 * @param AR The aspect ratio (width/height) of the screen (default is 1.6...MacBook standard)
 * @param T (optional) The Transformation applied to the default camera position  (see [Camera] for default position)
 *
 * @see Camera
 *
 */
class PerspectiveCamera (
    private val dist : Float = 1.0F,
    private val AR : Float = 1.6F,
    private val T : Transformation = Transformation()
) : Camera () {

    override fun fireRay(u: Float, v: Float): Ray {
        val origin = Point(-dist, 0.0F, 0.0F)
        val direction = Vector(dist, AR * (1.0F - 2.0F*u), 2.0F*v - 1.0F)
        return T*Ray(origin= origin, dir = direction, tmin=1.0F)
    }
}