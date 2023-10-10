package com.uchi.resqsync.ui.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.uchi.resqsync.R
import com.uchi.resqsync.adapters.usercirclechip.UserCircleChipAdapter
import com.uchi.resqsync.adapters.usercirclelist.UserCircleListAdapter
import com.uchi.resqsync.adapters.usercirclelist.UserCircleListModel
import com.uchi.resqsync.models.JoiningCode
import com.uchi.resqsync.models.UserCircleModel
import com.uchi.resqsync.models.UserModel
import com.uchi.resqsync.snackbar.showSnackbar
import com.uchi.resqsync.ui.dialog.CreateCircleDialog
import com.uchi.resqsync.utils.FirebaseUtils
import com.uchi.resqsync.utils.PrefConstant
import com.uchi.resqsync.utils.UIUtils
import timber.log.Timber


class FamilyFriendsFragment : Fragment() {
    lateinit var addCircleButton: Chip

    private lateinit var addMembersButton: MaterialButton
    private lateinit var memberCode: TextInputEditText
    private lateinit var membersListRecyclerView: RecyclerView
    private lateinit var membersListAdapter: UserCircleListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_family_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        membersListRecyclerView=view.findViewById(R.id.user_circle_list_recyclerview)

        //list of members
        membersListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        membersListAdapter = UserCircleListAdapter(familyList())
        membersListRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        membersListRecyclerView.adapter = membersListAdapter

        //TODO: make these avialable after first release
        //chips for circle
        val recyclerView = view.findViewById<RecyclerView>(R.id.user_circle_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UserCircleChipAdapter(FirebaseUtils().retrieveCollectionNames())
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView?.adapter = adapter

        memberCode = view.findViewById(R.id.member_code_input_text)
        addCircleButton = view.findViewById(R.id.chip_button_add_circle)
        addCircleButton.setOnClickListener {
            val dialog = CreateCircleDialog()
            dialog.show(parentFragmentManager, "CreateCircleDialog")
        }

        addMembersButton = view.findViewById(R.id.add_members_button)
        addFamilyMember()

    }

    private fun addFamilyMember() {
        addMembersButton.setOnClickListener {
            if (memberCode.text.toString().isNotEmpty()) {
                val memberJoiningCode = memberCode.text.toString()
                var memberUid = ""
                FirebaseUtils().getUniqueCodeDetails()
                    .whereEqualTo("uniqueCode", memberJoiningCode).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {
                                memberUid = document.toObject(JoiningCode::class.java).userId
                            }
                            getMemberAndAdd(memberUid)

                        }
                    }
            }
        }
    }


    private fun getMemberAndAdd(memberUid: String) {
        Timber.d("Adding member to the circle")
        if (memberUid.isNotEmpty()) {
            FirebaseUtils().getUserDetails().whereEqualTo("userId", memberUid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            val details = document.toObject(UserModel::class.java)
                            val presentMembers = PrefConstant.getMembersDetails(requireContext(),"family")
                            if(presentMembers!=null){
                                presentMembers.familyMembers.add(UserModel(details.name,details.email,details.userId))
                                FirebaseUtils().userCircleDetails("family")
                                    .set(presentMembers)
                                    .addOnCompleteListener {
                                        membersListAdapter.notifyItemInserted(presentMembers.familyMembers.size - 1)
                                        PrefConstant.saveMembersDetails(requireContext(),"family",presentMembers)
                                        showSnackbar("Added ${details.name} as your family member")
                                    }
                            }else{
                                val newMember = UserCircleModel(arrayListOf(UserModel(details.name,details.email,details.userId)))
                                FirebaseUtils().userCircleDetails("family")
                                    .set(newMember)
                                    .addOnCompleteListener {
                                        membersListAdapter.notifyItemInserted(0)
                                        PrefConstant.saveMembersDetails(requireContext(),"family",newMember)
                                        showSnackbar("Added ${details.name} as your family member")
                                    }
                            }

                        }
                    }
                }
        }

    }

    fun familyList(): ArrayList<UserCircleListModel> {
        val xe = ArrayList<UserCircleListModel>()
        val presentMembers = PrefConstant.getMembersDetails(requireContext(),"family")
        if(presentMembers!=null){
            for(member in presentMembers.familyMembers){
                    xe.add(UserCircleListModel(member.name,member.email))
            }
        }
        return xe
    }

}