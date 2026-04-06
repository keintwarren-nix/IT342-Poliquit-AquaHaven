package edu.cit.poliquit.aquahaven

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.cit.poliquit.aquahaven.model.LoginRequest
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail      = findViewById<EditText>(R.id.etEmail)
        val etPassword   = findViewById<EditText>(R.id.etPassword)
        val btnLogin     = findViewById<Button>(R.id.btnLogin)
        val tvMessage    = findViewById<TextView>(R.id.tvMessage)
        val tvGoRegister = findViewById<TextView>(R.id.tvGoRegister)

        btnLogin.setOnClickListener {
            val email    = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                tvMessage.text = "Please fill in all fields"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.instance.login(
                        LoginRequest(email, password)
                    )
                    if (response.isSuccessful && response.body()?.success == true) {
                        val name = response.body()?.data?.user?.firstname ?: "User"
                        tvMessage.text = "✅ Welcome, $name!"
                        delay(1000)
                        val intent = Intent(
                            this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("firstname", name)
                        startActivity(intent)
                        finish()
                    } else {
                        tvMessage.text = "❌ Invalid email or password"
                    }
                } catch (e: Exception) {
                    tvMessage.text = "❌ Cannot connect to server"
                }
            }
        }

        tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}