import org.junit.Test

import org.junit.Assert.*

class PCGTest {

    @Test
    @kotlin.ExperimentalUnsignedTypes
    fun rand() {
        val pcg = PCG()

        assertTrue(pcg.state == 1753877967969059832UL)
        assertTrue(pcg.inc == 109UL)
        val expected = listOf<UInt>(
            2707161783U, 2068313097U,
            3122475824U, 2211639955U,
            3215226955U, 3421331566U
        )

        for (e in expected) {
            assertTrue(e == pcg.randInt())
        }
    }
}