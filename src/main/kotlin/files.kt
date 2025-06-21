package org.example

import java.io.File

fun main() {

    val wordFile = File("Words.txt")
    val lines = wordFile.readLines()

    for (line in lines) {
        println(line)
    }
}