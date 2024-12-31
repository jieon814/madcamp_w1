package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<Button>(R.id.login_button)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val registerButton = findViewById<Button>(R.id.register_button)

        loginButton.isEnabled = false

        // 입력 감지
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        emailInput.addTextChangedListener(textWatcher)
        passwordInput.addTextChangedListener(textWatcher)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            Log.d(TAG, "로그인 버튼 클릭 - 이메일: $email, 비밀번호: ${"*".repeat(password.length)}")

            loginUser(email, password) { success ->
                if (success) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "로그인 성공 - 이메일: $email")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "로그인 실패. 아이디 또는 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "로그인 실패 - 이메일: $email")
                }
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Firebase 로그인 함수
    private fun loginUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Firebase 로그인 성공 - 사용자: ${FirebaseAuth.getInstance().currentUser?.email}")
                    onComplete(true)
                } else {
                    val errorMessage = task.exception?.localizedMessage ?: "알 수 없는 오류 발생"
                    Log.e(TAG, "Firebase 로그인 실패: $errorMessage")
                    onComplete(false)
                }
            }
    }
}
