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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DataManager 초기화
        dataManager = DataManager(requireContext())

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 2)) // 간격 추가

        photoAdapter = PhotoAdapter(photoList) { post ->
            // 사진 클릭 시 다이얼로그 표시
            val dialog = ViewPostDialogFragment.newInstance(
                photoUri = post.photoUri,
                postText = post.text,
                additionalText = post.additionalText // 추가 텍스트 전달
            )
            dialog.show(parentFragmentManager, "ViewPostDialog")
        }
        recyclerView.adapter = photoAdapter

        // 데이터 로드
        loadPosts()
    }

    // SharedPreferences에서 데이터 로드
    private fun loadPosts() {
        photoList.clear()
        photoList.addAll(dataManager.loadPosts())
        photoAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadPosts() // 탭으로 돌아올 때 데이터 갱신
    }
}
