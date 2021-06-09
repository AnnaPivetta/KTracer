import RED
import RESET

class GrammarError (source : SourceLocation, mex : String):
    RuntimeException ("${source.fileName}:${source.line}:${source.col}: " +
            RED + "Error" + RESET + ": " + mex){
}