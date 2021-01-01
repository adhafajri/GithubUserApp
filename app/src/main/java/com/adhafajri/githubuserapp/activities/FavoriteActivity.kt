package com.adhafajri.githubuserapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhafajri.githubuserapp.R
import com.adhafajri.githubuserapp.adapters.ListUserAdapter
import com.adhafajri.githubuserapp.databinding.ActivityFavoriteBinding
import com.adhafajri.githubuserapp.databinding.ActivityMainBinding
import com.adhafajri.githubuserapp.entities.User
import com.adhafajri.githubuserapp.helpers.APIHelper
import com.adhafajri.githubuserapp.helpers.MappingHelper
import com.adhafajri.githubuserapp.helpers.UserHelper
import com.adhafajri.githubuserapp.utils.Constants
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private val TAG = FavoriteActivity::class.java.simpleName
    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var userHelper: UserHelper
    private lateinit var adapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userHelper = UserHelper.getInstance(this)
        userHelper.open()

        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = ListUserAdapter(this)
        binding.rvUsers.adapter = adapter

        getFavoriteUserData()

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(Constants.USER_USERNAME, data.username)
                startActivity(intent)
            }
        })
    }

    private fun getFavoriteUserData() {
        binding.pbUsers.visibility = AdapterView.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            val deferredUsers = async(Dispatchers.IO) {
                val cursor = userHelper.getAllUserData()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredUsers.await()
            if (users.size > 0) {
                adapter.listUsers = users
            } else {
                adapter.listUsers = ArrayList()
                Snackbar.make(binding.rvUsers, getString(R.string.no_favorite_users), Snackbar.LENGTH_SHORT).show()
            }
            binding.pbUsers.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userHelper.close()
    }
}