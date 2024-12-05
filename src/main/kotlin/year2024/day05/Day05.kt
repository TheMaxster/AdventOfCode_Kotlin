package year2024.day05

import application.Day
import utils.ImportUtils
import java.util.*

/**
 * See https://adventofcode.com/2024/day/5
 */
class Day05 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day05/input_test_01.txt"

    override fun part1(input: List<String>): String {
        // val importInput = ImportUtils.readAsList(FILE_PATH);

        val rules: MutableMap<Int, MutableList<Int>> = HashMap()
        val updates: MutableList<List<Int>> = ArrayList()
        parseInput(input, rules, updates)

        val validUpdates = updates.filter { isUpdateValid(it, rules) }

        val sum = validUpdates.sumOf { it[it.size / 2] }
        return sum.toString()
    }

    override fun part2(input: List<String>): String {
        // val importInput = ImportUtils.readAsList(FILE_PATH);

        val rules: MutableMap<Int, MutableList<Int>> = HashMap()
        val updates: MutableList<List<Int>> = ArrayList()
        parseInput(input, rules, updates)

        val invalidUpdates = updates.filter { !isUpdateValid(it, rules) }
        val validUpdates = sortInvalidUpdates(invalidUpdates, rules)

        val sum = validUpdates.sumOf { it[it.size / 2] }
        return sum.toString()
    }


    private fun isUpdateValid(update: List<Int>, rules: Map<Int, List<Int>>): Boolean {
        return update.zipWithNext().all { (current, next) ->
            rules[current]?.contains(next) ?: false
        }
    }

    private fun parseInput(
        input: List<String>,
        rules: MutableMap<Int, MutableList<Int>>,
        updates: MutableList<List<Int>>
    ) {
        for (line in input) {
            when {
                "|" in line -> {
                    val (key, value) = line.split("|").map { it.toInt() }
                    rules.computeIfAbsent(key) { mutableListOf() }.add(value)
                }

                "," in line -> {
                    updates.add(line.split(",").map { it.toInt() })
                }
            }
        }

        log("Rules: $rules")
        log("Updates: $updates")
    }


    private fun sortInvalidUpdates(
        invalidUpdates: List<List<Int>>,
        rules: Map<Int, MutableList<Int>>
    ): List<List<Int>> {
        return invalidUpdates.stream().map { u: List<Int> -> backtrackAsQueue(u, rules) }.toList()
    }

    private fun backtrackAsQueue(
        invalidUpdate: List<Int>,
        rules: Map<Int, List<Int>>

    ): List<Int> {
        val queue: Queue<Node> = ArrayDeque()
        invalidUpdate.forEachIndexed { index, value ->
            queue.add(Node(listOf(index), listOf(value)))
        }

        while (!queue.isEmpty()) {
            val (visited, temp) = queue.poll()

            if (visited.size == invalidUpdate.size) {
                log("Solution: $temp")
                return temp // We got a solution.
            }

            for (i in invalidUpdate.indices) {
                // We find no rules, or we already visited the node -> we skip to the next
                val nextValue = invalidUpdate[i];
                val nextNodes: List<Int>? = rules[temp.last()]
                if (visited.contains(i) || nextNodes == null || nextValue !in nextNodes) {
                    continue
                }

                queue.add(
                    Node(
                        visited + i,
                        temp + nextValue
                    )
                )
            }
        }

        return emptyList()
    }

    data class Node(
        val visited: List<Int>,
        val temp: List<Int>
    )

    override val loggingEnabled: Boolean
        get() = true
}
