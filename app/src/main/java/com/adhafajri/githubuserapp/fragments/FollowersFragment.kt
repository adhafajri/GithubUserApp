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
import com.adhafajri.githubuserapp.databinding.FragmentFollowersBinding
import com.adhafajri.githubuserapp.helpers.APIHelper
import com.adhafajri.githubuserapp.entities.User
import com.adhafajri.githubuserapp.helpers.UserHelper
import com.adhafajri.githubuserapp.utils.Constants

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding

    private lateinit var adapter: ListUserAdapter

    private var isRunning: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFollowersBinding.bind(view)

        binding.rvFollowers.setHasFixedSize(true)
        binding.rvFollowers.layoutManager = LinearLayoutManager(context)
        adapter = ListUserAdapter(activity!!)
        binding.rvFollowers.adapter = adapter

        var username: String? = null

        arguments?.let {
            username = it.getString(Constants.USER_USERNAME)
        }
        getuserFollowersData(username)

    }

    private fun getuserFollowersData(username: String?) {
        binding.pbFollowers.visibility = View.VISIBLE

        val connectionHelper = APIHelper()
        connectionHelper.processJSON("${Constants.URL_USER}$username/followers",
            Constants.URL_USERS,
            object : APIHelper.MyCallback {
                override fun onCallback(
                    listUsers: ArrayList<User>?,
                    user: User?,
                    isSuccessful: Boolean,
                    error: Throwable?,
                ) {
                    if (isRunning) {
                        if (isSuccessful) {
                            binding.pbFollowers.visibility = AdapterView.INVISIBLE
                            loadUserFollowersData(listUsers!!)
                        } else {
                            binding.pbFollowers.visibility = View.INVISIBLE
                        }
                    }
                }
            })
    }

    private fun loadUserFollowersData(listUsers: ArrayList<User>) {
        adapter.listUsers = listUsers
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(Constants.USER_USERNAME, data.username)
                startActivity(intent)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            FollowersFragment().apply {
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