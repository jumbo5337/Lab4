package sample

fun main(){

    val arr = arrayListOf<IntArray>(
            intArrayOf(1,2,3,4),
            intArrayOf(5,6,7,8),
            intArrayOf(11,12,13,15)
    )

   val ar2 =  arr.subList(0,2).map { it.slice(0..2) }

    ar2.forEach{println(it)}
}