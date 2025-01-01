package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Tab2 : Fragment(R.layout.fragment_tab2), AddPostListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPostButton: ImageButton
    private lateinit var photoAdapter: PhotoAdapter
    private val photoList = mutableListOf<Post>()
    private lateinit var dataManager: DataManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DataManager 초기화
        dataManager = DataManager(requireContext())

        // RecyclerView와 추가 버튼 초기화
        recyclerView = view.findViewById(R.id.recyclerView)
        addPostButton = view.findViewById(R.id.addPostButtonTab2)

        // RecyclerView 설정
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(3, 2)) // 간격 추가 (1dp)

        photoAdapter = PhotoAdapter(photoList) { post ->
            // 사진 클릭 시 다이얼로그 표시
            val dialog = ViewPostDialogFragment.newInstance(post.photoUri, post.text, post.additionalText)
            dialog.show(parentFragmentManager, "ViewPostDialog")
        }
        recyclerView.adapter = photoAdapter

        // 추가 버튼 클릭 시 다이얼로그 표시
        addPostButton.setOnClickListener {
            val dialog = AddPostDialogFragment.newInstance()

            // ***Tab2를 명시적으로 설정***
            dialog.setTargetFragment(this, 0)

            dialog.show(parentFragmentManager, "AddPostDialog")
        }
        // 데이터 로드
        loadPosts()
    }

    // SharedPreferences에서 데이터 로드
    private fun loadPosts() {
        photoList.clear()
        photoList.addAll(dataManager.loadPosts())
        photoAdapter.notifyDataSetChanged()
    }

    // AddPostListener 구현
    override fun onPostAdded(photoUri: String, postText: String, additionalText: String) {
        Log.d("Tab2", "onPostAdded 호출됨: $photoUri, $postText, $additionalText")

        val newPost = Post(photoUri = photoUri, text = postText, additionalText = additionalText)
        photoList.add(0, newPost) // 맨 위에 추가
        Log.d("Tab2", "photoList 크기: ${photoList.size}")
        photoAdapter.notifyItemInserted(0) // RecyclerView 업데이트
        recyclerView.scrollToPosition(0) // 추가된 위치로 스크롤
        dataManager.savePosts(photoList) // SharedPreferences에 저장
    }
}
