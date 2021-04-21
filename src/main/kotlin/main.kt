import java.io.FileInputStream
import java.lang.RuntimeException

fun main(/*args:Array<String>*/) {
/*    val img = HdrImage()
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
*/
    val ID4x4 = Array (4) {i -> FloatArray(4) {k -> if (k!= i) 0.0F else 1.0F}}
    for (el in ID4x4) {
            print(el[0])
            print(el[1])
            print(el[2])
            println(el[3])
    }
    }
