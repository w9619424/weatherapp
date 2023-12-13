package com.example.weatherapp.Firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.MainActivity
import com.example.weatherwonder.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        // Check if the user is already authenticated
        if (firebaseAuth?.currentUser != null) {
            // User is already signed in, redirect to the main part of your app
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish() // Finish the LoginActivity
        }

        setContent {
            Login(
                onLoginClick = { email, password ->
                    login(email.trim(), password.trim())
                },
                onSignUpClick = ::signup
            )
        }
    }

    private fun login(email: String, password: String) {

        // Check if email and password are not empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this@LoginActivity,
                "Email and password cannot be empty",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Perform login with Firebase
        firebaseAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(
                this@LoginActivity,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI or navigate to the next activity
                        Toast.makeText(
                            this@LoginActivity,
                            "Login successful!",
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(i)
                        finish() // Finish the LoginActivity
                    } else {
                        // If sign in fails, display a message to the user and log the error
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("LoginActivity", "Login failed: ${task.exception?.message}")
                    }
                })
    }

    fun signup() {
        val i = Intent(this@LoginActivity, SignUpActivity::class.java)
        startActivity(i)
    }
}

@Preview(showBackground = true)
@Composable
fun Login(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onSignUpClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.app_primary))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Text(text = "LOGIN", color = Color.White, fontSize = 40.sp)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                unfocusedIndicatorColor = colorResource(id = R.color.app_primary_dark12),
                focusedIndicatorColor = colorResource(id = R.color.app_primary_dark12),
            ),
            placeholder = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                cursorColor = Color.Black,
                unfocusedIndicatorColor = colorResource(id = R.color.app_primary_dark12),
                focusedIndicatorColor = colorResource(id = R.color.app_primary_dark12),
            ),
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onLoginClick(email, password) },
                modifier = Modifier.weight(1f),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.black))
            ) {
                Text(text = "LOGIN")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onSignUpClick() },
                modifier = Modifier.weight(1f),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.black))
            ) {
                Text(text = "SIGN UP")
            }
        }
    }
}
