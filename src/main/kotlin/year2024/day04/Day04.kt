package year2024.day04

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
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

        return AocArrayUtils.findAllOccurences(matrix, "X")
            .sumOf { checkLetterInNeighborhood(it, matrix) }.toString()
    }

    private fun checkLetterInNeighborhood(
        xCoordinate: Coordinate,
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
        xCoordinate: Coordinate,
        matrix: Array<Array<String>>,
        mPair: Pair<Int, Int>,
        aPair: Pair<Int, Int>,
        sPair: Pair<Int, Int>,
    ): Int {
        try {
            if (matrix[xCoordinate.x + mPair.first][xCoordinate.y + mPair.second] == "M"
                && matrix[xCoordinate.x + aPair.first][xCoordinate.y + aPair.second] == "A"
                && matrix[xCoordinate.x + sPair.first][xCoordinate.y + sPair.second] == "S"
            ) {
                return 1
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            // Nothing to do here.
        }
        return 0
    }

    override fun part2(input: List<String>): String {
        //        val testInput = ImportUtils.readAsList(FILE_PATH);
        val matrix = ImportUtils.convertListToArray(input)

        return AocArrayUtils.findAllOccurences(matrix, "A")
            .sumOf { checkLetterInNeighborhoodPart2(it, matrix) }.toString()
    }


    private fun checkLetterInNeighborhoodPart2(
        aCoordinate: Coordinate,
        matrix: Array<Array<String>>
    ): Int {
        return (checkMasPart2(aCoordinate, matrix, -1 to -1, -1 to 1, 1 to 1, 1 to -1)
                + checkMasPart2(aCoordinate, matrix, -1 to 1, 1 to 1, 1 to -1, -1 to -1)
                + checkMasPart2(aCoordinate, matrix, 1 to 1, 1 to -1, -1 to -1, -1 to 1)
                + checkMasPart2(aCoordinate, matrix, 1 to -1, -1 to -1, -1 to 1, 1 to 1))
    }

    private fun checkMasPart2(
        xCoordinate: Coordinate,
        matrix: Array<Array<String>>,
        m1Pair: Pair<Int, Int>,
        m2Pair: Pair<Int, Int>,
        s1Pair: Pair<Int, Int>,
        s2Pair: Pair<Int, Int>
    ): Int {
        try {
            if (matrix[xCoordinate.x + m1Pair.first][xCoordinate.y + m1Pair.second] == "M"
                && matrix[xCoordinate.x + m2Pair.first][xCoordinate.y + m2Pair.second] == "M"
                && matrix[xCoordinate.x + s1Pair.first][xCoordinate.y + s1Pair.second] == "S"
                && matrix[xCoordinate.x + s2Pair.first][xCoordinate.y + s2Pair.second] == "S"
            ) {
                return 1
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            // Nothing to do here.
        }
        return 0
    }


}
