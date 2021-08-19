import org.junit.Test

import org.junit.Assert.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

class PerlinTest {

    @Test
    fun noise() {
        val w=256
        val h=256
        val img = HdrImage(w, h)
        for (x in 0 until w){
            for (y in 0 until h) {
                val k = 16F
                val c = 0.5F + 0.5F * Perlin.turbulence(x.toFloat()/k, y.toFloat()/k, octaves= 64F)
                img.setPixel(x, y, Color(c, c, c))
            }
            img.saveLDRImg("testPerlin.png", "png")
    }
}
}
