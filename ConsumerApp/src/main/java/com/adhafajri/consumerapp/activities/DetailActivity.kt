package com.adhafajri.consumerapp.activities

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.adhafajri.consumerapp.R
import com.adhafajri.consumerapp.databinding.ActivityDetailBinding
import com.adhafajri.consumerapp.entities.User
import com.adhafajri.consumerapp.helpers.MappingHelper
import com.adhafajri.consumerapp.networks.DatabaseContract
import com.adhafajri.consumerapp.networks.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.adhafajri.consumerapp.utils.Constants
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private val TAG = DetailActivity::class.java.simpleName

    private lateinit var uriWithUsername: Uri

    private lateinit var binding: ActivityDetailBinding
    private var isRunning: Boolean = true

    lateinit var user: User
    lateinit var username: String
    private var isFavorite: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(Constants.USER_USERNAME).toString()
        uriWithUsername = Uri.parse("$CONTENT_URI/$username")
        getUserDataByUsername()

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                removeDataFromFavorite()
            } else {
                saveDataToFavorite()
            }
        }
    }

    private fun removeDataFromFavorite() {
        contentResolver.delete(uriWithUsername, null, null)
        Log.e(TAG, "Success!")
        isFavorite = false
        checkFavoriteButton()
    }

    private fun saveDataToFavorite() {
        val values = ContentValues()
        values.put(DatabaseContract.UserColumns.USERNAME, user.username)
        values.put(DatabaseContract.UserColumns.NAME, user.name)
        values.put(DatabaseContract.UserColumns.LOCATION, user.location)
        values.put(DatabaseContract.UserColumns.REPOSITORY, user.repository)
        values.put(DatabaseContract.UserColumns.COMPANY, user.company)
        values.put(DatabaseContract.UserColumns.FOLLOWERS, user.followers)
        values.put(DatabaseContract.UserColumns.FOLLOWING, user.following)
        values.put(DatabaseContract.UserColumns.AVATAR, user.avatar)
        contentResolver.insert(CONTENT_URI, values)
        Log.e(TAG, "Success!")
        isFavorite = true
        checkFavoriteButton()

    }


    private fun getUserDataByUsername() {
        binding.pbUser.visibility = AdapterView.VISIBLE
        val cursor = contentResolver.query(uriWithUsername, null, null, null, null)
        val user: User = MappingHelper.mapCursorToUser(cursor)

        if (user.username != null) {
            this@DetailActivity.user = user
            isFavorite = true
            checkFavoriteButton()
            loadUserData()
            setupActionBar()
            Log.e(TAG, "Fav True!")
            binding.pbUser.visibility = View.INVISIBLE
        } else {
            isFavorite = false
            checkFavoriteButton()
            Log.e(TAG, "Fav False!")
        }
    }

    private fun loadUserData() {
        binding.tvUsername.text = user.username

        if (user.name!!.isEmpty() || user.name == null)
            binding.tvName.visibility = View.GONE
        else
            binding.tvName.text = user.name

        if (user.avatar!!.isNotEmpty()) {
            Glide.with(this)
                .load(user.avatar)
                .override(150)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.civAvatar)
        }

        if (user.company!!.isEmpty() || user.company == "null") {
            binding.ivCompany.visibility = View.GONE
            binding.tvCompany.visibility = View.GONE
        } else
            binding.tvCompany.text = user.company

        if (user.location!!.isEmpty() || user.location == "null") {
            binding.ivLocation.visibility = View.GONE
            binding.tvLocation.visibility = View.GONE
        } else
            binding.tvLocation.text = user.location

        if (user.repository!!.isEmpty() || user.repository == "null") {
            binding.ivRepository.visibility = View.GONE
            binding.tvRepository.visibility = View.GONE
        } else
            binding.tvRepository.text = getString(R.string.repository_text, user.repository)
    }

    private fun checkFavoriteButton() {
        if (isFavorite) {
            binding.btnFavorite.setBackgroundResource(R.color.colorAccent)
            binding.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
            binding.btnFavorite.text = getString(R.string.unfavorite)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                binding.btnFavorite.setTextColor(resources.getColor(R.color.colorPrimary, null))
            else
                binding.btnFavorite.setTextColor(resources.getColor(R.color.colorPrimary))
        } else {
            binding.btnFavorite.setBackgroundResource(R.color.colorPrimary)
            binding.btnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border_24, 0, 0, 0)
            binding.btnFavorite.text = getString(R.string.favorite)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
                binding.btnFavorite.setTextColor(resources.getColor(R.color.colorAccent, null))
            else
                binding.btnFavorite.setTextColor(resources.getColor(R.color.colorAccent))

        }
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = user.name
        supportActionBar?.elevation = 0f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (isRunning) isRunning = false
    }

}