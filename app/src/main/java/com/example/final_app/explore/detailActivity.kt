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

        setContentView(R.layout.activity_detail)
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        val bundle = intent.extras
        if (bundle != null) {
                var locale = Locale.getDefault()
                if (locale.toString() == "vi_GB") {
                  var dogName =   bundle.getString("dog_name")
                    if (dogName == "Chó Bắc Hà") {
                        webView.loadUrl("https://wikipet.vn/cho-bac-ha")
                    } else if (dogName == "Chó Mông Cộc") {
                        webView.loadUrl("https://wikipet.vn/cho-mong-coc")
                    } else if (dogName == "Dingo Đông Dương (chó Lài)") {
                        webView.loadUrl("https://wikipet.vn/cho-dingo-dong-duong")
                    } else if (dogName == "Chó Phú Quốc") {
                      webView.loadUrl("https://wikipet.vn/cho-phu-quoc")
                    } else {
                        webView.loadUrl("https://vi.m.wikipedia.org/wiki/" + bundle.getString("dog_name"))
                    }
            } else {
                webView.loadUrl("https://en.m.wikipedia.org/wiki/" + bundle.getString("dog_name"));
            }
        }
    }
}
