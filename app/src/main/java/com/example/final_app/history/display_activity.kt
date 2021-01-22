package com.example.final_app.history

import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import com.example.final_app.R
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

        //display
        var mLabelPath_1 = "dog_label_vi.txt"
        var mLabelPath_2 = "dog_label.txt"

        var dogList_1 = loadLabelList(assets, mLabelPath_1)
        var dogList_2 = loadLabelList(assets, mLabelPath_2)

        val bundle = intent.extras
        if (bundle != null) {

            var dogName = bundle.getString("display_name")
            var check = bundle.getInt("check")
            var pos = bundle.getInt("pos")

            var locale = Locale.getDefault()

            if (locale.toString() == "vi_GB") {
                if (check == 1) {
                    dogName = dogList_1[pos]
                }
                if (dogName == "Chó Bắc Hà") {
                    webView.loadUrl("https://wikipet.vn/cho-bac-ha")
                } else if (dogName == "Chó Mông Cộc") {
                    webView.loadUrl("https://wikipet.vn/cho-mong-coc")
                } else if (dogName == "Dingo Đông Dương (chó Lài)") {
                    webView.loadUrl("https://wikipet.vn/cho-dingo-dong-duong")
                } else if (dogName == "Chó Phú Quốc") {
                    webView.loadUrl("https://wikipet.vn/cho-phu-quoc")
                } else {
                    webView.loadUrl("https://vi.m.wikipedia.org/wiki/" + dogName)
                }
            } else {
                if (check == 0) {
                    dogName = dogList_2[pos]
                }
                webView.loadUrl("https://en.m.wikipedia.org/wiki/" + dogName);
            }
        }
    }
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }
}
