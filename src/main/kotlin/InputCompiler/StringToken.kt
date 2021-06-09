package InputCompiler

class StringToken (location : SourceLocation = SourceLocation(), val string : String) : Token(location){
    override fun toString(): String {
        return string
    }
}