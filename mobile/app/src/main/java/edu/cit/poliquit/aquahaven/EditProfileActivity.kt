package edu.cit.poliquit.aquahaven.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import edu.cit.poliquit.aquahaven.R
import edu.cit.poliquit.aquahaven.model.UserProfile
import edu.cit.poliquit.aquahaven.utils.SessionManager

class EditProfileActivity : AppCompatActivity() {

    private var selectedPhotoUri: String = ""

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedPhotoUri = it.toString()

                val ivAvatar = findViewById<ImageView>(R.id.ivAvatar)
                val tvAvatar = findViewById<TextView>(R.id.tvAvatar)

                ivAvatar.visibility = View.VISIBLE
                tvAvatar.visibility = View.GONE

                Glide.with(this)
                    .load(it)
                    .centerCrop()
                    .circleCrop()
                    .into(ivAvatar)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val tvAvatar = findViewById<TextView>(R.id.tvAvatar)
        val ivAvatar = findViewById<ImageView>(R.id.ivAvatar)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val etBio = findViewById<EditText>(R.id.etBio)
        val btnPick = findViewById<Button>(R.id.btnPickPhoto)
        val btnSave = findViewById<Button>(R.id.btnSave)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val user = SessionManager.getUser(this)
        val profile = SessionManager.getProfile(this)

        val first = user?.firstname ?: ""
        val last = user?.lastname ?: ""

        tvAvatar.text =
            (first.firstOrNull()?.uppercaseChar()?.toString() ?: "U") +
                    (last.firstOrNull()?.uppercaseChar()?.toString() ?: "")

        selectedPhotoUri = profile.photoUri

        if (selectedPhotoUri.isNotBlank()) {
            ivAvatar.visibility = View.VISIBLE
            tvAvatar.visibility = View.GONE

            Glide.with(this)
                .load(Uri.parse(selectedPhotoUri))
                .centerCrop()
                .circleCrop()
                .into(ivAvatar)
        }

        etPhone.setText(profile.phone)
        etLocation.setText(profile.location)
        etBio.setText(profile.bio)

        btnPick.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnSave.setOnClickListener {
            SessionManager.saveProfile(
                this,
                UserProfile(
                    bio = etBio.text.toString().trim(),
                    photoUri = selectedPhotoUri,
                    phone = etPhone.text.toString().trim(),
                    location = etLocation.text.toString().trim()
                )
            )

            Toast.makeText(
                this,
                "Profile updated ✓",
                Toast.LENGTH_SHORT
            ).show()

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}