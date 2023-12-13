package com.example.weatherapp.Firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.MainActivity
import com.example.weatherwonder.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var passwordEditTextC: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        passwordEditTextC = findViewById(R.id.editConfirmPassword)

        val signUpButton: Button = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val email: String = emailEditText?.text.toString().trim()
            val password: String = passwordEditText?.text.toString().trim()
            val cpassword: String = passwordEditTextC?.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || cpassword.isEmpty()) {
                Toast.makeText(this@SignUpActivity, "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            } else if (password != cpassword) {
                Toast.makeText(this@SignUpActivity, "Passwords do not match", Toast.LENGTH_SHORT)
                    .show()
            } else {
                firebaseAuth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(
                        this@SignUpActivity,
                        OnCompleteListener<AuthResult?> { task ->
                            if (task.isSuccessful) {
                                // Sign up success, update UI or navigate to the next activity
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Sign up successful!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish() // Finish the sign-up activity and go back to the previous activity
                                val i = Intent(this@SignUpActivity, MainActivity::class.java)
                                startActivity(i)
                            } else {
                                // If sign up fails, display a message to the user.
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Sign up failed. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
            }
        }
    }

    fun login(view: View?) {
        val i = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(i)
    }
}
