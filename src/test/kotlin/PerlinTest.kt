import org.junit.Test

import org.junit.Assert.*

class PerlinTest {

    @Test
    fun noise() {
        val w=256
        val h=256
        val img = HdrImage(w, h)
        for (x in 0 until w){
            for (y in 0 until h) {
                val c = 0.5F + 0.5F * Perlin.noise(x.toFloat()/128F, y.toFloat()/128F)

                img.setPixel(x, y, Color(c, c, c))
            }

        img.saveLDRImg("testPerlinMarble.png", "png")
    }
}
}
