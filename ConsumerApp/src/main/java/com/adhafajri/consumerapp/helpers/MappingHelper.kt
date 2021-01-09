package com.adhafajri.consumerapp.helpers

import android.database.Cursor
import com.adhafajri.consumerapp.entities.User
import com.adhafajri.consumerapp.networks.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val usersList = ArrayList<User>()
        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOSITORY))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))

                usersList.add(User(username, name, location, repository, company, followers, following, avatar))
            }
        }
        return usersList
    }

    fun mapCursorToUser(usersCursor: Cursor?): User {
        var user = User()
        if (usersCursor != null && usersCursor.moveToFirst()) {
            usersCursor.apply {
                moveToFirst()
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.LOCATION))
                val repository =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.REPOSITORY))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.COMPANY))
                val followers =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS))
                val following =
                    getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))

                user = User(username,
                    name,
                    location,
                    repository,
                    company,
                    followers,
                    following,
                    avatar)
            }
        }
        return user
    }
}