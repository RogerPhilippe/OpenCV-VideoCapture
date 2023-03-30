package br.com.phs.opencvvideocapture

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.videoio.VideoCapture
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import kotlin.system.exitProcess

class Camera: JFrame() {

    // Camera Screen
    private lateinit var cameraScreen: JLabel
    private lateinit var btnCapture: JButton
    private lateinit var capture: VideoCapture
    private lateinit var image: Mat
    private lateinit var icon: ImageIcon
    private var mustCapture = false
    private var running = true

    fun load() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        println("Load success!")
        this.setup()
        Thread { this.createCamera() }.start()
        this.setupListeners()

    }

    private fun setup() {

        // Design UI
        layout = null

        cameraScreen = JLabel()
        cameraScreen.setBounds(0, 0, 640, 480)
        add(cameraScreen)

        btnCapture = JButton("Capturar")
        btnCapture.setBounds(300, 490, 100, 40)
        add(btnCapture)


        size = Dimension(640, 580)
        setLocationRelativeTo(null)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isVisible = true

    }

    private fun setupListeners() {

        btnCapture.addActionListener { mustCapture = true }

        addWindowListener(object : WindowAdapter() {

            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                running = false
            }

        })

    }

    private fun createCamera() {
        capture = VideoCapture(0)
        image = Mat()
        var imageData = byteArrayOf()

        while (running) {
            // read image to matrix
            capture.read(image)
            // Matrix to byte
            val buf = MatOfByte()
            Imgcodecs.imencode(".jpg", image, buf)

            imageData = buf.toArray()
            icon = ImageIcon(imageData)
            cameraScreen.icon = icon

            if (mustCapture) {
                mustCapture = false
                val name = SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(Date())
                Imgcodecs.imwrite("images/${name}.jpg", image)
                JOptionPane.showMessageDialog(this, "Imagem $name salva!")
            }

        }

        this.exit()

    }

    private fun exit() {
        println("Finished!")
        capture.release()
        image.release()
        exitProcess(0)
    }

}
