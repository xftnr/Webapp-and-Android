package com.example.cocochen.woodoapp

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

// Source: https://github.com/crisuvas/DevelopApp
class AdapterInfographic(var list: ArrayList<Infographic>): RecyclerView.Adapter<AdapterInfographic.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.each_post, parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){



        fun bindItems(data:Infographic){
            val title : TextView = itemView.findViewById(R.id.title)
            val description : TextView = itemView.findViewById(R.id.description)
            val post_info : TextView = itemView.findViewById(R.id.post_info)
            val post_location : TextView = itemView.findViewById(R.id.post_location)
            val image: ImageView = itemView.findViewById(R.id.imgViewMain)

            //put strings value into right spots on the card view
            title.setText(data.title)
            description.setText(data.description)
            post_info.setText(data.post_info)
            post_location.setText(data.post_location)
            Glide.with(itemView.context).load(data.imgId).into(image)
        }
    }
}