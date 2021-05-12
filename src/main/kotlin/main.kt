import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int

import java.io.FileInputStream
import java.lang.RuntimeException

/*sealed class LoadMode(name: String): OptionGroup(name)
class Demo : LoadMode("Demo") {
    val height by option().int()
    val width by option().int()
}
class Conversion : LoadMode("Conversion from pfm to jpg") {
    val fileIn by option().file().required()
    val fileOut by option().file()
}
class KTracer : CliktCommand () {
    val mode by option().groupChoice(
        "demo" to Demo(),
        "pfm2jpg" to Conversion()
    )

    override fun run() {
        TODO("Not yet implemented")
    }
}*/

class KTracer : CliktCommand() {
    override fun run() = Unit
}

class Demo : CliktCommand(name = "demo") {

    val width by option().int().default(480)
    val height by option().int().default(480)
    val orthogonal by option().flag(default = false)
    val pfmoutput by option()
    val ldroutput by option()
    val factor by option().float().default(0.2F)
    val gamma by option().float().default(1.0F)
    val format by option()

    override fun run() {
        //Set 10 Spheres in the World
        val world = World()
        val vertices = arrayOf(-0.5F, 0.5F)
        for (x in vertices) {
            for (y in vertices) {
                for (z in vertices) {
                    world.add(
                        Sphere(
                            T = Transformation().translation(Vector(x, y, z)) *
                                    Transformation().scaling(Vector(0.1F, 0.1F, 0.1F))
                        )
                    )
                }
            }
        }
        val ar = width.toFloat() / height.toFloat()
        val camera = if (orthogonal) OrthogonalCamera(AR = ar, T = Transformation().translation(-2.0F * VECX))
                else PerspectiveCamera(AR = ar, T = Transformation().translation(-2.0F * VECX))
        val im = HdrImage(width, height)
        val computeColor: (Ray) -> Color = { if (world.rayIntersection(it) == null) BLACK else WHITE }
        val tracer = ImageTracer(im, camera).fireAllRays(computeColor)

        //Save HDR Image
        im.saveHDRImg(pfmoutput.toString())
        echo ("PFM Image has been saved to ${System.getProperty("user.dir")}/${pfmoutput}")

        //Tone Mapping
        echo ("Applying tone mapping with default values...")
        im.normalizeImg(factor= factor)
        im.clampImg()

        im.saveLDRImg(ldroutput.toString(), format.toString(), gamma)
        echo ("LDR Image has been saved to ${System.getProperty("user.dir")}/${ldroutput}")
    }
}

class Conversion : CliktCommand(name = "pfm2ldr") {

    override fun run() {
        TODO()
    }
}


/*fun main(args:Array<String>) {

    // -------- CONVERSION FROM PFM --------
    val img = HdrImage()
    val params = Parameters()
    try{
    params.parseCommandLine(args)
    }
    catch (e:RuntimeException) {
        println("Error $e")
        return
    }
    println("Reading file...")
    FileInputStream(params.inputPFMFileName).use{
        INStream -> img.readPfmFile(INStream)
    }
    println("File successfully read")

    println("Normalizing pixels luminosity...")
    img.normalizeImg(factor= params.factor)
    img.clampImg()
    println("Image normalized")
    println("Writing image on disk...")
    img.saveLDRImg(params.outputFileName, params.format, params.gamma)
    println("Done! Your image has been saved to ${System.getProperty("user.dir")}/${params.outputFileName}")
    // -------------------------------------



}*/
