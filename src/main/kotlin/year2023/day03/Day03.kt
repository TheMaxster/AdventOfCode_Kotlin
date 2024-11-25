package year2023.day03

import application.Day
import utils.ImportUtils

/**
 * See https://adventofcode.com/2023/day/3
 */
class Day03 : Day() {

    private val VALID_NUMBERS = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    private val ASTERISK = "*"

    override val loggingEnabled: Boolean
        get() = true

    override fun part1(input: List<String>): String {
        val inputArray: Array<Array<String>> = ImportUtils.convertListToArray(input)

        val foundValidNumberDatas = determineValidNumberDatas(inputArray)

        val foundValidNumbers = foundValidNumberDatas.stream().map(FoundNumber::number).toList()
        log("FoundValidNumbers: $foundValidNumbers")
        return foundValidNumbers.sum().toString()
    }

    override fun part2(input: List<String>): String {
        val inputArray: Array<Array<String>> = ImportUtils.convertListToArray(input)
        val asteriskMap: MutableMap<String, FoundAsterisk> = HashMap()

        val foundValidNumberDatas = determineValidNumberDatas(inputArray)
        for (foundValidNumberData in foundValidNumberDatas) {
            checkSurroundingAsterisk(inputArray, foundValidNumberData, asteriskMap)
        }

        val asteriskSummands: MutableList<Int> = ArrayList()
        // log("AsteriskMap:" + asteriskMap);
        for ((_, value) in asteriskMap) {
            // log("AsteriskList:" + stringFoundAsteriskEntry.getValue().getNumbers());

            val numbers: List<Int> = value.numbers
            if (numbers.size == 2) {
                asteriskSummands.add(numbers[0] * numbers[1])
            }
        }

        return asteriskSummands.sum().toString()
    }

    /**
     * Contains all information about the found asterisk.
     */
    private class FoundAsterisk(
        private val line: Int,
        private val column: Int
    ) {
        val foundByNumber: MutableSet<FoundNumber> = HashSet()

        val numbers: MutableList<Int> = ArrayList()
    }

    /**
     * Contains all information about the found number.
     */
    data class FoundNumber(val indexStart: Int, val indexEnd: Int, val line: Int, val number: Int)


    private fun determineValidNumberDatas(inputArray: Array<Array<String>>): List<FoundNumber> {
        val foundValidNumberDatas: MutableList<FoundNumber> = ArrayList()

        for (lineIndex in inputArray.indices) {
            for (columnIndex in inputArray[lineIndex].indices) {
                val foundNumberAsString = lookForNumber(inputArray, lineIndex, columnIndex, StringBuilder())
                if (foundNumberAsString !== "") {
                    val foundNumber = foundNumberAsString.toInt()

                    val indexStart = columnIndex
                    val indexEnd = indexStart + foundNumberAsString.length - 1

                    if (!checkIfNumberHasOtherNumberNeighbours(inputArray, indexStart, indexEnd, lineIndex)) {
                        val isValid = checkSurrounding(inputArray, indexStart, indexEnd, lineIndex)

                        if (isValid) {
                            val foundNumberData = FoundNumber(indexStart, indexEnd, lineIndex, foundNumber)
                            foundValidNumberDatas.add(foundNumberData)
                        }
                    }
                }
            }
        }
        return foundValidNumberDatas
    }

    private fun lookForNumber(
        inputArray: Array<Array<String>>,
        lineIndex: Int,
        columnIndex: Int,
        numberBuilder: StringBuilder
    ): String {
        try {
            val potentialNumber = inputArray[lineIndex][columnIndex]
            if (VALID_NUMBERS.contains(potentialNumber)) {
                //  log("Found number: " + potentialNumber);
                numberBuilder.append(inputArray[lineIndex][columnIndex])
                return lookForNumber(inputArray, lineIndex, columnIndex + 1, numberBuilder)
            }
            return numberBuilder.toString()
        } catch (e: NumberFormatException) {
            return numberBuilder.toString()
        } catch (e: ArrayIndexOutOfBoundsException) {
            return numberBuilder.toString()
        }
    }

    private fun checkIfNumberHasOtherNumberNeighbours(
        array: Array<Array<String>>,
        indexStart: Int,
        indexEnd: Int,
        line: Int
    ): Boolean {
        // Check character to the left via parsing.

        try {
            val charToAnalyze = array[line][indexStart - 1]
            val leftNumber = charToAnalyze.toInt()
            return true
        } catch (e: NumberFormatException) {
            //...
        } catch (e: ArrayIndexOutOfBoundsException) {
        }

        // Check character to the right via parsing.
        try {
            val charToAnalyze = array[line][indexEnd + 1]
            val rightNumber = charToAnalyze.toInt()
            return true
        } catch (e: NumberFormatException) {
            //...
        } catch (e: ArrayIndexOutOfBoundsException) {
        }

        return false
    }

    private fun checkSurrounding(
        array: Array<Array<String>>,
        indexStart: Int,
        indexEnd: Int,
        line: Int
    ): Boolean {
        val rangeList = listOf(-1, 0, 1)

        for (additional in 0..indexEnd - indexStart) {
            for (rangeI in rangeList) {
                for (rangeJ in rangeList) {
                    val i = line + rangeJ
                    val j = indexStart + rangeI + additional

                    try {
                        val charToAnalyze = array[i][j]

                        // log("Check: " + i + " " + j);
                        if (!VALID_NUMBERS.contains(charToAnalyze) && charToAnalyze != ".") {
                            // log("ValidChar: " + charToAnalyze + "(Coords: " + i + " " + j + ")");
                            return true
                        }
                    } catch (e: Exception) {
                        //...
                    }
                }
            }
        }

        return false
    }

    private fun checkSurroundingAsterisk(
        array: Array<Array<String>>,
        foundValidNumberData: FoundNumber,
        asteriskMap: MutableMap<String, FoundAsterisk>
    ): Boolean {
        // log("Check for number: " + foundValidNumberData.getNumber());

        val indexStart = foundValidNumberData.indexStart
        val indexEnd = foundValidNumberData.indexEnd
        val line = foundValidNumberData.line

        val rangeList = listOf(-1, 0, 1)

        // Check the surrounding around the whole string.
        for (additional in 0..indexEnd - indexStart) {
            for (rangeI in rangeList) {
                for (rangeJ in rangeList) {
                    val i = line + rangeJ
                    val j = indexStart + rangeI + additional

                    try {
                        val charToAnalyze = array[i][j]

                        //  log("Check: " + i + " " + j + " : " + charToAnalyze);
                        if (charToAnalyze != ASTERISK) {
                            continue
                        }
                        //    log("Valid Asterisk: " + charToAnalyze + "(Coords: " + i + " " + j + ")");

                        val key = "$i-$j"
                        asteriskMap.computeIfAbsent(key) { FoundAsterisk(i, j) }
                        val asterisk = asteriskMap[key]!!
                        if (!asterisk.foundByNumber.contains(foundValidNumberData)) {
                            asterisk.numbers.add(foundValidNumberData.number)
                            asterisk.foundByNumber.add(foundValidNumberData)
                        }
                    } catch (e: Exception) {
                        //...
                    }
                }
            }
        }

        return false
    }
}
