package com.example.myapplication

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class ViewCafeDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT) // 화면 전체를 채움
            setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
            setBackgroundDrawableResource(android.R.color.white) // 흰 배경 설정
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cafe_view_post, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<ImageButton>(R.id.backButton) //뒤로가기 버튼 추가
        val photoImageView = view.findViewById<ImageView>(R.id.photoImageView)
        val postTextView = view.findViewById<TextView>(R.id.postTextView)

        val photoUri = arguments?.getString(ARG_PHOTO_URI) ?: ""
        val postText = arguments?.getString(ARG_POST_TEXT) ?: "텍스트 없음"

        Log.d("ViewPostDialogFragment", "photoUri: $photoUri")
        Log.d("ViewPostDialogFragment", "postText: $postText")

        // 뒤로가기 버튼 클릭 이벤트 처리
        backButton.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }


        // 권한 확인 및 요청
        checkAndRequestPermission(photoImageView, photoUri)

        // 텍스트 설정
        postTextView.text = postText
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_READ_STORAGE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            val photoUri = arguments?.getString(ARG_PHOTO_URI) ?: ""
            view?.findViewById<ImageView>(R.id.photoImageView)?.let {
                loadPhoto(it, photoUri)
            }
        } else {
            Log.e("ViewPostDialogFragment", "Permission denied. Cannot load photo.")
        }
    }

    private fun checkAndRequestPermission(photoImageView: ImageView, photoUri: String) {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissions.any { ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED }) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE_READ_STORAGE)
        } else {
            loadPhoto(photoImageView, photoUri)
        }
    }

    private fun loadPhoto(imageView: ImageView, uri: String) {
        Log.d("ViewPostDialogFragment", "Entered loadPhoto function with URI: $uri")
        try {
            Glide.with(this)
                .load(Uri.parse(uri)) // URI 직접 로드
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("ViewPostDialogFragment", "Glide load failed", e)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("ViewPostDialogFragment", "Glide load succeeded")
                        return false
                    }
                })
                .into(imageView)
        } catch (e: Exception) {
            Log.e("ViewPostDialogFragment", "Error in loadPhoto", e)
        }
    }

    companion object {
        private const val ARG_PHOTO_URI = "photoUri"
        private const val ARG_POST_TEXT = "postText"
        private const val REQUEST_CODE_READ_STORAGE = 101

        fun newInstance(photoUri: String, postText: String): ViewPostDialogFragment {
            val fragment = ViewPostDialogFragment()
            val args = Bundle()
            args.putString(ARG_PHOTO_URI, photoUri)
            args.putString(ARG_POST_TEXT, postText)
            fragment.arguments = args
            return fragment
        }
    }
}
