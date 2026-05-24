package edu.cit.poliquit.aquahaven.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import edu.cit.poliquit.aquahaven.MainActivity
import edu.cit.poliquit.aquahaven.ProductDetailActivity
import edu.cit.poliquit.aquahaven.R
import edu.cit.poliquit.aquahaven.model.*
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import edu.cit.poliquit.aquahaven.utils.CartManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {

    private lateinit var rvCategories:  RecyclerView
    private lateinit var rvProducts:    RecyclerView
    private lateinit var etSearch:      EditText
    private lateinit var tvEmpty:       TextView
    private lateinit var progressBar:   ProgressBar
    private lateinit var tvResultCount: TextView

    private val categories = mutableListOf<Category>()
    private val products   = mutableListOf<Product>()

    private var selectedSlug: String? = null
    private var searchJob:    Job?    = null
    private var currentPage  = 0
    private var totalPages   = 1
    private var isLoading    = false

    private lateinit var catAdapter:  CategoryChipAdapter
    private lateinit var prodAdapter: ProductGridAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_products, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvCategories  = view.findViewById(R.id.rvCategories)
        rvProducts    = view.findViewById(R.id.rvProducts)
        etSearch      = view.findViewById(R.id.etSearch)
        tvEmpty       = view.findViewById(R.id.tvEmpty)
        progressBar   = view.findViewById(R.id.progressBar)
        tvResultCount = view.findViewById(R.id.tvResultCount)

        catAdapter = CategoryChipAdapter(categories) { slug ->
            selectedSlug = slug; currentPage = 0; products.clear(); loadProducts()
        }
        rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = catAdapter

        prodAdapter = ProductGridAdapter(
            items       = products,
            onAddToCart = { product ->
                CartManager.add(product)
                (activity as? MainActivity)?.updateCartBadge()
                Toast.makeText(requireContext(), "Added ${product.name} to cart 🛒", Toast.LENGTH_SHORT).show()
            },
            onCardClick = { product ->
                startActivity(Intent(requireContext(), ProductDetailActivity::class.java).apply {
                    putExtra("id",           product.id)
                    putExtra("name",         product.name)
                    putExtra("price",        product.price)
                    putExtra("imageUrl",     product.imageUrl)
                    putExtra("stock",        product.stock)
                    putExtra("categoryName", product.categoryName)
                    putExtra("categorySlug", product.categorySlug)
                    putExtra("categoryIcon", product.categoryIcon)
                    putExtra("description",  product.description)
                    putExtra("waterType",    product.waterType)
                })
            }
        )

        rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        rvProducts.adapter = prodAdapter

        rvProducts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val lm = rv.layoutManager as GridLayoutManager
                if (!isLoading && currentPage < totalPages - 1 &&
                    lm.findLastVisibleItemPosition() >= products.size - 4
                ) { currentPage++; loadProducts(append = true) }
            }
        })

        etSearch.addTextChangedListener {
            searchJob?.cancel()
            searchJob = viewLifecycleOwner.lifecycleScope.launch {
                delay(400); currentPage = 0; products.clear(); loadProducts()
            }
        }

        loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = RetrofitClient.instance.getCategories()
                if (resp.isSuccessful && resp.body()?.success == true) {
                    val all = listOf(Category(0, "All", "", "🌊", 0)) +
                            (resp.body()?.data ?: emptyList())
                    categories.clear(); categories.addAll(all)
                    catAdapter.notifyDataSetChanged()
                }
            } catch (_: Exception) {}
        }
    }

    private fun loadProducts(append: Boolean = false) {
        if (isLoading) return
        isLoading = true
        if (!append) { progressBar.visibility = View.VISIBLE; tvEmpty.visibility = View.GONE }

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = RetrofitClient.instance.getProducts(
                    keyword      = etSearch.text.toString().trim().ifEmpty { null },
                    categorySlug = if (selectedSlug.isNullOrEmpty()) null else selectedSlug,
                    page = currentPage, size = 20
                )
                val body = resp.body()
                if (resp.isSuccessful && body?.success == true && body.data != null) {
                    totalPages = body.data.totalPages
                    if (append) {
                        val start = products.size
                        products.addAll(body.data.content)
                        prodAdapter.notifyItemRangeInserted(start, body.data.content.size)
                    } else {
                        products.clear(); products.addAll(body.data.content)
                        prodAdapter.notifyDataSetChanged()
                    }
                    tvResultCount.text = "${body.data.totalElements} products"
                    tvEmpty.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
                } else {
                    if (!append) { tvEmpty.text = "No products found"; tvEmpty.visibility = View.VISIBLE }
                }
            } catch (e: Exception) {
                if (!append) { tvEmpty.text = "❌ Failed to load products"; tvEmpty.visibility = View.VISIBLE }
            } finally {
                isLoading = false; progressBar.visibility = View.GONE
            }
        }
    }
}

class CategoryChipAdapter(
    private val items: List<Category>,
    private val onSelect: (String?) -> Unit
) : RecyclerView.Adapter<CategoryChipAdapter.VH>() {

    private var selectedPos = 0

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tv: TextView = v.findViewById(R.id.tvChip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_category_chip, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val cat = items[position]; val sel = position == selectedPos
        h.tv.text = "${cat.icon} ${cat.name}"
        h.tv.setBackgroundResource(if (sel) R.drawable.chip_selected else R.drawable.chip_unselected)
        h.tv.setTextColor(if (sel) 0xFFFFFFFF.toInt() else 0xFF111111.toInt())
        h.itemView.setOnClickListener {
            val old = selectedPos; selectedPos = h.adapterPosition
            notifyItemChanged(old); notifyItemChanged(selectedPos)
            onSelect(if (cat.slug.isEmpty()) null else cat.slug)
        }
    }
}

class ProductGridAdapter(
    private val items: List<Product>,
    private val onAddToCart: (Product) -> Unit,
    private val onCardClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductGridAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val ivProduct:  ImageView = v.findViewById(R.id.ivProduct)
        val tvEmoji:    TextView  = v.findViewById(R.id.tvEmoji)
        val tvName:     TextView  = v.findViewById(R.id.tvName)
        val tvPrice:    TextView  = v.findViewById(R.id.tvPrice)
        val tvStock:    TextView  = v.findViewById(R.id.tvStock)
        val tvCategory: TextView  = v.findViewById(R.id.tvCategory)
        val btnAdd:     Button    = v.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_product_card, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val p = items[position]

        if (!p.imageUrl.isNullOrBlank()) {
            h.ivProduct.visibility = View.VISIBLE
            h.tvEmoji.visibility   = View.GONE
            Glide.with(h.ivProduct.context)
                .load(p.imageUrl)
                .placeholder(android.R.color.darker_gray)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(h.ivProduct)
        } else {
            h.ivProduct.visibility = View.GONE
            h.tvEmoji.visibility   = View.VISIBLE
            h.tvEmoji.text         = p.categoryIcon()
        }

        h.tvCategory.text  = p.categoryName ?: ""
        h.tvName.text      = p.name
        h.tvPrice.text     = "₱${String.format("%,.2f", p.price)}"
        h.tvStock.text     = if (p.stock > 0) "In stock (${p.stock})" else "Out of stock"
        h.tvStock.setTextColor(if (p.stock > 0) 0xFF2E7D32.toInt() else 0xFFB91C1C.toInt())
        h.btnAdd.isEnabled = p.stock > 0
        h.btnAdd.setOnClickListener { onAddToCart(p) }
        h.itemView.setOnClickListener { onCardClick(p) }
    }
}