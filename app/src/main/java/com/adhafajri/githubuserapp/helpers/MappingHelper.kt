package com.adhafajri.githubuserapp.helpers

import android.database.Cursor
import com.adhafajri.githubuserapp.entities.User

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User> {
        val usersList = ArrayList<User>()
        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.NAME))
                val location = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.REPOSITORY))
                val company = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.COMPANY))
                val followers = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.FOLLOWING))
                val avatar = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.AVATAR))

                usersList.add(User(username, name, location, repository, company, followers, following, avatar))
            }
        }
        return usersList
    }

    fun mapCursorToUser(notesCursor: Cursor?): User? {
        var user: User? = null
        notesCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.NAME))
                val location = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.REPOSITORY))
                val company = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.COMPANY))
                val followers = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.FOLLOWING))
                val avatar = getString(getColumnIndexOrThrow(DatabaseHelper.UserColumns.AVATAR))

                user = User(username, name, location, repository, company, followers, following, avatar)
            }
        }
        return user
    }
}