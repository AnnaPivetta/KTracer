class FirePigment () : Pigment() {

    val c1 = YELLOW.copy()
    val c2 = BLACK.copy()

    override fun getColor(vec: Vector2d): Color {
        val t = 0.5F + Perlin.noise(vec.u, vec.v) * 0.5F
        //println(t)
        /*println(Color(
            lerp(t, c1.r, c2.r),
            lerp(t, c1.g, c2.g),
            lerp(t, c1.b, c2.b)))

         */
        return Color(
            t,
            t,
            t
        )
    }

    private fun lerp(t: Float, a: Float, b: Float): Float {
        return a + t * (b - a)
    }
}