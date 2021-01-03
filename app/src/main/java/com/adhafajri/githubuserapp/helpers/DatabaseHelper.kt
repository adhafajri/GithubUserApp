package com.adhafajri.githubuserapp.helpers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns
import android.service.notification.Condition.SCHEME
import com.adhafajri.consumerapp.networks.DatabaseContract
import com.adhafajri.consumerapp.networks.DatabaseContract.UserColumns.Companion.TABLE_NAME

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "githubuserapp"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.UserColumns.USERNAME} TEXT PRIMARY KEY," +
                " ${DatabaseContract.UserColumns.NAME} TEXT," +
                " ${DatabaseContract.UserColumns.LOCATION} TEXT," +
                " ${DatabaseContract.UserColumns.REPOSITORY} TEXT," +
                " ${DatabaseContract.UserColumns.COMPANY} TEXT," +
                " ${DatabaseContract.UserColumns.FOLLOWERS} TEXT," +
                " ${DatabaseContract.UserColumns.FOLLOWING} TEXT," +
                " ${DatabaseContract.UserColumns.AVATAR} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}