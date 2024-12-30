package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentTab1Binding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.myapplication.databinding.ActivityMainBinding


class Tab1 : Fragment(R.layout.fragment_tab1) {
    private lateinit var binding: FragmentTab1Binding
    private lateinit var adapter: Tab1Adapter
    private lateinit var purposeType: ArrayList<Purpose>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        purposeType = arrayListOf(
            Purpose("Study Pick"),
            Purpose("Date Pick"),
            Purpose("Pet Pick")
        )
    }


    override fun onCreateView(
        inflatter: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTab1Binding.inflate(inflatter, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, purposeType)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.mySpinner.adapter = spinnerAdapter

        binding.mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedItem = parent?.getItemAtPosition(position) as Purpose
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // empty here
                }
            }

        // Fragment 내부 로직
        // RecyclerView 설정
        val items = listOf("Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9", "Item10", "Item11", "Item12")
        adapter = Tab1Adapter(items)

       binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }
}