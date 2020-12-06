package com.melq.roomtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(private val userList: List<User>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.tv_id)
        val fn: TextView = view.findViewById(R.id.tv_fn)
        val ln: TextView = view.findViewById(R.id.tv_ln)
        val age: TextView = view.findViewById(R.id.tv_age)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = userList[position]

        holder.id.text = userData.id.toString()
        holder.fn.text = userData.firstName
        holder.ln.text = userData.lastName
        holder.age.text = userData.age.toString()
    }

    override fun getItemCount() = userList.size
}