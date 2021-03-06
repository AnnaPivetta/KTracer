/**
 * Ray Tracer
 * This class sends light rays through each pixel of the screen and computes the associated radiance
 *
 * Class properties:
 * - [image] - The [HdrImage] to fill with colors
 * - [camera] - The [Camera] observing the scene
 */
class ImageTracer (
    var image : HdrImage,
    var camera: Camera
    )  {

    /**
     * Sends one light ray from the [camera] through the selected pixel
     * @param col The column index of the pixel
     * @param row The row index of the pixel
     * @param uPixel A number in range [0,1] specifying where the ray should hit the pixel along the horizontal direction
     * @param vPixel A number in range [0,1] specifying where the ray should hit the pixel along the vertical direction
     */
    fun fireRay (col : Int, row : Int, uPixel : Float =0.5F, vPixel :Float =0.5F) : Ray {
        val u = (col + uPixel)/image.getWidth()
        val v = 1.0F - (row + vPixel)/image.getHeight()
        return camera.fireRay(u, v)
    }

    /**
     * Sends light rays through each pixel and computes the radiance of each pixel
     *
     * @param function The function specifying how to compute the radiance
     */
    fun fireAllRays (function : (Ray) -> Color ) {
        for (row in 0 until image.getHeight()) {
            for (col in 0 until image.getWidth()){
                val ray = fireRay(col, row)
                val color = function(ray)
                image.setPixel(col, row, color)
            }
        }

    }

    /**
     * Overloads [fireAllRays] adding the algorithm of Anti Aliasing.
     * I consists in sampling the radiance of each pixel multiple time, dividing it in squares and randomly choosing
     * the position where the ray is sent at.
     * Averaging the results improves the quality of the renedering
     *
     * @param AAgrid The number of squares in which each side of the pixel is divided. Namely AAgrid*AAgrid is the total
     * number of samples per pixel
     * @param pcg The random number generator used
     */
    @ExperimentalUnsignedTypes
    fun fireAllRays(function: (Ray) ->Color, AAgrid : Int?, pcg: PCG) {
        if (AAgrid == null) {
            fireAllRays(function)
            return
        } else {
            for (row in 0 until image.getHeight()) {
                for (col in 0 until image.getWidth()) {

                    var addColor = Color(0.0F, 0.0F, 0.0F)
                    for (i in 0 until AAgrid) {
                        for (j in 0 until AAgrid) {
                            val newU = pcg.rand((1.0F / AAgrid) * i, (1.0F / AAgrid) * (i + 1))
                            val newV = pcg.rand((1.0F / AAgrid) * j, (1.0F / AAgrid) * (j + 1))
                            val ray = fireRay(col, row, newU, newV)
                            val color = function(ray)
                            addColor+=color
                        }
                    }
                    image.setPixel(
                        col,
                        row,
                        addColor*(1.0F/(AAgrid*AAgrid))
                    )
                }
            }
        }
    }
}