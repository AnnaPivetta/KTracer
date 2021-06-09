class KeywordToken(location : SourceLocation = SourceLocation(), val keyword : KeywordEnum) : Token(location) {
    override fun toString(): String {
        return keyword.toString()
    }
}