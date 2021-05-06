package parser

import Scanner
import Token
import exception.SyntaxException


class Block(var token: Token?, val scanner: Scanner) {

    init {
        block()
    }

    private fun block() {
        expectedOpenBrackets()

        while (!expectedClosingBrackets()) {
            when (token?.text) {
                "while" -> {
                    Loop(token, scanner)
                }
                "if" -> {
                    ConditionalExpression(token, scanner)
                }
                //Declaration
                "int", "float", "char" -> {
                    Declaration(token, scanner)
                }
            }
        }

    }

    private fun expectedOpenBrackets() {
        token = scanner.nextToken()
        if (token!!.text != "{") {
            throw SyntaxException("'{' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectedClosingBrackets(): Boolean {
        token = scanner.nextToken() ?: throw SyntaxException("Closing '}' expected", scanner.term)
        return token?.text == "}"
    }
}