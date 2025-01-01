package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewFragment2 : Fragment(R.layout.tab3_2_fragment) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter
    private val photoList = mutableListOf<Post>()
    private lateinit var dataManager: DataManager
    private lateinit var pickManager: PickManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DataManager 및 PickManager 초기화
        dataManager = DataManager(requireContext())
        pickManager = PickManager(requireContext())

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 16)) // 간격 추가

        photoAdapter = PhotoAdapter(photoList) { post ->
            // 사진 클릭 시 다이얼로그 표시
            val dialog = ViewPostDialogFragment.newInstance(post.photoUri, post.text)
            dialog.show(parentFragmentManager, "ViewPostDialog")
        }
        recyclerView.adapter = photoAdapter

        // 좋아요한 항목만 로드
        loadLikedPosts()
    }

    // 좋아요한 항목만 로드
    private fun loadLikedPosts() {
        photoList.clear()

        // 모든 게시물 로드
        val allPosts = dataManager.loadPosts()

        // 좋아요한 게시물 필터링
        val likedPosts = allPosts.filter { post ->
            val pickData = pickManager.loadPickData(post.text) // 게시물 이름(text)을 기반으로 좋아요 상태 확인
            pickData.isStudyPicked || pickData.isDatePicked || pickData.isPetPicked
        }

        photoList.addAll(likedPosts)
        photoAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadLikedPosts() // 탭으로 돌아올 때 좋아요 데이터 갱신
    }
}