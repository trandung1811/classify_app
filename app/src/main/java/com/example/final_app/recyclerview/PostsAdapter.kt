package com.example.final_app.recyclerview

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_app.R
import com.example.final_app.interfer.onCarItemClickListener
import com.example.final_app.model.Model

class PostsAdapter(var context: Context, val posts: ArrayList<Model>, var clickListener: onCarItemClickListener): RecyclerView.Adapter<PostsAdapter.ViewHolder>() {


    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
       var firstName: TextView = itemView.findViewById(R.id.nameTextView)
        var imageView: ImageView = itemView.findViewById(R.id.circleImageView)
        var confidence: TextView = itemView.findViewById(R.id.accTextView)
     fun initialize(item: Model, context: Context, action: onCarItemClickListener) {
         val bitmap = BitmapFactory.decodeStream(context.openFileInput(item.imgFileName))
         imageView.setImageBitmap(bitmap)
         firstName.text = item.name
         confidence.text = item.acc +"%"
         itemView.setOnClickListener {
             action.onItemClick(item,adapterPosition)
         }

     }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_post, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initialize(posts[position], context, clickListener)
    }


    override fun getItemCount() = posts.size
}



