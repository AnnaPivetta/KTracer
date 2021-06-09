class GrammarError (source : SourceLocation, mex : String):
    RuntimeException ("${source.fileName}:${source.line}:${source.col}: " + mex){
}