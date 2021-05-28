/*** A Uniform Pigment
 *
 * This class inherits from [Pigment] and represents the simplest pigment, i.e the uniform one
 *
 * @param color The [Color] of the pigment
 *
 * @see Pigment
 */

class UniformPigment (val color : Color = Color()) : Pigment () {

    override fun getColor (vec : Vector2d) : Color {
        return color
    }



}