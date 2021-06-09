package InputCompiler

class SymbolToken (location : SourceLocation = SourceLocation(), val symbol : String) : Token(location){
    override fun toString(): String {
        return symbol
    }
}