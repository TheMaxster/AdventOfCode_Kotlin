package year2023

import java.io.File

class ImportUtils {

    companion object {
        fun readAsSequence(filePath: String): Sequence<String> {
            return File(filePath).bufferedReader().lineSequence()
        }

        fun readAsList(filePath: String): List<String> {
            return File(filePath).bufferedReader().lineSequence().toList()
        }

    }
}
