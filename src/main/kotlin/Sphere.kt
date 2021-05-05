import kotlin.math.*

class Sphere (T : Transformation): Shape(T)  {
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
            ray = r
        )
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