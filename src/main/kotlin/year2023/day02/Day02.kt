package year2023.day02

import application.Day

/**
 * See https://adventofcode.com/2023/day/2
 */
class Day02 : Day() {

    /**
     * The configuration for the balls.
     */
    private enum class BallConfiguration(val maxAmount: Int) {
        RED(12),
        GREEN(13),
        BLUE(14)
    }

    override val loggingEnabled: Boolean
        get() = false

    override fun part1(input: List<String>): String {
        // Map for saving id (key) and the game result map (value)

        val resultMap = input.mapNotNull { line -> processGame(line) }.toMap();

        log("ResultMap: $resultMap")

        val validIdNumberList = calculatePart1(resultMap)
        log("ValidIdNumberList: $validIdNumberList")
        return validIdNumberList.sum().toString()
    }

    private fun calculatePart1(resultMap: Map<Int, Map<String, Int>>): List<Int> {
        return resultMap.entries.stream()
            .filter { entry: Map.Entry<Int, Map<String, Int>> -> isGameValid(entry.value) }
            .map { a -> a.key }
            .toList()
    }

    override fun part2(input: List<String>): String {
        // Map for saving id (key) and the game result map (value)

        val resultMap = input.mapNotNull { line -> processGame(line) }.toMap();

        log("ResultMap: $resultMap")

        val productList = calculatePart2(resultMap)
        log("ProductList: $productList")
        return productList.sum().toString()
    }

    private fun calculatePart2(resultMap: Map<Int, Map<String, Int>>): List<Int> {
        return resultMap.values.stream()
            .map { maxColorMapOfGame: Map<String, Int> -> getProductOfNecessaryBalls(maxColorMapOfGame) }
            .toList()
    }

    fun processGame(
        line: String,
    ): Pair<Int, MutableMap<String, Int>>? {
        val parts = line.split(":").map(String::trim)
        if (parts.size != 2) {
            log("Invalid game data format: $line")
            return null
        }

        val gameId = parts[0].removePrefix("Game ").toIntOrNull()
        if (gameId == null) {
            log("Invalid game ID format: ${parts[0]}")
            return null
        }

        // Map to save the game results of each turn.
        val maxColorMapOfGame: MutableMap<String, Int> = HashMap()

        val games = parts[1].split(";")
        for (game in games) {
            // Map for saving of the appearance of each color for the specific turn.
            val colorMapOfTurn = processGameSection(game)

            // Save only the maximum appearance of each color in the map.
            for ((turnKey, turnValue) in colorMapOfTurn) {
                maxColorMapOfGame[turnKey] = maxOf(maxColorMapOfGame[turnKey] ?: 0, turnValue)
            }
        }

        return gameId to maxColorMapOfGame
    }

    private fun processGameSection(section: String): Map<String, Int> {
        return section.split(",")
            .mapNotNull {
                val (count, color) = it.trim().split(" ", limit = 2)
                val colorName = color.lowercase()
                count.toIntOrNull()?.let { num -> colorName to num }
            }
            .toMap()
            .also { log(it.toString()) }
    }

    private fun getProductOfNecessaryBalls(maxColorMapOfGame: Map<String, Int>): Int {
        return maxColorMapOfGame.values.stream()
            .reduce(1) { x: Int, y: Int -> x * y }
    }


    private fun isGameValid(colorMap: Map<String, Int>): Boolean {
        return BallConfiguration.values().all { config ->
            val count = colorMap[config.name.lowercase()] ?: 0
            count in 1..config.maxAmount
        }
    }

}
