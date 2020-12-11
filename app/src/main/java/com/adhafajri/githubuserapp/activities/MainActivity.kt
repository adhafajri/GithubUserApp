package com.adhafajri.githubuserapp.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhafajri.githubuserapp.R
import com.adhafajri.githubuserapp.R.*
import com.adhafajri.githubuserapp.adapters.ListUserAdapter
import com.adhafajri.githubuserapp.helpers.ConnectionHelper
import com.adhafajri.githubuserapp.helpers.ConnectionHelper.MyCallback
import com.adhafajri.githubuserapp.models.User
import com.adhafajri.githubuserapp.utils.Constants
import com.adhafajri.githubuserapp.utils.Constants.Companion.STATE_RESULT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val TAG = ConnectionHelper::class.java.simpleName

    private var query: String? = null
    private var isSearching = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setupActionBar()

        if (!checkSavedInstance(savedInstanceState)) getUserData()
    }

    private fun checkSavedInstance(savedInstanceState: Bundle?): Boolean {
        return if (savedInstanceState != null) {
            if (!savedInstanceState.getString(STATE_RESULT).isNullOrEmpty()) {
                val query = savedInstanceState.getString(STATE_RESULT)
                this.query = query!!

                true
            } else false
        } else false
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(mipmap.logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun getUserData() {
        pb_users.visibility = VISIBLE

        val connectionHelper = ConnectionHelper()
        connectionHelper.processJSON(Constants.URL_USERS, Constants.URL_USERS, object : MyCallback {
            override fun onCallback(
                listUsers: ArrayList<User>?,
                user: User?,
                isSuccessful: Boolean,
                error: Throwable?
            ) {
                if (isSuccessful) {
                    pb_users.visibility = INVISIBLE
                    loadUsersData(listUsers!!)
                } else {
                    pb_users.visibility = INVISIBLE
                    Toast.makeText(
                        this@MainActivity,
                        "Data not loaded! ${error.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun getUserDataBySearch(query: String?) {
        pb_users.visibility = VISIBLE

        val connectionHelper = ConnectionHelper()
        connectionHelper.processJSON("${Constants.URL_SEARCH}$query", Constants.URL_SEARCH, object : MyCallback {
            override fun onCallback(
                listUsers: ArrayList<User>?,
                user: User?,
                isSuccessful: Boolean,
                error: Throwable?
            ) {
                if (isSuccessful) {
                    pb_users.visibility = INVISIBLE
                    loadUsersData(listUsers!!)
                } else {
                    pb_users.visibility = INVISIBLE
                    Toast.makeText(
                        this@MainActivity,
                        "Data not loaded! ${error.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                isSearching = false
            }
        })
    }

    private fun loadUsersData(listUsers: ArrayList<User>? = null) {
        rv_users.setHasFixedSize(true)
        rv_users.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(listUsers!!)
        rv_users.adapter = listUserAdapter
        listUserAdapter.notifyDataSetChanged()

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(Constants.USER_USERNAME, user.username)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem = menu?.findItem(id.search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = resources.getString(string.search_hint)
        searchView.setOnQueryTextListener(this)

        if (!TextUtils.isEmpty(query)) {
            searchItem.expandActionView()
            searchView.setQuery(query, true)
            searchView.clearFocus()
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == id.language) {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(TAG, "Search query: $query")
        if (!isSearching) {
            isSearching = true
            this.query = query!!
            getUserDataBySearch(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        Log.d(TAG, "Search query: $query")
        if (TextUtils.isEmpty(query)) {
            isSearching = false
            this.query = query!!
            getUserData()
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, query)
    }
}