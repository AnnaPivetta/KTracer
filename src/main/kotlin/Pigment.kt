/** A Pigment
 *
 * This abstract class represents the color for a specific point on a surface
 *
 * Concrete Pigments are:
 * - [UniformPigment]
 * - [CheckeredPigment]
 * - [ImagePigment]
 *
 * @see UniformPigment
 * @see CheckeredPigment
 * @see ImagePigment
 */

abstract class Pigment () {
    abstract fun getColor (vec : Vector2d) : Color
}