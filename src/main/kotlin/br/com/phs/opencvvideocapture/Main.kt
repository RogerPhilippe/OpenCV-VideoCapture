package br.com.phs.opencvvideocapture

import org.opencv.core.Core
import java.awt.EventQueue

fun main(args: Array<String>) {

    println("OpenCV version: ${Core.VERSION}")

    EventQueue.invokeLater {
        Camera().load()
    }


}