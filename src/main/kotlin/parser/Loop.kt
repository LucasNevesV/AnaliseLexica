package parser

import Scanner
import Token
import exception.SyntaxException

class Loop(private val scanner: Scanner) {
    init {
        loop()
    }

    private fun loop(){
        expectWhile()
        expectOpeningParenthesis()

        ConditionalOperation(scanner)

        expectClosingParenthesis()
        Block(scanner)
    }

    private fun checkWhile(): Boolean {
        return TokenSingleton.text == "while"
    }

    private fun expectWhile() {
        if (!checkWhile()){
            throw SyntaxException("'while' expected, found '${scanner.term}'", scanner.term)
        }
    }

    private fun expectOpeningParenthesis(){
        scanner.nextToken()
        if (TokenSingleton.text != "(") {
            throw SyntaxException("'(' expected, found '${scanner.term}'", scanner.term)
        }
    }

    fun expectClosingParenthesis(){
        if (TokenSingleton.text != ")") {
            throw SyntaxException("')' expected, found '${scanner.term}'", scanner.term)
        }
    }
}