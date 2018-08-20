package com.example.xiapengdi.woodoandroid

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso




class HomeFragment : Fragment() {

    // fragment function with recyclerView and cardView
    // Source: https://github.com/crisuvas/DevelopApp

    private val posts: ArrayList<Posts> = ArrayList()

    // gone for now
//    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_home, container, false)
        val linearLayoutManager = LinearLayoutManager(this@HomeFragment.activity)
        val recyclerView = view.findViewById<RecyclerView>(R.id.scroll_posts)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        //same format as Profile fragment
        //use infographics to format what are on the card
//        hard code part
//        infographics.add(Infographic(R.string.title1, R.string.description1, R.string.post_info1, R.string.post_location1, R.drawable.weird_animals_1))
        val rvAdapter = RecyclerViewAdapter(posts)
        recyclerView.adapter = rvAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swiperefresh = view.findViewById<SwipeRefreshLayout>(R.id.swiperefresh)
        val recyclerView = view.findViewById<RecyclerView>(R.id.scroll_posts)
        swiperefresh.setOnRefreshListener {
//            login but do not care now
            if (true) {
                val queue = Volley.newRequestQueue(this.context)
                val url = "https://woodo-apad.appspot.com/mobile/home"
                // Request a string response from the provided URL.
                println("+++++ URL: " + url)
                val stringRequest = object : StringRequest(com.android.volley.Request.Method.GET, url,
                        com.android.volley.Response.Listener<String> { response ->
                            //textView.text = response
                            val gson = Gson()
                            println(response)
                            //val reportListType = object: TypeToken<ArrayList<Report>>(){}.type
                            //val reportList: List<Report> = gson.fromJson<List<Report>>(response, reportListType)
                            val typeofposts = object : TypeToken<ArrayList<Posts>>(){}.type
                            val postslist: ArrayList<Posts> = gson.fromJson(response, typeofposts)
                            println("******* reportList: " + postslist)
                            posts.clear()
                            for (report in postslist) {
                                posts.add(report)
                            }
                            recyclerView.adapter?.notifyDataSetChanged()
                        },
                        com.android.volley.Response.ErrorListener { println("******** That didn't work!") }) {}
                // Add the request to the RequestQueue.
                queue.add(stringRequest)
            }
            swiperefresh.setRefreshing(false);
        }
//        login here also
        if (true) {
            val queue = Volley.newRequestQueue(this.context)
            val url = "https://woodo-apad.appspot.com/mobile/home"
            // Request a string response from the provided URL.
            println("+++++ URL: " + url)
            val stringRequest = object : StringRequest(com.android.volley.Request.Method.GET, url,
                    com.android.volley.Response.Listener<String> { response ->
                        //textView.text = response
                        val gson = Gson()
                        println(response)
//                        change by json data structure
                        val typeofposts = object : TypeToken<ArrayList<Posts>>(){}.type
                        val postslist: ArrayList<Posts> = gson.fromJson(response, typeofposts)
                        println("******* reportList: " + postslist)
                        for (report in postslist) {
                            posts.add(report)
                        }
                        recyclerView.adapter?.notifyDataSetChanged()
                    },
                    com.android.volley.Response.ErrorListener { println("******** That didn't work!") }) {}
            // Add the request to the RequestQueue.
            queue.add(stringRequest)
        }

    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

}


data class Posts (val title: String, val content: String, val imgurl: String)

class ReportViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val cardView: CardView
    val posttitle: TextView
    val postinfo: TextView
    val postloc: TextView
    val postcont: TextView
    val postimg: ImageView
    init{
        cardView = itemView.findViewById<CardView>(R.id.cardview)
        posttitle = itemView.findViewById<TextView>(R.id.title)
        postcont = itemView.findViewById<TextView>(R.id.description)
        postinfo = itemView.findViewById<TextView>(R.id.post_info)
        postloc = itemView.findViewById<TextView>(R.id.post_location)
        postimg = itemView.findViewById<ImageView>(R.id.imgViewMain)
    }
}

class RecyclerViewAdapter(val posts: List<Posts>?): RecyclerView.Adapter<ReportViewHolder>(){

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ReportViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.each_post, viewGroup, false)
        val reportViewHolder = ReportViewHolder(view)
        return reportViewHolder
    }

    override fun onBindViewHolder(reportViewHolder: ReportViewHolder, i: Int) {
        reportViewHolder.postloc.text = "Austin, TX"
        reportViewHolder.posttitle.text = posts?.get(i)?.title
        reportViewHolder.postinfo.text = "Anonymous"
        reportViewHolder.postcont.text = posts?.get(i)?.content
        //tried
//        since we do not have return value, we just use same image in assert
        Picasso.get().load(R.drawable.weird_animals_1).into(reportViewHolder.postimg)

        println("******* LINE COUNT: " + reportViewHolder.postcont.lineCount)
    }

    override fun getItemCount(): Int {
        return posts?.size ?: 0
    }

}