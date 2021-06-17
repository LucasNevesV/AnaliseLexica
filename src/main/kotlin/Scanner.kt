import exception.LexicalException
import java.io.File

class Scanner(filename: String) {

    private lateinit var content: CharArray
    private var estado: Int = 0
    var pos: Int = 0

    private var currentChar: Char = '\u0000'
    var term: String = ""

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

        var token: Token? = null
        while (token == null) {
            currentChar = nextChar()
            when (estado) {
                0 -> {
                    if (isEOF()){
                        break
                    }
                    token = estado0()
                }
                1 -> token = estado1()
                2 -> token = estado2()
                3 -> token = estado3()
            }
        }
        TokenSingleton.init(token)
        return token
    }

    //Estado Inicial
    private fun estado0(): Token? {
        when {
            !isChar(currentChar) && specialChars(currentChar.toString())-> {
                term += currentChar
                return Token(TokenTypes.TK_SPECIAL_CHAR, term)
            }
            isChar(currentChar) || specialChars(currentChar.toString()) || currentChar == '_' -> {
                term += currentChar
                estado = 1
            }
            isDigit(currentChar) -> {
                term += currentChar
                estado = 2
            }
            isSpace(currentChar) -> {
                estado = 0
            }
            isOperator(currentChar) || isArithmeticOperator(currentChar.toString()) -> {
                term += currentChar
                estado = 3
            }
            else -> throw LexicalException("Unrecognized SYMBOL", term + currentChar)
        }
        return null
    }

    //Estado Identificadores
    private fun estado1(): Token? {
        when {
            specialChars(term) -> {
                if (!isEOF(currentChar))
                    back()
                return Token(TokenTypes.TK_SPECIAL_CHAR, term)
            }
            isChar(currentChar) || isDigit(currentChar) || currentChar == '_' -> {
                term += currentChar
                estado = 1
            }
            isSpace(currentChar) || isOperator(currentChar) || isArithmeticOperator(currentChar.toString()) || isEOF(currentChar) || specialChars(currentChar.toString()) -> {
                if (!isEOF(currentChar))
                    back()

                return when {
                    reservedWords(term) -> Token(TokenTypes.TK_RESERVED_WORD, term)
                    specialChars(term) -> Token(TokenTypes.TK_SPECIAL_CHAR, term)
                    else -> Token(TokenTypes.TK_IDENTIFIER, term)
                }
            }
            else -> throw LexicalException("Malformed Identifier", term + currentChar)
        }
        return null
    }

    //Estado Numeros(Inteiro e Float)
    private fun estado2(): Token? {
        when {
            isDigit(currentChar) || currentChar == '.' -> {
                term += currentChar
                estado = 2
            }
            !isChar(currentChar) || isEOF(currentChar) -> {
                if (!isEOF(currentChar))
                    back()
                if(term.contains('.')){
                    return Token(TokenTypes.TK_FLOAT, term)
                }
                return Token(TokenTypes.TK_NUMBER, term)
            }
            else -> throw LexicalException("Unrecognized Number", term + currentChar)
        }
        return null
    }

    //Estado Operadores()
    private fun estado3(): Token? {
        when {
            isOperator(currentChar) -> {
                term += currentChar
                estado = 3
            }
            isChar(currentChar) || isDigit(currentChar) || isSpace(currentChar) || isEOF(currentChar) -> {
                if (!isEOF(currentChar))
                    back()

                return if (isArithmeticOperator(term)) {
                    Token(TokenTypes.TK_ARITHMETIC_OPERATOR, term)
                } else {
                    Token(TokenTypes.TK_RELATIONAL_OPERATOR, term)
                }
            }
            else -> throw LexicalException("Unrecognized Operator", term + currentChar)
        }
        return null
    }

    //region utils
    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun isChar(c: Char): Boolean {
        return c in 'a'..'z'
    }

    private fun isOperator(c: Char): Boolean {
        val operators = listOf(
            '>', '<', '=', '!', "<=", ">=", "==", "!="
        )

        if (term.isNotBlank())
            return operators.contains(term + c)

        return operators.contains(c)
    }

    private fun isArithmeticOperator(text: String): Boolean {
        val operators = listOf(
            "+", "-", "*", "/"
        )
        return operators.contains(text)
    }

    private fun isSpace(c: Char): Boolean {
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

    private fun isEOF(c: Char): Boolean {
        return c == '\u0000'
    }

    private fun reservedWords(text: String): Boolean {
        val operators = listOf(
            "main", "if", "else", "while", "do", "for", "int", "float", "char"
        )
        return operators.contains(text)
    }

    private fun specialChars(text: String): Boolean {
        val operators = listOf(
            ")", "(", "{", "}", ",", ";"
        )
        return operators.contains(text)
    }

    private fun back() {
        pos--
    }
    //endregion
}