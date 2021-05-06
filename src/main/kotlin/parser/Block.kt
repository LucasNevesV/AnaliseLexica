package parser

import Scanner
import Token
import exception.SyntaxException


class Block(val scanner: Scanner) {

    init {
        block()
    }

    var jump: Boolean = false

    private fun block() {
        expectedOpenBrackets()

        while (!expectedClosingBrackets()) {
            val tokenText = TokenSingleton.text
            when {
                TokenSingleton.type == TokenTypes.TK_IDENTIFIER -> {
                    Attribution(scanner)
                }
                tokenText == "while" -> {
                    Loop(scanner)
                }
                tokenText == "if" -> {
                    ConditionalExpression(scanner)
                    jump = true
                }
                //Declaration
                tokenText == "int" || tokenText == "float" || tokenText == "char" -> {
                    Declaration(scanner)
                }
            }
        }

    }

    private fun expectedOpenBrackets() {
        scanner.nextToken()
        if (TokenSingleton.text != "{") {
            throw SyntaxException("'{' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectedClosingBrackets(): Boolean {
        if(!jump) scanner.nextToken()
        jump = false
        TokenSingleton.text ?: throw SyntaxException("Closing '}' expected", scanner.term)
        return TokenSingleton.text == "}"
    }
}