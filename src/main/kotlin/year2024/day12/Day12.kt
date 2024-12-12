package year2024.day12

import application.Day
import main.kotlin.utils.AocArrayUtils
import main.kotlin.utils.Coordinate
import utils.ImportUtils
import java.util.*
import java.util.stream.Collectors

/**
 * See https://adventofcode.com/2024/day/12
 */
class Day12 : Day() {
    override val loggingEnabled: Boolean
        get() = false

    val FILE_PATH: String = "src/main/resources/year2024/day12/input_test_01.txt"

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val map = ImportUtils.convertListToArray(input)
        val gardenMap = parseInput(map)

        val groupedGardens = gardenMap.flatten().groupBy { it.regionId }
        val (price1, _) = calculatePrices(groupedGardens)
        return price1.toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        val map = ImportUtils.convertListToArray(input)
        val gardenMap = parseInput(map)

        val groupedGardens = gardenMap.flatten().groupBy { it.regionId }
        val (_, price2) = calculatePrices(groupedGardens)
        return price2.toString()
    }

    private fun calculatePrices(mappedGarden: Map<String, List<GardenPlot>>): Pair<Int, Int> {
        var totalPrice1 = 0
        var totalPrice2 = 0
        for ((regionId, plots) in mappedGarden) {
            log("Region: " + regionId + ": " + plots[0].letter)
            val areaSize = plots.size
            log("AreaSize: $areaSize")
            val perimeter = plots.sumOf { it.diffNeighbors }
            log("Perimeter: $perimeter")
            val price = areaSize * perimeter
            log("Price: $price")
            val corners = plots.sumOf { it.cornerAmount }
            log("Sides: $corners")
            val price2 = areaSize * corners
            log("Price2: $price2")
            log("") // New line
            totalPrice1 += price
            totalPrice2 += price2
        }
        return totalPrice1 to totalPrice2
    }

    private fun parseInput(map: Array<Array<String>>): Array<Array<GardenPlot>> {
        val rows = map.size
        val cols = map[0].size
        val gardenMap = Array(rows) { Array<GardenPlot?>(cols) { null } }
        var regionId = 0

        for (x in 0 until rows) {
            for (y in 0 until cols) {
                if (gardenMap[x][y] == null) {
                    fillRegion(map, gardenMap, x, y, regionId)
                    regionId++
                }
            }
        }
        return gardenMap as Array<Array<GardenPlot>>
    }

    private fun fillRegion(
        map: Array<Array<String>>,
        gardenMap: Array<Array<GardenPlot?>>,
        startX: Int,
        startY: Int,
        regionId: Int
    ) {
        val queue: Queue<Coordinate> = ArrayDeque()
        val regionLetter = map[startX][startY]
        queue.add(Coordinate(startX, startY))

        while (queue.isNotEmpty()) {
            val (x, y) = queue.poll()

            if (gardenMap[x][y] != null) continue

            val diffNeighbors = countDifferentNeighbors(map, x, y)
            val cornerAmount = countCorners(map, x, y)

            gardenMap[x][y] = GardenPlot(
                visited = true,
                regionId = regionId.toString(),
                letter = regionLetter,
                x = x,
                y = y,
                diffNeighbors = diffNeighbors,
                cornerAmount = cornerAmount
            )

            // Add valid neighbors
            val neighbors = AocArrayUtils.filterSurroundingCoordinates(map, Coordinate(x, y), regionLetter)
            neighbors.filter { gardenMap[it.x][it.y] == null }.forEach { queue.add(it) }
        }
    }

    private fun countDifferentNeighbors(map: Array<Array<String>>, x: Int, y: Int): Int {
        val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
        return directions.count { (dx, dy) ->
            val nx = x + dx
            val ny = y + dy
            !AocArrayUtils.isWithinBounds(map, nx, ny) || map[nx][ny] != map[x][y]
        }
    }

    private fun countCorners(map: Array<Array<String>>, i: Int, j: Int): Int {
        // Check all possible "L" shaped patterns around the current point
        val current = map[i][j]
        var corners = 0

        // First check the outer corners.
        corners += if (differs(map, i - 1, j, current) && differs(map, i, j - 1, current)) 1 else 0
        corners += if (differs(map, i - 1, j, current) && differs(map, i, j + 1, current)) 1 else 0
        corners += if (differs(map, i + 1, j, current) && differs(map, i, j - 1, current)) 1 else 0
        corners += if (differs(map, i + 1, j, current) && differs(map, i, j + 1, current)) 1 else 0

        // Second check the inner corners.
        corners += if (same(map, i - 1, j, current) && same(map, i, j - 1, current) && differs(map, i - 1, j - 1, current)) 1 else 0
        corners += if (same(map, i - 1, j, current) && same(map, i, j + 1, current) && differs(map, i - 1, j + 1, current)) 1 else 0
        corners += if (same(map, i + 1, j, current) && same(map, i, j - 1, current) && differs(map, i + 1, j - 1, current)) 1 else 0
        corners += if (same(map, i + 1, j, current) && same(map, i, j + 1, current) && differs(map, i + 1, j + 1, current)) 1 else 0

        return corners
    }

    private fun differs(map: Array<Array<String>>, x: Int, y: Int, current: String): Boolean {
        return !AocArrayUtils.isWithinBounds(map, x, y) || map[x][y] != current
    }

    private fun same(map: Array<Array<String>>, x: Int, y: Int, current: String): Boolean {
        return !differs(map, x, y, current)
    }

    data class GardenPlot(
        var visited: Boolean,
        var regionId: String,
        var letter: String,
        var x: Int,
        var y: Int,
        var diffNeighbors: Int,
        var cornerAmount: Int
    )

}
