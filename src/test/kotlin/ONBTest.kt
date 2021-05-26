import org.junit.Test

import org.junit.Assert.*

class ONBTest {

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun createONBfromZ() {
        val pcg = PCG ()

        for (i in 0 until 10000) {
            val normal = Vector (pcg.rand(), pcg.rand(), pcg.rand())
            normal.normalize()
            val (e1, e2, e3) = createONBfromZ (normal.toNormal())
            //verifying that normal and z axis are //
            assertTrue(e3.isClose(normal, epsilon = 1e-5F))

            // verifying that the basis is orthogonal
            assertTrue(0.0F.isClose(e1.dot(e2), epsilon = 1e-5F))
            assertTrue(0.0F.isClose(e2.dot(e3), epsilon = 1e-5F))
            assertTrue(0.0F.isClose(e3.dot(e1), epsilon = 1e-5F))


            //verifying that each component is normalized
            assertTrue(1.0F.isClose(e1.norm2(), epsilon = 1e-5F))
            assertTrue(1.0F.isClose(e2.norm2(), epsilon = 1e-5F))
            assertTrue(1.0F.isClose(e3.norm2(), epsilon = 1e-5F))
        }
    }
}