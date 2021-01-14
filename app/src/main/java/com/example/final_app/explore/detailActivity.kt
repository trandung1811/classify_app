package com.example.final_app.explore

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import com.example.final_app.R
import org.intellij.lang.annotations.Language
import java.util.*

class detailActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // custom action bar
        val mActionBar = supportActionBar
        mActionBar?.setDisplayShowHomeEnabled(false)
        mActionBar?.setDisplayShowTitleEnabled(false)
        val mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null)
        mActionBar?.customView = mCustomView
        mActionBar?.setDisplayShowCustomEnabled(true)
        val btnRateUs: ImageButton = findViewById(R.id.btnRate)
        val btnGoToHis: ImageButton = findViewById(R.id.btn_history)
        btnGoToHis.visibility = View.INVISIBLE
        btnRateUs.visibility = View.INVISIBLE

        //alear Dialog
        var locale = Locale.getDefault()
        if (locale.toString() == "vi_GB") {
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            var key = sharedPreferences.getBoolean("key", false)
            if (!key) {
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.sample, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                var button_1: Button = mAlertDialog.findViewById(R.id.btnSample)
                button_1.setOnClickListener {
                    mAlertDialog.dismiss()
                }
                var button_2: Button = mAlertDialog.findViewById(R.id.btnDismiss)
                button_2.setOnClickListener {
                    var check = true
                    val sharedPreferences =
                        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean("key", check)
                    editor.apply()
                    mAlertDialog.dismiss()
                }
            }
        }

        setContentView(R.layout.activity_detail)
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        val bundle = intent.extras
        if (bundle != null) {
            var dog_name = bundle.getString("dog_name")
            if (dog_name == "Bac Ha") {
                var locale = Locale.getDefault()
                if (locale.toString() == "vi_GB") {
                    webView.loadUrl("https://sieupet.com/giong-cho-bac-ha.html")
                } else {
                    webView.loadUrl("https://en.m.wikipedia.org/wiki/" + bundle.getString("dog_name"));
                }
            } else if (dog_name == "H'Mong Coc Duoi") {
                var locale = Locale.getDefault()
                if (locale.toString() == "vi_GB") {
                    webView.loadUrl("https://www.vietdvm.com/thu-canh/giong-thu-canh/giong-cho-hmong-coc-duoi.html")
                } else {
                    webView.loadUrl("https://en.m.wikipedia.org/wiki/" + bundle.getString("dog_name"));
                }
            } else {
                webView.loadUrl("https://en.m.wikipedia.org/wiki/" + bundle.getString("dog_name"));
            }
        }
    }
}
