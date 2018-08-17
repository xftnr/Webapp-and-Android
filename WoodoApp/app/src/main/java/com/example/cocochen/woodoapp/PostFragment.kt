package com.example.cocochen.woodoapp

import android.Manifest
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
import android.widget.Button
import android.provider.MediaStore
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.widget.Toast

class PostFragment : Fragment() {

    private val SELECT_PICTURE = 1

    internal lateinit var spinner: Spinner
    internal val name = arrayOf("People", "Animals", "Events")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_post, container, false)

        //spinner  used to create the dropdown list for category choices
        spinner = view.findViewById(R.id.categorySpinner) as Spinner

        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, name)
        spinner.adapter = adapter

        //clear button to clear all the text input value
        val clear: Button = view.findViewById(R.id.clear)
        clear.setOnClickListener {
            input_title.text = null
            input_description.text = null
            input_tags.text = null
        }

        //button to external activity of choosing images from gallery
        val btn_to_image: Button = view.findViewById(R.id.btn_to_image)
        btn_to_image.setOnClickListener{
            val intent = Intent(activity, ImageActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newInstance(): PostFragment = PostFragment()
    }
}