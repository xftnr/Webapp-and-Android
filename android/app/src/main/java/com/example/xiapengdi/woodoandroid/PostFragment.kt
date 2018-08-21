package com.example.xiapengdi.woodoandroid

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

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.result.Result

import android.provider.MediaStore
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.widget.Toast
import com.example.xiapengdi.woodoandroid.ImageActivity
import com.example.xiapengdi.woodoandroid.R
import android.widget.EditText
//import javax.xml.transform.Result


class PostFragment : Fragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        if (v === button){
            postresponse(v)
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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


    private fun postresponse(view: View?){
//        val text = findViewById(R.id.vnosEmaila) as EditText
//        val value = text.text.toString()
        val titletool = view?.findViewById(R.id.input_title) as EditText
        val title = titletool.text.toString()
        val contentool = view?.findViewById(R.id.textView7) as EditText
        val content = contentool.text.toString()
        val tagtool= view?.findViewById(R.id.input_tags) as EditText
        val tag = tagtool.text.toString()
        val catetool= view?.findViewById(R.id.categorySpinner) as EditText
        val category = catetool.text.toString()

//        not apply image here
//        var image =getText(R.id.input_title)

        val body = listOf("title" to title, "content" to content, "tag" to tag, "category" to category)
        FuelManager.instance.basePath = "https://woodo-apad.appspot.com"
        Fuel.post("/mobile/post", parameters = body)
                .responseString{ request, response, result ->
                    when (result){
                        is Result.Success -> println(result)
                    }
                }

    }

    companion object {
        fun newInstance(): PostFragment = PostFragment()
    }
}