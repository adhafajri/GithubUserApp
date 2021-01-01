package com.adhafajri.githubuserapp.activities

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.adhafajri.githubuserapp.R
import com.adhafajri.githubuserapp.adapters.SectionsPagerAdapter
import com.adhafajri.githubuserapp.databinding.ActivityDetailBinding
import com.adhafajri.githubuserapp.helpers.APIHelper
import com.adhafajri.githubuserapp.entities.User
import com.adhafajri.githubuserapp.helpers.DatabaseHelper
import com.adhafajri.githubuserapp.helpers.MappingHelper
import com.adhafajri.githubuserapp.helpers.UserHelper
import com.adhafajri.githubuserapp.utils.Constants
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private val TAG = DetailActivity::class.java.simpleName

    private lateinit var binding: ActivityDetailBinding
    private var isRunning: Boolean = true

    private lateinit var userHelper: UserHelper
    lateinit var user: User
    lateinit var username: String
    private var isFavorite: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(this)
        userHelper.open()
        username = intent.getStringExtra(Constants.USER_USERNAME).toString()
        getUserDataByUsername(username)

        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                removeDataFromFavorite()
            } else {
                saveDataToFavorite()
            }
        }

    }

    private fun removeDataFromFavorite() {
        val result = userHelper.deleteByUsername(user.username)
        if (result > 0) {
            Log.e(TAG, "Success!")
            isFavorite = false;
            checkFavoriteButton()
        } else {
            Log.e(TAG, "Failed!")
        }
    }

    private fun saveDataToFavorite() {
        val values = ContentValues()
        values.put(DatabaseHelper.UserColumns.USERNAME, user.username)
        values.put(DatabaseHelper.UserColumns.NAME, user.name)
        values.put(DatabaseHelper.UserColumns.LOCATION, user.location)
        values.put(DatabaseHelper.UserColumns.REPOSITORY, user.repository)
        values.put(DatabaseHelper.UserColumns.COMPANY, user.company)
        values.put(DatabaseHelper.UserColumns.FOLLOWERS, user.followers)
        values.put(DatabaseHelper.UserColumns.FOLLOWING, user.following)
        values.put(DatabaseHelper.UserColumns.AVATAR, user.avatar)
        val result = userHelper.insert(values)
        if (result > 0) {
            Log.e(TAG, "Success!")
            isFavorite = true;
            checkFavoriteButton()
        } else {
            Log.e(TAG, "Failed!")
        }
    }


    private fun getUserDataByUsername(username: String) {
        binding.pbUser.visibility = AdapterView.VISIBLE

        getDataFromFavorite(username)

        if (!isFavorite) {
            val connectionHelper = APIHelper()
            connectionHelper.processJSON("${Constants.URL_USER}$username", Constants.URL_USER, object :
                APIHelper.MyCallback {
                override fun onCallback(
                    listUsers: ArrayList<User>?,
                    user: User?,
                    isSuccessful: Boolean,
                    error: Throwable?,
                ) {
                    if (isRunning) {
                        if (isSuccessful) {
                            this@DetailActivity.user = user!!
                            loadUserData()
                            setupActionBar()
                            binding.pbUser.visibility = AdapterView.INVISIBLE
                        } else {
                            binding.pbUser.visibility = AdapterView.INVISIBLE
                            Toast.makeText(this@DetailActivity,
                                getString(R.string.data_not_loaded, error.toString()),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

    }

    private fun getDataFromFavorite(username: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = userHelper.getUserData(username)
                MappingHelper.mapCursorToUser(cursor)
            }
            val user: User? = deferredUser.await()
            if (user != null) {
                this@DetailActivity.user = user
                isFavorite = true
                checkFavoriteButton()
                Log.e(TAG, "Fav True!")

            } else {
                isFavorite = false
                checkFavoriteButton()
            }
            binding.pbUser.visibility = View.INVISIBLE
        }
    }

    private fun loadUserData() {
        binding.tvUsername.text = user.username

        if (user.name.isEmpty() || user.name == "null")
            binding.tvName.visibility = View.GONE
        else
            binding.tvName.text = user.name

        if (user.avatar.isNotEmpty()) {
            Glide.with(this)
                .load(user.avatar)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(binding.civAvatar)
        }

        if (user.company.isEmpty() || user.company == "null") {
            binding.ivCompany.visibility = View.GONE
            binding.tvCompany.visibility = View.GONE
        } else
            binding.tvCompany.text = user.company

        if (user.location.isEmpty() || user.location == "null") {
            binding.ivLocation.visibility = View.GONE
            binding.tvLocation.visibility = View.GONE
        } else
            binding.tvLocation.text = user.location

        if (user.repository.isEmpty() || user.repository == "null") {
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

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, username)
        binding.vpUser.adapter = sectionsPagerAdapter
        binding.tlUser.setupWithViewPager(binding.vpUser)

        supportActionBar?.elevation = 0f
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
        if (isRunning) isRunning = false
    }

}