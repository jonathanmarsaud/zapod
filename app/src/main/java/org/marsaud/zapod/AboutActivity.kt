package org.marsaud.zapod

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }

    /**
     * Visit www.marsaud.org, triggered by XML onClick attributes.
     *
     * @param view Mandatory signature function for onClick attributes on XML.
     */
    fun goToMarsaudOrg(view: View) {
        val marsaudOrgIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.marsaud.org/"))
        startActivity(marsaudOrgIntent)
    }

    /**
     * Visit kotlinlang.org, triggered by XML onClick attributes.
     *
     * @param view Mandatory signature function for onClick attributes on XML.
     */
    fun goToKotlinWebsite(view: View) {
        val kotlinWebsiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://kotlinlang.org/"))
        startActivity(kotlinWebsiteIntent)
    }

    /**
     * Visit https://square.github.io/okhttp/, triggered by XML onClick attributes.
     *
     * @param view Mandatory signature function for onClick attributes on XML.
     */
    fun goToOkHttpWebsite(view: View) {
        val okHttpWebsiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://square.github.io/okhttp/"))
        startActivity(okHttpWebsiteIntent)
    }

    /**
     * Visit jsoup.org, triggered by XML onClick attributes.
     *
     * @param view Mandatory signature function for onClick attributes on XML.
     */
    fun goToJsoupWebsite(view: View) {
        val jsoupWebsiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://jsoup.org/"))
        startActivity(jsoupWebsiteIntent)
    }

    /**
     * Visit www.nasa.gov, triggered by XML onClick attributes.
     *
     * @param view Mandatory signature function for onClick attributes on XML.
     */
    fun goToNasaWebsite(view: View) {
        val nasaWebsiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.nasa.gov/"))
        startActivity(nasaWebsiteIntent)
    }
}