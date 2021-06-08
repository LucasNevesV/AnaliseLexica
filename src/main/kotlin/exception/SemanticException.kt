package exception

import java.lang.RuntimeException

class SemanticException (override val message: String, val term: String) : RuntimeException()