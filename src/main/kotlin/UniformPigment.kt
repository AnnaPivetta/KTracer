/*** A Uniform Pigment
 * This class (which is derived from Pigment) represents the simplest type of color, i.e. a uniform one.
 */

class UniformPigment (val color : Color = Color()) : Pigment () {

    override fun getColor (vec : Vector2d) : Color {
        return color
    }



}