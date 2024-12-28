package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class Tab3:Fragment(R.layout.fragment_tab3) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment 내부 로직
        val textView = view.findViewById<TextView>(R.id.textView3)
        textView.text = "This is the Settings tab"
    }
}