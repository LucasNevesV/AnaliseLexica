package parser

import Scanner
import Token
import exception.SyntaxException

class Arithmetic(val scanner: Scanner) {
    init {
        arithmetic()
    }

    private fun arithmetic() {
        expectNumberOrIdentifier()
        arithmeticLoop()
    }

    private fun arithmeticLoop() {
        val token = scanner.nextToken()
        if (token != null && checkCompatibility()) {
            expectArithmeticOperator()
            expectNumberOrIdentifier()
            arithmeticLoop()
        }
    }

    fun expectArithmeticOperator() {
        if (TokenSingleton.type != TokenTypes.TK_ARITHMETIC_OPERATOR && TokenSingleton.type != TokenTypes.TK_RELATIONAL_OPERATOR) {
            throw SyntaxException(
                "Operator Expected, found " + TokenSingleton.type + " (" + TokenSingleton.text + ")", scanner.term
            )
        }
    }

    private fun expectNumberOrIdentifier() {
        scanner.nextToken()
        if (TokenSingleton.type != TokenTypes.TK_IDENTIFIER && TokenSingleton.type != TokenTypes.TK_NUMBER) {
            throw SyntaxException("'identifier or number Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun checkCompatibility(): Boolean {
        return TokenSingleton.type == TokenTypes.TK_IDENTIFIER || TokenSingleton.type == TokenTypes.TK_NUMBER ||
                (TokenSingleton.type == TokenTypes.TK_ARITHMETIC_OPERATOR && TokenSingleton.text != "=")
    }
}