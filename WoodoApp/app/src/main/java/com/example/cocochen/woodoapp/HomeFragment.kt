package com.example.cocochen.woodoapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class HomeFragment : Fragment() {

    // fragment function with recyclerView and cardView
    // Source: https://github.com/crisuvas/DevelopApp
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.scroll_posts)
        val infographics = ArrayList<Infographic>()
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)

        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_people_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_events_1))
        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))

        val adapter = AdapterInfographic(infographics)
        recyclerView.adapter = adapter

        return view
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

}