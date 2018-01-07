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
}