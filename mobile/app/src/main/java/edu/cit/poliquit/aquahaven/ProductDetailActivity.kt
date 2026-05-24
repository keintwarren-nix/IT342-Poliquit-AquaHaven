package edu.cit.poliquit.aquahaven

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import edu.cit.poliquit.aquahaven.model.Product
import edu.cit.poliquit.aquahaven.utils.CartManager

class ProductDetailActivity : AppCompatActivity() {

    private var quantity = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val toolbar     = findViewById<Toolbar>(R.id.toolbar)
        val ivProduct   = findViewById<ImageView>(R.id.ivProduct)
        val tvEmoji     = findViewById<TextView>(R.id.tvEmoji)
        val tvCategory  = findViewById<TextView>(R.id.tvCategory)
        val tvName      = findViewById<TextView>(R.id.tvName)
        val tvPrice     = findViewById<TextView>(R.id.tvPrice)
        val tvStock     = findViewById<TextView>(R.id.tvStock)
        val tvWaterType = findViewById<TextView>(R.id.tvWaterType)
        val tvDesc      = findViewById<TextView>(R.id.tvDescription)
        val tvQty       = findViewById<TextView>(R.id.tvQty)
        val btnMinus    = findViewById<ImageButton>(R.id.btnMinus)
        val btnPlus     = findViewById<ImageButton>(R.id.btnPlus)
        val btnAdd      = findViewById<Button>(R.id.btnAddToCart)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val product = Product(
            id           = intent.getLongExtra("id", 0),
            name         = intent.getStringExtra("name") ?: "",
            price        = intent.getDoubleExtra("price", 0.0),
            imageUrl     = intent.getStringExtra("imageUrl"),
            stock        = intent.getIntExtra("stock", 0),
            categoryName = intent.getStringExtra("categoryName"),
            categorySlug = intent.getStringExtra("categorySlug"),
            categoryIcon = intent.getStringExtra("categoryIcon"),
            description  = intent.getStringExtra("description"),
            waterType    = intent.getStringExtra("waterType")
        )

        supportActionBar?.title = product.name

        if (!product.imageUrl.isNullOrBlank()) {
            ivProduct.visibility = View.VISIBLE
            tvEmoji.visibility   = View.GONE
            Glide.with(this)
                .load(product.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivProduct)
        } else {
            ivProduct.visibility = View.GONE
            tvEmoji.visibility   = View.VISIBLE
            tvEmoji.text         = product.categoryIcon()
        }

        tvCategory.text = product.categoryName ?: ""
        tvName.text     = product.name
        tvPrice.text    = "₱${String.format("%,.2f", product.price)}"

        if (product.stock > 0) {
            tvStock.text = "In stock (${product.stock})"
            tvStock.setTextColor(0xFF2E7D32.toInt())
            tvStock.setBackgroundColor(0xFFE8F5E9.toInt())
        } else {
            tvStock.text = "Out of stock"
            tvStock.setTextColor(0xFFB91C1C.toInt())
            tvStock.setBackgroundColor(0xFFFFEBEE.toInt())
        }

        tvWaterType.text = product.waterType?.let { "Water type: $it" } ?: ""
        tvDesc.text      = product.description?.ifBlank { "No description available." }
            ?: "No description available."

        btnAdd.isEnabled = product.stock > 0

        btnMinus.setOnClickListener {
            if (quantity > 1) { quantity--; tvQty.text = quantity.toString() }
        }
        btnPlus.setOnClickListener {
            if (quantity < product.stock) { quantity++; tvQty.text = quantity.toString() }
        }

        btnAdd.setOnClickListener {
            repeat(quantity) { CartManager.add(product) }
            Toast.makeText(this, "Added $quantity × ${product.name} to cart 🛒", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}