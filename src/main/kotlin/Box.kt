import kotlin.math.max
import kotlin.math.min

/**
 * Shape implementing a 3D Axis Aligned Box
 *
 * This class inherits from abstract class Shape and implements a box with the edges aligned to the cartesian axes and
 * its every possible transformation
 *
 * Class properties:
 * - [min] - The [Point] representing the minimum value of each component (x, y, z). Default is for canonical Cube (-0.5, -0.5, -0.5)
 * - [max] - The [Point] representing the maximum value of each component (x, y, z). Default is for canonical Cube (0.5, 0.5, 0.5)
 * - [T] - (optional) The [Transformation] to apply to the Box
 *
 * @see Shape
 */

class Box(
    private val min: Point = Point(-0.5F, -0.5F, -0.5F),
    private val max: Point = Point(0.5F, 0.5F, 0.5F),
    T: Transformation = Transformation()
) : Shape(T) {
    /**
     * This functions evaluates if the given [Ray] intersects the sphere and returns the
     * closest intersection from the observer point of view
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the  closest intersection to the [Ray.origin] if any. Otherwise null is returned
     */
    override fun rayIntersection(r: Ray): HitRecord? {
        val ir = T.inverse() * r
        //Kotlin handling of 0 division is returning Infinity --> this is how we need it

        val mintX = (min.x - ir.origin.x) / ir.dir.x
        val maxtX = (max.x - ir.origin.x) / ir.dir.x
        val mintY = (min.y - ir.origin.y) / ir.dir.y
        val maxtY = (max.y - ir.origin.y) / ir.dir.y
        val mintZ = (min.z - ir.origin.z) / ir.dir.z
        val maxtZ = (max.z - ir.origin.z) / ir.dir.z

        val minT = maxOf(mintX, mintY, mintZ)
        val maxT = minOf(maxtX, maxtY, maxtZ)
        if (minT > maxT) return null


        return HitRecord(
            worldPoint = T * ir.at(minT),
            normal = getNormal(mintX, mintY, mintZ, ir.dir),
            surfacePoint = Vector2d(0.5F, 0.5F),
            t = minT,
            ray = r
        )
    }

    fun getNormal(mintX: Float, mintY: Float, mintZ: Float, rayDir: Vector): Normal {
        val minT = maxOf(mintX, mintY, mintZ)
        var norm = when (minT) {
            mintX -> VECX.toNormal()
            mintY -> VECY.toNormal()
            mintZ -> VECZ.toNormal()
            else -> Normal()
        }
        return if (norm.toVector() * rayDir > 0) -norm else norm
    }
}
