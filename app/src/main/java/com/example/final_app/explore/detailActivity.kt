package com.example.final_app.explore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import com.example.final_app.R
class detailActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // custom action bar
        val mActionBar = supportActionBar
        mActionBar?.setDisplayShowHomeEnabled(false)
        mActionBar?.setDisplayShowTitleEnabled(false)
        val mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar,null)
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
        if(bundle!=null){
            webView.loadUrl( "https://en.m.wikipedia.org/wiki/" + bundle.getString("dog_name"));
        }
    }
}
