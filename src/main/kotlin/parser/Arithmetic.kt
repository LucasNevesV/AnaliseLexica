package parser

import Scanner
import Token
import exception.SyntaxException

class Arithmetic(var token: Token?, val scanner: Scanner) {
    init {
        arithmetic()
    }

    private fun arithmetic() {
        expectNumberOrIdentifier()
        arithmeticLoop()
    }

    private fun arithmeticLoop() {
        token = scanner.nextToken()
        if (token != null && checkCompatibility()) {
            expectArithmeticOperator()
            expectNumberOrIdentifier()
            arithmeticLoop()
        }
    }

    fun expectArithmeticOperator() {
        if (token!!.type != TokenTypes.TK_ARITHMETIC_OPERATOR && token!!.type != TokenTypes.TK_RELATIONAL_OPERATOR) {
            throw SyntaxException(
                "Operator Expected, found " + token!!.type + " (" + token!!.text + ")", scanner.term
            )
        }
    }

    private fun expectNumberOrIdentifier() {
        token = scanner.nextToken()
        if (token!!.type != TokenTypes.TK_IDENTIFIER && token!!.type != TokenTypes.TK_NUMBER) {
            throw SyntaxException("'identifier or number Expected' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun checkCompatibility(): Boolean {
        return token!!.type == TokenTypes.TK_IDENTIFIER || token!!.type == TokenTypes.TK_NUMBER ||
                (token!!.type == TokenTypes.TK_ARITHMETIC_OPERATOR && token!!.text != "=")
    }
}