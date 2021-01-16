package com.example.final_app.history

import android.content.Intent
import android.content.res.AssetManager
import android.graphics.*
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
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.*

class historyViewMainActivity : AppCompatActivity() {

    private lateinit var historyImageView: ImageView
    private lateinit var hisDogImageView: ImageView
    private lateinit var hisDogImageView_1: ImageView
    private lateinit var hisDogImageView_2: ImageView
    private lateinit var historyName: TextView
    private lateinit var historyAcc: TextView
    private lateinit var hisExplore: TextView
    private lateinit var historyAcc_1: TextView
    private lateinit var historyAcc_2: TextView
    private lateinit var btnHisShare: Button
    private lateinit var btnHisExplore_1: Button
    private lateinit var btnHisExplore: Button
    private lateinit var btnHisExplore_2: Button

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
        historyAcc = findViewById(R.id.hisTextView2)
        historyAcc_1 = findViewById(R.id.hisTextView3)
        historyAcc_2 = findViewById(R.id.hisTextView4)
        btnHisShare = findViewById(R.id.btnHisShare)
        hisExplore = findViewById(R.id.historyMainExplore_1)
        btnHisExplore = findViewById(R.id.btnHisExplore)
        btnHisExplore_1 = findViewById(R.id.btnHisExplore_1)
        btnHisExplore_2 = findViewById(R.id.btnHisExplore_2)
        hisDogImageView = findViewById(R.id.hisDogBreedImageView)
        hisDogImageView_1 = findViewById(R.id.imageView2)
        hisDogImageView_2 = findViewById(R.id.imageView3)

        val intent = intent
        val result = intent.getStringExtra("historyName")
        val confidence = intent.getStringExtra("historyConfidence")
        val result_1 = intent.getStringExtra("historyName_1")
        val confidence_1 = intent.getStringExtra("historyConfidence_1")
        val result_2 = intent.getStringExtra("historyName_2")
        val confidence_2 = intent.getStringExtra("historyConfidence_2")
        val fileName = intent.getStringExtra("historyImg")
        val bitmap = BitmapFactory.decodeStream(openFileInput(fileName))

        historyImageView.setImageBitmap(bitmap)
        historyName.text =  result
        historyAcc.text = " " + result + " - " + confidence +"%"
        historyAcc_1.text = " " + result_1 + " - " + confidence_1 +"%"
        historyAcc_2.text = " " + result_2 + " - " + confidence_2 +"%"
        hisExplore.text = getString(R.string.information)

        // new section

        var label_list_1 = loadLabelList(assets,"dog_label_vi.txt" )
        var label_list_2 = loadLabelList(assets, "dog_label.txt")
        var link_list = loadLabelList(assets, "DogBreedList.txt")

        var position: Int = 200
        var check2 = -1
        for (i in label_list_1.indices) {
            if (result == label_list_1[i]) {
                position = i
                check2 = 0
                break
            }
            if (result == label_list_2[i]) {
                position = i
                check2 = 1
                break
            }
        }
        if (position != 200) {
            Picasso.with(this).load(link_list[position]).into(hisDogImageView)
        }

        btnHisExplore.text = result
        btnHisExplore.setOnClickListener {
            val intent = Intent(this, display_activity::class.java)
            intent.putExtra("display_name", result)
            intent.putExtra("check",check2)
            intent.putExtra("pos",position)
            startActivity(intent)
        }


        var position_1: Int = 200
        var check_ = -1
        for (i in label_list_1.indices) {
            if (result_1 == label_list_1[i]) {
                position_1 = i
                check_ = 0
                break
            }
            if (result_1 == label_list_2[i]) {
                position_1 = i
                check_ = 1
                break
            }
        }
        if (position_1 != 200) {
            Picasso.with(this).load(link_list[position_1]).into(hisDogImageView_1)
        }

        btnHisExplore_1.text = result_1
        btnHisExplore_1.setOnClickListener {
            val intent = Intent(this, display_activity::class.java)
            intent.putExtra("display_name", result_1)
            intent.putExtra("check",check_)
            intent.putExtra("pos",position_1)
            startActivity(intent)
        }


        var position_2: Int = 200
        var check: Int = -1
        for (i in label_list_1.indices) {
            if (result_2 == label_list_1[i]) {
                position_2 = i
                check = 0
                break
            }
            if (result_2 == label_list_2[i]) {
                position_2 = i
                check = 1
                break
            }
        }
        if (position_2 != 200) {
            Picasso.with(this).load(link_list[position_2]).into(hisDogImageView_2)
        }

        btnHisExplore_2.text = result_2
        btnHisExplore_2.setOnClickListener {
            val intent = Intent(this, display_activity::class.java)
            intent.putExtra("display_name", result_2)
            intent.putExtra("check",check)
            intent.putExtra("pos",position_2)
            startActivity(intent)
        }
        //Creat image


        btnHisShare.setOnClickListener {
            var string = getString(R.string.result_1) +" " + confidence + getString(R.string.result_2) +" " + result
            val paint = Paint()
            val height: Float = paint.measureText("yY")
            val bitHeight = bitmap.height
            val bitmap_ = Bitmap.createBitmap(bitmap.width, (height+ bitHeight/5).toInt(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap_)

            paint.color = Color.WHITE
            paint.style = Paint.Style.FILL
            canvas.drawPaint(paint)
            paint.color = Color.BLACK
            paint.isAntiAlias = true
            paint.textSize = (bitmap.width/25).toFloat()
            paint.textAlign = Paint.Align.CENTER

            val x_coord: Int = bitmap_.width /2
            canvas.drawText(string, x_coord.toFloat(), height + bitHeight/12, paint)
            var newBitmap = combineImages(bitmap, bitmap_)
            val bytes = ByteArrayOutputStream()
            newBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(contentResolver, newBitmap, "Title", null)
            val imageUri: Uri = Uri.parse(path)
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"
            share.putExtra(Intent.EXTRA_STREAM, imageUri)
            startActivity(Intent.createChooser(share, "Select"))
        }

    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }
    fun combineImages(c: Bitmap, s: Bitmap): Bitmap? {
        var cs: Bitmap? = null
        var height = c.height + s.height
        var width = c.width
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val comboImage = Canvas(cs)
        comboImage.drawBitmap(c, 0f, 0f, null)
        comboImage.drawBitmap(s,0f,c.height.toFloat()+ 10f , null)
        return cs
    }
}
