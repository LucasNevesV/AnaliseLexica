package exception

import java.lang.RuntimeException

class LexicalException (override val message: String, val term: String) : RuntimeException()