package com.example.myapplication

import PhotoAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileOutputStream


class Tab2 : Fragment(R.layout.fragment_tab2) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var addPhotoButton: Button
    private val pickImageRequestCode = 100

    private val photoList = mutableListOf<Uri>() // 사진 URI를 저장하는 리스트
    private lateinit var photoAdapter: PhotoAdapter

    // UI 요소 초기화 및 이벤트 리스너 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        addPhotoButton = view.findViewById(R.id.addPhotoButton)

        // RecyclerView 간격 설정
        val spacing = resources.getDimensionPixelSize(R.dimen.recycler_view_spacing) // 0.4dp
        //recyclerView.addItemDecoration(GridSpacingItemDecoration(3, spacing))
        recyclerView.addItemDecoration(VerticalItemDecorator( spacing))
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false) // 3열 그리드

        // RecyclerView 어댑터 설정
        photoAdapter = PhotoAdapter(photoList)
        recyclerView.adapter = photoAdapter

        // 초기 스크롤 위치 설정
        recyclerView.post {
            recyclerView.scrollToPosition(0)
        }

        // 사진 추가 버튼 클릭 리스너
        addPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequestCode)
        }
    }

    override fun onResume() {
        super.onResume()

        // 저장된 사진을 로드하여 RecyclerView 업데이트
        val savedImages = getSavedImages()
        if (photoList != savedImages) { // 기존 리스트와 비교하여 업데이트
            photoList.clear()
            photoList.addAll(savedImages)
            photoAdapter.notifyDataSetChanged()
            Log.d("RecyclerView", "RecyclerView updated with saved images")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                // 선택한 이미지를 저장
                val fileName = "image_${System.currentTimeMillis()}.jpg"
                val isSaved = saveImageToInternalStorage(it, fileName) // 로컬 저장소에 저장
                if (isSaved) {
                    val newUri = Uri.fromFile(File(requireContext().filesDir, fileName))
                    photoList.add(0, newUri) // 맨 앞에 추가
                    photoAdapter.notifyItemInserted(0) // 특정 항목 갱신
                    recyclerView.scrollToPosition(0) // 맨 위로 스크롤
                    Toast.makeText(requireContext(), "Photo added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to save photo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri, fileName: String): Boolean {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Log.d("SaveImage", "File saved at: ${file.absolutePath}")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SaveImage", "Failed to save image", e)
            false
        }
    }

    private fun getSavedImages(): List<Uri> {
        val directory = requireContext().filesDir
        val files = directory.listFiles()?.filter { it.extension == "jpg" } ?: emptyList()
        files.forEach { Log.d("GetSavedImages", "File found: ${it.absolutePath}") }
        return files.map { Uri.fromFile(it) }
    }
}
