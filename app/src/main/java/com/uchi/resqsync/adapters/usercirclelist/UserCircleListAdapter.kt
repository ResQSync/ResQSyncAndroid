package com.uchi.resqsync.adapters.usercirclelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uchi.resqsync.R
import com.uchi.resqsync.adapters.usercirclechip.UserCircleChipModel

class UserCircleListAdapter(val list: List<UserCircleListModel>):
    RecyclerView.Adapter<UserCircleListAdapter.ModelViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserCircleListAdapter.ModelViewHolder {
        return ModelViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.user_circle_members_layout, parent, false))
    }

    override fun onBindViewHolder(holder: UserCircleListAdapter.ModelViewHolder, position: Int) {
        val currentitem =list[position]
        holder.memberName.text=currentitem.name

    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var memberName: TextView
        var linearLayout: LinearLayout
        init {
            linearLayout = itemView.findViewById(R.id.circle_members_layout)
            memberName = itemView.findViewById(R.id.circle_member_name_text)
        }
    }
}