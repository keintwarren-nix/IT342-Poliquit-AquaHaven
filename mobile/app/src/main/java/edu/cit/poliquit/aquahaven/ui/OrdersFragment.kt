package edu.cit.poliquit.aquahaven.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import edu.cit.poliquit.aquahaven.R
import edu.cit.poliquit.aquahaven.model.Order
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import edu.cit.poliquit.aquahaven.utils.SessionManager
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private lateinit var rvOrders:    RecyclerView
    private lateinit var tvEmpty:     TextView
    private lateinit var tvError:     TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRefresh:  Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_orders, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvOrders    = view.findViewById(R.id.rvOrders)
        tvEmpty     = view.findViewById(R.id.tvEmpty)
        tvError     = view.findViewById(R.id.tvError)
        progressBar = view.findViewById(R.id.progressBar)
        btnRefresh  = view.findViewById(R.id.btnRefresh)

        rvOrders.layoutManager = LinearLayoutManager(requireContext())
        btnRefresh.setOnClickListener { loadOrders() }
        loadOrders()
    }

    private fun loadOrders() {
        val token = SessionManager.getToken(requireContext())
        if (token.isNullOrBlank()) {
            showError("Session expired. Please log out and log in again.")
            return
        }

        progressBar.visibility = View.VISIBLE
        tvEmpty.visibility     = View.GONE
        tvError.visibility     = View.GONE
        rvOrders.visibility    = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val resp = RetrofitClient.instance.myOrders("Bearer $token")
                val body = resp.body()
                progressBar.visibility = View.GONE

                when {
                    !resp.isSuccessful -> showError("❌ Server error (${resp.code()}). Tap Refresh to retry.")
                    body?.success == true -> {
                        val orders = body.data ?: emptyList()
                        if (orders.isEmpty()) {
                            tvEmpty.visibility  = View.VISIBLE
                        } else {
                            rvOrders.visibility = View.VISIBLE
                            rvOrders.adapter    = OrderAdapter(orders)
                        }
                    }
                    else -> showError("❌ ${body?.message ?: "Failed to load orders"}")
                }
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                showError("❌ Cannot connect to server.\n${e.message}")
            }
        }
    }

    private fun showError(msg: String) {
        tvError.text        = msg
        tvError.visibility  = View.VISIBLE
        rvOrders.visibility = View.GONE
        tvEmpty.visibility  = View.GONE
    }
}

class OrderAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvRef:     TextView = v.findViewById(R.id.tvRef)
        val tvStatus:  TextView = v.findViewById(R.id.tvStatus)
        val tvDate:    TextView = v.findViewById(R.id.tvDate)
        val tvTotal:   TextView = v.findViewById(R.id.tvTotal)
        val tvPayment: TextView = v.findViewById(R.id.tvPayment)
        val tvAddress: TextView = v.findViewById(R.id.tvAddress)
        val tvItems:   TextView = v.findViewById(R.id.tvItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false))

    override fun getItemCount() = orders.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val o = orders[position]
        h.tvRef.text     = "Order #${o.orderRef}"
        h.tvTotal.text   = "₱${String.format("%,.2f", o.totalAmount)}"
        h.tvPayment.text = o.paymentMethod
        h.tvAddress.text = o.shippingAddress
        h.tvDate.text    = o.createdAt?.take(10) ?: ""

        val (color, label) = when (o.status.uppercase()) {
            "PENDING"   -> 0xFFF59E0B.toInt() to "⏳ Pending"
            "CONFIRMED" -> 0xFF2563EB.toInt() to "✅ Confirmed"
            "SHIPPED"   -> 0xFF7C3AED.toInt() to "🚚 Shipped"
            "DELIVERED" -> 0xFF059669.toInt() to "📦 Delivered"
            "CANCELLED" -> 0xFFDC2626.toInt() to "❌ Cancelled"
            else        -> 0xFF6B7280.toInt() to o.status
        }
        h.tvStatus.text = label
        h.tvStatus.setTextColor(color)

        h.tvItems.text = o.items.joinToString("\n") {
            "• ${it.productName} × ${it.quantity}  ₱${String.format("%,.2f", it.subtotal)}"
        }
    }
}