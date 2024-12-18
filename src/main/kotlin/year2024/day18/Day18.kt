package year2024.day18

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
import utils.ImportUtils
import java.util.*
import java.util.regex.Pattern

/**
 * See https://adventofcode.com/2024/day/18
 */
class Day18 : Day() {
    override val loggingEnabled: Boolean
        get() = false

    val FILE_PATH: String = "src/main/resources/year2024/day18/input_test_01.txt";

    // Directions: North, East, South, West
    val DIRECTIONS: Array<IntArray> = arrayOf(
        intArrayOf(-1, 0),  // North
        intArrayOf(0, 1),  // East
        intArrayOf(1, 0),  // South
        intArrayOf(0, -1) // West
    )

    val TEST_WIDTH: Int = 7
    val TEST_HEIGHT: Int = 7

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val widthToUse = 71 // TEST_WIDTH;
        val heightToUse = 71 // TEST_HEIGHT;
        val bytesToFall = 1024 // 12
        val importToUse = input

        //        final int widthToUse = TEST_WIDTH;
        //        final int heightToUse = TEST_HEIGHT;
        //        final int bytesToFall = 12;
        //        final List<String> importToUse = importInput;
        val bytes: List<Coordinate> = parseInput(importToUse)
        val start: Coordinate = Coordinate(0, 0)
        val end: Coordinate = Coordinate(widthToUse - 1, heightToUse - 1)

        val map = createMap(widthToUse, heightToUse, bytesToFall, bytes)
        logMap(map)

        val steps = findShortestPath(map, start, end)

        return steps.toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)

        val widthToUse = 71 // TEST_WIDTH;
        val heightToUse = 71 // TEST_HEIGHT;
        val importToUse = input

        //        final int widthToUse = TEST_WIDTH;
        //        final int heightToUse = TEST_HEIGHT;
        //        final List<String> importToUse = importInput;
        val bytes: List<Coordinate> = parseInput(importToUse)
        val start = Coordinate(0, 0)
        val end = Coordinate(widthToUse - 1, heightToUse - 1)

        var result: Coordinate = Coordinate(0, 0)
        for (i in 1..<bytes.size) {
            val map = createMap(widthToUse, heightToUse, i, bytes)

            val steps = findShortestPath(map, start, end)
            if (steps == -1) {
                result = bytes[i - 1]
                break
            }
        }

        return "${result.y},${result.x}"
    }

    private fun createMap(width: Int, height: Int, bytesToFall: Int, bytes: List<Coordinate>): Array<Array<String>> {
        return Array(width) { Array(height) { "." } }.apply {
            bytes.take(bytesToFall).forEach { this[it.x][it.y] = "#" }
        }
    }

    private fun parseInput(input: List<String>): List<Coordinate> {
        val regex = Regex("(\\d+),(\\d+)")
        return input.mapNotNull { line ->
            regex.matchEntire(line)?.destructured?.let { (x, y) -> Coordinate(y.toInt(), x.toInt()) }
        }
    }


    fun findShortestPath(
        map: Array<Array<String>>,
        start: Coordinate,
        end: Coordinate
    ): Int {
        val rows = map.size
        val cols = map[0].size

        // Queue for BFS: stores [x, y, distance]
        val queue: Queue<State> = LinkedList()
        queue.add(State(start, 0)) // Start point with distance 0

        // Visited array
        val visited = Array(rows) { BooleanArray(cols) }
        visited[start.x][start.y] = true

        while (!queue.isEmpty()) {
            val current = queue.poll()

            // Check if we reached the end
            if (current.coordinate == end) return current.score

            // Explore neighbors in all 4 directions
            DIRECTIONS.forEach { (dx, dy) ->
                val nx = current.coordinate.x + dx
                val ny = current.coordinate.y + dy

                // Check if the next position is within bounds, not visited, and not blocked
                if (AocArrayUtils.isWithinBounds(map, nx, ny) && !visited[nx][ny] && (map[nx][ny] != "#")) {
                    visited[nx][ny] = true // Mark as visited
                    queue.add(State(Coordinate(nx, ny), current.score + 1)) // Add to queue with updated distance
                }
            }
        }

        return -1 // No path found
    }

    data class State(
        val coordinate: Coordinate,
        val score: Int
    )

}
