package com.example.trackingcontrol.moduls.users.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.trackingcontrol.R
import com.example.trackingcontrol.databinding.ItemUserBinding
import com.example.trackingcontrol.models.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView

class UsersAdapter(
    private val context: Context,
    private val navController: NavController
): RecyclerView.Adapter<UsersAdapter.UsersAdapterViewHolder>() {

    private val userList: MutableList<UserData?> = mutableListOf()

    class UsersAdapterViewHolder(var binding:ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapterViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersAdapterViewHolder, position: Int) {
        val user = userList[position]
        val name = user?.name

        holder.binding.name.text=name

        if (user != null) {
            Glide.with(context)
                .load(user.photoUrl)
                .signature(ObjectKey(user.photoUrl.hashCode()))
                .into(holder.binding.image)
        }

        holder.itemView.setOnClickListener{ view ->
            val bundle = bundleOf(
                "uid" to user?.uid,
            )
            view.findNavController().navigate(R.id.action_navigation_users_to_navigation_map,bundle)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setUsers(users: List<UserData?>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

}