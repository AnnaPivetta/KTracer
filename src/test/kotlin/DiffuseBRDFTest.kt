import org.junit.Test

import org.junit.Assert.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DiffuseBRDFTest {

    @Test
    @kotlin.ExperimentalUnsignedTypes
    fun scatterRay() {
        val brdf = DiffuseBRDF()
        val pcg = PCG() //Default seeds
        val inDir = -VECZ.copy()
        val hitPoint = Point()
        val n = VECZ.toNormal()
        val d = 0

        println(System.getProperty("user.dir"))
        val expectedR = File("./src/test/src/Rays.txt").readLines()
            for (i in 0..10) {
                val r = brdf.scatterRay(pcg, inDir, hitPoint, n, d)
                assertTrue (expectedR[i] == "${r.dir.x} ${r.dir.y} ${r.dir.z}")

            }
        }
    }