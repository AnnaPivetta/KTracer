/**
 * [PCG](https://www.pcg-random.org) Random Number Generator
 *
 * The PCG implemented here is the 32-bit one, which has period 2^32 -1
 *
 * Params should not be changed
 */
@kotlin.ExperimentalUnsignedTypes
class PCG (var state : ULong = 0UL, var inc : ULong = 0UL){
    init {
        val initState = 42UL
        val initSeq = 54UL
        state = 0UL
        //Get rid of first 2 numbers
        inc = (initSeq shl 1 ) or 1UL
        randInt()
        state += initState
        randInt()
    }

    /**
     * Uniform Random Integer Generator
     *
     * @return An [Int] uniformly distributed in the range [0, 2^31-1)
     */

    fun randInt() : UInt{
        val oldState = state

        state = oldState * 6364136223846793005UL + inc

        val xorshifted = ( ( (oldState shr 18) xor oldState ) shr 27 ).toUInt()

        val rot : Int = (oldState shr 59).toInt()

        return ((xorshifted shr rot) or (xorshifted shl ((-rot) and 31)))
    }

    /**
     * Uniform Random
     *
     * @return A float uniformly distributed in the range [0,1)
     */
    fun rand() : Float {
        return randInt().toFloat() / 0xffffffff
    }

    /**
     * Uniform Random
     *
     * @param a The first end of range
     * @param b The second end of range
     * @return A float uniformly distributed in the range [a,b)
     */
    fun rand(a : Float, b : Float) : Float {
        return rand()* (b-a) + a
    }
}