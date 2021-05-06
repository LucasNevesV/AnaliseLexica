package parser

import Scanner
import Token
import exception.SyntaxException

class ConditionalExpression(private var token: Token?, private val scanner: Scanner) {
    init {
        conditionalExpression()
    }

    private fun conditionalExpression(){
        expectIf()
        expectOpeningParenthesis()
        ConditionalOperation(token, scanner)
        expectClosingParenthesis()
        Block(token, scanner)

        if (elseExists()){
            Block(token, scanner)
        }
    }

    private fun expectIf(){
        if (token!!.type != TokenTypes.TK_RELATIONAL_OPERATOR && token!!.text != "if") {
            throw SyntaxException("'if' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun elseExists(): Boolean {
        token = scanner.nextToken()
        if (token!!.text == "else") {
            return true
        }

        return false
    }

    private fun expectOpeningParenthesis(){
        token = scanner.nextToken()
        if (token!!.text != "(") {
            throw SyntaxException("'(' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectClosingParenthesis(){
        token = scanner.nextToken()
        if (token!!.text != ")") {
            throw SyntaxException("')' expected, found '${scanner.term}'", scanner.term)
        }
    }
}