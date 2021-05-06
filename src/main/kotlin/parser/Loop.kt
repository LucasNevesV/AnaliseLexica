package parser

import Scanner
import Token
import exception.SyntaxException

class Loop(private var token: Token?, private val scanner: Scanner) {
    init {
        loop()
    }

    private fun loop(){
        expectWhile()
        expectOpeningParenthesis()

        ConditionalOperation(token,scanner)

        expectClosingParenthesis()
        Block(token, scanner)
    }

    private fun checkWhile(): Boolean {
        return token!!.text == "while"
    }

    private fun expectWhile() {
        if (!checkWhile()){
            throw SyntaxException("'while' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectOpeningParenthesis(){
        token = scanner.nextToken()
        if (token!!.text != "(") {
            throw SyntaxException("'(' expected, found '${scanner.term}'", scanner.term)
        }
    }

    fun expectClosingParenthesis(){
        token = scanner.nextToken()
        if (token!!.text != ")") {
            throw SyntaxException("')' expected, found '${scanner.term}'", scanner.term)
        }
    }
}