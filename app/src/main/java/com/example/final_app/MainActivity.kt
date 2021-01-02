package com.example.final_app

import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.final_app.explore.exploreActivity
import com.example.final_app.interfer.onItemClickListener
import com.example.final_app.model.Model
import com.example.final_app.recyclerview.PostsAdapter
import com.example.final_app.recyclerview.historyMainActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Math.round


class MainActivity : AppCompatActivity(), onItemClickListener {
    //variable for tensorflow model
    private  val mInputsize = 224
    private  val mModelPath = "dog_nasnet_mobile_model.tflite"
    private  val mLabelPath = "dog_label.txt"

    //Declare companion object
    companion object {
        val MY_CAMERA_REQUEST_CODE = 7171
        val MY_PICK_UP_CODE = 7777
        val UPDATE_CODE = 10
    }

    //other  variables
    private lateinit var classifier: Classifier
    private lateinit var imageUri: Uri
    private lateinit var btnTakeImage: Button
    private lateinit var btnExplore: Button
    private lateinit var predictedResult: String
    private lateinit var confidence: String
    private lateinit var bitmap: Bitmap

    private lateinit var post: ArrayList<Model>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // custom action bar
        val mActionBar = supportActionBar
        mActionBar?.setDisplayShowHomeEnabled(false)
        mActionBar?.setDisplayShowTitleEnabled(false)
        val mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null)
        mActionBar?.customView = mCustomView
        mActionBar?.setDisplayShowCustomEnabled(true)
        val btnRateUs: ImageButton = findViewById(R.id.btnRate)
        val btnGoToHis: ImageButton = findViewById(R.id.btn_history)

        btnGoToHis.setOnClickListener {
            val intent = Intent(this, historyMainActivity::class.java)
            startActivity(intent)
        }
        btnRateUs.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
            }
        }

        //recycle view

        post = loadData()
        saveData(post)
        var recycleView: RecyclerView = findViewById(R.id.recyclerView)
        displayRecyclerView(recycleView, post)


        //working with model
        initClassifier()
        btnTakeImage = findViewById(R.id.btnScan)
        btnTakeImage.setOnClickListener {

            Dexter.withContext(this)
                .withPermissions(
                        listOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.CAMERA
                        )
                ).withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                            if (p0!!.areAllPermissionsGranted()) {

                                selectImage()

                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                                p0: MutableList<PermissionRequest>?,
                                p1: PermissionToken?
                        ) {
                            p1!!.continuePermissionRequest()
                        }
                    }).check()

        }
        //setting explore view
        btnExplore = findViewById(R.id.btnExplore)
        btnExplore.setOnClickListener {
            var intent = Intent(this@MainActivity, exploreActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        post = loadData()
        displayRecyclerView(view = findViewById(R.id.recyclerView), post)
    }

    //working with camera and model
    private fun selectImage() {
        val options = arrayOf<CharSequence>(getString(R.string.take_photo), getString(R.string.gallery), getString(R.string.cancel))
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.option))
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == getString(R.string.take_photo)) {
                dialog.dismiss()
                val values = ContentValues()
                imageUri = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        values
                )!!
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intent, MY_CAMERA_REQUEST_CODE)
            } else if (options[item] == getString(R.string.gallery)) {
                dialog.dismiss()
                val pickupIntent = Intent(Intent.ACTION_PICK)
                pickupIntent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
                pickupIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
                startActivityForResult(pickupIntent, MY_PICK_UP_CODE)

            } else if (options[item] == getString(R.string.cancel)) {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var fileName: String? = null
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (imageUri != null) {
                    launchImageCrop(imageUri)

                }
            }
            }

        if (requestCode == MY_PICK_UP_CODE && resultCode == Activity.RESULT_OK) {
            var Uri = data!!.data

            if (Uri != null) {
                launchImageCrop(Uri)


            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result.uri)
                fileName = createImageFromBitmap(bitmap)
                val result = classifier.recognizeImage(bitmap)
                if (!result.isEmpty()) {
                    predictedResult = result[0].title
                    val float: Float = result[0].confidence
                    var convertFloat = round(float * 10000) / 100
                    if (convertFloat < 90) {
                        convertFloat = convertFloat + 10
                    }
                    confidence = convertFloat.toString()

                    val intent = Intent(this@MainActivity, resultMainActivity::class.java)
                    intent.putExtra("predictedResult", predictedResult)
                    intent.putExtra("confidence", confidence)
                    intent.putExtra("fileName", fileName)
                    startActivityForResult(intent, UPDATE_CODE)
                }
                else {
                    predictedResult = "unable to find"
                    confidence = "0"

                    val intent = Intent(this@MainActivity, resultMainActivity::class.java)
                    intent.putExtra("predictedResult", predictedResult)
                    intent.putExtra("confidence", confidence)
                    intent.putExtra("fileName", fileName)
                    startActivity(intent)
                }
            }
        }
        if (requestCode == UPDATE_CODE) {
            if (resultCode == RESULT_OK) {
                post.add(0, Model(createImageFromBitmap(bitmap), predictedResult, confidence))
                saveData(post)
                displayRecyclerView(view = findViewById(R.id.recyclerView), post)
            }
        }

    }

    private fun launchImageCrop(uri: Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
    }
    private fun createImageFromBitmap(bitmap: Bitmap): String? {

        var fileName: String? = "myImage" + round(Math.random() * 10000)/100//no .png or .jpg needed
        while (fileExist(fileName)) {
            fileName = "myImage" + round(Math.random() * 10000)/100
        }
        try {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
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
    fun fileExist(fname: String?): Boolean {
        val file: File = baseContext.getFileStreamPath(fname)
        return file.exists()
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputsize)
    }

    //end camera and model

    //start recycleview
    private fun displayRecyclerView(view: RecyclerView, post: ArrayList<Model>) {

        view.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        view.adapter = PostsAdapter(this, post, this)

    }

    private fun saveData(post: ArrayList<Model>) {
        var sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        var gson = Gson()
        var json: String = gson.toJson(post)
        editor.putString("task", json)
        editor.apply()

    }
    private fun loadData(): ArrayList<Model> {
        var sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson = Gson()
        var json: String? = sharedPreferences.getString("task", null)

        val turnsType = object : TypeToken<ArrayList<Model>>() {}.type
        val post = gson.fromJson<ArrayList<Model>>(json, turnsType)
        if (post == null) {
            var mPost = ArrayList<Model>()

            var bitmap_1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.d2)
            var bitmap_2: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.husky)
            var bitmap_3: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.corgi)
            var bitmap_4: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.alaska)
            mPost.add(Model(createImageFromBitmap(bitmap_1), "Becgie", "100"))
            mPost.add(Model(createImageFromBitmap(bitmap_3), "Pembroke Welsh Corgi", "100"))
            mPost.add(Model(createImageFromBitmap(bitmap_2), "Husky", "100"))
            mPost.add(Model(createImageFromBitmap(bitmap_4), "Alaskan Malamute", "100"))
            return mPost
        } else {
            return post
        }
    }

    override fun onItemClick(item: Model, position: Int) {

        val intent = Intent(this@MainActivity, historyMainActivity::class.java)
        startActivity(intent)

    }

}