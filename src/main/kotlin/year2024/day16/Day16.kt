package year2024.day16

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
import utils.ImportUtils
import java.util.*
import java.util.stream.Collectors

/**
 * See https://adventofcode.com/2024/day/16
 */
class Day16 : Day() {

    val FILE_PATH: String = "src/main/resources/year2024/day16/input_test_01.txt"
    // val FILE_PATH: String = "src/main/resources/year2024/day16/input_test_02.txt";

    override val loggingEnabled: Boolean
        get() = false

    // Directions: North, East, South, West
    val DIRECTIONS: Array<IntArray> = arrayOf(
        intArrayOf(-1, 0),  // North
        intArrayOf(0, 1),  // East
        intArrayOf(1, 0),  // South
        intArrayOf(0, -1) // West
    )


    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val map = ImportUtils.convertListToArray(input)
        val shortestRouts = findAllShortestPaths(map)
        return java.lang.String.valueOf(shortestRouts[0].score)
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val map = ImportUtils.convertListToArray(input)
        val shortestRoutes = findAllShortestPaths(map)
        val uniqueCoords: Set<Coordinate> = shortestRoutes.stream()
            .flatMap { route: State -> route.path.stream() }
            .collect(Collectors.toSet())
        return uniqueCoords.size.toString()
    }

    fun findAllShortestPaths(maze: Array<Array<String>>): List<State> {
        val start: Coordinate = AocArrayUtils.findAllOccurences(maze, "S").get(0)
        val end: Coordinate = AocArrayUtils.findAllOccurences(maze, "E").get(0)

        // Priority queue for BFS (min-heap by score)
        val pq = PriorityQueue(Comparator.comparingInt { s: State -> s.score })
        // Map to track visited states
        val visited: MutableMap<String, Int> = HashMap()
        val bestPaths: MutableList<State> = ArrayList()
        var minScore = Int.MAX_VALUE

        // Initial state: start facing East (index 1 in DIRECTIONS)
        pq.add(State(start, 1, 0, listOf(start)))
        visited[encodeState(start, 1)] = 0

        while (!pq.isEmpty()) {
            val current = pq.poll()

            // If we reach the end, check the score
            if (current.coordinate.x === end.x && current.coordinate.y === end.y) {
                if (current.score < minScore) {
                    // Found a new minimum score
                    minScore = current.score
                    bestPaths.clear()
                }
                if (current.score == minScore) {
                    bestPaths.add(current)
                }
                continue
            }

            // Try all possible actions
            // 1. Move forward
            val currentCoordinate: Coordinate = current.coordinate
            val newX: Int = currentCoordinate.x + DIRECTIONS.get(current.direction).get(0)
            val newY: Int = currentCoordinate.y + DIRECTIONS.get(current.direction).get(1)

            if (AocArrayUtils.isWithinBounds(maze, newX, newY) && maze[newX][newY] != "#") {
                val path: MutableList<Coordinate> = ArrayList(current.path)
                val newCoord: Coordinate = Coordinate(newX, newY)
                path.add(newCoord)
                val nextState = State(newCoord, current.direction, current.score + 1, path)
                val encoded = encodeState(newCoord, current.direction)

                // Allow revisit if this path has the same or better score
                if (!visited.containsKey(encoded) || visited[encoded]!! >= nextState.score) {
                    visited[encoded] = nextState.score
                    pq.add(nextState)
                }
            }

            // 2. Rotate clockwise
            val newDirectionClockwise = (current.direction + 1) % 4
            val nextClockwise = State(current.coordinate, newDirectionClockwise, current.score + 1000, current.path)
            val encodedClockwise = encodeState(current.coordinate, newDirectionClockwise)

            // Allow revisit if this path has the same or better score
            if (!visited.containsKey(encodedClockwise) || visited[encodedClockwise]!! >= nextClockwise.score) {
                visited[encodedClockwise] = nextClockwise.score
                pq.add(nextClockwise)
            }

            // 3. Rotate counterclockwise
            val newDirectionCounterclockwise = (current.direction + 3) % 4 // +3 is equivalent to -1 modulo 4
            val nextCounterclockwise = State(
                current.coordinate, newDirectionCounterclockwise, current.score + 1000,
                current.path
            )
            val encodedCounterclockwise = encodeState(current.coordinate, newDirectionCounterclockwise)

            // Allow revisit if this path has the same or better score
            if (!visited.containsKey(encodedCounterclockwise) || visited[encodedCounterclockwise]!! >= nextCounterclockwise.score) {
                visited[encodedCounterclockwise] = nextCounterclockwise.score
                pq.add(nextCounterclockwise)
            }
        }

        return bestPaths
    }

    // Encode the state as a string for visited tracking
    private fun encodeState(
        coodinate: Coordinate,
        direction: Int
    ): String {
        return "${coodinate.x},${coodinate.y},${direction}"
    }

    // State class representing a position in the maze with direction and score
    data class State (
        val coordinate: Coordinate,
        val direction: Int,
        val score: Int,
        val path: List<Coordinate>
    )


}
