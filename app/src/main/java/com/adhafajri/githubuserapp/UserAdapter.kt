package com.adhafajri.githubuserapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var users = arrayListOf<User>()

    override fun getCount(): Int = users.size

    override fun getItem(position: Int): Any = users[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if (itemView == null) itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)

        val viewHolder = ViewHolder(itemView as View)
        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    private inner class ViewHolder constructor(private val view: View) {
        fun bind(dataUser: User) {
            with(view) {
                civ_avatar.setImageResource(dataUser.avatar)
                tv_name.text = dataUser.name
                tv_username.text = dataUser.username
                tv_followers.text = context.getString(R.string.followers_text, dataUser.followers)
                tv_following.text = context.getString(R.string.following_text, dataUser.following)
            }
        }
    }

}