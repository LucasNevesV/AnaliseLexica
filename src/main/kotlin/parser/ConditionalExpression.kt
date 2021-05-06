package parser

import Scanner
import Token
import TokenSingleton
import exception.SyntaxException

class ConditionalExpression(private val scanner: Scanner) {
    init {
        conditionalExpression()
    }

    private fun conditionalExpression(){
        expectIf()
        expectOpeningParenthesis()
        ConditionalOperation(scanner)
        expectClosingParenthesis()
        Block(scanner)

        if (elseExists()){
            Block(scanner)
        }
    }

    private fun expectIf(){
        if (TokenSingleton.type != TokenTypes.TK_RELATIONAL_OPERATOR && TokenSingleton.text != "if") {
            throw SyntaxException("'if' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun elseExists(): Boolean {
        scanner.nextToken()
        if (TokenSingleton.text == "else") {
            return true
        }

        return false
    }

    private fun expectOpeningParenthesis(){
        scanner.nextToken()
        if (TokenSingleton.text != "(") {
            throw SyntaxException("'(' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectClosingParenthesis(){
        if (TokenSingleton.text != ")") {
            throw SyntaxException("')' expected, found '${scanner.term}'", scanner.term)
        }
    }
}