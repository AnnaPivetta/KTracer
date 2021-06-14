import VIDEORED
import VIDEORESET

class GrammarError (source : SourceLocation, mex : String):
    RuntimeException ("${source.fileName}:${source.line}:${source.col}: " +
            VIDEORED + "Error" + VIDEORESET + ": " + mex){
}