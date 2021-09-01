import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class Hyperboloid (
    T : Transformation = Transformation(),
    material: Material = Material()
    ) : Shape(T, material) {

    override fun isPointInternal(p: Point): Boolean {
        val realP = T.inverse() * p
        return realP.x*realP.x + realP.y*realP.y - realP.z * realP.z < -1.0F && realP.z in -0.5F..0.5F
    }


    /**
     * This function evaluates if the given [Ray] intersects the [Hyperboloid] and returns the
     * closest intersection from the observer point of view
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the closest intersection to the [Ray.origin] if any. Otherwise null is returned
     */
    override fun rayIntersection(r: Ray): HitRecord? {
        val ir = T.inverse() * r
        //Compute intersection
        //Determinant/4 ( -b +/- sqrt(b*b - ac) )/a
        val a = ir.dir.x*ir.dir.x + ir.dir.y*ir.dir.y - ir.dir.z*ir.dir.z
        val b = ir.origin.x*ir.dir.x + ir.origin.y*ir.dir.y - ir.origin.z*ir.dir.z
        val c = ir.origin.x*ir.origin.x + ir.origin.y*ir.origin.y - ir.origin.z*ir.origin.z - 1.0F
        val det4 = b * b - a*c
        //Intersections
        val t1 = (-b - sqrt(det4)) / a
        val t2 = (-b + sqrt(det4)) / a

        val tHit = when {
            t1 in r.tmin..r.tmax && ir.at(t1).z in -0.5F..0.5F -> {
                t1
            }
            t2 in r.tmin..r.tmax && ir.at(t2).z in -0.5F..0.5F -> {
                t2
            }
            else -> {
                return null
            }
        }
        val hit = ir.at(tHit)
        return HitRecord(
            worldPoint = T * hit,
            normal = T * getNormal(hit, ir.dir),
            surfacePoint = toSurPoint(hit),
            t = tHit,
            ray = r,
            shape = this
        )
    }

    /**
     * This function evaluates if the given [Ray] intersects the [Hyperboloid] and returns the
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
        //Compute intersection
        //Determinant/4 ( -b +/- sqrt(b*b - ac) )/a
        val a = ir.dir.norm2()
        val b = ir.origin.x*ir.dir.x + ir.origin.y*ir.dir.y - ir.origin.z*ir.dir.z
        val c = ir.origin.x*ir.origin.x + ir.origin.y*ir.origin.y - ir.origin.z*ir.origin.z + 1.0F
        val det4 = b * b - a*c
        //Intersections
        val t1 = (-b - sqrt(det4)) / a
        val t2 = (-b + sqrt(det4)) / a

        val hits = mutableListOf<HitRecord>()
        if (t1 in r.tmin..r.tmax && ir.at(t1).z in -0.5F..0.5F) {
            val hit = ir.at(t1)
            hits.add(
                HitRecord(
                    worldPoint = T * hit,
                    normal = T * getNormal(hit, ir.dir),
                    surfacePoint = toSurPoint(hit),
                    t = t1,
                    ray = r,
                    shape = this
                )
            )
        }
        if (t2 in r.tmin..r.tmax && ir.at(t2).z in -0.5F..0.5F) {
            val hit = ir.at(t2)
            hits.add(
                HitRecord(
                    worldPoint = T * hit,
                    normal = T * getNormal(hit, ir.dir),
                    surfacePoint = toSurPoint(hit),
                    t = t2,
                    ray = r,
                    shape = this
                )
            )
        }

        return if (hits.isEmpty()) null
        else hits.sortedBy { it.t }
    }


    private fun getNormal(p: Point, rayDir: Vector): Normal {
        val n = Normal(p.x, p.y, -p.z)
        return if (n.toVector() * rayDir < 0.0F) n else -n
    }

    private fun toSurPoint(hit: Point): Vector2d {
        return Vector2d(        //Lateral Face
        u = ((atan2(hit.y, hit.x) + (2.0F * PI.toFloat())) % (2.0F * PI.toFloat())) / (2.0F * PI.toFloat()),
        v = hit.z +0.5F
        )

    }
}

