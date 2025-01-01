
package com.example.myapplication

import SpaceItemDecoration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentTab1Binding


class Tab1 : Fragment(R.layout.fragment_tab1) {

    private lateinit var binding: FragmentTab1Binding
    private lateinit var tab1DataManager: Tab1_DataManager
    private lateinit var purposeType: ArrayList<Purpose>
    private lateinit var adapter: Tab1Adapter
    private lateinit var pickManager: PickManager


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tab1_DataManager 초기화
        tab1DataManager = Tab1_DataManager(requireContext())

        // Spinner 데이터 초기화
        purposeType = arrayListOf(
            Purpose("Study Pick Rank"),
            Purpose("Date Pick Rank"),
            Purpose("Pet Pick Rank")
        )
        // PickManager 초기화
        pickManager = SharedPickManager.pickManager
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // View Binding 초기화
        binding = FragmentTab1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView 초기화
        setupRecyclerView()
        // 변경 사항 감지
        SharedPickManager.registerListener {
            updateRecyclerView()
        }
        // Spinner 초기화
        setupSpinner()
    }

    private fun updateRecyclerView() {
        // 현재 RecyclerView에서 표시 중인 카페 데이터 목록을 가져옴
        val cafes = adapter.getCurrentData()

        // 카페 데이터와 Pick 상태를 결합
        val updatedCafes = cafes.map { cafe ->
            val pickData = pickManager.loadPickData(cafe.name) // Pick 상태를 가져옴
            Pair(cafe, pickData) // CafeData와 CafePickData를 Pair로 묶음
        }

        // 어댑터에 업데이트된 데이터 전달
        adapter.updateData(updatedCafes)
    }

    private fun setupRecyclerView() {
        // 데이터 로드
        val cafes = tab1DataManager.loadCafes()
        // RecyclerView 설정
        adapter = Tab1Adapter(requireContext(), cafes, pickManager)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        // ItemDecoration 추가 (아이템 간 간격 설정)
        val spaceInPixels = resources.getDimensionPixelSize(R.dimen.item_spacing)
        binding.recyclerView.addItemDecoration(SpaceItemDecoration(spaceInPixels))
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun setupSpinner() {
        // Spinner 어댑터 설정
        val spinnerAdapter = CustomSpinnerAdapter(requireContext(), purposeType)
        binding.mySpinner.adapter = spinnerAdapter

        // Spinner 선택 이벤트 처리
        binding.mySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedItem = parent?.getItemAtPosition(position) as Purpose
            // 현재 데이터 로드
            val cafes = tab1DataManager.loadCafes()

            // 선택된 정렬 기준에 따라 데이터 정렬
            val sortedCafes = when (selectedItem.purpose) {
                "Study Pick Rank" -> cafes.sortedByDescending { pickManager.loadPickData(it.name).studyPick }
                "Date Pick Rank" -> cafes.sortedByDescending { pickManager.loadPickData(it.name).datePick }
                "Pet Pick Rank" -> cafes.sortedByDescending { pickManager.loadPickData(it.name).petPick }
                else -> cafes // 기본 정렬
            }

            // 어댑터에 정렬된 데이터 전달
            adapter.updateData(sortedCafes.map { Pair(it, pickManager.loadPickData(it.name)) })
        }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않은 상태 처리
            }
        }
    }
}
