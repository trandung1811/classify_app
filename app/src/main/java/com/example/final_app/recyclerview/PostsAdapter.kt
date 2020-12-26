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

class PostsAdapter(var context: Context, val posts: ArrayList<Model>): RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
       val firstName: TextView = itemView.findViewById(R.id.nameTextView)
        val imageView: ImageView = itemView.findViewById(R.id.circleImageView)
        val confidence: TextView = itemView.findViewById(R.id.accTextView)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_post, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = BitmapFactory.decodeStream(context.openFileInput(posts[position].imgFileName))
        holder.imageView.setImageBitmap(bitmap)
        holder.firstName.text = posts[position].name
        holder.confidence.text = posts[position].acc
    }


    override fun getItemCount() = posts.size
}