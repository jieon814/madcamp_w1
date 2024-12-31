package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 애니메이션 초기화
        val splashSubtext = findViewById<TextView>(R.id.splash_subtext)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // 애니메이션 시작
        splashSubtext.startAnimation(fadeInAnimation)

        // 일정 시간 후 로그인 화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish() // SplashActivity 종료
        }, 1600) // 1.5초 대기
    }
}