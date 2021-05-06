package parser

import Scanner
import Token
import exception.SyntaxException

class Parser(private val scanner: Scanner) {


    fun program() {
        val token = scanner.nextToken()

        TokenSingleton.init(token!!)

        if (TokenSingleton.text != "int") {
            throw SyntaxException("'int' expected, found '${scanner.term}'", scanner.term)
        }

        scanner.nextToken()
        if (TokenSingleton.text != "main") {
            throw SyntaxException("'main' expected, found '${scanner.term}'", scanner.term)
        }
        scanner.nextToken()
        if (TokenSingleton.text != "(") {
            throw SyntaxException("'(' expected, found '${scanner.term}'", scanner.term)
        }
        scanner.nextToken()
        if (TokenSingleton.text != ")") {
            throw SyntaxException("')' expected, found '${scanner.term}'", scanner.term)
        }

        Block(scanner)
    }
}