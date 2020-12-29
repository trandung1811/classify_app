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
    private lateinit var explore: TextView


    private lateinit var btnSave: Button
    private lateinit var btnExplore: Button
    private lateinit var btnShare: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val intent = intent
        val result = intent.getStringExtra("predictedResult")
        val confidence = intent.getStringExtra("confidence")
        val fileName = intent.getStringExtra("fileName")
        val bitmap = BitmapFactory.decodeStream(openFileInput(fileName))

        imageView = findViewById(R.id.resultView)
        predictedResult = findViewById(R.id.resultName)
        confidenceTextview = findViewById(R.id.resultAcc)
        explore = findViewById(R.id.resultExplore)
        btnSave = findViewById(R.id.btnSave)
        btnExplore = findViewById(R.id.btnResExplore)
        btnShare = findViewById(R.id.btnShare)


        if (confidence == "0") {
            btnSave.isEnabled = false
            imageView.setImageBitmap(bitmap)
            predictedResult.text = "Unable to find"
            confidenceTextview.text = "Please try again by focusing on dog face"
            explore.text = "More information about dog can be found in Explore"
            explore.isEnabled = false
            btnExplore.isEnabled = false
            btnShare.isEnabled = false



        } else {

            imageView.setImageBitmap(bitmap)
            predictedResult.text = result
            confidenceTextview.text = "Your dog look $confidence% similar to $result"
            explore.text = "More information about $result: "
            btnSave.setOnClickListener {
                val new_intent = Intent()
                setResult(RESULT_OK, new_intent)
                finish()
            }

        }
    }
}