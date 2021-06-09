package InputCompiler

class SymbolToken (location : SourceLocation = SourceLocation(), val symbol : Char) : Token(location){
    override fun toString(): String {
        return symbol.toString()
    }
}