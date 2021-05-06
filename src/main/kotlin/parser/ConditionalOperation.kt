package parser

import Scanner
import Token
import exception.SyntaxException

class ConditionalOperation(private var token: Token?, private val scanner: Scanner) {
    init {
        conditional()
    }

    private fun conditional(){
        Arithmetic(token, scanner)

        expectRelationalOperator()
        Arithmetic(token, scanner)
    }

    private fun checkWhile(): Boolean {
        return token!!.text == "while"
    }

    private fun expectRelationalOperator(){
        token = scanner.cuurentToken
        if (token!!.type != TokenTypes.TK_RELATIONAL_OPERATOR) {
            throw SyntaxException("'relational operator' expected, found '${scanner.term}'", scanner.term)
        }
    }
}