package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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
            // 화면 양옆 꽉 채우고 위로 붙이고 아래는 일부 공간 비움
            val params = attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = (resources.displayMetrics.heightPixels * 0.955).toInt() // 높이를 80%로 설정
            params.gravity = Gravity.TOP // 위로 붙이기
            setAttributes(params)

            // 배경을 투명하게 설정
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰 초기화
        val backButton = view.findViewById<ImageButton>(R.id.backButton_add)
        val selectedImageView = view.findViewById<ImageView>(R.id.selectedImageView)
        val selectPhotoButton = view.findViewById<ImageButton>(R.id.selectPhotoButton)
        val postEditText = view.findViewById<EditText>(R.id.postEditText)
        val additionalTextEdit = view.findViewById<EditText>(R.id.additionalTextEdit)
        val addPostButton = view.findViewById<Button>(R.id.addPostButtonDialog)

        // ImageView를 정사각형으로 설정
        setSquareImageView(selectedImageView)

        // 뒤로가기 버튼 동작
        backButton.setOnClickListener { dismiss() }

        // 사진 선택 버튼
        selectPhotoButton.setOnClickListener { openImagePicker() }

        // 추가 버튼
        addPostButton.setOnClickListener {
            val postText = postEditText.text.toString()
            val additionalText = additionalTextEdit.text.toString()
            if (validateInputs(postText, additionalText)) {
                val listener = targetFragment as? AddPostListener
                listener?.onPostAdded(selectedImageUri.toString(), postText, additionalText)
                Log.d("AddPostDialogFragment", "onPostAdded 호출됨")
                dismiss()
            }
        }
    }

    private fun setSquareImageView(imageView: ImageView) {
        imageView.post {
            val width = imageView.width
            imageView.layoutParams.height = width // 높이를 가로와 동일하게 설정
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun validateInputs(postText: String, additionalText: String): Boolean {
        return if (selectedImageUri != null && postText.isNotEmpty() && additionalText.isNotEmpty()) {
            true
        } else {
            Toast.makeText(requireContext(), "사진과 텍스트를 입력하세요.", Toast.LENGTH_SHORT).show()
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            updateSelectedImage()
        }
    }

    private fun updateSelectedImage() {
        view?.findViewById<ImageView>(R.id.selectedImageView)?.apply {
            setImageURI(selectedImageUri)
        }
        view?.findViewById<ImageButton>(R.id.selectPhotoButton)?.visibility = View.GONE
    }

    companion object {
        private const val PICK_IMAGE_REQUEST_CODE = 100

        fun newInstance(): AddPostDialogFragment {
            return AddPostDialogFragment()
        }
    }
}
