package com.example.final_app.explore

import android.content.Intent
import android.content.res.AssetManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_app.R
import com.example.final_app.interfer.onItemClickListener
import com.example.final_app.model.Model
import java.util.*
import kotlin.collections.ArrayList

class exploreActivity : AppCompatActivity() {

    private  val mLabelPath = "dog_label.txt"
    private  var label_list: ArrayList<String> = ArrayList()
    private  var displayList: ArrayList<String> = ArrayList()
    private lateinit var exploreAdapter: exploreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

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

        var sub_label_list = loadLabelList(assets, mLabelPath)
        label_list = ArrayList<String>(sub_label_list)
        displayList.addAll(label_list)
        var listView = findViewById<ListView>(R.id.exploreListView)
        exploreAdapter = exploreAdapter(this, R.layout.explore_row, displayList)
        listView.adapter = exploreAdapter
        listView.setOnItemClickListener { parent: AdapterView<*>, view: View, position: Int, id: Long ->
            val intent = Intent(this, detailActivity::class.java)
            intent.putExtra("dog_name", displayList[position])
            startActivity(intent)
        }
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val menuItem = menu!!.findItem(R.id.search)
        if (menuItem != null) {
            val searchView = menuItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return  true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        displayList.clear()
                        val search = newText.toLowerCase(Locale.getDefault())
                        label_list.forEach{
                            if (it.toLowerCase(Locale.getDefault()).contains(search)) {
                                displayList.add(it)
                            }
                            exploreAdapter.notifyDataSetChanged()
                        }
                    }
                    else {
                        displayList.clear()
                        displayList.addAll(label_list)
                        exploreAdapter.notifyDataSetChanged()
                    }
                    return true
                }


            })
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}