package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContentView(R.layout.activity_main)


        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // ViewPager2와 TabLayout 연결
        val adapter = TabAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
             when (position) {
                0 -> {
                    tab.text = "Coffic"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.bottom_bar_tab1_icon)
                }      // 첫 번째 탭
                1 -> {
                    tab.text = "U-pick"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.bottom_bar_tab2_icon)
                }    // 두 번째 탭
                2 -> {
                    tab.text = "My-pick"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.bottom_bar_tab3_icon)
                }       // 세 번째 탭
                else -> null
            }
        }.attach()

        // WindowInsets 적용 (상태바/네비게이션바 패딩 설정)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
