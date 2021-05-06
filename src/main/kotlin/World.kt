/** The collection of shapes which populate the scene
 * This class is meant to hold a list of shapes. The shapes together make the world where the scene is set
 */

class World (
    var shapes : MutableList<Shape> = mutableListOf<Shape>()
        ){
    operator fun plus( shape : Shape) {
        shapes.add(shape)
    }
    fun add (shape : Shape ) {
        shapes.add(shape)
    }
    fun rayIntersection (ray : Ray) : HitRecord? {
        var closest : HitRecord? = null
        for (shape in shapes) {
            var intersection = shape.rayIntersection(ray) ?: continue //elvis operator
            if (closest == null || intersection.t < closest.t ) {
                closest = intersection
            }
        }
    return closest
    }
}