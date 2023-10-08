package com.uchi.resqsync.adapters.usercirclechip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uchi.resqsync.R

class UserCircleChipAdapter(val list: List<UserCircleChipModel>):
    RecyclerView.Adapter<UserCircleChipAdapter.ModelViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserCircleChipAdapter.ModelViewHolder {
        return ModelViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.user_circle_layout, parent, false))
    }

    override fun onBindViewHolder(holder: UserCircleChipAdapter.ModelViewHolder, position: Int) {
        val currentitem =list[position]
        holder.chipName.text=currentitem.name
        holder.chipName.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
       return list.size
    }
    inner class ModelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var chipName: TextView
        var linearLayout: LinearLayout
        init {
            linearLayout = itemView.findViewById(R.id.user_circle_layout)
            chipName = itemView.findViewById(R.id.family_friends_chip)
        }
    }
}