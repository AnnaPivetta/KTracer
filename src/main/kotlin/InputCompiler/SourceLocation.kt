package InputCompiler

data class SourceLocation (
    val fileName: String = "",
    val line: Int = 0,
    val col: Int = 0
        )