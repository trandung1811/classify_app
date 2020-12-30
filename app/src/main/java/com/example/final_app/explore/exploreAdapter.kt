package com.example.final_app.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.final_app.R

class exploreAdapter (var mCtx: Context, var resources: Int, var items: List<String>):
    ArrayAdapter<String>(mCtx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources, null)
        val imageView: ImageView = view.findViewById(R.id.exploreImageView)
        val titleTextView: TextView = view.findViewById(R.id.exploreName)

        titleTextView.text = items[position]

        return view
    }
}