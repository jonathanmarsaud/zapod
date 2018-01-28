package org.marsaud.zapod

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File

class ImageActivity : AppCompatActivity() {
    var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide() // no ActionBar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN) // no StatusBar (so FullScreen)
        setContentView(R.layout.activity_image)

        file = File(filesDir, "apod.png")
        photoView.setImageURI(Uri.fromFile(file))
    }

    override fun onDestroy() {
        super.onDestroy()

        file = File(filesDir, "apod.png")
        file?.delete()
    }
}