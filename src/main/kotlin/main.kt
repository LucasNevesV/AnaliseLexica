import exception.LexicalException
import exception.SemanticException
import exception.SyntaxException
import parser.Parser
import parser.Symbols

fun main(args: Array<String>) {
    try {
        val scanner = Scanner("input.txt")
        val parser = Parser(scanner)

        parser.program()
        println("Compiled successfully !!")

        println(Symbols.value)
    } catch (ex: LexicalException) {
        println("Lexical ERROR at '${ex.term}'; Message: ${ex.message}")
    } catch (ex: SyntaxException) {
        println("Syntax ERROR at '${ex.term}'; Message: ${ex.message}")
    } catch (ex: SemanticException) {
        println("Semantic ERROR at '${ex.term}'; Message: ${ex.message}")
        println(Symbols.value)
    }
}