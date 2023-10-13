package com.uchi.resqsync.adapters.emergencycontact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uchi.resqsync.R
import com.uchi.resqsync.models.ContactDetails

class EmergencyContactAdapter(private val contacts:List<ContactDetails>) :
    RecyclerView.Adapter<EmergencyContactAdapter.ContactViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmergencyContactAdapter.ContactViewHolder {
        return ContactViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.emergency_contact_layout_card, parent, false))
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val currentItem = contacts[position]
        holder.contactPhone.text=currentItem.phone
        holder.contactName.text=currentItem.name
        holder.deleteBtn.setOnClickListener {

        }

    }

    inner class ContactViewHolder(contactView : View):RecyclerView.ViewHolder(contactView) {
        val deleteBtn:ImageView
        val contactName:TextView
        val contactPhone:TextView
        init {
            deleteBtn = contactView.findViewById(R.id.delete_image_view)
            contactName= contactView.findViewById(R.id.emergency_contact_name)
            contactPhone = contactView.findViewById(R.id.emergency_contact_phone)
        }

    }
}