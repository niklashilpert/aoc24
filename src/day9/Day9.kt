package day9

import java.io.File


fun main() {
    val blockLayout = File("src/day9/input.txt").readLines().first().toCharArray().map { it.digitToInt() }
    val blockList = parseBlockLayout(blockLayout)
    rearrangeBlocks(blockList)
    var checksum = 0L
    blockList.forEachIndexed { i, value ->
        if (value != -1) {
            checksum += i.toLong() * value.toLong()
        }
    }
    println(checksum)

    val blockList2 = parseBlockLayout(blockLayout)
    rearrangeFiles(blockList2)

    var checksum2 = 0L
    blockList2.forEachIndexed { i, value ->
        if (value != -1) {
            checksum2 += i.toLong() * value.toLong()
        }
    }
    println(checksum2)
}

fun rearrangeBlocks(blockList: MutableList<Int>) {
    for(i in blockList.indices.reversed()) {
        if (blockList[i] != -1) {
            for (j in blockList.indices) {
                if (j >= i) {
                    break
                }
                if (blockList[j] == -1) {
                    blockList[j] = blockList[i]
                    blockList[i] = -1
                    break
                }
            }
        }
    }
}

fun rearrangeFiles(blocks: MutableList<Int>) {
    var blockIndex = blocks.lastIndex
    while (blockIndex >= 0) {
        // Find file from current index
        val fileId = blocks[blockIndex]
        if ((blockIndex + 1) in blocks.indices && blocks[blockIndex + 1] == fileId) {
            blockIndex--
            continue // Only look at indices what point to the right bound of a file
        }
        val (fileStart, fileEnd) = findBlockWithSameId(blocks, blockIndex, true)!!
        val fileLength = fileEnd - fileStart + 1

        // Find a suitable interval of free space
        var spaceStartIndex = 0
        while (spaceStartIndex < blockIndex) {
            if (blocks[spaceStartIndex] == -1) {
                val (spaceStart, spaceEnd) = findBlockWithSameId(blocks, spaceStartIndex)!!
                val spaceLength = spaceEnd - spaceStart + 1
                if (fileLength <= spaceLength) {
                    break
                }
            }
            spaceStartIndex++
        }

        // Swaps spaces
        val spaceFound = spaceStartIndex < blockIndex
        if (spaceFound) {
            for (i in fileStart..fileEnd) {
                blocks[i] = -1
            }
            for (i in spaceStartIndex..<spaceStartIndex+fileLength) {
                blocks[i] = fileId
            }
        }

        blockIndex--
    }
}

fun findBlockWithSameId(blocks: List<Int>, index: Int, backwards: Boolean = false): Pair<Int, Int>? {
    val currentFileLength = countImmediateNumberRepetitions(blocks, index, if (backwards) -1 else 1)
    if (currentFileLength == -1) return null
    var startIndex = index
    var endIndex = index + currentFileLength - 1
    if (backwards) {
        startIndex = index - (currentFileLength - 1)
        endIndex = index
    }
    return startIndex to endIndex


}

fun countImmediateNumberRepetitions(source: List<Int>, index: Int, step: Int): Int {
    if (index in source.indices) {
        val reference = source[index]
        var i = 0
        var count = 0
        while(index + i in source.indices && source[index + i] == reference) {
            count++
            i += step
        }
        return count
    } else {
        return -1
    }
}

fun parseBlockLayout(blockLayout: List<Int>): MutableList<Int> {
    val blockList = mutableListOf<Int>()
    var index = 0
    var elementIsFile = true
    for (block in blockLayout) {
        val valueToAdd = if (elementIsFile) { index } else { -1 }
        for (i in 0..<block) {
            blockList += valueToAdd
        }
        if (elementIsFile) index++
        elementIsFile = !elementIsFile
    }
    return blockList
}

fun p(blockList: MutableList<Int>) {
    for(i in blockList) {
        if (i == -1) {
            print(".")
        } else {
            print(i)
        }
    }
    println()
}