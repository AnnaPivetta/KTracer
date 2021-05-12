import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.file
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

class KTracer : CliktCommand () {
    override fun run() = Unit
}
class Demo : CliktCommand (name = "demo") {
    override fun run() {
        TODO()
    }
}
class Conversion : CliktCommand (name = "pfm2ldr") {

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
