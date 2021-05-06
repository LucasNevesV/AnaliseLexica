import exception.LexicalException
import exception.SyntaxException
import parser.Parser

fun main(args: Array<String>) {
    try {
        val scanner = Scanner("input.txt")
        val parser = Parser(scanner)

        parser.program()
        println("Compiled successfully !!")
    } catch (ex: LexicalException) {
        println("Lexical ERROR at '${ex.term}'; Message: ${ex.message}")
    } catch (ex: SyntaxException) {
        println("Syntax ERROR at '${ex.term}'; Message: ${ex.message}")
    }
}