package com.example.final_app.recyclerview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


class recyMainActivity : AppCompatActivity(), onItemClickListener {

    private lateinit var post: ArrayList<Model>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_main)
        //start the second recycle view

        post = loadData()
        var recycleView: RecyclerView = findViewById(R.id.historyRecyclerview)
        displayRecyclerView(recycleView, post)

    }
    private fun loadData(): ArrayList<Model> {
        var sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson = Gson()
        var json: String? = sharedPreferences.getString("task", null)

        val turnsType = object : TypeToken<ArrayList<Model>>() {}.type
        val post = gson.fromJson<ArrayList<Model>>(json, turnsType)
        if (post == null) {
            var mPost = ArrayList<Model>()

            var bitmap_1: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.d2)
            var bitmap_2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.husky)
            var bitmap_3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.corgi)
            var bitmap_4: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.alaska)
            mPost.add(Model(createImageFromBitmap(bitmap_1), "Becgie","100%"))
            mPost.add(Model(createImageFromBitmap(bitmap_2), "Husky", "100%"))
            mPost.add(Model(createImageFromBitmap(bitmap_3), "Pembroke Welsh Corgis", "100%"))
            mPost.add(Model(createImageFromBitmap(bitmap_4), "Alaskan Malamute", "100%"))
            return mPost
        } else {
            return post
        }
    }
    private fun createImageFromBitmap(bitmap: Bitmap): String? {
        var fileName: String? = "myImage" + Math.random() //no .png or .jpg needed
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val fo: FileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            fo.write(bytes.toByteArray())
            // remember close file output
            fo.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            fileName = null
        }
        return fileName
    }
    private fun displayRecyclerView(view: RecyclerView, post: ArrayList<Model>) {

        view.layoutManager = LinearLayoutManager(this)
        view.adapter = historyAdapter(this,post, this)

    }

    override fun onItemClick(item: Model, position: Int) {
        val intent = Intent(this, historyViewMainActivity::class.java)
        intent.putExtra("historyName", item.name)
        intent.putExtra("historyConfidence",item.acc)
        intent.putExtra("historyImg", item.imgFileName)
        startActivity(intent)
    }
}