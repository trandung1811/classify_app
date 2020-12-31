package com.example.final_app

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream
import java.io.File

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
            predictedResult.text = getString(R.string.unable_to_find)
            confidenceTextview.text = getString(R.string.try_again)
            explore.text = getString(R.string.more_information)
            explore.isEnabled = false
            btnExplore.isEnabled = false
            btnShare.isEnabled = false

        } else {

            imageView.setImageBitmap(bitmap)
            predictedResult.text = result
            confidenceTextview.text = getString(R.string.result_1) +" " + confidence + getString(R.string.result_2) +" " + result
            explore.text = getString(R.string.information) + " " + result + ":"
            btnSave.setOnClickListener {
                val new_intent = Intent()
                setResult(RESULT_OK, new_intent)
                finish()
            }

        }
        btnExplore.setOnClickListener {
             val intent = Intent(this, display_activity::class.java)
            intent.putExtra("display_name", result)
            startActivity(intent)
        }


        //Create image
        val paint = Paint()
        val height: Float = paint.measureText("yY")
        val bitHeight = bitmap.height
        val bitmap_ = Bitmap.createBitmap(bitmap.width, (height+ bitHeight/5).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap_)

        var string = getString(R.string.result_1) +" " + confidence + getString(R.string.result_2) +" " + result

        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawPaint(paint)
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.textSize = (bitmap.height/20).toFloat()
        paint.textAlign = Paint.Align.CENTER

        val x_coord: Int = bitmap_.width /2
        canvas.drawText(string, x_coord.toFloat(), height + bitHeight/7, paint)
        var newBitmap = combineImages(bitmap, bitmap_)
        val bytes = ByteArrayOutputStream()
        newBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, newBitmap, "Title", null)
        val imageUri: Uri = Uri.parse(path)
        btnShare.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"
            share.putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(Intent.createChooser(share, "Select"))

        }


        val file: File = this.getFileStreamPath(fileName)
        val deleted: Boolean = file.delete()
    }
    fun combineImages(c: Bitmap, s: Bitmap): Bitmap? {
        var cs: Bitmap? = null
        var width = c.width
        var height = c.height + s.height


        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val comboImage = Canvas(cs)
        comboImage.drawBitmap(c, 0f, 0f, null)
        comboImage.drawBitmap(s, 0f, c.height.toFloat() +10f, null)
    return cs
    }
}