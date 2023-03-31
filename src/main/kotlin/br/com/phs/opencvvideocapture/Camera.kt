package br.com.phs.opencvvideocapture

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoCapture
import java.awt.Dimension
import java.awt.Image
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*
import kotlin.system.exitProcess

class Camera: JFrame() {

    companion object {
        private const val WIDTH = 655
        private const val HEIGHT = 580
    }

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
        cameraScreen.setBounds(0, 0, WIDTH, HEIGHT-100)
        add(cameraScreen)

        btnCapture = JButton("Capturar")
        btnCapture.setBounds((WIDTH/2)-60, 490, 100, 40)
        add(btnCapture)


        size = Dimension(WIDTH, HEIGHT)
        setLocationRelativeTo(null)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        isResizable = false
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
        //capture = VideoCapture(0) // WebCam
        capture = VideoCapture("rtsp://admin:Um9nZXI=@192.168.15.33:554/mainStream") // Video Streaming
        image = Mat()
        var imageData: ByteArray

        if (!capture.isOpened) {
            running = false
            println("Video flow not opened!")
            JOptionPane.showMessageDialog(this, "Fluxo de vídeo não aberto!")
        }

        while (running) {

            // read image to matrix
            capture.read(image)

            val newFrame = Mat()
            Imgproc.resize(image, newFrame, Size(640.0, 480.0))

            // Matrix to byte
            val buf = MatOfByte()
            Imgcodecs.imencode(".jpg", newFrame, buf)

            imageData = buf.toArray()
            icon = ImageIcon(imageData)
            cameraScreen.icon = icon
            //val newImage = icon.image.getScaledInstance(640, 480, Image.SCALE_SMOOTH)
            //cameraScreen.icon = ImageIcon(newImage)

            if (mustCapture) {
                mustCapture = false
                val name = SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(Date())
                Imgcodecs.imwrite("images/${name}.jpg", image)
                JOptionPane.showMessageDialog(this, "Imagem $name salva!")
            }

        }

        capture.release()
        this.exit()

    }

    private fun exit() {
        println("Finished!")
        capture.release()
        image.release()
        exitProcess(0)
    }

}
