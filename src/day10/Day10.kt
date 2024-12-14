package day10

import java.awt.Dimension
import java.awt.Point
import java.io.File

fun main() {
    val map = Map(File("src/day10/input.txt").readLines().map {it.toCharArray()})
    val scores = map.calculateScoresOfTrailheads()
    println(scores.sum())

    val ratings = map.calculateRatingsOfTrailheads()
    println(ratings.sum())

}


operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)

class Map(rows: List<CharArray>) {
    private val map = rows.map { row -> row.map { it.digitToInt() }.toMutableList() }.toMutableList()
    private val bounds = Dimension(rows[0].size, rows.size)

    fun calculateScoresOfTrailheads(): List<Int> {
        val trailheads = findTrailheads()
        return trailheads.fold(mutableListOf()) { list: MutableList<Int>, trailhead: Point ->
            list.add(findPeaksFromPoint(trailhead).distinct().size)
            list
        }
    }


    fun calculateRatingsOfTrailheads(): List<Int> {
        val trailheads = findTrailheads()
        return trailheads.fold(mutableListOf()) { list: MutableList<Int>, trailhead: Point ->
            list.add(findPeaksFromPoint(trailhead).size)
            list
        }
    }

    private fun findPeaksFromPoint(point: Point): List<Point> {
        if (this[point] == 9) {
            return mutableListOf(point)
        } else {
            val topNeighbor = point + Point(0, 1)
            val leftNeighbor = point + Point(-1, 0)
            val downNeighbor = point + Point(0, -1)
            val rightNeighbor = point + Point(1, 0)
            val foundPeaks = mutableListOf<Point>()
            if (!isOutOfBounds(topNeighbor) && this[topNeighbor] == this[point] + 1) {
                foundPeaks += findPeaksFromPoint(topNeighbor)
            }
            if (!isOutOfBounds(leftNeighbor) && this[leftNeighbor] == this[point] + 1) {
                foundPeaks += findPeaksFromPoint(leftNeighbor)
            }
            if (!isOutOfBounds(downNeighbor) && this[downNeighbor] == this[point] + 1) {
                foundPeaks += findPeaksFromPoint(downNeighbor)
            }
            if (!isOutOfBounds(rightNeighbor) && this[rightNeighbor] == this[point] + 1) {
                foundPeaks += findPeaksFromPoint(rightNeighbor)
            }
            return foundPeaks
        }
    }


    private fun findTrailheads(): List<Point> = findOccurrences(0)

    private fun findOccurrences(value: Int): List<Point> {
        val occurrences = mutableListOf<Point>()
        map.forEachIndexed {y: Int, row: List<Int> ->
            row.forEachIndexed {x: Int, v: Int ->
                if (value == v) {
                    occurrences.add(Point(x, y))
                }
            }
        }
        return occurrences
    }

    operator fun get(pos: Point) = this[pos.x, pos.y]
    operator fun get(x: Int, y: Int) = map[y][x]
    operator fun set(x: Int, y: Int, value: Int) {
        map[y][x] = value
    }

    private fun isOutOfBounds(pos: Point): Boolean {
        return pos.x !in 0..<bounds.width || pos.y !in 0..<bounds.height
    }
}