package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class AddPostDialogFragment : DialogFragment() {

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog) // 전체 화면 스타일
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_add_post, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageButton>(R.id.backButton_add)
        val selectedImageView = view.findViewById<ImageView>(R.id.selectedImageView)
        val selectPhotoButton = view.findViewById<ImageButton>(R.id.selectPhotoButton)
        val postEditText = view.findViewById<EditText>(R.id.postEditText)
        val addPostButton = view.findViewById<Button>(R.id.addPostButtonDialog)



        // ImageView를 정사각형으로 설정
        selectedImageView.post {
            val width = selectedImageView.width
            val layoutParams = selectedImageView.layoutParams
            layoutParams.height = width // 높이를 가로와 동일하게 설정
            selectedImageView.layoutParams = layoutParams
        }
        backButton.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }

        // 사진 선택 버튼
        selectPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }

        // 추가 버튼
        addPostButton.setOnClickListener {
            val postText = postEditText.text.toString()
            val listener = targetFragment as? AddPostListener // targetFragment 사용
            if (selectedImageUri != null && postText.isNotEmpty()) {
                listener?.onPostAdded(selectedImageUri.toString(), postText)
                Log.d("AddPostDialogFragment", "onPostAdded 호출됨")
                dismiss()
            } else {
                Toast.makeText(requireContext(), "사진과 텍스트를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 갤러리에서 사진 선택
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            view?.findViewById<ImageView>(R.id.selectedImageView)?.setImageURI(selectedImageUri)

            // 사진 선택 버튼 숨기기
            val selectPhotoButton = view?.findViewById<ImageButton>(R.id.selectPhotoButton)
            selectPhotoButton?.visibility = View.GONE
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 100

        fun newInstance(): AddPostDialogFragment {
            return AddPostDialogFragment()
        }
    }
}
