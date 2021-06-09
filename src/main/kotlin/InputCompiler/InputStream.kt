package InputCompiler

import RED
import RESET
import java.io.InputStreamReader
import kotlin.test.assertFalse
import kotlin.test.assertTrue

val WHITESPACE = listOf(' ', '\n', '\r', '\t')
val SYMBOLS = listOf('(', ')', '<', '>', '[', ']', ',', '*')

class InputStream(
    private val stream: InputStreamReader,
    fileName: String = "",
    private val tab: Int = 4
) {

    var location = SourceLocation(fileName, 1, 1)
    var savedChar: Char? = null
    var savedLoc = location.copy()
    var savedToken: Token? = null

    /**
     * Update `location` after having read `ch` from the stream
     */
    fun updatePos(ch: Char?) {

        when {
            ch == null -> return
            ch == '\n' -> {
                location.line += 1
                location.col = 1
            }
            ch == '\t' -> location.col += tab
            else -> location.col += 1
        }
    }

    /**
     * Read a new character from the stream
     */
    fun readChar(): Char? {
        val ch: Char?
        if (savedChar != null) {
            ch = savedChar
            savedChar = null
        } else {
            val r = stream.read()
            ch = if (r != -1) r.toChar() else null
        }

        savedLoc = location.copy()
        this.updatePos(ch)
        return ch
    }

    /**
     * Push a character back to the stream
     */

    fun unreadChar(ch: Char) {
        assertTrue(savedChar == null)
        savedChar = ch
        location = savedLoc.copy()
    }

    /**
     * Keep reading characters until a non-whitespace character is found
     * or a comment
     */
    fun skipWhiteAndComment() {
        var ch: Char? = this.readChar() ?: return
        while (ch in WHITESPACE || ch == '#') {
            // It's a comment! Keep reading until the end of the line (include the case "", the end-of-file)
            if (ch == '#') while (this.readChar() !in listOf(null, '\n', '\t')) continue

            ch = readChar()
            if (ch == null) return
        }
        //Put the non-whitespace character back
        this.unreadChar(ch!!)
    }

    fun parseStringToken(tokenLoc: SourceLocation): StringToken {
        var token = ""
        var ch: Char?
        while (true) {
            ch = this.readChar()
            if (ch == '"') break

            if (ch == null) throw GrammarError(tokenLoc, "Unterminated string")

            token += ch
        }
        return StringToken(tokenLoc, token)
    }

    fun parseFloatToken(firstChar: String, tokenLoc: SourceLocation): LiteralNumberToken {
        var token = firstChar
        var ch: Char?
        while (true) {
            ch = this.readChar()
            if (ch == null) break
            if (!(ch.isDigit() || ch == '.' || ch in listOf('e', 'E'))) {
                this.unreadChar(ch)
                break
            }
            token += ch
        }
        val value: Float
        try {
            value = token.toFloat()
        } catch (e: NumberFormatException) {
            throw GrammarError(tokenLoc, "$token is an invalid floating-point number")
        }
        return LiteralNumberToken(tokenLoc, value)

    }

    fun parseKeywordOrIdentifierToken(firstChar: String, tokenLoc: SourceLocation): Token  {
        var token = firstChar
        var ch: Char?

        while (true) {
            ch = this.readChar()
            if (ch == null) break
            //Digits are ok after the first character
            if (!(ch.isLetterOrDigit() || ch == '_')) {
                this.unreadChar(ch)
                break
            }

            token += ch
        }

        // If it is a keyword, it must be listed in the KEYWORDS dictionary
        // If we got KeyError, it is not a keyword and thus it must be an identifier
        return if (KEYWORDS.containsKey(token)) KeywordToken(tokenLoc, KEYWORDS[token]!!)
        else IdentifierToken(tokenLoc, token)
    }

    /**
     * Read a token from the stream
     * Raise :class:`.ParserError` if a lexical error is found."""
     */

    fun readToken(): Token {
        if (savedToken != null) {
            val result : Token = savedToken!!
            savedToken = null
            return result
        }

        this.skipWhiteAndComment()
        // At this point we're sure that ch does *not* contain a whitespace character
        //Neither it's part of a comment
        val ch = this.readChar() ?: return StopToken(location=location)

        // At this point we must check what kind of token begins with the "ch" character (which has been
        // put back in the stream with unreadChar). First, we save the position in the stream
        val tokenLoc = location.copy()

        //One-character symbol, like '(' or ','
        return if (ch in SYMBOLS) SymbolToken(tokenLoc, ch)

        //A literal string (used for file names)
        else if (ch == '"') this.parseStringToken(tokenLoc)

        // A floating-point number
        else if (ch.isDigit() || ch in listOf('+', '-', '.')) this.parseFloatToken(ch.toString(), tokenLoc)

        //If it begins with an alphabetic character, it must either be a keyword or a identifier
        else if (ch.isLetter() || ch == '_') this.parseKeywordOrIdentifierToken(ch.toString(), tokenLoc)

        //We got some weird character, like '@` or `&`
        else throw GrammarError(location, "Invalid character $ch")
    }

    /**
     * Make as if `token` were never read from `input_file`
     */
    fun unreadToken(token: Token) {
        assertTrue(savedToken== null)
        savedToken = token
    }

}

