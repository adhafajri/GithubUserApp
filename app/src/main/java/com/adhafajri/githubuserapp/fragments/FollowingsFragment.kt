package com.adhafajri.githubuserapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adhafajri.githubuserapp.R
import com.adhafajri.githubuserapp.activities.DetailActivity
import com.adhafajri.githubuserapp.adapters.ListUserAdapter
import com.adhafajri.githubuserapp.helpers.ConnectionHelper
import com.adhafajri.githubuserapp.models.User
import com.adhafajri.githubuserapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_followings.*

class FollowingsFragment : Fragment() {
    private var isRunning: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var username: String? = null

        arguments?.let {
            username = it.getString(Constants.USER_USERNAME)
        }
        getUserFollowingsData(username)
    }

    private fun getUserFollowingsData(username: String?) {
        pb_followings.visibility = View.VISIBLE

        val connectionHelper = ConnectionHelper()
        connectionHelper.processJSON("${Constants.URL_USER}$username/following",
            Constants.URL_USERS,
            object : ConnectionHelper.MyCallback {
                override fun onCallback(
                    listUsers: ArrayList<User>?,
                    user: User?,
                    isSuccessful: Boolean,
                    error: Throwable?,
                ) {
                    if (isRunning) {
                        if (isSuccessful) {
                            pb_followings.visibility = AdapterView.INVISIBLE
                            loadUserFollowingsData(listUsers!!)
                        } else {
                            pb_followings.visibility = View.INVISIBLE
                        }
                    }
                }
            })
    }

    private fun loadUserFollowingsData(listUsers: ArrayList<User>) {
        rv_followings.setHasFixedSize(true)
        rv_followings.layoutManager = LinearLayoutManager(context)
        val listUserAdapter = ListUserAdapter(listUsers)
        rv_followings.adapter = listUserAdapter
        listUserAdapter.notifyDataSetChanged()

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(Constants.USER_USERNAME, user.username)
                startActivity(intent)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            FollowingsFragment().apply {
                arguments = Bundle().apply {
                    putString(Constants.USER_USERNAME, username)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRunning) isRunning = false
    }
}