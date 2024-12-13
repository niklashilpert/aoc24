package day9

import java.io.File

fun main() {
    val blockLayout = File("src/day9/input.txt").readLines().first().toCharArray().map { it.digitToInt() }
    val blockList = parseBlockLayout(blockLayout)
    p(blockList)
    rearrange(blockList)
    var checksum = 0L
    blockList.forEachIndexed { i, value ->
        if (value != -1) {
            checksum += i.toLong() * value.toLong()
        }
    }
    p(blockList)
    println(checksum)

}

fun rearrange(blockList: MutableList<Int>) {
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