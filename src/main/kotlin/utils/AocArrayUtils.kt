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

        fun isWithinBounds(matrix: Array<Array<String>>, coordinate: Coordinate): Boolean {
            if (matrix.size == 0 || matrix[0].size == 0) {
                return false
            }

            val x = coordinate.x
            val y = coordinate.y

            return x >= 0 && x < matrix.size && y >= 0 && y < matrix[0].size
        }

        fun removeFirstOccurrence(array: Array<String>, target: String): Array<String> {
            val index = array.indexOfFirst { it == target }
            if (index == -1) { // When not found, return original array
                return array
            }

            return array.sliceArray(0 until index) + array.sliceArray(index + 1 until array.size)
        }

        fun removeFirstOccurrence(array: Array<Long>, target: Long): Array<Long> {
            val index = array.indexOfFirst { it == target }
            if (index == -1) { // When not found, return original array
                return array
            }

            return array.sliceArray(0 until index) + array.sliceArray(index + 1 until array.size)
        }

        fun filterSurroundingCoordinates(
            map: Array<Array<String>>,
            coords: Coordinate,
            target: String
        ): List<Coordinate> {
            // Check boundaries to avoid ArrayIndexOutOfBoundsException
            val rows = map.size
            val cols = map[0].size

            val x: Int = coords.x
            val y: Int = coords.y

            val result: MutableList<Coordinate> = java.util.ArrayList()

            // Check the left cell
            if (x > 0 && map[x - 1][y] == target) {
                result.add(Coordinate(x - 1, y))
            }

            // Check the right cell
            if (x < rows - 1 && map[x + 1][y] == target) {
                result.add(Coordinate(x + 1, y))
            }

            // Check the top cell
            if (y > 0 && map[x][y - 1] == target) {
                result.add(Coordinate(x, y - 1))
            }

            // Check the bottom cell
            if (y < cols - 1 && map[x][y + 1] == target) {
                result.add(Coordinate(x, y + 1))
            }

            return result
        }
    }




}
