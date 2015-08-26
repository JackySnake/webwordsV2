package com.ssreader.example

/**
 * Created by joseph on 26/08/2015.
 */
object ImplicitExample extends App {
  /*implicit def stringWrapper(s: String) =
    new RandomAccessSeq[Char] {
        def length = s.length
        def apply(i: Int) = s.charAt(i)
    }*/

    implicit class StringImprovements(s: String) {
        def inc = s.map(c => (c + 1).toChar)
    }

    val result = "TEST".inc

    println(result)
}
