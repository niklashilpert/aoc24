package day12

import java.awt.Dimension
import java.awt.Point
import java.io.File

val UP = Point(0, -1)
val LEFT = Point(-1, 0)
val DOWN = Point(0, 1)
val RIGHT = Point(1, 0)

fun main() {
    val map = Map(File("src/day12/input.txt").readLines())
    println(map.calculatePrice())
    println(map.getSidesPriceAll())
}

class Area(val char: Char, val points: MutableSet<Point>) {
    override fun toString() = "Area{$char, $points}"
}

class Map(rows: List<String>) {
    private val map = rows.map { row -> row.toMutableList() }.toMutableList()
    private val bounds = Dimension(rows[0].length, rows.size)

    val areas = mutableSetOf<Area>()
    init {
        findAreas()
    }

    fun calculatePrice() = areas.fold(0) {acc: Int, area: Area ->
        acc + calculatePrice(area)
    }

    fun calculatePrice(area: Area): Int {
        return area.points.size * getPerimeter(area)
    }

    private fun getPerimeter(area: Area): Int {
        var perimeter = 0
        perimeter += getPlotsWithFenceInDirection(area, UP).size
        perimeter += getPlotsWithFenceInDirection(area, LEFT).size
        perimeter += getPlotsWithFenceInDirection(area, DOWN).size
        perimeter += getPlotsWithFenceInDirection(area, RIGHT).size
        return perimeter
    }
    private fun isFence(area: Area, plot: Point, direction: Point): Boolean {
        return plot in area.points && plot + direction !in area.points
    }

    private fun getPlotsWithFenceInDirection(area: Area, direction: Point): List<Point> {
        return area.points.filter { isFence(area, it, direction) }
    }

    fun getSidesPriceAll(): Int {
        return areas.fold(0) { acc: Int, area: Area ->
            acc + area.points.size * getSidesCount(area)
        }
    }
    fun getSidesCount(area: Area): Int {
        var count = 0

        count += getLeftSideCount(area)
        count += getRightSideCount(area)
        count += getDownSideCount(area)
        count += getUpSideCount(area)

        return count
    }

    private fun getLeftSideCount(area: Area): Int {
        var count = 0
        val fences = getPlotsWithFenceInDirection(area, LEFT).sortedBy { it.x }
        val xValues = fences.map { it.x }.distinct()
        for (x in xValues) {
            val column = fences.filter { it.x == x && it + LEFT !in fences }.sortedBy { it.y }
            val yValues = column.map { it.y }.distinct()
            var lastYValue = Int.MIN_VALUE
            for (y in yValues) {
                if (lastYValue + 1 != y) {
                    count++
                }
                lastYValue = y
            }
        }
        return count
    }

    private fun getUpSideCount(area: Area): Int {
        var count = 0
        val fences = getPlotsWithFenceInDirection(area, UP).sortedBy { it.y }
        val yValues = fences.map { it.y }.distinct()
        for (y in yValues) {
            val column = fences.filter { it.y == y && it + UP !in fences }.sortedBy { it.x }
            val xValues = column.map { it.x }.distinct()
            var lastXValue = Int.MIN_VALUE
            for (x in xValues) {
                if (lastXValue + 1 != x) {
                    count++
                }
                lastXValue = x
            }
        }
        return count
    }

    private fun getRightSideCount(area: Area): Int {
        var count = 0
        val fences = getPlotsWithFenceInDirection(area, RIGHT).sortedBy { it.x }
        val xValues = fences.map { it.x }.distinct()
        for (x in xValues) {
            val column = fences.filter { it.x == x && it + RIGHT !in fences }.sortedBy { it.y }
            val yValues = column.map { it.y }.distinct()
            var lastYValue = Int.MIN_VALUE
            for (y in yValues) {
                if (lastYValue + 1 != y) {
                    count++
                }
                lastYValue = y
            }
        }
        return count
    }

    private fun getDownSideCount(area: Area): Int {
        var count = 0
        val fences = getPlotsWithFenceInDirection(area, DOWN).sortedBy { it.y }
        val yValues = fences.map { it.y }.distinct()
        for (y in yValues) {
            val column = fences.filter { it.y == y && it + DOWN !in fences }.sortedBy { it.x }
            val xValues = column.map { it.x }.distinct()
            var lastXValue = Int.MIN_VALUE
            for (x in xValues) {
                if (lastXValue + 1 != x) {
                    count++
                }
                lastXValue = x
            }
        }
        return count
    }

        private fun findAreas() {
        for (y in 0..<bounds.height) {
            for (x in 0..<bounds.width) {
                val point = Point(x, y)
                if (!wasVisited(point)) {
                    val area = Area(this[point], mutableSetOf())
                    areas.add(area)
                    floodFill(this[point], point, area.points)
                }
            }
        }
    }

    private fun floodFill(char: Char, center: Point, accumulator: MutableSet<Point>) {
        if (!isOutOfBounds(center) && !wasVisited(center) && this[center] == char) {
            accumulator += center
            floodFill(char, center + Point(0, -1), accumulator)
            floodFill(char, center + Point(0, 1), accumulator)
            floodFill(char, center + Point(-1, 0), accumulator)
            floodFill(char, center + Point(1, 0), accumulator)
        }
    }

    private fun wasVisited(point: Point): Boolean {
        for (area in areas) {
            if (point in area.points) {
                return true
            }
        }
        return false
    }

    operator fun get(pos: Point) = this[pos.x, pos.y]
    operator fun get(x: Int, y: Int) = map[y][x]
    operator fun set(x: Int, y: Int, value: Char) {
        map[y][x] = value
    }

    private fun isOutOfBounds(pos: Point): Boolean {
        return pos.x !in 0..<bounds.width || pos.y !in 0..<bounds.height
    }
}

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)
operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)
