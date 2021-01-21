package com.example.final_app.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_app.R
import com.example.final_app.history.historyAdapter
import com.example.final_app.history.historyViewMainActivity
import com.example.final_app.interfer.onItemClickListener
import com.example.final_app.model.Model
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


class historyMainActivity : AppCompatActivity(), onItemClickListener {

    private lateinit var post: ArrayList<Model>
    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //mobile ads

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
                Log.d(TAG, "Ad was dismissed.");
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.");
                mInterstitialAd = null
            }
        }

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

        setContentView(R.layout.activity_history_main)
        //start the second recycle view

        post = loadData()
        var recycleView: RecyclerView = findViewById(R.id.historyRecyclerview)
        displayRecyclerView(recycleView, post)
       // saveData(post)
    }
    private fun loadData(): ArrayList<Model> {
        var sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson = Gson()
        var json: String? = sharedPreferences.getString("task", null)

        val turnsType = object : TypeToken<ArrayList<Model>>() {}.type
        val post = gson.fromJson<ArrayList<Model>>(json, turnsType)

        return post
    }


    private fun displayRecyclerView(view: RecyclerView, post: ArrayList<Model>) {

        view.layoutManager = LinearLayoutManager(this)
        view.adapter = historyAdapter(this,post, this)

    }

    override fun onItemClick(item: Model, position: Int) {
        val intent = Intent(this, historyViewMainActivity::class.java)
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
        intent.putExtra("historyName", item.name)
        intent.putExtra("historyConfidence",item.acc)
        intent.putExtra("historyName_1", item.name_1)
        intent.putExtra("historyConfidence_1",item.acc_1)
        intent.putExtra("historyName_2", item.name_2)
        intent.putExtra("historyConfidence_2",item.acc_2)
        intent.putExtra("historyImg", item.imgFileName)
        startActivity(intent)
    }
}