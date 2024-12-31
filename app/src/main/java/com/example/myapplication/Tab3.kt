package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth

class Tab3 : Fragment(R.layout.fragment_tab3) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.small_tabLayout)
        val viewPager = view.findViewById<ViewPager2>(R.id.tab3_viewPager)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email ?: "Guest"

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
                0 -> "탭 1"
                1 -> "탭 2"
                else -> "탭"
            }
        }.attach()
    }
}
