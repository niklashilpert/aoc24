package day11

import java.io.File

val cache = hashMapOf<Pair<Long, Int>, Long>()

fun main() {
    val stones = File("src/day11/input.txt").readLines().first().split(" ").map { it.toLong() }.toMutableList()

    var startTime = System.currentTimeMillis()
    val count1 = stones.fold(0L) { acc: Long, stone: Long ->
        acc + iterateRecursive(stone, 25)
    }
    println("Part 1: $count1. Duration: ${System.currentTimeMillis() - startTime} ms")

    startTime = System.currentTimeMillis()
    val count2 = stones.fold(0L) { acc: Long, stone: Long ->
        acc + iterateRecursive(stone, 75)
    }
    println("Part 2: $count2. Duration: ${System.currentTimeMillis() - startTime} ms")
}

fun iterateRecursive(stone: Long, depth: Int): Long {
    if (depth == 0) {
        return 1
    }
    if (stone to depth in cache) {
        return cache[stone to depth]!!
    }

    val numberLength = length(stone)
    val result: Long
    if (stone == 0L) {
        result = iterateRecursive(1, depth-1)
    } else if (numberLength % 2 == 0) {
        val left = getLeftPart(stone, numberLength)
        val right = getRightPart(stone, numberLength, left)
        result = iterateRecursive(left, depth-1) + iterateRecursive(right, depth-1)
    } else {
        result =  iterateRecursive(stone * 2024, depth-1)
    }

    if (cache[stone to depth] == null) {
        cache[stone to depth] = result
    }
    return result
}

fun getLeftPart(number: Long, length: Int): Long {
    var result = number
    for(i in 0..<length/2) {
        result /= 10
    }
    return result
}

fun getRightPart(number: Long, length: Int, leftPart: Long): Long {
    var factor = 1
    for (i in 0..<length/2) {
        factor *= 10
    }
    return number - leftPart * factor
}

fun length(number: Long): Int {
    var len = 0
    var temp = number
    while(temp != 0L) {
        temp /= 10
        len++
    }
    return len
}