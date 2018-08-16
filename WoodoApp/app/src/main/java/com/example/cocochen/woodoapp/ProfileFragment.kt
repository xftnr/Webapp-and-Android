package com.example.cocochen.woodoapp

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

class ProfileFragment : Fragment() {
    // fragment function with recyclerView and cardView
    // Source: https://github.com/crisuvas/DevelopApp
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_profile, container, false)
        recyclerView = view.findViewById(R.id.profile_posts)
        val infographics = ArrayList<Infographic>()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_people_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_events_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))

        val adapter = AdapterInfographic(infographics)
        recyclerView.adapter = adapter

        //Source: https://www.raywenderlich.com/230-introduction-to-google-maps-api-for-android-with-kotlin
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