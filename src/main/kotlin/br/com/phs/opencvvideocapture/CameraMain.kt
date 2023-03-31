package br.com.phs.opencvvideocapture

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.highgui.HighGui
import org.opencv.imgproc.Imgproc
import org.opencv.videoio.VideoCapture
import org.opencv.videoio.Videoio
import kotlin.system.exitProcess


fun main() { CameraMain() }
class CameraMain {

    init {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        //System.setProperty("OPENCV_FFMPEG_CAPTURE_OPTIONS", "rtsp_transport;udp")

        val frame = Mat()
        val cap = VideoCapture("rtsp://admin:Um9nZXI=@192.168.15.33:554/mainStream", Videoio.CAP_FFMPEG)

        if (!cap.isOpened) {
            println("Cannot open RTSP stream")
            exitProcess(-1)
        }

        println("Camera flow opened.")

        while (true) {

            cap.read(frame)
            val newFrame = Mat()
            Imgproc.resize(frame, newFrame, Size(640.0, 480.0))
            HighGui.imshow("RTSP stream", newFrame)

            if (HighGui.waitKey(1) == 27) {
                break
            }

        }

        cap.release()
        HighGui.destroyAllWindows()

        exitProcess(0)

    }

}