package com.example.cocochen.woodoapp

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_image.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImageActivity : AppCompatActivity() {

    //choose image from gallery and get the preview
    //Borrow from https://demonuts.com/pick-image-gallery-camera-android-kotlin/
    private var image_button: Button? = null
    private var imageview: ImageView? = null
    private val GALLERY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        //declare the button, and setOnClickListener functions
        image_button = findViewById<View>(R.id.image_button) as Button
        imageview = findViewById<View>(R.id.image_preview) as ImageView

        image_button!!.setOnClickListener { choosePhotoFromGallary()}

        val back = findViewById<View>(R.id.back) as Button
        back.setOnClickListener{
            finish()
        }
    }

    //give the intent to get into gallery
    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    //image chose from the gallery saved to the preview, go to saveImage()
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this@ImageActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageview!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@ImageActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }


    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString())
        // have the object build the directory structure, if needed.
        Log.d("test",wallpaperDirectory.toString())

        return ""
    }


}
