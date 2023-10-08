package com.uchi.resqsync.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.uchi.resqsync.R
import com.uchi.resqsync.adapters.usercirclechip.UserCircleChipAdapter
import com.uchi.resqsync.adapters.usercirclechip.UserCircleChipModel
import com.uchi.resqsync.ui.dialog.CreateCircleDialog
import com.uchi.resqsync.ui.dialog.onDialogFinishCallback
import com.uchi.resqsync.utils.FirebaseUtils
import timber.log.Timber


class FamilyFriendsFragment : Fragment() {
    lateinit var addCircleButton:Chip

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_family_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.user_circle_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UserCircleChipAdapter(FirebaseUtils().retrieveCollectionNames())
        recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView?.adapter = adapter

        addCircleButton = view.findViewById(R.id.chip_button_add_circle)
        addCircleButton.setOnClickListener {
            val dialog = CreateCircleDialog()
            dialog.show(parentFragmentManager,"CreateCircleDialog")
        }

    }

}