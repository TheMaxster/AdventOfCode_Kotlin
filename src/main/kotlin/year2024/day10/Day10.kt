package year2024.day10

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
import utils.ImportUtils
import java.util.*

/**
 * See https://adventofcode.com/2024/day/10
 */
class Day10 : Day() {

    override val loggingEnabled: Boolean
        get() = false

    val FILE_PATH: String = "src/main/resources/year2024/day10/input_test_01.txt"

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val map = ImportUtils.convertListToArray(importInput)
        logMap(map)

        val completeWays: Set<List<Coordinate>> = calculateAllCompleteWays(map)

        val uniquePaths = completeWays
            .map { path -> "${path.first()}${path.last()}" }
            .distinct()
            .count()

        return uniquePaths.toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val map = ImportUtils.convertListToArray(importInput)
        logMap(map)

        val completeWays: Set<List<Coordinate>> = calculateAllCompleteWays(map)
        return completeWays.size.toString()
    }

    private fun calculateAllCompleteWays(map: Array<Array<String>>): Set<List<Coordinate>> {
        val allWays: Queue<List<Coordinate>> = ArrayDeque<List<Coordinate>>()

        // Initialize queue with all "0" coordinates
        AocArrayUtils.findAllOccurences(map, "0").forEach { zero ->
            allWays.add(listOf(zero))
        }

        val completeWays: MutableSet<List<Coordinate>> = HashSet<List<Coordinate>>()

        while (allWays.isNotEmpty()) {
            val way: List<Coordinate> = allWays.poll()
            val last: Coordinate = way[way.size - 1]

            // Get next height and surrounding coordinates
            val nextHeight = getNextHeight(map, last)
            val nextHeights = AocArrayUtils.filterSurroundingCoordinates(map, last, nextHeight)

            nextHeights.forEach { next ->
                val newPath = way + next
                if (nextHeight == "9") {
                    completeWays.add(newPath)
                } else {
                    allWays.add(newPath)
                }
            }
        }
        return completeWays
    }

    private fun getNextHeight(
        map: Array<Array<String>>,
        last: Coordinate
    ): String {
        return (map[last.x][last.y].toInt() + 1).toString()
    }


}
