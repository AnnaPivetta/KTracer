import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

@ExperimentalUnsignedTypes
class MarblePigment(
    val c1: Color = WHITE.copy(),
    val c2: Color = BLACK.copy(),
    val noiseWidth: Int = 128,
    val noiseHeight: Int = 128,
    val initState: ULong = 42UL,
    val initSeq: ULong = 54UL
) : Pigment() {


    val noise = Array(noiseHeight) { FloatArray(noiseWidth) { 0.0F } }

    init {
        val pcg = PCG(initState, initSeq)
        for (i in 0 until noiseHeight)
            for (j in 0 until noiseWidth) noise[i][j] = pcg.rand()
    }

    override fun getColor(vec: Vector2d): Color {
        var x = (vec.u * noiseWidth).toInt()
        var y = (vec.v * noiseHeight).toInt()

        if (x >= noiseWidth) {
            x = noiseWidth - 1
        }
        if (y >= noiseHeight) {
            y = noiseHeight - 1
        }

        //xPeriod and yPeriod together define the angle of the lines
        //xPeriod and yPeriod both 0 ==> it becomes a normal clouds or turbulence pattern
        //xPeriod and yPeriod together define the angle of the lines
        //xPeriod and yPeriod both 0 ==> it becomes a normal clouds or turbulence pattern
        val xPeriod = 0.0F //defines repetition of marble lines in x direction
        val yPeriod = 1.0F //defines repetition of marble lines in y direction

        //turbPower = 0 ==> it becomes a normal sine pattern
        //turbPower = 0 ==> it becomes a normal sine pattern
        val turbPower = 6.0F //makes twists

        val turbSize = 64.0F //initial size of the turbulence
        val xyValue = x * xPeriod / noiseWidth + y * yPeriod / noiseHeight +
                turbPower * turbulence(x.toFloat(), y.toFloat(), turbSize) / 256.0F
        val sineValue = abs(sin(xyValue * PI.toFloat()))

        return Color(
            c2.r + c1.r * sineValue,
            c2.g + c1.g * sineValue,
            c2.b + c1.b * sineValue
        )
    }

    private fun smoothNoise(x: Float, y: Float): Float {
        //get fractional part of x and y
        val fractX = x - x.toInt()
        val fractY = y - y.toInt()

        //wrap around
        val x1 = (x.toInt() + noiseWidth) % noiseWidth
        val y1 = (y.toInt() + noiseHeight) % noiseHeight

        //neighbor values
        val x2 = (x1 + noiseWidth - 1) % noiseWidth
        val y2 = (y1 + noiseHeight - 1) % noiseHeight

        //smooth the noise with bilinear interpolation
        var value = 0.0F
        value += fractX * fractY * noise[y1][x1]
        value += (1 - fractX) * fractY * noise[y1][x2]
        value += fractX * (1 - fractY) * noise[y2][x1]
        value += (1 - fractX) * (1 - fractY) * noise[y2][x2]

        return value
    }

    private fun turbulence(x: Float, y: Float, pSize: Float): Float {
        var value = 0.0F
        val initialSize = pSize
        var size = pSize
        while (size >= 1) {
            value += smoothNoise(x / size, y / size) * size
            size *= 0.5F
        }
        return (128.0F * value / initialSize)
    }
}
