package com.example.myapplication

import PhotoAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        addPhotoButton = view.findViewById(R.id.addPhotoButton)

        // RecyclerView 설정
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 3열 그리드
        val photoAdapter = PhotoAdapter(photoList)
        recyclerView.adapter = photoAdapter

        // 사진 추가 버튼 클릭 리스너
        addPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequestCode)
        }
    }

    override fun onResume() {
        super.onResume()

        // 저장된 사진을 로드하여 RecyclerView 업데이트
        photoList.clear() // 기존 목록 초기화
        photoList.addAll(getSavedImages()) // 저장된 사진 추가
        recyclerView.adapter?.notifyDataSetChanged() // RecyclerView 업데이트
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageRequestCode && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                // 선택한 이미지를 저장
                val fileName = "image_${System.currentTimeMillis()}.jpg"
                val isSaved = saveImageToInternalStorage(it, fileName)
                if (isSaved) {
                    photoList.add(Uri.fromFile(File(requireContext().filesDir, fileName)))
                    recyclerView.adapter?.notifyDataSetChanged()
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

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun getSavedImages(): List<Uri> {
        val directory = requireContext().filesDir
        val files = directory.listFiles()?.filter { it.extension == "jpg" } ?: emptyList()
        return files.map { Uri.fromFile(it) }
    }
}
