package com.example.cocochen.woodoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.fragment_post.*
import java.io.IOException
import android.provider.MediaStore

class PostFragment : Fragment() {

    val SELECT_PICTURE = 1

    internal lateinit var spinner: Spinner
    internal val name = arrayOf("People", "Animals", "Events")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_post, container, false)

        spinner = view.findViewById(R.id.categorySpinner) as Spinner

        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, name)
        spinner.adapter = adapter

        //Source: https://www.youtube.com/watch?v=LYRi7-PmyOc  check the video to implement both together
        //buttonGallery.setOnClickListener{
            //dispatchGalleryIntent()
        //}
        return view
    }

    /*private fun dispatchGalleryIntent(){
        val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, SELECT_PICTURE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK){
            try{
                val url = data!!.data
                button_image.setImageURI(url)
            }
            catch (e:IOException){
                e.printStackTrace()
            }
        }
    }*/

    companion object {
        fun newInstance(): PostFragment = PostFragment()
    }
}