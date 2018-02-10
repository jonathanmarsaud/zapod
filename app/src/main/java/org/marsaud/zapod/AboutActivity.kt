package org.marsaud.zapod

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.net.toUri
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        kotlinLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, "https://kotlinlang.org/".toUri())) }
        okHttpLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, "https://square.github.io/okhttp/".toUri())) }
        jsoupLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, "https://jsoup.org/".toUri())) }
        nasaLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, "https://www.nasa.gov/".toUri())) }
    }
}