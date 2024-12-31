package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomSpinnerAdapter(
    private val context: Context,
    private val items: List<Purpose>
) : ArrayAdapter<Purpose>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView1(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView2(position, convertView, parent)
    }

    private fun createCustomView1(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)

        val item = items[position]
        val spinnerText = view.findViewById<TextView>(R.id.spinnerText)
        spinnerText.text = item.purpose
        return view
    }

    private fun createCustomView2(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)

        val item = items[position]
        val spinnerText = view.findViewById<TextView>(R.id.spinnerText)
        val spinnerImage = view.findViewById<ImageView>(R.id.spinnerImage)

        val textResource = when (item.purpose) {
            "Study Pick Rank" -> "Study Pick"
            "Date Pick Rank" -> "Date Pick"
            "Pet Pick Rank" -> "Pet Pick"
            else -> R.drawable.ic_heart_blue_filled
        }
        spinnerText.text = textResource.toString()

        // Purpose에 따라 이미지 설정
        val imageResource = when (item.purpose) {
            "Study Pick Rank" -> R.drawable.ic_heart_orange_filled
            "Date Pick Rank" -> R.drawable.ic_heart_yellow_filled
            "Pet Pick Rank" -> R.drawable.ic_heart_blue_filled
            else -> R.drawable.ic_heart_blue_filled
        }
        spinnerImage.setImageResource(imageResource)

        return view
    }
}