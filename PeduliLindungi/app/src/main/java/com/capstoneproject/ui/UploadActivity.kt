package com.capstoneproject.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstoneproject.Firestore
import com.capstoneproject.R
import com.capstoneproject.utils.Constants
import com.capstoneproject.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.IOException

class UploadActivity : AppCompatActivity(),View.OnClickListener {
    private var mSelectedImage: Uri? = null
    private var mImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        val fullname_upload: TextView = findViewById(R.id.fullname_upload)

        val sharedPreferences = getSharedPreferences(Constants.NAME_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_USERNAME, "")!!
        fullname_upload.text = "$username"

        val email = sharedPreferences.getString(Constants.EMAIL_DATA, "")!!
        email_upload.text = "$email"




        btn_add_image_upload.setOnClickListener(this@UploadActivity)
        btn_diagnose.setOnClickListener(this@UploadActivity)


    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.btn_add_image_upload ->{
                    if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ){
                        Constants.showImageChooser(this)
                        Toast.makeText(this,"You already have the storage permission.", Toast.LENGTH_SHORT).show()
                    } else{

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_PERMISSION
                        )
                    }
                }

                R.id.btn_diagnose ->{
                    if (mSelectedImage != null){
                        Firestore().uploadImagetoCloudStorage(this, mSelectedImage)
                        Toast.makeText(this,"Image Uploaded.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        updateImage()
                    }
                }
            }
        }
    }
    private fun updateImage(){
        val userHashMap = HashMap<String, Any>()
        if (mImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mImageURL
        }
        Firestore().updateImageURL(this, userHashMap)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Constants.READ_PERMISSION){
            if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)

            }else{
                Toast.makeText(this,"read storage permission is denied.", Toast.LENGTH_SHORT).show()

            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE){
                if (data != null){
                    try {
                        mSelectedImage = data.data!!

                        GlideLoader(this).loadImage(mSelectedImage!!,iv_xray_upload)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this,"Image selection failed.", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    fun imageUploadSuccess(imageURL: String){

        Toast.makeText(this@UploadActivity,"Upload Image successfully. Image URL is $imageURL", Toast.LENGTH_SHORT).show()

        mImageURL = imageURL
        updateImage()
    }


}

