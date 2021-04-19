import java.lang.RuntimeException

const val RED = "\u001b[0;31m"
const val RESET = "\u001b[0m"

class Parameters (
    var inputPFMFileName : String = "",
    var factor : Float = 0.2F,
    var gamma : Float = 1.0F,
    var format : String = "png",
    var outputFileName :String = "")
{

    fun parseCommandLine(args : Array<String>) {
        if (args.size !=5) throw RuntimeException ("Usage: PFMfileIn name, factor, gamma, format, fileOut name")
        inputPFMFileName = args[0]

        try {
             factor= args[1].toFloat()
        }
        catch (e : NumberFormatException) {
            throw RuntimeException ("Invalid factor, it must be a floating-point value.")
        }
        try {
            gamma= args[2].toFloat()
        }
        catch (e : NumberFormatException) {
            throw RuntimeException ("Invalid gamma, it must be a floating-point value.")
        }
        format = args[3]
        outputFileName = args[4]

        val ext = outputFileName.split(".").last()
        if (!ext.equals(format, ignoreCase= true)) {
            println(RED + "Warning: Format mismatch between file extension and given format" + RESET)
        }
    }

}