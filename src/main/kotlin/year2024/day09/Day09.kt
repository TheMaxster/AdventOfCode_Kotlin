package year2024.day09

import application.Day
import utils.ImportUtils

/**
 * See https://adventofcode.com/2024/day/9
 */
class Day09 : Day() {

    override val loggingEnabled: Boolean
        get() = false


    val FILE_PATH: String = "src/main/resources/year2024/day09/input_test_01.txt"

    override fun part1(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        log(input.toString())

        val parsedInput = parseInputForPart1(input)
        log(createLoggableBlocklist(parsedInput))

        val convertedInput = convertInput(parsedInput)
        log(createLoggableBlocklist(convertedInput))

        val product = multiplyBlocks(convertedInput)

        return product.toString()
    }

    override fun part2(input: List<String>): String {
        val importInput = ImportUtils.readAsList(FILE_PATH)
        log(input.toString())

        val parsedInput = parseInputForPart2(input)
        log(createLoggableBlocklist(parsedInput))

        val convertedInput = convertInput(parsedInput)
        log(createLoggableBlocklist(convertedInput))

        val product = multiplyBlocks(convertedInput)

        return product.toString()
    }

    private fun parseInputForPart1(importInput: List<String>): MutableList<Block> {
        val string = importInput[0]

        val array = string.toCharArray()
        val result: MutableList<Block> = ArrayList()
        for (i in array.indices) {
            val value = array[i].toString().toInt()
            if (i % 2 == 0) {
                for (j in 0..<value) {
                    result.add(Block((i / 2).toString(), 1, false))
                }
            } else {
                for (j in 0..<value) {
                    result.add(Block(".", 1, false))
                }
            }
        }

        return result
    }

    private fun parseInputForPart2(importInput: List<String>): MutableList<Block> {
        val string = importInput[0]

        val array = string.toCharArray()
        val result: MutableList<Block> = ArrayList()
        for (i in array.indices) {
            val value = array[i].toString().toInt()
            if (i % 2 == 0) {
                result.add(Block((i / 2).toString(), value, false))
            } else {
                result.add(Block(".", value, false))
            }
        }

        return result
    }

    private fun convertInput(blockList: MutableList<Block>): List<Block> {
        log(createLoggableBlocklist(blockList))
        while (true) {
            val numberIndex = findUnvisitedLastNumberIndexForBlocks(blockList)
            if (numberIndex == -1) break


            val numberBlock = blockList[numberIndex].apply { visited = true }
            val dotIndex = findDotIndexForBlock(blockList, numberBlock.amount)

            if (dotIndex > numberIndex || dotIndex == -1) continue


            val dotBlock = blockList[dotIndex]
            val newDots: Int = dotBlock.amount - numberBlock.amount
            dotBlock.amount = newDots
            blockList.add(dotIndex, numberBlock)
            blockList.removeAt(numberIndex + 1)
            blockList.add(numberIndex, Block(".", numberBlock.amount, false))
            log(createLoggableBlocklist(blockList))
        }

        return blockList
    }

    private fun createLoggableBlocklist(blockList: List<Block>): String =
        blockList.joinToString("") { it.number.repeat(it.amount) }

    private fun findDotIndexForBlock(
        blockList: List<Block>,
        blockAmount: Int
    ): Int {
        for (i in blockList.indices) {
            val dotBlock = blockList[i]
            if (dotBlock.number == "." && blockAmount <= dotBlock.amount) {
                return i
            }
        }
        return -1
    }


    private fun findUnvisitedLastNumberIndexForBlocks(stringList: List<Block>): Int {
        for (i in stringList.indices.reversed()) {
            val block = stringList[i]
            if (block.number != "." && !block.visited) {
                return i
            }
        }
        return -1
    }

    private fun multiplyBlocks(blockList: List<Block>): Long {
        return blockList.flatMap { block ->
            List(block.amount) { if (block.number == ".") 0L else block.number.toLong() }
        }.withIndex().sumOf { (index, value) -> index * value }
    }


    data class Block(
        var number: String,
        var amount: Int,
        var visited: Boolean
    )


}
