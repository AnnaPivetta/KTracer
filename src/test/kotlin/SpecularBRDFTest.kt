import org.junit.Test

import org.junit.Assert.*

class SpecularBRDFTest {

    @Test
    fun scatterRay() {
        val brdf = SpecularBRDF()
        val pcg = PCG() //Default seeds
        val inDir = -VECZ.copy()
        val hitPoint = Point()
        val n = VECZ.toNormal()
        val d = 0

        val ray1 = brdf.scatterRay(pcg, inDir, hitPoint, n, d)
        assertTrue(ray1.dir.isClose(VECZ.copy()))

        val inDir2 = Vector(-1.0F, -1.0F,-1.0F)

        val ray2 = brdf.scatterRay(pcg, inDir2, hitPoint, n, d)
        val expRay2 = Vector(-1.0F, -1.0F,1.0F)
        expRay2.normalize()

        assertTrue(ray2.dir.isClose(expRay2))
    }
}