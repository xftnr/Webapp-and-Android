package com.example.xiapengdi.woodoandroid

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.cxiapengdi.woodoandroid.AdapterInfographic
import com.example.cxiapengdi.woodoandroid.Infographic
import com.example.xiapengdi.woodoandroid.R

import com.example.xiapengdi.woodoandroid.ReportViewHolder
import com.example.xiapengdi.woodoandroid.RecyclerViewAdapter

class ProfileFragment : Fragment() {
    // fragment function with recyclerView and cardView
    // Source: https://github.com/crisuvas/DevelopApp
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
//        val linearLayoutManager = LinearLayoutManager(this@ProfileFragment.activity)
//        val recyclerView = view.findViewById<RecyclerView>(R.id.profile_posts)
//        recyclerView.layoutManager = linearLayoutManager
//        recyclerView.setHasFixedSize(true)
        recyclerView = view.findViewById(R.id.profile_posts)
        val infographics = ArrayList<Infographic>()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        //same format as Home fragment
        //use infographics to format what are on the card
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_people_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_events_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))

        //invoke the the adapter
        val adapter = AdapterInfographic(infographics)
        recyclerView.adapter = adapter

        //click button to go the map view
        val map : Button = view.findViewById(R.id.geo_view)
        map.setOnClickListener{
            val intent = Intent(activity, GeoView::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }


}