package core

import solutions.CoinProblem
import solutions.SupplyChainProblem
import solutions.WarehouseProblem
import java.lang.Math.pow
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime
import kotlin.time.toDuration


fun main() {

//    measureTime {
//        SupplyChainProblem.entryPoint("A,G|D,E|B,C,F,H\nA,B,C,D,F,G||E\nA,B,C,E,G|D|F,H\nA,B,C,E,F,G,H||D|\nA|B,C,D,E,F,H|G\nA,B,C,D,E,G,H||F\nA,B,D,E,F,G||C\n||A,B,C,D,E,F,G,H")
//    }.run { println(this) }
    measureTime {
        SupplyChainProblem.entryPoint("A,B,F,G,I,J,L|C,D,H|E,K\nA,C,D,E,F,G,H,I,J,K,L||B\nB,C,D,E,F,G,H,I,J,K,L||A\nA,B,f,g,i,j,l|c,e,k|d,h\nA,B,D,E,F,g,H,i,j,k,l||c\nA,B,C,D,E,G,H,I,J,K,L||F\nA,B,C,E,F,G,H,I,J,K,L||D\nd,e,h,j,k|c|A,B,F,G,i,l\na,b,f,g,i,j,l|c,d,h,k|E\n||A,B,C,D,E,F,G,H,I,J,K,L\nA,B,C,D,E,H,I,J,K,L||f,g\nA,B,D,E,F,G,H,J,K,L|c|i")
    }.run { println(this) }


    /*
    fun stringToInt(string: String): Double = string.foldIndexed(.0) { index, acc, it -> acc + (it.toInt() - 96) * 26.0.pow((string.length - index - 1).toDouble()) }
    fun intToString(int: Int): String
    {
        var varInt = int
        var retStr = ""
        while (varInt > 0)
        {
            retStr += ((varInt % 27) + 96).toChar()
            varInt /= 27
        }
        return retStr.reversed()
    }
    val essens = stringToInt("essens").toInt()
    val s = stringToInt("s").toInt()
    println("essens (${essens}) - s (${s}) = ${intToString(essens - s)} (${essens - s})")
     */



    // test if blocked contains future elem and early retrun Int.MAX


}
