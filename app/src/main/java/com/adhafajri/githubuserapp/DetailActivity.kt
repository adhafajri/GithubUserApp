package com.adhafajri.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_USER = "key_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val dataUser = intent.getParcelableExtra(KEY_USER) as User

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = dataUser.name

        tv_name.text = dataUser.name
        tv_username.text = dataUser.username
        tv_location.text = dataUser.location
        tv_repository.text = getString(R.string.repository_text, dataUser.repository)
        tv_company.text = dataUser.company
        tv_followers.text = getString(R.string.followers_text, dataUser.followers)
        tv_following.text = getString(R.string.following_text, dataUser.following)
        civ_avatar.setImageResource(dataUser.avatar)
    }
}