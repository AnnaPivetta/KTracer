

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
    private var min: Point = Point(-0.5F, -0.5F, -0.5F),
    private var max: Point = Point(0.5F, 0.5F, 0.5F),
    T: Transformation = Transformation()
) : Shape(T) {

    init {
        for (i in 0 until 3) {
            if (min[i]>max[i]) {
                println(RED + "Warning: Box has no consistent minimum and maximum vertices. " +
                        "Default values will be used" + RESET)
                min = Point(-0.5F, -0.5F, -0.5F)
                max = Point(0.5F, 0.5F, 0.5F)
                break
            }
        }
    }
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

        var t1 = ir.tmin
        var t2 = ir.tmax
        var minDir = -1
        var maxDir = -1

        for (i in 0 until 3) {
            var tmin = (min[i] - (ir.origin)[i]) / ir.dir[i]
            var tmax = (max[i] - (ir.origin)[i]) / ir.dir[i]

            if (tmin > tmax) {
                    val t = tmin
                    tmin = tmax
                    tmax = t
                }
            if(tmin>t1) {
                t1 = tmin
                minDir = i
            }
            if(tmax<t2) {
                t2 = tmax
                maxDir = i
            }

            //t1 always increases and t2 decreases
            //if t1 > t2 at a given direction, then it will be forever
            //no intersection occurs
            if (t1 > t2) return null
        }

        //If minDir = -1 it means that the first positive intersection has occurred in t2
        //to avoid counting backward intersection
        var t = t1
        var dir = minDir
        if (minDir == -1) {
            t = t2
            dir = maxDir
        }


        return HitRecord(
            worldPoint = T * ir.at(t),
            normal = getNormal(dir, ir.dir),
            surfacePoint = Vector2d(0.5F, 0.5F),
            t = t,
            ray = r
        )
    }

    private fun getNormal(minDir : Int, rayDir: Vector): Normal {
        val norm = when (minDir) {
            0 -> VECX.toNormal()
            1 -> VECY.toNormal()
            2 -> VECZ.toNormal()
            else -> Normal()
        }
        return if (norm.toVector() * rayDir > 0) -norm else norm
    }
}
