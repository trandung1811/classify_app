package com.example.final_app.interfer

import com.example.final_app.model.Model

interface onCarItemClickListener {
    fun onItemClick(item: Model, position: Int)
}