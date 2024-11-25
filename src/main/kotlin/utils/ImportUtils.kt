package utils

import java.io.File

class ImportUtils {

    companion object {
        fun readAsSequence(filePath: String): Sequence<String> {
            return File(filePath).bufferedReader().lineSequence()
        }

        fun readAsList(filePath: String): List<String> {
            return File(filePath).bufferedReader().lineSequence().toList()
        }

        fun convertListToArray(list: List<String>): Array<Array<String>> {
            val array: Array<Array<String>> = Array(list.size) { emptyArray() }

            for (i in list.indices) {
                array[i] = list[i].toCharArray().map { it.toString() }.toTypedArray()
            }

            return array
        }

    }
}
