package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "RegisterActivity"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailInput = findViewById<EditText>(R.id.email_input_reg)
        val passwordInput = findViewById<EditText>(R.id.password_input_reg)
        val registerButton = findViewById<Button>(R.id.register_button_reg)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Log.d(TAG, "Attempting to register user with email: $email")
                registerUser(email, password) { success ->
                    if (success) {
                        Log.d(TAG, "Registration successful for email: $email")
                        Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                        finish() // 회원가입 성공 후 LoginActivity로 돌아가기
                    } else {
                        Log.e(TAG, "Registration failed for email: $email")
                        Toast.makeText(this, "회원가입 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.w(TAG, "Email or password is empty")
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String, onComplete: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                } else {
                    Log.e(TAG, "createUserWithEmail:failure", task.exception)
                }
                onComplete(task.isSuccessful)
            }
    }
}
