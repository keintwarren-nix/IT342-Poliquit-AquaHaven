package edu.cit.poliquit.aquahaven

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.poliquit.aquahaven.model.RegisterRequest
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import kotlinx.coroutines.delay
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

        btnRegister.setOnClickListener {
            val firstname = etFirstname.text.toString().trim()
            val lastname  = etLastname.text.toString().trim()
            val email     = etEmail.text.toString().trim()
            val phone     = etPhone.text.toString().trim()
            val password  = etPassword.text.toString().trim()

            if (firstname.isEmpty() || lastname.isEmpty() ||
                email.isEmpty() || password.isEmpty()) {
                tvMessage.text = "Please fill in all required fields"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.register(
                        RegisterRequest(firstname, lastname, email, password, phone)
                    )
                    if (response.isSuccessful && response.body()?.success == true) {
                        tvMessage.text = "✅ Registration successful!"
                        delay(1500)
                        startActivity(Intent(
                            this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        val error = response.body()?.error?.message
                        tvMessage.text = "❌ ${error ?: "Registration failed"}"
                    }
                } catch (e: Exception) {
                    tvMessage.text = "❌ Cannot connect to server"
                }
            }
        }

        tvGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}