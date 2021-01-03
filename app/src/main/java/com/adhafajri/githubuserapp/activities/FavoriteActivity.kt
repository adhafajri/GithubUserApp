package com.adhafajri.githubuserapp.activities

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhafajri.consumerapp.networks.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.adhafajri.githubuserapp.R
import com.adhafajri.githubuserapp.adapters.ListUserAdapter
import com.adhafajri.githubuserapp.databinding.ActivityFavoriteBinding
import com.adhafajri.githubuserapp.databinding.ActivityMainBinding
import com.adhafajri.githubuserapp.entities.User
import com.adhafajri.githubuserapp.helpers.APIHelper
import com.adhafajri.githubuserapp.helpers.MappingHelper
import com.adhafajri.githubuserapp.helpers.UserHelper
import com.adhafajri.githubuserapp.utils.Constants
import com.adhafajri.githubuserapp.utils.Constants.Companion.STATE_RESULT
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private val TAG = FavoriteActivity::class.java.simpleName
    private lateinit var binding: ActivityFavoriteBinding

    private lateinit var adapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.favorite)



        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = ListUserAdapter(this)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(Constants.USER_USERNAME, data.username)
                startActivity(intent)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                getFavoriteUserData()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)



        if (savedInstanceState == null) {
            getFavoriteUserData()
        } else {
            savedInstanceState.getParcelableArrayList<User>(STATE_RESULT)?.also { adapter.listUsers = it }
        }



    }

    private fun getFavoriteUserData() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.pbUsers.visibility = AdapterView.VISIBLE

            val deferredUsers = async(Dispatchers.IO) {
                Log.e(TAG, CONTENT_URI.toString())
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_RESULT, adapter.listUsers)
    }
}