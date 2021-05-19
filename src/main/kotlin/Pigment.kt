/*** A Pigment
 * This abstract class represents a specific point on a surface
 */

abstract class Pigment {
    abstract fun getColor (vec : Vector2d) : Color
}