package com.adhafajri.githubuserapp.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.adhafajri.githubuserapp.helpers.DatabaseHelper.UserColumns.Companion.TABLE_NAME

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val USERNAME = "username"
            const val NAME = "name"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val COMPANY = "company"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
            const val AVATAR ="avatar"
        }
    }

    companion object {
        private const val DATABASE_NAME = "githubuserapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumns.USERNAME} TEXT PRIMARY KEY," +
                " ${UserColumns.NAME} TEXT," +
                " ${UserColumns.LOCATION} TEXT," +
                " ${UserColumns.REPOSITORY} TEXT," +
                " ${UserColumns.COMPANY} TEXT," +
                " ${UserColumns.FOLLOWERS} TEXT," +
                " ${UserColumns.FOLLOWING} TEXT," +
                " ${UserColumns.AVATAR} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}