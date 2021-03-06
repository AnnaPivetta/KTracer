/** The shapes in the scene
 *
 * This class is an abstract class for implementing the different kind of geometric shapes
 *
 * The class always works with canonical shapes if possible (i.e. unitary and placed in the origin) and uses
 * transformations to deal with different positioning and scaling.
 *
 * Class properties:
 * - [T] - The Transformation to apply to the canonical shape
 * - [material] The material of which the shape is made of
 *
 * Concrete Shapes are:
 * - [Sphere]
 * - [Box]
 * - [Plane]
 * - [Cylinder]
 * - [Hyperboloid]
 * - CSGs
 *
 *
 * @see Sphere
 * @see Box
 * @see Plane
 * @see Cylinder
 * @see Hyperboloid
 * @see CSGDifference
 * @see CSGUnion
 * @see CSGIntersection
 */


abstract class Shape (val T : Transformation, val material : Material) {
    abstract fun isPointInternal (p : Point) : Boolean
    abstract fun rayIntersection (r : Ray) : HitRecord?
    abstract fun rayIntersectionList (r : Ray) : List<HitRecord>?
}