package com.adhafajri.githubuserapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adhafajri.githubuserapp.R
import com.adhafajri.githubuserapp.models.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

class ListUserAdapter(private val listUsers: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {

                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(civ_avatar)
                tv_username.text = user.username

                itemView.setOnClickListener{onItemClickCallback?.onItemClicked(user)}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = listUsers.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}