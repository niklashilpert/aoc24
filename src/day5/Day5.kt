package day5

import java.io.File

class Rule (val start: Int, val target: Int)

class AdjacencyMatrix(rules: List<Rule>) {
    private val indexMap = hashMapOf<Int, Int>()
    private val matrix: Array<IntArray>

    init {
        val pageSet = rules.map { it.start }.toSet() + rules.map { it.target }.toSet()
        matrix = Array(pageSet.size) { IntArray(pageSet.size) {0} }
        pageSet.forEachIndexed { index, value ->
            indexMap[value] = index
        }
        for (rule in rules) {
            this[rule.start, rule.target] = 1
            if (rule.start == 67 && rule.target == 92) {
                println("!")
            }
            this[rule.target, rule.start] = -1
        }
    }

    operator fun get(start: Int, target: Int) = matrix[indexMap[start]!!][indexMap[target]!!]
    operator fun set(start: Int, target: Int, value: Int) {
        matrix[indexMap[start]!!][indexMap[target]!!] = value
    }

    private fun getPagesImmediatelyAfter(start: Int): List<Int> {
        return getConnectedPagesWithWeight(start, 1)
    }

    private fun getPagesImmediatelyBefore(start: Int): List<Int> {
        return getConnectedPagesWithWeight(start, -1)
    }

    private fun getConnectedPagesWithWeight(start: Int, weight: Int): List<Int> {
        val pageList = mutableListOf<Int>()
        for (page in indexMap.keys) {
            if (this[start, page] == weight) {
                pageList += page
            }
        }
        return pageList
    }

    fun getPagesAfter(start: Int): List<Int> {
        if (!indexMap.keys.contains(start))
            return emptyList()
        val pageList = mutableListOf(start)
        var index = 0
        while(index < pageList.size) {
            val followingPages = getPagesImmediatelyAfter(pageList[index])
            for (page in followingPages) {
                if (!pageList.contains(page)) {
                    pageList += page
                }
            }
            index++
        }
        pageList.removeFirst()
        return pageList
    }

    fun getPagesBefore(start: Int): List<Int> {
        if (!indexMap.keys.contains(start))
            return emptyList()
        val pageList = mutableListOf(start)
        var index = 0
        while(index < pageList.size) {
            val followingPages = getPagesImmediatelyBefore(pageList[index])
            for (page in followingPages) {
                if (!pageList.contains(page)) {
                    pageList += page
                }
            }
            index++
        }
        pageList.removeFirst()
        return pageList
    }
}

fun main() {
    val (ruleStrings, updateStrings) = extractParts(readLines())
    val rules = ruleStrings.map {
        val parts = it.split("|")
        Rule(parts[0].toInt(), parts[1].toInt())
    }

    var sum1 = 0
    var sum2 = 0
    for (updateString in updateStrings) {
        val pages = updateString.split(",").map { it.toInt() }
        val appliedRules = rules.filter { rule -> pages.contains(rule.start) && pages.contains(rule.target) }
        val matrix = createAdjacencyMatrix(appliedRules)

        val result = checkUpdate(pages, matrix)
        if (result != -1) {
            sum1 += result
        } else {
            println(pages)
            val sortedPages = pages.sortedWith { a, b ->
                if (a == b) {
                    0
                } else if (a.isLeftOf(b, matrix)) {
                    1
                } else {
                    -1
                }
            }
            println(pages)
            sum2 += sortedPages[sortedPages.size / 2]
        }

    }
    println("Part1: $sum1")
    println("Part2: $sum2")


}

fun checkUpdate(pages: List<Int>, matrix: AdjacencyMatrix): Int {
    for (i in 0..pages.size-2) {
        val pagesBeforeInUpdate = pages.subList(0, i).sorted()
        val pagesAfterInUpdate = pages.subList(i+1, pages.size).sorted()

        println("PagesBeforeInUpdate: $pagesBeforeInUpdate")
        println("PagesAfterInUpdate:  $pagesAfterInUpdate")

        val pagesBeforeInRules = matrix.getPagesBefore(pages[i])
        val pagesAfterInRules = matrix.getPagesAfter(pages[i])

        println("PagesBeforeInRules: $pagesBeforeInRules")
        println("PagesAfterInRules: $pagesAfterInRules")

        if (
            isIntersecting(pagesBeforeInUpdate, pagesAfterInRules) ||
            isIntersecting(pagesAfterInUpdate, pagesBeforeInRules)
        ) {
            println("Not valid: $pages")
            return -1
        }
    }
    println("Valid: $pages")
    return pages[pages.size / 2]
}

fun <T: Any> isIntersecting(leftList: List<T>, rightList: List<T>): Boolean {
    leftList.forEach { leftItem ->
        rightList.forEach { rightItem ->
            if (leftItem == rightItem) {
                return true
            }
        }
    }

    for (leftItem in leftList) {
        for (rightItem in rightList) {
            if (leftItem == rightItem) {
                return true
            }
        }
    }
    return false
}

fun createAdjacencyMatrix(rules: List<Rule>): AdjacencyMatrix {
    return AdjacencyMatrix(rules)
}

fun readLines(): List<String> {
    return File("src/day5/input.txt").readLines()
}

fun extractParts(lines: List<String>): Pair<List<String>, List<String>> {
    val ruleStrings = mutableListOf<String>()
    val updateStrings = mutableListOf<String>()
    var addToRules = true
    for (line in lines) {
        if (line.isBlank()) {
            addToRules = false
            continue
        }
        if (addToRules) {
            ruleStrings += line
        } else {
            updateStrings += line
        }
    }
    return ruleStrings to updateStrings
}

fun Int.isLeftOf(other: Int, matrix: AdjacencyMatrix) = matrix.getPagesAfter(other).contains(this)
