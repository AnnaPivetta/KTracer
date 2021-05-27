import kotlin.math.abs

/***A 3D infinite plane parallel to the x and y axis and passing through the origin
 *
 */

class Plane (T : Transformation = Transformation(),
             val material: Material = Material()
): Shape(T) {

    override fun isPointInternal(p: Point): Boolean {
        return true
    }

    /***
     * checks if there is an intersection between the plane and a ray.
     * If the intersection occurs, returns a HitRecord, else returns null.
     */
    override fun rayIntersection(r: Ray): HitRecord? {
        val ir = T.inverse() * r
        if (abs(ir.dir.z) < 1e-5) {return null}
        val t = -(ir.origin.z)/(ir.dir.z)
        if (t <= ir.tmin || t>=ir.tmax) {return null}
        val point = ir.at(t)
        val normal = if (ir.dir.z < 0.0F) {
            T*Normal(0.0F, 0.0F, 1.0F)
        } else {
            T*Normal(0.0F, 0.0F, -1.0F)
        }
        val hit = HitRecord (worldPoint = T*point,
            normal = normal,
            surfacePoint = Vector2d(point.x -(point.x).toInt(), point.y -(point.y).toInt()),
            t = t,
            ray = r)
        return hit
    }

    override fun rayIntersectionList(r: Ray): List<HitRecord>? {
        return if (rayIntersection(r)==null) {
            null
        } else {
            listOf(rayIntersection(r)!!)
        }


    }
}