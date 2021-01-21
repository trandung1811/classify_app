package com.example.final_app.bmi

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.final_app.MainActivity
import com.example.final_app.R
import com.example.final_app.recyclerview.historyMainActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.util.*

class detailBMi : AppCompatActivity() {

    private lateinit var imageView: CircleImageView
    private lateinit var bmiTextView: TextView
    private lateinit var resultBMI: TextView
    private lateinit var inputW: TextView
    private lateinit var inputH: TextView
    private lateinit var titleTextView: TextView
    private lateinit var underTextViewM: TextView
    private lateinit var underTextViewF: TextView
    private lateinit var correctTextViewM: TextView
    private lateinit var correctTextViewF: TextView
    private lateinit var upperTextViewM: TextView
    private lateinit var upperTextViewF: TextView
    private lateinit var reCalculate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_bmi)

        // illitialize ID
        imageView = findViewById(R.id.bmiImage)
        bmiTextView = findViewById(R.id.bmiTextView)
        resultBMI = findViewById(R.id.resultBMITextView)
        inputW = findViewById(R.id.inputTextView1)
        inputH = findViewById(R.id.inputTextView2)
        titleTextView = findViewById(R.id.ideaTextView)
        underTextViewM = findViewById(R.id.uTextView1)
        underTextViewF = findViewById(R.id.uTextView2)
        correctTextViewM = findViewById(R.id.cTextView1)
        correctTextViewF = findViewById(R.id.cTextView2)
        upperTextViewM = findViewById(R.id.upTextView1)
        upperTextViewF = findViewById(R.id.upTextView2)
        reCalculate = findViewById(R.id.btnRecalculate)
        // custom action bar
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
        // take data
        val intent = intent
        var breed = intent.getStringExtra("Breed")
        var dogSex = intent.getStringExtra("sex")
        var bmiIndex = intent.getStringExtra("bmi")
        var weight = intent.getStringExtra("weight")
        var height = intent.getStringExtra("height")
        var wUnit = intent.getStringExtra("wUnit")
        var hUnit = intent.getStringExtra("hUnit")
        //get dog position

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
        var bmiList = loadLabelList(assets, "BMI.txt")
        var position: Int = 200
        for (i in 0 until label_list.size) {
            if (breed == label_list[i]) {
                position = i
                break
            }
        }

        // display image
        if (position != 200) {
            Picasso.with(this).load(link_list[position]).into(imageView)
        }
        // dog BMi
        bmiTextView.text = getString(R.string.bmi_calcultated) + " " + bmiIndex
        titleTextView.text = getString(R.string.ideal_bmi) + " " + breed
        //result

        var m_1 = 0;
        var f_1 = 0
        var m_2 = 0
        var f_2 = 0
        if (bmiList[position] == "None")  {
                 m_1 = 50
                 f_1 = 50
                 m_2 = 80
                 f_2 = 80
            } else {
                var numberArray = bmiList[position].split(",").toTypedArray()
                 m_1 = numberArray[0].toInt()
                 f_1 = numberArray[1].toInt()
                 m_2 = numberArray[2].toInt()
                 f_2 = numberArray[3].toInt()
            }
        var bmi = bmiIndex?.toInt()!!
        if (dogSex == "Male") {
            if (bmi < m_1)  {
                resultBMI.text = getString(R.string.under_weight)
            }  else if (bmi > m_2) {
                resultBMI.text = getString(R.string.your_dog_can_be_overweight)
            } else {
                resultBMI.text = getString(R.string.good_weight)
            }
        } else {
            if (bmi < f_1)  {
                resultBMI.text = getString(R.string.under_weight)
            }  else if (bmi > f_2) {
                resultBMI.text = getString(R.string.your_dog_can_be_overweight)
            } else {
                resultBMI.text = getString(R.string.good_weight)
            }
        }

        //others
        inputW.text = getString(R.string.you_entered_weight_as) + " " + weight + " " + wUnit
        inputH.text = getString(R.string.enter_height) + " " + height + " " + hUnit
        underTextViewM.text = " " + getString(R.string.b) + " " + m_1 + " "
        underTextViewF.text = " " + getString(R.string.b) + " " + f_1 + " "
        upperTextViewM.text = " " + getString(R.string.o) + " " + m_2 + " "
        upperTextViewF.text = " " + getString(R.string.o) + " " + f_2 + " "
        correctTextViewM.text = " " + getString(R.string.between) + " " + m_1 + " " + getString(R.string.and) + " " + m_2 + " "
        correctTextViewF.text = " " + getString(R.string.between) + " " + f_1 + " " + getString(R.string.and) +" "+  f_2 + " "

        // button
        reCalculate.setOnClickListener {
            var intent = Intent(this, bmiActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
            startActivity(intent)
        }
        var xButton: Button = findViewById(R.id.xButton)
        xButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
            startActivity(intent)
        }
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }
}