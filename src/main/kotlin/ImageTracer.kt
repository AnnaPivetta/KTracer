class ImageTracer (
    var image : HdrImage,
    var camera: Camera
    )  {
    fun fireRay (col : Int, row : Int, uPixel : Float =0.5F, vPixel :Float =0.5F) : Ray {
        var u : Float = (col + uPixel)/(image.getWidth() -1)
        var v : Float = (row + vPixel)/(image.getHeight() -1)
        return camera.fireRay(u, v)
    }
    fun fireAllRays (function : Function<Color> ) {
        var ray : Ray = Ray()
        var color : Color = Color()
        for (row in 0..image.getHeight()) {
            for (col in 0..image.getWidth()){
                ray = fireRay(col, row)
               // color = function (ray)
                image.setPixel(col, row, color)
            }
        }


    }

}