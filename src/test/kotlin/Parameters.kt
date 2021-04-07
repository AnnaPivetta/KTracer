import java.lang.RuntimeException

data class Parameters (
    var inputPFMFileName : String = "",
    var factor : Float = 0.2F,
    var gamma : Float = 1.0F,
    var outputFileName :String = "")
{

    fun parseCommandLine(args : Array<String>) {
        if (args.size !=4) throw RuntimeException ("Usage: PFMfileIn name, factor, gamma, fileOut name")
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
        outputFileName = args[3]
    }

}