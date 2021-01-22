package com.example.final_app.bmi

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.final_app.R
import com.example.final_app.recyclerview.historyMainActivity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class bmiActivity : AppCompatActivity() {

    private lateinit var spinner_1: Spinner
    private lateinit var spinner_2: Spinner
    private lateinit var spinner_3: Spinner
    private lateinit var spinner_4: Spinner
    private lateinit var btnCalculate: Button
    private lateinit var wTextView: EditText
    private lateinit var hTexView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

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
        // find dog name based on locale
        var locale = Locale.getDefault()
        var mLabelPath = ""
        if (locale.toString() == "vi_GB") {
            mLabelPath = "dog_label_vi.txt"
        }
        else {
            mLabelPath = "dog_label.txt"
        }

        //set up spinner
        var dogList = loadLabelList(assets, mLabelPath)
        spinner_1 = findViewById(R.id.breedSpinner)
        val adapter_1: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dogList)
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_1.adapter = adapter_1

        spinner_2 = findViewById(R.id.sexSpinner)
        var sex = ArrayList<String>()
        sex.add("Male")
        sex.add("Female")
        val adapter_2: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sex)
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_2.adapter = adapter_2

        spinner_3 = findViewById(R.id.weghtSpinner)
        var weightUnit = ArrayList<String>()
        weightUnit.add("kg")
        weightUnit.add("lbs")
        val adapter_3: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weightUnit)
        adapter_3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_3.adapter = adapter_3

        spinner_4 = findViewById(R.id.heightSpinner)
        var heightUnit = ArrayList<String>()
        heightUnit.add("Centimetres")
        heightUnit.add("Metres")
        heightUnit.add("Inches")
        val adapter_4: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,heightUnit)
        adapter_4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_4.adapter = adapter_4

        // calculate buttom
        btnCalculate = findViewById(R.id.calButton)
        wTextView = findViewById(R.id.weightInput)
        hTexView = findViewById(R.id.heightInput)

        btnCalculate.setOnClickListener {
            var breed = spinner_1.selectedItem.toString()
            var dogSex = spinner_2.selectedItem.toString()
            var wUnit = spinner_3.selectedItem.toString()
            var hUnit = spinner_4.selectedItem.toString()
            var wValue = wTextView.text.toString()
            var hValue = hTexView.text.toString()
            var w_1 = wValue
            var h_1 = hValue
            if (wValue == "") {
                Toast.makeText(this, getString(R.string.enter_weight), Toast.LENGTH_SHORT).show()
            } else if (hValue == "") {
                Toast.makeText(this, getString(R.string.e_height), Toast.LENGTH_SHORT).show()
            } else {
                if (wUnit == "lbs") {
                    var temp1 = wValue.toFloat()
                    var temp2 = temp1/2.2
                    wValue = temp2.toString()
                }
                if (hUnit == "Inches") {
                    var temp3 = hValue.toFloat()
                    var temp4 = temp3*0.0254
                    hValue = temp4.toString()
                }
                if (hUnit == "Centimetres") {
                    var temp5 = hValue.toFloat()
                    var temp6 = temp5/100
                    hValue = temp6.toString()
                }
                var height = hValue.toFloat()
                var weight = wValue.toFloat()
                var bmiIndex = weight/(height * height)
                var bmiIndex_1 = bmiIndex.roundToInt().toString()
                val intent = Intent(this, detailBMi::class.java)
                intent.putExtra("Breed", breed)
                intent.putExtra("sex",dogSex)
                intent.putExtra("bmi",bmiIndex_1)
                intent.putExtra("weight", w_1)
                intent.putExtra("height", h_1)
                intent.putExtra("wUnit", wUnit)
                intent.putExtra("hUnit", hUnit)
                startActivity(intent)
            }
        }

    }
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }
}