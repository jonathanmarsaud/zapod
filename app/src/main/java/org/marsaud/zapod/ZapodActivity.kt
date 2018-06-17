package org.marsaud.zapod

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_zapod.*
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.util.*

class ZapodActivity : AppCompatActivity() {
    val baseUrl = "https://apod.nasa.gov/apod/"
    val client = OkHttpClient()
    val version = "2.20"
    var bmp: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zapod)
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build()) // networking stuff in UI thread, avoid "android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork"

        bmp = getImage(getPage(baseUrl + "astropix.html"))
        if (bmp != null) {
            apod.setImageBitmap(bmp)
            val title = getPage(baseUrl + "astropix.html", 1)
            if (title != null) {
                titleTextView.text = title
            } else {
                titleTextView.visibility = View.GONE
            }
            sizeTextView.text = "${bmp?.width} x ${bmp?.height}"
            val display = windowManager.defaultDisplay
            val displaySize = Point()
            display.getSize(displaySize)
            screenSizeTextView.text = "(${displaySize.x} x ${displaySize.y})"
        } else {
            setWallpaperButton.visibility = View.GONE
            errorTextView.visibility = View.VISIBLE
        }

        val alarmExist = (PendingIntent.getBroadcast(this, 0, Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE) != null)
        if (!alarmExist) {
            setRecurringAlarm(this)
        }
        client.newCall(Request.Builder().url("https://www.marsaud.org/zapod").header("User-Agent", "Zapod/$version").build()).execute() // for stats
    }

    /**
     * Inflate ActionBar from res/menu/menu_main.xml.
     *
     * @param menu Mandatory signature for this function.
     * @return Mandatory signature for this function.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * After ActionBar is inflated, inflate the menu itself.
     *
     * @param item Item id of the menu.
     * @return Mandatory signature for this function.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.apodWeb -> {
                startActivity(Intent(Intent.ACTION_VIEW, "https://apod.nasa.gov/".toUri()))
                return super.onOptionsItemSelected(item)
            }
            R.id.marsaudWeb -> {
                startActivity(Intent(Intent.ACTION_VIEW, "https://www.marsaud.org/".toUri()))
                return super.onOptionsItemSelected(item)
            }
            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return super.onOptionsItemSelected(item)
            }
            else -> return false
        }
    }

    /**
     * Get apod.nasa.gov webpage and parse its content to find the APOD and tht title of it (using OKHTTP + Jsoup).
     *
     * @param url URL of the APOD homepage.
     * @param type Int of value "0" by default, which get the image URL; if it's different from 0, get the title instead.
     * @return Return a String containing the URL to the APOD.
     */
    fun getPage(url: String, type: Int = 0): String? {
        // Get webpage via OKHTTP
        val request = Request.Builder().url(url).header("User-Agent", "Zapod/$version").build()
        val response = client.newCall(request).execute()
        val responseString = response.body()?.string()

        // Parse webpage via JSoup
        val document = Jsoup.parse(responseString)
        var parsedString: String? = null
        if (type == 0) {
            val element = document.select("a")[1] // using get(1) instead of first() (= 0) to have the second element - Kotlin conversion: replace ".get(1)" by "[1]"
            parsedString = element.attr("href")
            return baseUrl + parsedString
        } else {
            val element = document.select("b").first()
            parsedString = element.text()
            return parsedString
        }
    }

    /**
     * Get the APOD (using OKHTTP).
     *
     * @param url URL of the APOD.
     * @return Return a Bitmap type containing the APOD.
     */
    fun getImage(url: String?): Bitmap? {
        val request = Request.Builder().url(url).header("User-Agent", "Zapod/$version").build()
        val response = client.newCall(request).execute()
        val stream = response.body()?.byteStream()
        return BitmapFactory.decodeStream(stream)
    }

    /**
     * Start ImageActivity to let user rotate/pinch-to-zoom.
     *
     * @param view Mandatory to use XML onClick attribute.
     */
    fun startImageActivity(view: View) {
        if (bmp != null) { // avoid using the PhotoView on the place-holder "NO DATA"
            Snackbar.make(rootView, R.string.loading, Snackbar.LENGTH_LONG).show()
            launch {
                val file = File(filesDir, "apod.png")
                val fileOut = FileOutputStream(file)
                bmp?.compress(Bitmap.CompressFormat.PNG, 100, fileOut) // 100 is quality but ignored for PNGs as it's a lossless format
                fileOut.flush()
                fileOut.close()
                startActivity(Intent(this@ZapodActivity, ImageActivity::class.java))
            }
        }
    }

    /**
     * Action triggered by the "define" button.
     *
     * @param view Mandatory to use XML onClick attribute.
     */
    fun defineWallpaper(view: View) {
        launch {
            val wallpaper = WallpaperManager.getInstance(applicationContext)
            wallpaper.setBitmap(bmp) // system wallpaper
            if (Build.VERSION.SDK_INT >= 24) { // only supported since Android 7.0 (Nougat)
                wallpaper.setBitmap(bmp, null, true, WallpaperManager.FLAG_LOCK) // lockscreen wallpaper
            }
        }
        Snackbar.make(rootView, R.string.defined, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Set a recurring alarm to wake-up the AlarmReceiver
     *
     * @param context Context to be used.
     */
    fun setRecurringAlarm(context: Context) {
        val updateTime = Calendar.getInstance()
        updateTime.timeZone = TimeZone.getDefault()
        updateTime.set(Calendar.HOUR_OF_DAY, 6)
        updateTime.set(Calendar.MINUTE, 0)
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("notification_content", getString(R.string.notification_content)) // R.string is not available through a class which is not an Activity
        intent.putExtra("notification_channeldescription", getString(R.string.notification_channeldescription)) // R.string is not available through a class which is not an Activity
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }
}