package com.example.xiapengdi.woodoandroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

import com.example.xiapengdi.woodoandroid.Infographic
import com.example.xiapengdi.woodoandroid.AdapterInfographic
import com.example.xiapengdi.woodoandroid.R

class HomeFragment : Fragment() {

    // fragment function with recyclerView and cardView
    // Source: https://github.com/crisuvas/DevelopApp
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.scroll_posts)
        val infographics = ArrayList<Infographic>()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        //same format as Profile fragment
        //use infographics to format what are on the card
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_people_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_events_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))

        //invoke the the adapter
        val adapter = AdapterInfographic(infographics)
        recyclerView.adapter = adapter

        return view
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

}