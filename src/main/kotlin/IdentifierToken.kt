class IdentifierToken (location : SourceLocation = SourceLocation(), val identifier : String) : Token(location) {
    override fun toString(): String {
        return identifier
    }

}