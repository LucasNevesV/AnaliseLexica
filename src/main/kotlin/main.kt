import exception.LexicalException

fun main(args: Array<String>) {
    try {
        val scanner = Scanner("input.txt")
        var token: Token?

        do {
            token = scanner.nextToken()

            if (token != null) {
                println(token)
            }
        } while (token != null)
    } catch (ex: LexicalException) {
        println("Lexical ERROR at '${ex.term}'; Message: ${ex.message}")
    }
}