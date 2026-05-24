package edu.cit.poliquit.aquahaven

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import edu.cit.poliquit.aquahaven.utils.SessionManager
import kotlinx.coroutines.launch
import edu.cit.poliquit.aquahaven.model.LoginRequest

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If already logged in, go directly to main screen
        if (SessionManager.isLoggedIn(this)) {
            goHome()
            return
        }

        setContentView(R.layout.activity_login)

        val etEmail    = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin   = findViewById<Button>(R.id.btnLogin)
        val tvMessage  = findViewById<TextView>(R.id.tvMessage)
        val tvGoRegister = findViewById<TextView>(R.id.tvGoRegister)
        val progress   = findViewById<View>(R.id.progressBar)

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                showError(tvMessage, "Please fill in all fields")
                return@setOnClickListener
            }

            btnLogin.isEnabled = false
            progress.visibility = View.VISIBLE
            tvMessage.text = ""

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.login(LoginRequest(email, password))
                    val body = response.body()

                    if (response.isSuccessful && body?.success == true) {
                        val token = body.accessToken
                        val user  = body.user

                        if (!token.isNullOrBlank() && user != null) {
                            SessionManager.save(this@LoginActivity, token, user)
                            goHome()
                        } else {
                            showError(tvMessage, body.message ?: "Login failed. Please try again.")
                        }
                    } else {
                        val errorMsg = body?.message ?: "Invalid email or password"
                        showError(tvMessage, "❌ $errorMsg")
                    }

                } catch (e: Exception) {
                    showError(tvMessage, "❌ Cannot connect to server. Check your connection.")
                } finally {
                    btnLogin.isEnabled = true
                    progress.visibility = View.GONE
                }
            }
        }

        tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showError(tv: TextView, msg: String) {
        tv.text = msg
        tv.setTextColor(0xFFB91C1C.toInt())
    }

    private fun goHome() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}