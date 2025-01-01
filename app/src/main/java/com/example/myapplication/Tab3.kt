package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class Tab3 : Fragment(R.layout.fragment_tab3) {

    private lateinit var imageView: ImageView
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.small_tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.tab3_viewPager)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email ?: "Guest"
        imageView = view.findViewById(R.id.tab3_image)

        // ImageView 클릭 리스너
        imageView.setOnClickListener {
            checkPermissionAndOpenGallery()
        }

        // 이메일에서 '@' 앞 부분 추출
        val textView = view.findViewById<TextView>(R.id.tab3_text)
        val username = email.substringBefore("@")
        textView.text = username

        // ViewPager Adapter 설정
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2 // 두 개의 탭
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> RecyclerViewFragment1() // 첫 번째 탭 프래그먼트
                    1 -> RecyclerViewFragment2() // 두 번째 탭 프래그먼트
                    else -> throw IllegalArgumentException("Invalid position")
                }
            }
        }

        viewPager.adapter = adapter

        // TabLayout과 ViewPager 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "My-Pick"
                1 -> "My-Review"
                else -> "탭"
            }
        }.attach()
    }

    private fun checkPermissionAndOpenGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                // 권한 요청
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
            } else {
                // 권한이 허용되어 있음, 갤러리 열기
                openGallery()
            }
        } else {
            // Android 6.0 이하, 바로 갤러리 열기
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            imageView.setImageURI(imageUri) // 선택한 이미지를 ImageView에 설정
            saveImageUri(imageUri) // 선택한 이미지 저장
        }
    }

    private fun saveImageUri(imageUri: Uri?) {
        val sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("profileImageUri", imageUri?.toString())
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        loadImageUri()
    }

    private fun loadImageUri() {
        val sharedPreferences = requireContext().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val savedUri = sharedPreferences.getString("profileImageUri", null)
        if (savedUri != null) {
            imageView.setImageURI(Uri.parse(savedUri))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
}