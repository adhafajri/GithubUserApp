package com.adhafajri.githubuserapp

import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import com.adhafajri.githubuserapp.R.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var dataUsername: Array<String>
    private lateinit var dataName: Array<String>
    private lateinit var dataAvatar: TypedArray
    private lateinit var dataCompany: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(style.AppTheme);
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setLogo(mipmap.logo)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = UserAdapter(this)
        rv_user.adapter = adapter

        getUserData()
        loadUserData()

        rv_user.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java)

            val selectedUser: User = users[position]
            intent.putExtra(DetailActivity.KEY_USER, selectedUser)

            startActivity(intent)
        }
    }

    private fun getUserData() {
        dataUsername = resources.getStringArray(array.username)
        dataName = resources.getStringArray(array.name)
        dataAvatar = resources.obtainTypedArray(array.avatar)
        dataCompany = resources.getStringArray(array.company)
        dataLocation = resources.getStringArray(array.location)
        dataRepository = resources.getStringArray(array.repository)
        dataFollowers = resources.getStringArray(array.followers)
        dataFollowing = resources.getStringArray(array.following)
    }

    private fun loadUserData() {
        for (position in dataName.indices) {
            val user = User(
                dataUsername[position],
                dataName[position],
                dataLocation[position],
                dataRepository[position],
                dataCompany[position],
                dataFollowers[position],
                dataFollowing[position],
                dataAvatar.getResourceId(position, -1)
            )
            users.add(user)
        }
        adapter.users = users
    }
}