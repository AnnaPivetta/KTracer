/*** A Pigment given through an image
 * This class (which is derived from Pigment) represents  a pigment whose color is given through a PFM image
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