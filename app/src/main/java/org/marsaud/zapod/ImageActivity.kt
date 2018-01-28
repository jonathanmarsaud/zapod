package org.marsaud.zapod

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val bmp = intent.getParcelableExtra<Bitmap>("apod")
        photoView.setImageBitmap(bmp)
    }
}