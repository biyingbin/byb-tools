package com.laobi.yuxianxiangsidu

/**
 * 余弦相似度
 */
object yxxsd {

  def main(args: Array[String]): Unit = {

    val fz = 4 * 1 + 3 * 0.8 + 2 * 0.6 + 0

    val fm = math.sqrt(4 * 4 + 3 * 3 + 2 * 2 + 1) * math.sqrt( 1 * 1 + 0.8 * 0.8 + 0.6 * 0.6 + 0)

    println(fz)
    println(fm)

    println(fz / fm)

  }

}
