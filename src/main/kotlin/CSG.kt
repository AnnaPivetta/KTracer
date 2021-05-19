/**
 * Constructive Solid Geometry
 *
 *
 */
class CSG {

    fun union(s1: Shape, s2: Shape, r: Ray): HitRecord? {
        val hits = mutableListOf<HitRecord>()
        val hits1 = s1.rayIntersectionList(r)
        val hits2 = s2.rayIntersectionList(r)
        if (hits1 != null) hits.addAll(hits1.toList())
        if (hits2 != null) hits.addAll(hits2.toList())
        if (hits.isEmpty()) return null
        else {
            var closest = hits[0]
            for (h in hits.slice(1 until hits.size)) {
                if (h.t < closest.t) closest = h
            }
            return closest
        }
    }
}