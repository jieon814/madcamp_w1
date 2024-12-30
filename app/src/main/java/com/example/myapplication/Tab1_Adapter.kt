package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRecyclerviewBinding

class Tab1Adapter (private val items: List<String>) : RecyclerView.Adapter<Tab1Adapter.Tab1ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Tab1ViewHolder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Tab1ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Tab1ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Tab1ViewHolder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {

        }
    }
}
