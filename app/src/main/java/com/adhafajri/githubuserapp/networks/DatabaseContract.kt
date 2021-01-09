package com.adhafajri.consumerapp.networks

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.adhafajri.githubuserapp"
    const val SCHEME = "content"

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

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}