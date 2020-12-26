package com.example.final_app

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class resultMainActivity : AppCompatActivity() {
    private lateinit var predictedResult: TextView
    private lateinit var confidenceTextview: TextView
    private lateinit var imageView: ImageView
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val intent = intent
        val result = intent.getStringExtra("predictedResult")
        val confidence = intent.getStringExtra("confidence")
        val fileName = intent.getStringExtra("fileName")
        val bitmap = BitmapFactory.decodeStream(openFileInput(fileName))
        imageView = findViewById(R.id.resultImageView)
        predictedResult = findViewById(R.id.dogBreedTextView)
        confidenceTextview = findViewById(R.id.confidenceTextView)

        predictedResult.text = "Your dog is $result "
        confidenceTextview.text = "The confidence level is at $confidence"
        imageView.setImageBitmap(bitmap)

        btnSave = findViewById(R.id.btnSave)
        if (confidence == "0%") {
            btnSave.isEnabled = false
        } else {
            btnSave.setOnClickListener {
                val new_intent = Intent()
                setResult(RESULT_OK, new_intent)
                finish()
            }
        }
    }
}