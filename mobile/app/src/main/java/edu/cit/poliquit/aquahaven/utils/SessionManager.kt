package edu.cit.poliquit.aquahaven.utils

import android.content.Context
import android.content.SharedPreferences
import edu.cit.poliquit.aquahaven.model.UserInfo
import edu.cit.poliquit.aquahaven.model.UserProfile

object SessionManager {
    private const val PREFS        = "aquahaven_prefs"
    private const val KEY_TOKEN    = "access_token"
    private const val KEY_EMAIL    = "user_email"
    private const val KEY_FIRST    = "user_firstname"
    private const val KEY_LAST     = "user_lastname"
    private const val KEY_ROLE     = "user_role"
    private const val KEY_BIO      = "profile_bio"
    private const val KEY_PHOTO    = "profile_photo"
    private const val KEY_PHONE    = "profile_phone"
    private const val KEY_LOCATION = "profile_location"

    private fun prefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun save(ctx: Context, token: String, user: UserInfo) {
        prefs(ctx).edit().apply {
            putString(KEY_TOKEN, token)
            putString(KEY_EMAIL, user.email)
            putString(KEY_FIRST, user.firstname)
            putString(KEY_LAST,  user.lastname)
            putString(KEY_ROLE,  user.role)
            apply()
        }
    }

    fun getToken(ctx: Context): String? {
        val t = prefs(ctx).getString(KEY_TOKEN, null)
        return if (t.isNullOrBlank()) null else t
    }

    fun getUser(ctx: Context): UserInfo? {
        val p = prefs(ctx)
        val email = p.getString(KEY_EMAIL, null) ?: return null
        return UserInfo(email, p.getString(KEY_FIRST, null), p.getString(KEY_LAST, null), p.getString(KEY_ROLE, null))
    }

    fun isLoggedIn(ctx: Context) = !getToken(ctx).isNullOrBlank()

    fun saveProfile(ctx: Context, profile: UserProfile) {
        prefs(ctx).edit().apply {
            putString(KEY_BIO,      profile.bio)
            putString(KEY_PHOTO,    profile.photoUri)
            putString(KEY_PHONE,    profile.phone)
            putString(KEY_LOCATION, profile.location)
            apply()
        }
    }

    fun getProfile(ctx: Context): UserProfile {
        val p = prefs(ctx)
        return UserProfile(
            bio      = p.getString(KEY_BIO,      "") ?: "",
            photoUri = p.getString(KEY_PHOTO,    "") ?: "",
            phone    = p.getString(KEY_PHONE,    "") ?: "",
            location = p.getString(KEY_LOCATION, "") ?: ""
        )
    }

    fun clear(ctx: Context) = prefs(ctx).edit().clear().apply()
}