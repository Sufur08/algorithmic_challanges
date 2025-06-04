package core

import solutions.CoinProblem
import solutions.WarehouseProblem
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration


fun main() {

    measureTime {
        println(WarehouseProblem.findStorages(1..52, 1))
    }.run { println(this) }



    // test if blocked contains future elem and early retrun Int.MAX


}
