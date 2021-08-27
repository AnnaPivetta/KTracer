import kotlin.RuntimeException

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
 * - [material] - The [Material] of which the [Box] is made of
 * @see Shape
 */

class Box(
    var min: Point = Point(-0.5F, -0.5F, -0.5F),
    var max: Point = Point(0.5F, 0.5F, 0.5F),
    T: Transformation = Transformation(),
    material: Material = Material()
) : Shape(T, material) {

    init {
        for (i in 0 until 3) {
            if (min[i] > max[i]) {
                println(
                    VIDEORED + "Warning: Box has no consistent minimum and maximum vertices. " +
                            "Default values will be used" + VIDEORESET
                )
                min = Point(-0.5F, -0.5F, -0.5F)
                max = Point(0.5F, 0.5F, 0.5F)
                break
            }
        }
    }

    override fun isPointInternal(p: Point): Boolean {
        val realP = T.inverse() * p
        return realP.x in min.x..max.x && realP.y in min.y..max.y && realP.z in min.z..max.z
    }

    /**
     * This function evaluates if the given [Ray] intersects the sphere and returns the
     * closest intersection from the observer point of view
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the closest intersection to the [Ray.origin] if any. Otherwise null is returned
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
            if (tmin > t1) {
                t1 = tmin
                minDir = i
            }
            if (tmax < t2) {
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

        val hit = ir.at(t)
        val n = getNormal(dir, ir.dir)
        return HitRecord(
            worldPoint = T * hit,
            normal = T * n,
            surfacePoint = toSurPoint(hit, n),
            t = t,
            ray = r,
            shape = this
        )
    }

    /**
     * This function evaluates if the given [Ray] intersects the [Box] and returns the
     * list of all the intersections
     *
     * This method is used for CSG.
     *
     * @param r The [Ray] to check the intersections with
     * @return A [List] of[HitRecord] containing the intersections if any. Otherwise null is returned
     *
     * @see CSGUnion
     * @see CSGDifference
     * @see CSGIntersection
     *
     */

    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
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
            if (tmin > t1) {
                t1 = tmin
                minDir = i
            }
            if (tmax < t2) {
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
        if (minDir == -1) {
            val hit2 = ir.at(t2)
            val n2 = getNormal(maxDir, ir.dir)

            return listOf(
                HitRecord(
                    worldPoint = T * hit2,
                    normal = T * n2,
                    surfacePoint = toSurPoint(hit2, n2),
                    t = t2,
                    ray = r,
                    shape = this
                )
            )
        } else {
            val hit1 = ir.at(t1)
            val n1 = getNormal(minDir, ir.dir)

            val hit2 = ir.at(t2)
            val n2 = getNormal(maxDir, ir.dir)
            return listOf(
                HitRecord(
                    worldPoint = T * hit1,
                    normal = T * n1,
                    surfacePoint = toSurPoint(hit1, n1),
                    t = t1,
                    ray = r,
                    shape = this
                ),
                HitRecord(
                    worldPoint = T * hit2,
                    normal = T * n2,
                    surfacePoint = toSurPoint(hit2, n2),
                    t = t2,
                    ray = r,
                    shape = this
                )
            )
        }
    }

    private fun getNormal(minDir: Int, rayDir: Vector): Normal {
        val norm = when (minDir) {
            0 -> VECX.toNormal()
            1 -> VECY.toNormal()
            2 -> VECZ.toNormal()
            else -> Normal()
        }
        return if (norm.toVector() * rayDir >= 0) -norm else norm
    }


    /**
     * Function to map the surface of a cube onto a 2d projection
     *
     * The numeration of faces and their orientation in 3D space are consistent with the explanation
     * available on [Wikipedia](https://en.wikipedia.org/wiki/Cube_mapping:).
     * Each face is uniquely identified with its own normal, since this is an AAB
     *
     * When using this for texturing a Cube the original image should be taken with width=height and the cube map should
     * be deployed only in the 3/4 of its height starting from the bottom.
     * As an example we provide the following scheme:
     *
     *           --------------
     *           |            |
     *           |   |2|      |
     *           ||1||4||0||5||
     *           |   |3|      |
     *           --------------
     */
    private fun toSurPoint(hit: Point, normal: Normal): Vector2d {

        //Identifying the face using its normal
        //     ___
        // ___| 2 |________
        //| 1 | 4 | 0 | 5 |
        // '''| 3 |''''''''
        //     '''
        val face = when {
            normal.isClose(VECX.toNormal())  -> 0
            normal.isClose(-VECX.toNormal()) -> 1
            normal.isClose(VECY.toNormal())  -> 2
            normal.isClose(-VECY.toNormal()) -> 3
            normal.isClose(VECZ.toNormal())  -> 4
            normal.isClose(-VECZ.toNormal()) -> 5
            else -> -1
        }

        //For a complete understanding you may want to look to cited reference
        //Each face has a shift (may be 0) along u and v axes
        //The shift along v is negative because each face is mapped like :
        //            (1, 0)-(1, 1)
        //              |       |
        //            (0, 0)-(1, 0)
        //But the full image is mapped from a PFM file, which is:
        //
        //            (0, 0)-(0, 1)
        //              |       |
        //            (0, 1)-(1, 1)
        //
        //Then the coordinate are scaled in the range [0,1] with respect to the face side
        //Another scaling is performed with respect to the entire image, namely each face is a 1/4 of the full width/height
        return when (face) {
            0 -> Vector2d(u = 0.50F + (max.z - hit.z)/(max.z-min.z)*0.25F, v=0.75F - (hit.y - min.y)/(max.y-min.y)*0.25F )
            1 -> Vector2d(u =         (hit.z - min.z)/(max.z-min.z)*0.25F, v=0.75F - (hit.y - min.y)/(max.y-min.y)*0.25F )
            2 -> Vector2d(u = 0.25F + (hit.x - min.x)/(max.x-min.x)*0.25F, v=0.50F - (max.z - hit.z)/(max.z-min.z)*0.25F )
            3 -> Vector2d(u = 0.25F + (hit.x - min.x)/(max.x-min.x)*0.25F, v=1.00F - (hit.z - min.z)/(max.z-min.z)*0.25F )
            4 -> Vector2d(u = 0.25F + (hit.x - min.x)/(max.x-min.x)*0.25F, v=0.75F - (hit.y - min.y)/(max.y-min.y)*0.25F )
            5 -> Vector2d(u = 0.75F + (max.x - hit.x)/(max.x-min.x)*0.25F, v=0.75F - (hit.y - min.y)/(max.y-min.y)*0.25F )
            else -> throw RuntimeException()
        }

    }
}
