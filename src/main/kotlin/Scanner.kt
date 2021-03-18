import exception.LexicalException
import java.io.File

class Scanner(filename: String) {

    private lateinit var content: CharArray
    private var estado: Int = 0
    private var pos: Int = 0
    private var line: Int = 0
    private var column: Int = 0

    private var currentChar: Char = '\u0000'
    private var term: String = ""

    init {
        try {
            content = File(filename).readText().toCharArray()

            println("DEBUG -----------")
            println(content)
            println("-----------------")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun nextToken(): Token? {
        if (isEOF()) {
            return null
        }
        estado = 0
        term = ""
        while (true) {
            currentChar = nextChar()
            when (estado) {
                0 -> estado0()
                1 -> estado1()
                2 -> return estado2()
                3 -> estado3()
                4 -> return estado4()
            }
        }
    }

    private fun estado0() {
        when {
            isChar(currentChar) -> {
                term += currentChar
                estado = 1
            }
            isDigit(currentChar) -> {
                term += currentChar
                estado = 3
            }
            isSpace(currentChar) -> {
                estado = 0
            }
            isOperator(currentChar) -> {
                estado = 5
            }
            else -> throw LexicalException("Unrecognized SYMBOL")
        }
    }

    private fun estado1() {
        when {
            isChar(currentChar) || isDigit(currentChar) -> {
                term += currentChar
                estado = 1
            }
            isSpace(currentChar) || isOperator(currentChar) -> {
                estado = 2
            }
            else -> throw LexicalException("Malformed Identifier")
        }
    }

    private fun estado2(): Token {
        back()
        return Token(TokenTypes.TK_IDENTIFIER.ordinal, term)
    }

    private fun estado3() {
        when {
            isDigit(currentChar) -> {
                term += currentChar
                estado = 3
            }
            !isChar(currentChar) -> {
                estado = 4
            }
            else -> throw LexicalException("Unrecognized Number")
        }
    }

    private fun estado4(): Token {
        back()
        return Token(TokenTypes.TK_NUMBER.ordinal, term)
    }

    //region utils
    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9' || c == '.'
    }

    private fun isChar(c: Char): Boolean {
        return c in 'a'..'z'
    }

    private fun isOperator(c: Char): Boolean {
        val operators = listOf(
            '>', '<', '=', '!', '+', '-', '*', '/'
        )
        return operators.contains(c)
    }

    private fun isSpace(c: Char): Boolean {
        if (c == '\n' || c == '\r') {
            line++
            column = 0
        }
        val spaces = listOf(
            ' ', '\t', '\n', '\r'
        )

        return spaces.contains(c)
    }

    private fun nextChar(): Char {
        return if (isEOF()) {
            '\u0000'
        } else content[pos++]
    }

    private fun isEOF(): Boolean {
        return pos >= content.size
    }

    private fun back() {
        pos--
    }
    //endregion
}