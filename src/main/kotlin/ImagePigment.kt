/** A Pigment given through an image
 *
 * This class inherits from [Pigment] and represents a pigment whose color is taken from a PFM image
 *
 * @param image The [HdrImage] from which color is taken
 *
 * @see Pigment
*/

class ImagePigment (val image : HdrImage = HdrImage()) : Pigment() {
    override fun getColor(vec: Vector2d): Color {
        var col = (vec.u * image.getWidth()).toInt()
        var row = (vec.v * image.getHeight()).toInt()

        if (col >= image.getWidth()) { col = image.getWidth() - 1 }
        if (row >= image.getHeight()) { row = image.getHeight() - 1 }

        return image.getPixel(col, row)
    }
}