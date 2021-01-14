package com.example.final_app.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


class historyMainActivity : AppCompatActivity(), onItemClickListener {

    private lateinit var post: ArrayList<Model>
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