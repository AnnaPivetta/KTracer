/** The shapes in the scene
 * This class is an abstract class for implementing the different kind of objects
 */


abstract class Shape (val T : Transformation) {
    abstract fun rayIntersection (r : Ray) : HitRecord?
}