import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.prompt
import java.io.FileInputStream
import java.lang.RuntimeException

class Hello : CliktCommand() {
    val count: Int by option(help="Number of greetings").int().default(1)
    val name: String by option(help="The person to greet").prompt("Your name")

    override fun run() {
        repeat(count) {
            echo("Hello $name!")
        }
    }
}

fun main(args: Array<String>) = Hello().main(args)


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
