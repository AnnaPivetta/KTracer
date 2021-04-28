/** A Camera observing the scene
 *
 * This class is an abstract class for implementing different kind of projection onto the screen for the
 * observer of the scene
 *
 * The Camera is always set in (-d, 0, 0) with d the distance from the screen
 *
 * The vector "up" is (0, 0, 1)
 *
 * The vector "right" is (0, -AR, 0) with AR the Aspect Ratio of the screen
 *
 *
 * Concrete Cameras are:
 * - OrthogonalCamera
 * - PerspectiveCamera
 *
 * @see OrthogonalCamera
 * @see PerspectiveCamera
 */

abstract class Camera () {
    abstract fun fireRay (u : Float, v: Float) : Ray
}