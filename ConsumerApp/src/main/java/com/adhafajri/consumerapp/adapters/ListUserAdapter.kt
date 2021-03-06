package com.adhafajri.consumerapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adhafajri.consumerapp.R
import com.adhafajri.consumerapp.databinding.ItemUserBinding
import com.adhafajri.consumerapp.entities.User
import com.bumptech.glide.Glide

class ListUserAdapter(private val activity: Activity) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    var listUsers = ArrayList<User>()
        set(listUsers) {
            if (listUsers.size > 0) {
                this.listUsers.clear()
            }
            this.listUsers.addAll(listUsers)
            notifyDataSetChanged()
        }

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int = this.listUsers.size



    inner class ListViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemUserBinding.bind(itemView)

        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .placeholder(R.drawable.logo)
                    .override(60)
                    .error(R.drawable.logo)
                    .into(binding.civAvatar)
                binding.tvUsername.text = user.username

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }
}