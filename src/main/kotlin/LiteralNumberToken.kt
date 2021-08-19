class LiteralNumberToken (location : SourceLocation = SourceLocation(), val value : Float) : Token(location) {
    override fun toString(): String {
        return value.toString()
    }
}