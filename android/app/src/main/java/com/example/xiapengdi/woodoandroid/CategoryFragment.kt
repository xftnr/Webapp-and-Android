package com.example.xiapengdi.woodoandroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xiapengdi.woodoandroid.R

class CategoryFragment : Fragment() {

    //present three available categories to subscribe or unsubscribe
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_category, container, false)

    companion object {
        fun newInstance(): CategoryFragment = CategoryFragment()
    }
}