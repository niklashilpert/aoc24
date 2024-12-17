package day14

import java.io.File

const val ROOM_WIDTH = 101
const val ROOM_HEIGHT = 103

class Robot(var x: Int, var y: Int, val vx: Int, val vy: Int) {
    fun step() {
        x = (ROOM_WIDTH + x + vx) % ROOM_WIDTH
        y = (ROOM_HEIGHT + y + vy) % ROOM_HEIGHT
    }
}

fun main() {
    val robots = loadRobots()
    for (i in 0..99) {
        robots.forEach(Robot::step)
    }
    println(calculateSafetyFactor(robots))
}

fun calculateSafetyFactor(robots: List<Robot>): Int {
    val quadrants = Array(2) { IntArray(2) { 0 } }
    robots.forEach { robot ->
        if (robot.y in 0..<ROOM_HEIGHT/2) {
            if (robot.x in 0..<ROOM_WIDTH/2) {
                quadrants[0][0] += 1
            } else if (robot.x != ROOM_WIDTH/2) {
                quadrants[0][1] += 1
            }
        } else if (robot.y != ROOM_HEIGHT/2) {
            if (robot.x in 0..<ROOM_WIDTH/2) {
                quadrants[1][0] += 1
            } else if (robot.x != ROOM_WIDTH/2) {
                quadrants[1][1] += 1
            }
        }
    }
    return quadrants[0][0] * quadrants[0][1] * quadrants[1][0] * quadrants[1][1]
}

fun loadRobots(): List<Robot> {
    val robots = mutableListOf<Robot>()
    File("src/day14/input.txt").readLines().map { line ->
        val regex = Regex("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)")
        val match = regex.matchEntire(line)
        if (match != null) {
            robots.add(
                Robot(
                    match.groupValues[1].toInt(),
                    match.groupValues[2].toInt(),
                    match.groupValues[3].toInt(),
                    match.groupValues[4].toInt()
                )
            )
        }
    }
    return robots
}
