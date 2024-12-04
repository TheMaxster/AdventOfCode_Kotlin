package year2024.day04

import application.Day
import utils.ImportUtils

/**
 * See https://adventofcode.com/2024/day/4
 */
class Day04 : Day() {

    override val loggingEnabled: Boolean
        get() = true

    val FILE_PATH: String = "src/main/resources/year2024/day04/input_test_01.txt"


    override fun part1(input: List<String>): String {
//        val testInput = ImportUtils.readAsList(FILE_PATH);
        val matrix = ImportUtils.convertListToArray(input)

        val findings = findAllOccurences(matrix, "X")
            .sumOf { x: Pair<Int, Int> -> checkLetterInNeighborhood(x, matrix) }

        return findings.toString()
    }

    private fun checkLetterInNeighborhood(
        xCoordinate: Pair<Int, Int>,
        matrix: Array<Array<String>>
    ): Int {
        return (checkMas(xCoordinate, matrix, -1 to 0, -2 to 0, -3 to 0)
                + checkMas(xCoordinate, matrix, -1 to -1, -2 to -2, -3 to -3)
                + checkMas(xCoordinate, matrix, 0 to -1, 0 to -2, 0 to -3)
                + checkMas(xCoordinate, matrix, 1 to -1, 2 to -2, 3 to -3)
                + checkMas(xCoordinate, matrix, 1 to 0, 2 to 0, 3 to 0)
                + checkMas(xCoordinate, matrix, 1 to 1, 2 to 2, 3 to 3)
                + checkMas(xCoordinate, matrix, 0 to 1, 0 to 2, 0 to 3)
                + checkMas(xCoordinate, matrix, -1 to 1, -2 to 2, -3 to 3))
    }

    private fun checkMas(
        xCoordinate: Pair<Int, Int>,
        matrix: Array<Array<String>>,
        mPair: Pair<Int, Int>,
        aPair: Pair<Int, Int>,
        sPair: Pair<Int, Int>,
    ): Int {
        try {
            if (matrix[xCoordinate.first + mPair.first][xCoordinate.second + mPair.second] == "M"
                && matrix[xCoordinate.first + aPair.first][xCoordinate.second + aPair.second] == "A"
                && matrix[xCoordinate.first + sPair.first][xCoordinate.second + sPair.second] == "S"
            ) {
                return 1
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            // Nothing to do here.
        }
        return 0
    }

    private fun findAllOccurences(
        matrix: Array<Array<String>>,
        letter: String
    ): List<Pair<Int, Int>> {
        val occurences: MutableList<Pair<Int, Int>> = ArrayList()
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                if (matrix[i][j] == letter) {
                    occurences.add(Pair(i, j))
                }
            }
        }
        return occurences
    }

    override fun part2(input: List<String>): String {
        //        val testInput = ImportUtils.readAsList(FILE_PATH);
        val matrix = ImportUtils.convertListToArray(input)

        val findings = findAllOccurences(matrix, "A")
            .sumOf { a: Pair<Int, Int> -> checkLetterInNeighborhoodPart2(a, matrix) }

        return findings.toString()
    }


    private fun checkLetterInNeighborhoodPart2(
        aCoordinate: Pair<Int, Int>,
        matrix: Array<Array<String>>
    ): Int {
        return (checkMasPart2(aCoordinate, matrix, -1 to -1, -1 to 1, 1 to 1, 1 to -1)
                + checkMasPart2(aCoordinate, matrix, -1 to 1, 1 to 1, 1 to -1, -1 to -1)
                + checkMasPart2(aCoordinate, matrix, 1 to 1, 1 to -1, -1 to -1, -1 to 1)
                + checkMasPart2(aCoordinate, matrix, 1 to -1, -1 to -1, -1 to 1, 1 to 1))
    }

    private fun checkMasPart2(
        xCoordinate: Pair<Int, Int>,
        matrix: Array<Array<String>>,
        m1Pair: Pair<Int, Int>,
        m2Pair: Pair<Int, Int>,
        s1Pair: Pair<Int, Int>,
        s2Pair: Pair<Int, Int>
    ): Int {
        try {
            if (matrix[xCoordinate.first + m1Pair.first][xCoordinate.second + m1Pair.second] == "M"
                && matrix[xCoordinate.first + m2Pair.first][xCoordinate.second + m2Pair.second] == "M"
                && matrix[xCoordinate.first + s1Pair.first][xCoordinate.second + s1Pair.second] == "S"
                && matrix[xCoordinate.first + s2Pair.first][xCoordinate.second + s2Pair.second] == "S"
            ) {
                return 1
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            // Nothing to do here.
        }
        return 0
    }


}
