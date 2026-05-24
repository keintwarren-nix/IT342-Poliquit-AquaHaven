package edu.cit.poliquit.aquahaven

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.poliquit.aquahaven.model.RegisterRequest
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import edu.cit.poliquit.aquahaven.utils.SessionManager
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirstname = findViewById<EditText>(R.id.etFirstname)
        val etLastname  = findViewById<EditText>(R.id.etLastname)
        val etEmail     = findViewById<EditText>(R.id.etEmail)
        val etPhone     = findViewById<EditText>(R.id.etPhone)
        val etPassword  = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvMessage   = findViewById<TextView>(R.id.tvMessage)
        val tvGoLogin   = findViewById<TextView>(R.id.tvGoLogin)
        val progress    = findViewById<View>(R.id.progressBar)

        btnRegister.setOnClickListener {
            val firstname = etFirstname.text.toString().trim()
            val lastname  = etLastname.text.toString().trim()
            val email     = etEmail.text.toString().trim()
            val phone     = etPhone.text.toString().trim()
            val password  = etPassword.text.toString().trim()

            if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError(tvMessage, "Please fill in all required fields")
                return@setOnClickListener
            }

            if (password.length < 8) {
                showError(tvMessage, "Password must be at least 8 characters")
                return@setOnClickListener
            }

            btnRegister.isEnabled = false
            progress.visibility = View.VISIBLE
            tvMessage.text = ""

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.register(
                        RegisterRequest(firstname, lastname, email, password, phone)
                    )
                    val body = response.body()

                    if (response.isSuccessful && body?.success == true) {
                        val token = body.accessToken
                        val user  = body.user

                        if (!token.isNullOrBlank() && user != null) {
                            SessionManager.save(this@RegisterActivity, token, user)
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            })
                            finish()
                        } else {
                            showError(tvMessage, body.message ?: "Registration failed. Please try again.")
                        }
                    } else {
                        showError(tvMessage, "❌ ${body?.message ?: "Registration failed"}")
                    }

                } catch (e: Exception) {
                    showError(tvMessage, "❌ Cannot connect to server. Check your connection.")
                } finally {
                    btnRegister.isEnabled = true
                    progress.visibility = View.GONE
                }
            }
        }

        tvGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun showError(tv: TextView, msg: String) {
        tv.text = msg
        tv.setTextColor(0xFFB91C1C.toInt())
    }
}