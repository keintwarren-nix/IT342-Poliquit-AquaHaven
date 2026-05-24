package edu.cit.poliquit.aquahaven.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import edu.cit.poliquit.aquahaven.MainActivity
import edu.cit.poliquit.aquahaven.R
import edu.cit.poliquit.aquahaven.model.*
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import edu.cit.poliquit.aquahaven.utils.CartManager
import edu.cit.poliquit.aquahaven.utils.SessionManager
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private lateinit var rvCart:      RecyclerView
    private lateinit var tvEmpty:     TextView
    private lateinit var tvTotal:     TextView
    private lateinit var btnCheckout: Button
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvCart      = view.findViewById(R.id.rvCart)
        tvEmpty     = view.findViewById(R.id.tvEmpty)
        tvTotal     = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        cartAdapter = CartAdapter(
            CartManager.getItems().toMutableList(),
            onQtyChange = { id, qty -> CartManager.setQty(id, qty); refresh() },
            onRemove    = { id -> CartManager.remove(id); refresh() }
        )
        rvCart.layoutManager = LinearLayoutManager(requireContext())
        rvCart.adapter = cartAdapter

        btnCheckout.setOnClickListener { showCheckoutDialog() }
        refresh()
    }

    private fun refresh() {
        val items = CartManager.getItems()
        cartAdapter.updateItems(items)
        tvTotal.text = "Total: ₱${String.format("%,.2f", CartManager.totalPrice())}"

        val empty = items.isEmpty()
        tvEmpty.visibility     = if (empty) View.VISIBLE else View.GONE
        rvCart.visibility      = if (empty) View.GONE    else View.VISIBLE
        tvTotal.visibility     = if (empty) View.GONE    else View.VISIBLE
        btnCheckout.visibility = if (empty) View.GONE    else View.VISIBLE

        (activity as? MainActivity)?.updateCartBadge()
    }

    private fun showCheckoutDialog() {
        val ctx = requireContext()
        val dv  = layoutInflater.inflate(R.layout.dialog_checkout, null)
        val etAddress = dv.findViewById<EditText>(R.id.etAddress)
        val etNotes   = dv.findViewById<EditText>(R.id.etNotes)
        val rgPayment = dv.findViewById<RadioGroup>(R.id.rgPayment)

        val profile = SessionManager.getProfile(ctx)
        if (profile.location.isNotBlank()) etAddress.setText(profile.location)

        val dialog = android.app.AlertDialog.Builder(ctx)
            .setTitle("Place Order")
            .setView(dv)
            .setPositiveButton("Confirm", null)
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val address = etAddress.text.toString().trim()
                if (address.isEmpty()) { etAddress.error = "Address required"; return@setOnClickListener }
                val payment = when (rgPayment.checkedRadioButtonId) {
                    R.id.rbGcash -> "GCASH"
                    else         -> "COD"
                }
                dialog.dismiss()
                placeOrder(address, payment, etNotes.text.toString().trim().ifEmpty { null })
            }
        }
        dialog.show()
    }

    private fun placeOrder(address: String, payment: String, notes: String?) {
        val token = SessionManager.getToken(requireContext())
        if (token.isNullOrBlank()) {
            Toast.makeText(requireContext(), "Session expired. Please log in again.", Toast.LENGTH_LONG).show()
            return
        }

        val req = PlaceOrderRequest(
            items           = CartManager.getItems().map { OrderItemRequest(it.product.id, it.quantity) },
            shippingAddress = address,
            paymentMethod   = payment,
            notes           = notes
        )

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                btnCheckout.isEnabled = false
                btnCheckout.text      = "Placing order..."
                val resp = RetrofitClient.instance.placeOrder("Bearer $token", req)
                val body = resp.body()

                if (resp.isSuccessful && body?.success == true) {
                    CartManager.clear()
                    refresh()
                    Toast.makeText(requireContext(), "✅ Order placed!\nRef: ${body.data?.orderRef}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "❌ (${resp.code()}) ${body?.message ?: "Order failed"}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "❌ Cannot reach server:\n${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                btnCheckout.isEnabled = true
                btnCheckout.text      = "Place Order"
            }
        }
    }
}

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onQtyChange: (Long, Int) -> Unit,
    private val onRemove:    (Long) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val ivProduct:  ImageView   = v.findViewById(R.id.ivProduct)
        val tvEmoji:    TextView    = v.findViewById(R.id.tvEmoji)
        val tvName:     TextView    = v.findViewById(R.id.tvName)
        val tvPrice:    TextView    = v.findViewById(R.id.tvPrice)
        val tvQty:      TextView    = v.findViewById(R.id.tvQty)
        val btnMinus:   ImageButton = v.findViewById(R.id.btnMinus)
        val btnPlus:    ImageButton = v.findViewById(R.id.btnPlus)
        val btnRemove:  ImageButton = v.findViewById(R.id.btnRemove)
        val tvSubtotal: TextView    = v.findViewById(R.id.tvSubtotal)
    }

    fun updateItems(new: List<CartItem>) { items.clear(); items.addAll(new); notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val ci = items[position]; val p = ci.product

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

        h.tvName.text     = p.name
        h.tvPrice.text    = "₱${String.format("%,.2f", p.price)}"
        h.tvQty.text      = ci.quantity.toString()
        h.tvSubtotal.text = "₱${String.format("%,.2f", p.price * ci.quantity)}"

        h.btnMinus.setOnClickListener  { onQtyChange(p.id, ci.quantity - 1) }
        h.btnPlus.setOnClickListener   { if (ci.quantity < p.stock) onQtyChange(p.id, ci.quantity + 1) }
        h.btnRemove.setOnClickListener { onRemove(p.id) }
    }
}