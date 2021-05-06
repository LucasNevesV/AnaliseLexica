package parser

import Scanner
import Token
import exception.SyntaxException

class Parser(private val scanner: Scanner) {

    private var token: Token? = null

    fun program() {
        token = scanner.nextToken()

        if (token!!.text != "int") {
            throw SyntaxException("'int' expected, found '${scanner.term}'", scanner.term)
        }

        token = scanner.nextToken()
        if (token!!.text != "main") {
            throw SyntaxException("'main' expected, found '${scanner.term}'", scanner.term)
        }
        token = scanner.nextToken()
        if (token!!.text != "(") {
            throw SyntaxException("'(' expected, found '${scanner.term}'", scanner.term)
        }
        token = scanner.nextToken()
        if (token!!.text != ")") {
            throw SyntaxException("')' expected, found '${scanner.term}'", scanner.term)
        }

        Block(token, scanner)
    }

    //region Expressão Aritmetica
    fun expressaoAritmetica() {
        operando()
        expressaoAritmeticaLoop()
    }

    fun expressaoAritmeticaLoop() {
        token = scanner.nextToken()
        if (token != null) {
            operador()
            operando()
            expressaoAritmeticaLoop()
        }
    }

    fun operando() {
        token = scanner.nextToken()
        if (token!!.type != TokenTypes.TK_IDENTIFIER && token!!.type != TokenTypes.TK_NUMBER) {
            throw SyntaxException("ID or NUMBER Expected!, found " + token!!.type, scanner.term)
        }
    }

    fun operador() {
        if (token!!.type != TokenTypes.TK_ARITHMETIC_OPERATOR && token!!.type != TokenTypes.TK_RELATIONAL_OPERATOR) {
            throw SyntaxException(
                "Operator Expected, found " + token!!.type + " (" + token!!.text + ")", scanner.term
            )
        }
    }
    //endregion
}