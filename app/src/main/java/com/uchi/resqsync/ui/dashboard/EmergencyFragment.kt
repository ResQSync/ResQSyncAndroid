/*
 *  Copyright (c) 2023 Ashish Yadav <mailtoashish693@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.uchi.resqsync.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.uchi.resqsync.R
import com.uchi.resqsync.adapters.emergencycontact.EmergencyContactAdapter
import com.uchi.resqsync.adapters.emergencycontact.EmergencyContactModel
import com.uchi.resqsync.adapters.usercirclelist.UserCircleListAdapter
import com.uchi.resqsync.adapters.usercirclelist.UserCircleListModel
import com.uchi.resqsync.models.ContactDetails
import com.uchi.resqsync.models.EmergencyContactDataModel
import com.uchi.resqsync.models.UserCircleModel
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.snackbar.BaseSnackbarBuilderProvider
import com.uchi.resqsync.snackbar.SnackbarBuilder
import com.uchi.resqsync.snackbar.showSnackbar
import com.uchi.resqsync.utils.DoubleTapGestureListener
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.Utility

class EmergencyFragment : Fragment(), BaseSnackbarBuilderProvider {

    private lateinit var contactListRecyclerView: RecyclerView
    private lateinit var contactListAdapter: EmergencyContactAdapter
    private lateinit var fabAddContact: FloatingActionButton
    lateinit var actionSoSButton:MaterialCardView
    private lateinit var gestureDetector: GestureDetector

    override val baseSnackbarBuilder: SnackbarBuilder = {
        anchorView = view.findViewById<FloatingActionButton>(R.id.fab_add_contact)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_emergency, container, false)
    }

    // todo: make it right
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactListRecyclerView = view.findViewById(R.id.emergency_contact_recyclerview)
        fabAddContact = view.findViewById(R.id.fab_add_contact)
        actionSoSButton = view.findViewById(R.id.action_sos)

        contactListAdapter = EmergencyContactAdapter(contactList())
            contactListRecyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            contactListRecyclerView.adapter = contactListAdapter

        addContacts()

        primaryContactSoS()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun primaryContactSoS() {
        gestureDetector = GestureDetector(requireContext(), DoubleTapGestureListener(requireContext()) {
            val phone = "tel:${PrefConstant.getPrimarySoSContact(requireContext())}"
            if(PrefConstant.getPrimarySoSContact(requireContext())==null){
                view?.showSnackbar("Please setup your primary contact first")
            }else{
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse(phone)
                startActivity(intent)
            }
        })

        actionSoSButton.setOnTouchListener{_,event->
            gestureDetector.onTouchEvent(event)
            true
        }
    }




    private fun addContacts() {
        fabAddContact.setOnClickListener {
            showAlertDialog()
        }
    }

    fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.add_emergency_contact, null)
        val contactName = view.findViewById<TextInputEditText>(R.id.contact_name)
        val contactPhone = view.findViewById<TextInputEditText>(R.id.contact_phone)
        val dismissButton = view.findViewById<MaterialButton>(R.id.dismiss_contact_dialog_button)
        val addButton = view.findViewById<MaterialButton>(R.id.add_contact_button)
        val primaryContactToggle = view.findViewById<SwitchMaterial>(R.id.primary_contact_switch)
        builder.setView(view)
        builder.setTitle(resources.getString(R.string.add_contact))
        dismissButton.setOnClickListener {
            builder.dismiss()
        }
        addButton.setOnClickListener {
            primaryContactToggle.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked){
                    addContact(contactName.text.toString(),contactPhone.text.toString(),true)
                }else   addContact(contactName.text.toString(),contactPhone.text.toString(),false)
            }

        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }

    private fun addContact(name: String, phone: String, isPrimary: Boolean) {
        val currentContacts = PrefConstant.loadEmergencyContacts(requireContext())
        if (currentContacts == null) {
            val newMember = EmergencyContactDataModel(arrayListOf(ContactDetails(name,phone,isPrimary)))
            PrefConstant.saveEmergencyContacts(requireContext(), newMember)
            FirebaseUtils().emergencyContactsDetails().set(
                newMember
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                }
            }
        } else {
            val presentMembers = PrefConstant.loadEmergencyContacts(requireContext())
            if (isPrimary) {
                PrefConstant.savePrimarySoSContact(requireContext(),phone)
                presentMembers?.contacts?.forEach { contact ->
                    if(contact.primary){
                        contact.primary=true
                    }
                }
            } else {
                presentMembers?.contacts?.add(ContactDetails(name,phone,isPrimary))
                if (presentMembers != null) {
                    FirebaseUtils().emergencyContactsDetails().set(
                        presentMembers
                    ).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            PrefConstant.saveEmergencyContacts(requireContext(),presentMembers)
                        }

                    }
                }
            }
        }
    }

    fun contactList(): ArrayList<ContactDetails> {
        val xe = ArrayList<ContactDetails>()
        val presentMembers = PrefConstant.loadEmergencyContacts(requireContext())
        if(presentMembers!=null){
            for(member in presentMembers.contacts){
                xe.add(ContactDetails(member.name,member.phone,member.primary))
            }
        }
        return xe
    }
}