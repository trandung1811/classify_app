package com.example.final_app.history

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.final_app.R

class historyViewMainActivity : AppCompatActivity() {

    private lateinit var historyImageView: ImageView
    private lateinit var historyName: TextView
    private lateinit var historyAcc: TextView
    private lateinit var hisExplore: TextView
    private lateinit var btnHisShare: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_view_main)

        historyImageView = findViewById(R.id.historyMainView)
        historyName = findViewById(R.id.historyMainName)
        historyAcc = findViewById(R.id.historyMainAcc)
        btnHisShare = findViewById(R.id.btnHisShare)
        hisExplore = findViewById(R.id.historyMainExplore_1)

        val intent = intent
        val result = intent.getStringExtra("historyName")
        val confidence = intent.getStringExtra("historyConfidence")
        val fileName = intent.getStringExtra("historyImg")
        val bitmap = BitmapFactory.decodeStream(openFileInput(fileName))

        historyImageView.setImageBitmap(bitmap)
        historyName.text =  result
        historyAcc.text = "Your dog look $confidence% similar to $result"
        hisExplore.text = "More information about $result: "
        btnHisShare.isEnabled = false



    }
}