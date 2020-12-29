package com.example.final_app.history

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.final_app.R
import com.example.final_app.interfer.onCarItemClickListener
import com.example.final_app.model.Model
import com.google.gson.Gson

class historyAdapter (var context: Context, val posts: ArrayList<Model>, var clickListener: onCarItemClickListener): RecyclerView.Adapter<historyAdapter.hisViewHolder>() {

    class hisViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var firstName: TextView = itemView.findViewById(R.id.historyName)
        var imageView: ImageView = itemView.findViewById(R.id.hisImageView)
        var confidence: TextView = itemView.findViewById(R.id.historyAcc)
        var btnDelete: Button = itemView.findViewById(R.id.btnTDelete)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): hisViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.history_row, parent, false)

        return hisViewHolder(view)
    }

    override fun onBindViewHolder(holder: hisViewHolder, position: Int) {
        holder.initialize(posts[position], context, clickListener)
        holder.btnDelete.setOnClickListener {
            delete(position)
        }
    }

    private fun delete(position: Int) {
            posts.removeAt(position)
            saveData(posts)
            notifyItemRemoved(position)

    }
    private fun saveData(post: ArrayList<Model>) {
        var sharedPreferences = context.getSharedPreferences("shared preferences",
            AppCompatActivity.MODE_PRIVATE
        )
        var editor = sharedPreferences.edit()
        var gson = Gson()
        var json: String = gson.toJson(post)
        editor.putString("task", json)
        editor.apply()

    }
    override fun getItemCount() = posts.size
}