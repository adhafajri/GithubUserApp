package com.adhafajri.githubuserapp.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adhafajri.githubuserapp.R
import com.adhafajri.githubuserapp.adapters.SectionsPagerAdapter
import com.adhafajri.githubuserapp.helpers.ConnectionHelper
import com.adhafajri.githubuserapp.models.User
import com.adhafajri.githubuserapp.utils.Constants
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*

class DetailActivity : AppCompatActivity() {

    lateinit var user: User
    lateinit var username: String
    private var isRunning: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        username = intent.getStringExtra(Constants.USER_USERNAME)
        getUserDataByUsername(username)
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = user.name

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, username)
        vp_user.adapter = sectionsPagerAdapter
        tl_user.setupWithViewPager(vp_user)

        supportActionBar?.elevation = 0f
    }

    private fun getUserDataByUsername(username: String) {
        pb_user.visibility = AdapterView.VISIBLE

        val connectionHelper = ConnectionHelper()
        connectionHelper.processJSON("${Constants.URL_USER}$username", Constants.URL_USER, object :
            ConnectionHelper.MyCallback {
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
                        pb_user.visibility = AdapterView.INVISIBLE
                    } else {
                        pb_user.visibility = AdapterView.INVISIBLE
                        Toast.makeText(
                            this@DetailActivity,
                            "Data not loaded! ${error.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun loadUserData() {
        tv_username.text = user.username

        if (user.name.isEmpty() || user.name == "null") {
            tv_name.visibility = View.GONE
        } else tv_name.text = user.name

        if (user.avatar.isNotEmpty()) {
            Glide.with(this)
                .load(user.avatar)
                .into(civ_avatar)
        }
        if (user.company.isEmpty() || user.company == "null") {
            iv_company.visibility = View.GONE
            tv_company.visibility = View.GONE
        } else tv_company.text = user.company

        if (user.location.isEmpty() || user.location == "null") {
            iv_location.visibility = View.GONE
            tv_location.visibility = View.GONE
        } else tv_location.text = user.location

        if (user.repository.isEmpty() || user.repository == "null") {
            iv_repository.visibility = View.GONE
            tv_repository.visibility = View.GONE
        } else tv_repository.text = getString(R.string.repository_text, user.repository)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRunning) isRunning = false
    }

}