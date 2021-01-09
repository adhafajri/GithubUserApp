package com.adhafajri.githubuserapp.helpers

import android.util.Log
import com.adhafajri.githubuserapp.BuildConfig
import com.adhafajri.githubuserapp.entities.User
import com.adhafajri.githubuserapp.utils.Constants
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class APIHelper {

    private val TAG = APIHelper::class.java.simpleName

    fun processJSON(url: String, endpoint: String, myCallback: MyCallback) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${BuildConfig.GITHUB_API_TOKEN}")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
            ) {
                val result = String(responseBody)
                when (endpoint) {
                    Constants.URL_USERS -> {
                        val listUser = ArrayList<User>()
                        listUser.addAll(getAllUserData(result))
                        myCallback.onCallback(listUsers = listUser, isSuccessful = true)
                    }
                    Constants.URL_SEARCH -> {
                        val listUser = ArrayList<User>()
                        listUser.addAll(getSearchUserData(result))
                        myCallback.onCallback(listUsers = listUser, isSuccessful = true)
                    }
                    Constants.URL_USER -> {
                        val user: User = getUserData(result)
                        myCallback.onCallback(user = user, isSuccessful = true)
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?,
            ) {
                Log.e(TAG, error.toString())
                myCallback.onCallback(isSuccessful = false, error = error)
            }

        })
    }

    private fun getUserData(result: String): User {
        val userData = JSONObject(result)

        return User(
            userData.getString("login"),
            userData.getString("name"),
            userData.getString("location"),
            userData.getString("public_repos"),
            userData.getString("company"),
            userData.getString("followers"),
            userData.getString("following"),
            userData.getString("avatar_url")
        )
    }

    private fun getAllUserData(result: String): ArrayList<User> {
        val usersData = JSONArray(result)

        val listUser = ArrayList<User>()
        for (position in 0 until usersData.length()) {
            val userData = usersData.getJSONObject(position)

            val user = User(
                username = userData.getString("login"),
                avatar = userData.getString("avatar_url")
            )
            listUser.add(user)
        }
        return listUser
    }

    private fun getSearchUserData(result: String): ArrayList<User> {
        val searchData = JSONObject(result)
        val usersData = searchData.getJSONArray("items")

        val listUser = ArrayList<User>()
        for (position in 0 until usersData.length()) {
            val userData = usersData.getJSONObject(position)

            val user = User(
                username = userData.getString("login"),
                avatar = userData.getString("avatar_url")
            )
            listUser.add(user)
        }
        return listUser
    }

    interface MyCallback {
        fun onCallback(
            listUsers: ArrayList<User>? = null,
            user: User? = null,
            isSuccessful: Boolean,
            error: Throwable? = null,
        )
    }

}