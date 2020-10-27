package c.kapps.appinstaller

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import c.kapps.appinstaller.utils.DownloadController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_first.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
    }

    lateinit var downloadController: DownloadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        val apkUrl = "https://androidwave.com/source/apk/app-pagination-recyclerview.apk"
        downloadController = DownloadController(this, apkUrl)

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            // check storage permission granted if yes then start downloading file
            checkStoragePermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloadController.enqueueDownload()
            } else {
                // Permission request was denied.
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
                )
                Snackbar.make(mainLayout, R.string.storage_permission_denied, Snackbar.LENGTH_LONG).show()
            }
        }
    }


    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M || checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // start downloading
            downloadController.enqueueDownload()
        } else {
            // Permission is missing and must be requested.
            requestStoragePermission()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestStoragePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mainLayout, R.string.storage_access_required, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok){
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
                )
            }.show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_STORAGE
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.

        val m = arrayOf(
            "⚙ Preferences",
            "\uD83D\uDE4F  Share App",
            "\uD83C\uDF1F  Rate Us",
            "\uD83E\uDD1D  Help & Feedback",
            "\uD83E\uDD39\u200D♀️  More Apps",
            "\uD83D\uDECD  Shop",
            "\uD83D\uDC96  About Us",
            "\uD83D\uDEE1  Privacy Policy"
        )
        for (i in m.indices) {
            menu.add(i, i, i, m[i])
        }
//        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            1 -> startActivity(
                Intent(Intent.ACTION_SEND).setType("text/plain")
                    .putExtra(
                        Intent.EXTRA_TEXT,
                        """Finally Send WhatsApp messages without saving the contact number on phone!

Get this ultra light app
https://play.google.com/store/apps/details?id=c.kapps.easymessage.free """
                    )
            )
            2 -> startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=c.kapps.easymessage.free")))
            3 -> startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://f-qr.github.io/r/")))
            4 -> startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/dev?id=5009060970068759882")))
            5 -> startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://krishna-apps.github.io/Shop/")))
            6 -> startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://krishna-apps.github.io/")))
            7 -> {
                val qrId = 31
                startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://f-qr.github.io/r/?$qrId&p")))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}