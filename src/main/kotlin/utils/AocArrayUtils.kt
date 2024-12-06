package main.kotlin.utils

class AocArrayUtils {

    companion object {
        fun findAllOccurences(
            matrix: Array<Array<String>>,
            letter: String
        ): List<Coordinate> {
            val occurences: MutableList<Coordinate> = ArrayList()
            for (i in matrix.indices) {
                for (j in matrix[i].indices) {
                    if (matrix[i][j] == letter) {
                        occurences.add(Coordinate(i, j))
                    }
                }
            }
            return occurences
        }
    }

}
