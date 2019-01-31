package eu.spaj.panelconverter

import java.io.File
import kotlin.streams.asStream
import kotlin.streams.toList

/**
 * @author erafaja
 * Created on 31.01.19.
 */

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: panel-converter [--debug] <file.csv> <delimiter>")
        return
    }
    var debug = false
    var fileName = ""
    var delimiter = ","
    when {
        args.size == 2 -> {
            fileName = args[0]
            delimiter = args[1]
        }
        args.size > 2 -> {
            if (args[0].contains("debug")) debug = true
            fileName = args[1]
            delimiter = args[2]
        }
        else -> error("wrond args :(")
    }

    val file = File(fileName)
    val lines = file.useLines { it.toList() }

    // header
    if (lines.isEmpty()) {
        error("File is empty")
    }

    val headerSplit = lines[0].split(delimiter)
    val headerStrings = headerSplit.subList(1, headerSplit.size)
    val hourStrings = lines.asSequence()
        .asStream()
        .map {
            val split = it.split(delimiter)
            split.subList(0, 1)
        }
        .map { it[0] }
        .skip(1)
        .toList()

    val panels = lines.asSequence()
        .asStream()
        .skip(1)
        .map {
            val split = it.split(delimiter)
            split.subList(1, split.size)
        }.toList()



    if (debug) {
        println("HEADERS")
        println(headerStrings)
        println("HOURS")
        println(hourStrings)
        println("PANELS")
        panels.forEach {
            println(it)
        }
    }

    hourStrings.forEachIndexed { hourIndex, hour ->
        headerStrings.forEachIndexed { roomIndex, room ->
            var panel = panels[hourIndex][roomIndex]
            if (panel.isNotBlank()) {
                val author = panel.substringAfter('(').removeSuffix(")")
                panel = panel.substringBefore('(')
                // Print dart constructor
                println("Panel(\"$panel\", \"\", \"$hour\", \"$author\",\"SUN\",\"$room\"),")
            }
        }
    }

}

