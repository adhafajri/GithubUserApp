package com.adhafajri.consumerapp.activities

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhafajri.consumerapp.R
import com.adhafajri.consumerapp.R.*
import com.adhafajri.consumerapp.adapters.ListUserAdapter
import com.adhafajri.consumerapp.databinding.ActivityMainBinding
import com.adhafajri.consumerapp.entities.User
import com.adhafajri.consumerapp.helpers.MappingHelper
import com.adhafajri.consumerapp.networks.DatabaseContract
import com.adhafajri.consumerapp.utils.Constants
import com.adhafajri.consumerapp.utils.Constants.Companion.STATE_RESULT
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.setHasFixedSize(true)
        adapter = ListUserAdapter(this)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(Constants.USER_USERNAME, data.username)
                startActivity(intent)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                getUserData()
            }
        }

        contentResolver.registerContentObserver(DatabaseContract.UserColumns.CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            getUserData()
        } else {
            savedInstanceState.getParcelableArrayList<User>(STATE_RESULT)?.also {
                adapter.listUsers = it
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(mipmap.logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun getUserData() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.pbUsers.visibility = VISIBLE

            val deferredUsers = async(Dispatchers.IO) {
                Log.e(TAG, DatabaseContract.UserColumns.CONTENT_URI.toString())
                val cursor = contentResolver.query(DatabaseContract.UserColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredUsers.await()
            if (users.size > 0) {
                adapter.listUsers = users
            } else {
                adapter.listUsers = ArrayList()
                Snackbar.make(binding.rvUsers, getString(string.no_favorite_users), Snackbar.LENGTH_SHORT).show()
            }
            binding.pbUsers.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == id.settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_RESULT, adapter.listUsers)
    }
}