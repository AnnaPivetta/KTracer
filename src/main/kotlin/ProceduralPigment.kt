/**
 * Procedural texturing pigment
 * (ref: https://lodev.org/cgtutor/randomnoise.html)
 *
 * This is an abstract class that is the base for every procedural pigment.
 * This type of [Pigment] are generated by a mathematical function, with the help of a random noise. More specifically,
 * the _Perlin Noise_ is used with the implementation of value-parameter
 *
 * The random values are generated when the object is allocated. They are stored in a grid which then is
 * bilinerarly interpolated to obtain the random value associated to each pixel of the UV map.
 *
 * @see Pigment
 * @see MarblePigment
 * @see WoodPigment
 */

@ExperimentalUnsignedTypes
abstract class ProceduralPigment(
    val noiseWidth: Int = 256,
    val noiseHeight: Int = 256,
    initState: ULong = 42UL,
    initSeq: ULong = 54UL
) : Pigment() {

    private val noise = Array(noiseHeight) { FloatArray(noiseWidth) { 0.0F } }

    init {
        val pcg = PCG(initState, initSeq)
        for (i in 0 until noiseHeight)
            for (j in 0 until noiseWidth) noise[i][j] = pcg.rand()
    }



    private fun smoothNoise(x: Float, y: Float): Float {
        //get fractional part of x and y
        val fractX = x - x.toInt()
        val fractY = y - y.toInt()

        //wrap around (PBC style)
        val x1 = (x.toInt() + noiseWidth) % noiseWidth
        val y1 = (y.toInt() + noiseHeight) % noiseHeight

        //neighbor values
        val x2 = (x1 + noiseWidth - 1) % noiseWidth
        val y2 = (y1 + noiseHeight - 1) % noiseHeight

        //smooth the noise with bilinear interpolation (linear interpolation in a point t between a and b
        // is given by
        // v = a + t * (b - a)
        var value = 0.0F
        value += fractX * fractY * noise[y1][x1]
        value += (1 - fractX) * fractY * noise[y1][x2]
        value += fractX * (1 - fractY) * noise[y2][x1]
        value += (1 - fractX) * (1 - fractY) * noise[y2][x2]

        return value
    }



    fun turbulence(x: Float, y: Float, initialSize: Float): Float {
        //To get the turbulence different zoomed image are added together,
        //the lesser the zoom, the darker the image (to lower their effect of noise)

        var value = 0.0F
        var size = initialSize
        while (size >= 1) {
            value += smoothNoise(x / size, y / size) * size
            size *= 0.5F
        }
        return (value / initialSize)
    }
}


