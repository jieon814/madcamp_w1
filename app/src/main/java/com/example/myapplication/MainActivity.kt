package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
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
        SharedPickManager.pickManager = PickManager(applicationContext)


        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // ViewPager2와 TabLayout 연결
        val adapter = TabAdapter(this)
        viewPager.adapter = adapter

        viewPager.setCurrentItem(1, false) // smoothScroll을 false로 설정해 즉시 이동

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
             when (position) {
                0 -> {
                    tab.text = "Coffic"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.new_tab1)
                }      // 첫 번째 탭
                 1 -> {
                     // Custom Tab Layout 적용
                     val customTab = layoutInflater.inflate(R.layout.custom_tab, null)
                     val tabIcon = customTab.findViewById<ImageView>(R.id.tab_icon)
                     tabIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.main_logo))
                     tab.customView = customTab
                 }
                2 -> {
                    tab.text = "My-pick"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.new_tab3)
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
