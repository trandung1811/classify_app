package com.example.final_app.history

import android.content.Intent
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.final_app.R
import com.example.final_app.display_activity
import java.io.ByteArrayOutputStream
import java.io.File

class historyViewMainActivity : AppCompatActivity() {

    private lateinit var historyImageView: ImageView
    private lateinit var historyName: TextView
    private lateinit var historyAcc: TextView
    private lateinit var hisExplore: TextView
    private lateinit var btnHisShare: Button
    private lateinit var btnHisExplore: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_view_main)

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


        historyImageView = findViewById(R.id.historyMainView)
        historyName = findViewById(R.id.historyMainName)
        historyAcc = findViewById(R.id.historyMainAcc)
        btnHisShare = findViewById(R.id.btnHisShare)
        hisExplore = findViewById(R.id.historyMainExplore_1)
        btnHisExplore = findViewById(R.id.btnHisExplore)

        val intent = intent
        val result = intent.getStringExtra("historyName")
        val confidence = intent.getStringExtra("historyConfidence")
        val fileName = intent.getStringExtra("historyImg")
        val bitmap = BitmapFactory.decodeStream(openFileInput(fileName))

        historyImageView.setImageBitmap(bitmap)
        historyName.text =  result
        historyAcc.text = getString(R.string.result_1) +" " + confidence + getString(R.string.result_2) +" " + result
        hisExplore.text = getString(R.string.information) + " " + result + ":"


        btnHisExplore.setOnClickListener {
            val intent = Intent(this, display_activity::class.java)
            intent.putExtra("display_name", result)
            startActivity(intent)
        }

        //Creat image
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

        btnHisShare.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"
            val bytes = ByteArrayOutputStream()
            newBitmap?.compress(CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(contentResolver, newBitmap, "Title", null)
            val imageUri: Uri = Uri.parse(path)
            share.putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(Intent.createChooser(share, "Select"))
        }
        //delete file
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
        comboImage.drawBitmap(s,0f,c.height.toFloat()+ 10f , null)
        return cs
    }
}
