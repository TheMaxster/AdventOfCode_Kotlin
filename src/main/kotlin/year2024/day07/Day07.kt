package year2024.day07

import application.Day
import main.kotlin.utils.AocArrayUtils
import java.util.*

/**
 * See https://adventofcode.com/2024/day/7
 */
class Day07 : Day() {

    override val loggingEnabled: Boolean
        get() = false

    val FILE_PATH: String = "src/main/resources/year2024/day07/input_test_01.txt"

    override fun part1(input: List<String>): String {
        val calculations = parseInput(input)
        log("Original amount: ${calculations.size}")

        val validSolutions = calculations.mapNotNull { doCalculations(it, false) }
        log("Valid solutions: ${validSolutions.size}")

        return validSolutions.sumOf { it.result }.toString()
    }

    override fun part2(input: List<String>): String {
        val calculations = parseInput(input)
        log("Original amount: ${calculations.size}")

        val validSolutions = calculations.mapNotNull { doCalculations(it, true) }
        log("Valid solutions: ${validSolutions.size}")

        return validSolutions.sumOf { it.result }.toString()
    }

    private fun doCalculations(calculation: Calculation, activatePart2Operator: Boolean): Calculation? {
        val queue = initializeQueue(calculation)
        return if (processCalculation(queue, activatePart2Operator)) {
            calculation
        } else {
            null
        }
    }

    private fun processCalculation(
        queue: Queue<QueueElement>,
        activatePart2Operator: Boolean
    ): Boolean {
        while (!queue.isEmpty()) {
            val qel = queue.poll()

            val tmpVal: Long = qel.tmp
            val resultVal: Long = qel.result
            if (tmpVal > resultVal) {
                continue  // We are on the wrong path.
            }

            val remainingValues: Array<Long> = qel.values
            if (tmpVal == resultVal && (remainingValues.isEmpty())) {
                log(qel.calc)
                return true // We found a solution.
            }

            if (remainingValues.isEmpty()) {
                continue  // We are on the wrong path.
            }

            val numberForMath = remainingValues[0]
            val reducedValues: Array<Long> = AocArrayUtils.removeFirstOccurrence(remainingValues, numberForMath)

            // Doing + operator
            queue += (QueueElement(
                values = reducedValues,
                result = resultVal,
                tmp = qel.tmp + numberForMath,
                calc = "${qel.calc} + $numberForMath"
            ))

            // Doing * operator
            queue += QueueElement(
                values = reducedValues,
                result = resultVal,
                tmp = qel.tmp * numberForMath,
                calc = "${qel.calc} * $numberForMath"

            )

            // Part 2 operator
            if (activatePart2Operator) {
                // Doing || operator
                queue += QueueElement(
                    values = reducedValues,
                    result = resultVal,
                    tmp = "${qel.tmp}$numberForMath".toLong(),
                    calc = "${qel.calc} || $numberForMath"
                )
            }
        }

        return false
    }

    private fun initializeQueue(calculation: Calculation): Queue<QueueElement> {
        val result: Long = calculation.result
        val values: Array<Long> = calculation.values

        val queue: Queue<QueueElement> = ArrayDeque()
        queue.add(
            QueueElement(
                values = AocArrayUtils.removeFirstOccurrence(values, values[0]),
                result = result,
                tmp = values[0],
                calc = values[0].toString()
            )
        )
        return queue
    }

    private fun parseInput(input: List<String>): List<Calculation> {
        return input.map { line ->
            val (resultPart, valuesPart) = line.split(":").map(String::trim)
            Calculation(
                values = valuesPart.split(" ")
                    .filter { it.isNotEmpty() && it.all(Char::isDigit) }
                    .map(String::toLong)
                    .toTypedArray(),
                result = resultPart.toLong()
            )
        }
    }

    data class Calculation(
        var values: Array<Long>,
        var result: Long
    )

    data class QueueElement(
        var values: Array<Long>,
        var result: Long,
        var tmp: Long,
        var calc: String
    )

}
