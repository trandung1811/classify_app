package com.example.final_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import java.util.*

class display_activity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_activity)
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
        setContentView(R.layout.activity_display_activity)
        webView = findViewById(R.id.disWebView)
        webView.webViewClient = WebViewClient()

        val bundle = intent.extras
        if (bundle != null) {
            var locale = Locale.getDefault()
            if (locale.toString() == "vi_GB") {
                var dogName = bundle.getString("dog_name")
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
