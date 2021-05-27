import kotlin.math.*

/**
 * Shape implementing a sphere in the 3D space
 *
 * This class inherits from abstract class Shape and implements the object sphere and its every possible
 * transformation
 *
 * Class properties:
 * - [T] - The [Transformation] to apply to the canonical sphere
 * - [material] - The [Material] of which the [Sphere] is made of
 *
 * @see Shape
 */
class Sphere (T : Transformation = Transformation(), val material: Material = Material()): Shape(T)  {

    override fun isPointInternal (p : Point) : Boolean{
        return (T.inverse() * p).toVector().norm2() < 1.0F
    }


    /**
     * This function evaluates if the given [Ray] intersects the sphere and returns the
     * closest intersection from the observer point of view
     *
     * @param r The [Ray] to check the intersection with
     * @return A [HitRecord] containing the  closest intersection to the [Ray.origin] if any. Otherwise null is returned
     */
    override fun rayIntersection(r: Ray): HitRecord? {
        val ir = T.inverse() * r
        //Compute intersection
        //Determinant/4 ( -b +/- sqrt(b*b - ac) )/a
        val oVec = ir.origin.toVector()
        val b = oVec * ir.dir
        val d2 = ir.dir.norm2()
        val det4 = b*b -  d2* (oVec.norm2() - 1.0F)
        //Intersections
        val t1 = ( -b - sqrt(det4) )/d2
        val t2 = ( -b + sqrt(det4) )/d2


        val tHit = when {
            t1 in r.tmin..r.tmax -> {
                t1
            }
            t2 in r.tmin..r.tmax -> {
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

    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        val ir = T.inverse() * r
        //Compute intersection
        //Determinant/4 ( -b +/- sqrt(b*b - ac) )/a
        val oVec = ir.origin.toVector()
        val b = oVec * ir.dir
        val d2 = ir.dir.norm2()
        val det4 = b*b -  d2* (oVec.norm2() - 1.0F)
        //Intersections
        val t1 = ( -b - sqrt(det4) )/d2
        val t2 = ( -b + sqrt(det4) )/d2

        val hits = mutableListOf<HitRecord>()
        if (t1 in r.tmin..r.tmax){
            val hit = ir.at(t1)
            hits.add(
                HitRecord(
                    worldPoint = T * hit,
                    normal = T * getNormal(hit, ir.dir),
                    surfacePoint = toSurPoint(hit),
                    t = t1,
                    ray = r
                )
            )
        }
        if (t2 in r.tmin..r.tmax ) {
            val hit = ir.at(t2)
            hits.add(
                HitRecord(
                    worldPoint = T * hit,
                    normal = T * getNormal(hit, ir.dir),
                    surfacePoint = toSurPoint(hit),
                    t = t2,
                    ray = r
                )
            )
        }

        return if (hits.isEmpty()) null
        else hits.sortedBy { it.t }
    }


    private fun getNormal (p : Point, rayDir : Vector) : Normal {
        val n = Normal (p.x, p.y, p.z)
        return if (p.toVector()*rayDir<0.0F) n else -n
    }

    private fun toSurPoint (p : Point) : Vector2d {
        val atan = if (p.y >=0.0F) atan2(p.y, p.x) else atan2(p.y, p.x) + 2.0F*PI.toFloat()
        return Vector2d(u= atan / (2.0F * PI.toFloat()), v=acos(p.z) / PI.toFloat())
    }


}