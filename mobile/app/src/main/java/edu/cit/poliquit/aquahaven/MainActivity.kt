package edu.cit.poliquit.aquahaven

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.cit.poliquit.aquahaven.ui.*
import edu.cit.poliquit.aquahaven.utils.CartManager
import edu.cit.poliquit.aquahaven.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Guard: if not logged in, go back to login
        if (!SessionManager.isLoggedIn(this)) {
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        if (savedInstanceState == null) {
            loadFragment(ProductsFragment())
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_products -> { loadFragment(ProductsFragment()); true }
                R.id.nav_cart     -> { loadFragment(CartFragment());     true }
                R.id.nav_orders   -> { loadFragment(OrdersFragment());   true }
                R.id.nav_profile  -> { loadFragment(ProfileFragment());  true }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun updateCartBadge() {
        val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
        val count = CartManager.totalItems()
        badge.isVisible = count > 0
        if (count > 0) badge.number = count
    }

    override fun onResume() {
        super.onResume()
        updateCartBadge()
    }
}