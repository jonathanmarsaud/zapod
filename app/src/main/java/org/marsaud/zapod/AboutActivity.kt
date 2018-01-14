package org.marsaud.zapod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        kotlinLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://kotlinlang.org/"))) }
        okHttpLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://square.github.io/okhttp/"))) }
        jsoupLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://jsoup.org/"))) }
        nasaLogo.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nasa.gov/"))) }
    }
}