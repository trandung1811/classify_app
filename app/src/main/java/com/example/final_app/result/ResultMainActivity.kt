package com.example.final_app.result

import android.content.Intent
import android.content.res.AssetManager
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
import com.example.final_app.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class resultMainActivity : AppCompatActivity() {

    private lateinit var predictedResult: TextView
    private lateinit var confidenceTextview: TextView
    private lateinit var confidencTextview_1: TextView
    private lateinit var confidenceTextview_2: TextView
    private lateinit var imageView: ImageView
    private lateinit var explore: TextView
    private lateinit var dogBreedImage: ImageView
    private lateinit var dogBreedImage_1: ImageView
    private lateinit var dogBreedImage_2: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnExplore: Button
    private lateinit var btnShare: Button
    private lateinit var btnExplore_1: Button
    private lateinit var btnExplore_2: Button
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        //ads
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3924650906279453~7081555116", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message);
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.");
                mInterstitialAd = interstitialAd
            }
        })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }
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

        //take data
        val intent = intent
        val result = intent.getStringExtra("predictedResult")
        val confidence = intent.getStringExtra("confidence")
        val result_1 = intent.getStringExtra("predictedResult_1")
        val confidence_1 = intent.getStringExtra("confidence_1")
        val result_2 = intent.getStringExtra("predictedResult_2")
        val confidence_2 = intent.getStringExtra("confidence_2")
        val fileName = intent.getStringExtra("fileName")
        val bitmap = BitmapFactory.decodeStream(openFileInput(fileName))

        imageView = findViewById(R.id.resultView)
        dogBreedImage = findViewById(R.id.dogBreedImageView)
        dogBreedImage_1 = findViewById(R.id.dogBreedImageView_1)
        dogBreedImage_2 = findViewById(R.id.dogBreedImageView_2)
        predictedResult = findViewById(R.id.resultName)
        confidenceTextview = findViewById(R.id.resultAcc)
        confidencTextview_1 = findViewById(R.id.resultAcc_1)
        confidenceTextview_2 = findViewById(R.id.resultAcc_2)
        explore = findViewById(R.id.resultExplore)
        btnSave = findViewById(R.id.btnSave)
        btnExplore = findViewById(R.id.btnResExplore)
        btnShare = findViewById(R.id.btnShare)
        btnExplore_1 = findViewById(R.id.btnResExplore_1)
        btnExplore_2 = findViewById(R.id.btnResExplore_2)



        if (confidence == "0") {
            btnSave.isEnabled = false
            imageView.setImageBitmap(bitmap)
            predictedResult.text = getString(R.string.unable_to_find)
            confidenceTextview.visibility = View.INVISIBLE
            confidencTextview_1.visibility = View.INVISIBLE
            confidenceTextview_2.visibility = View.INVISIBLE
            var subText: TextView = findViewById(R.id.constTextView)
            subText.text = getString(R.string.try_again)
            explore.text = getString(R.string.more_information)
            explore.isEnabled = false
            btnExplore.isEnabled = false
            btnShare.isEnabled = false

        } else {

            imageView.setImageBitmap(bitmap)
            predictedResult.text = result
            confidenceTextview.text = " " + result + " - " + confidence +"%"
            confidencTextview_1.text = " " + result_1 + " - " + confidence_1 +"%"
            confidenceTextview_2.text = " " + result_2 + " - " + confidence_2 +"%"
            explore.text = getString(R.string.information)
            btnSave.setOnClickListener {
                val new_intent = Intent()
                setResult(RESULT_OK, new_intent)
                finish()
            }

        }
        // new section
        var locale = Locale.getDefault()
        var mLabelPath = ""
        if (locale.toString() == "vi_GB") {
            mLabelPath = "dog_label_vi.txt"
        }
        else {
            mLabelPath = "dog_label.txt"
        }
        var label_list = loadLabelList(assets, mLabelPath)
        var link_list = loadLabelList(assets, "DogBreedList.txt")

        var position: Int = 200
        for (i in 0 until label_list.size) {
            if (result == label_list[i]) {
                position = i
                break
            }
        }
        if (position != 200) {
            Picasso.with(this).load(link_list[position]).into(dogBreedImage)
        }
        btnExplore.text = result
        btnExplore.setOnClickListener {
             val intent = Intent(this, resultDisplay::class.java)
            intent.putExtra("display_name", result)
            startActivity(intent)
        }

        var position_1: Int = 200
        for (i in 0 until label_list.size) {
            if (result_1 == label_list[i]) {
                position_1 = i
                break
            }
        }
        if (position_1 != 200) {
            Picasso.with(this).load(link_list[position_1]).into(dogBreedImage_1)
        }
        btnExplore_1.text = result_1
        btnExplore_1.setOnClickListener {
            val intent = Intent(this, resultDisplay::class.java)
            intent.putExtra("display_name", result_1)
            startActivity(intent)
        }

        var position_2: Int = 200
        for (i in 0 until label_list.size) {
            if (result_2 == label_list[i]) {
                position_2 = i
                break
            }
        }
        if (position_2 != 200) {
            Picasso.with(this).load(link_list[position_2]).into(dogBreedImage_2)
        }
        btnExplore_2.text = result_2
        btnExplore_2.setOnClickListener {
            val intent = Intent(this, resultDisplay::class.java)
            intent.putExtra("display_name", result_2)
            startActivity(intent)
        }


        //Create image

        btnShare.setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
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


        val file: File = this.getFileStreamPath(fileName)
        val deleted: Boolean = file.delete()
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
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