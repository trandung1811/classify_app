package com.example.final_app.explore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.final_app.R
class detailActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        val bundle = intent.extras
        if(bundle!=null){
            webView.loadUrl("http://.wikipedia.org/wiki/" + bundle.getString("dog_name"));
        }
    }
}
