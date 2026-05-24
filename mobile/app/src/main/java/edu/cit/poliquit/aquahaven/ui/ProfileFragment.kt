package edu.cit.poliquit.aquahaven.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import edu.cit.poliquit.aquahaven.LoginActivity
import edu.cit.poliquit.aquahaven.R
import edu.cit.poliquit.aquahaven.network.RetrofitClient
import edu.cit.poliquit.aquahaven.utils.CartManager
import edu.cit.poliquit.aquahaven.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(view)
    }

    override fun onResume() {
        super.onResume()
        view?.let { bind(it) }
    }

    private fun bind(view: View) {

        val tvAvatar = view.findViewById<TextView>(R.id.tvAvatar)
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvRole = view.findViewById<TextView>(R.id.tvRole)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        val tvLocation = view.findViewById<TextView>(R.id.tvLocation)
        val tvBio = view.findViewById<TextView>(R.id.tvBio)
        val tvOrderCount = view.findViewById<TextView>(R.id.tvOrderCount)
        val tvCartCount = view.findViewById<TextView>(R.id.tvCartCount)

        val btnEdit = view.findViewById<View>(R.id.btnEditProfile)
        val btnLogout = view.findViewById<View>(R.id.btnLogout)

        val user = SessionManager.getUser(requireContext())
        val profile = SessionManager.getProfile(requireContext())

        val first = user?.firstname ?: ""
        val last = user?.lastname ?: ""

        val fullName = "$first $last"
            .trim()
            .ifEmpty { "User" }

        // Avatar
        if (profile.photoUri.isNotBlank()) {
            ivAvatar.visibility = View.VISIBLE
            tvAvatar.visibility = View.GONE

            Glide.with(this)
                .load(Uri.parse(profile.photoUri))
                .centerCrop()
                .circleCrop()
                .into(ivAvatar)

        } else {
            ivAvatar.visibility = View.GONE
            tvAvatar.visibility = View.VISIBLE

            tvAvatar.text =
                (first.firstOrNull()?.uppercaseChar()?.toString() ?: "U") +
                        (last.firstOrNull()?.uppercaseChar()?.toString() ?: "")
        }

        // User Info
        tvName.text = fullName
        tvEmail.text = user?.email ?: "—"

        tvRole.text = when (user?.role) {
            "ROLE_ADMIN" -> "👑 Administrator"
            "ROLE_CUSTOMER" -> "🐟 Aquarist"
            else -> user?.role ?: "Member"
        }

        tvPhone.text =
            profile.phone.ifBlank { "Not set" }

        tvLocation.text =
            profile.location.ifBlank { "Not set" }

        tvBio.text =
            profile.bio.ifBlank { "No bio yet." }

        tvCartCount.text =
            CartManager.totalItems().toString()

        // Orders Count
        val token = SessionManager.getToken(requireContext())

        if (!token.isNullOrBlank()) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val response =
                        RetrofitClient.instance
                            .myOrders("Bearer $token")

                    val count =
                        response.body()?.data?.size ?: 0

                    tvOrderCount.text =
                        count.toString()

                } catch (_: Exception) {
                    tvOrderCount.text = "—"
                }
            }
        } else {
            tvOrderCount.text = "0"
        }

        // Edit Profile
        btnEdit.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    EditProfileActivity::class.java
                )
            )
        }

        // Logout
        btnLogout.setOnClickListener {

            SessionManager.clear(requireContext())

            startActivity(
                Intent(
                    requireContext(),
                    LoginActivity::class.java
                ).apply {
                    flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )

            activity?.finish()
        }
    }
}