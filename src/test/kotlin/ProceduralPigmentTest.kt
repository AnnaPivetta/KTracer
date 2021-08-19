import org.junit.Test

import org.junit.Assert.*

class ProceduralPigmentTest {

    @Test
    fun turbulence() {
        val p = MarblePigment()
        val w=256
        val h=256
        val img = HdrImage(w, h)
        for (x in 0 until w){
            for (y in 0 until h) {
                val c = p.turbulence(x.toFloat(), y.toFloat(), 1.0F)
                img.setPixel(x, y, Color(c, c, c))
            }
        }
        img.saveLDRImg("testNoise.png", "png")
    }
}