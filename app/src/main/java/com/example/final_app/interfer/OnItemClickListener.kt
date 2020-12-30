package com.example.final_app.interfer

import com.example.final_app.model.Model

interface onItemClickListener {
    fun onItemClick(item: Model, position: Int)
}