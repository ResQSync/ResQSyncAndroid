package com.uchi.resqsync.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.uchi.resqsync.adapters.usercirclechip.UserCircleChipModel

import timber.log.Timber


public class FirebaseUtils {

    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().uid
    }

    fun isLoggedIn(): Boolean {
        return currentUserId() != null
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    fun currentUserDetails(): DocumentReference {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId()?:"")
    }

    fun currentUserLocationDetails() : DocumentReference{
        return FirebaseFirestore.getInstance().collection("userLocation").document(currentUserId()?:"")
    }

    fun uniqueCodeDetails() : DocumentReference{
        return FirebaseFirestore.getInstance().collection("uniqueCode").document(currentUserId()?:"")
    }

    fun userCircleDetails(name:String) : DocumentReference{
        return FirebaseFirestore.getInstance().collection("userCircle")
            .document(currentUserId()?:"").collection(name).document(name)
    }

    fun createCollection(name:String){
        val circleCollection: DatabaseReference = FirebaseDatabase.getInstance().getReference(FirebaseUtils().currentUserId()?:"")
        circleCollection.child("userCircleCollection").push().setValue(UserCircleChipModel(name))
    }

    /** This function is a addon as there is no API available in firebase to fetch the names of all
     * the collection in the document like in Nodejs so this is hack to sava the collection names**/
    fun retrieveCollectionNames() : ArrayList<UserCircleChipModel> {
        Timber.d("Fetching circle collection names")
        val helpList = ArrayList<UserCircleChipModel>()
        val circleCollectionRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(currentUserId()?:"")
        val userCircleCollectionRef: DatabaseReference =
            circleCollectionRef.child("userCircleCollection")
        userCircleCollectionRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (collectionSnapshot in dataSnapshot.children) {
                    val collectionName = collectionSnapshot.child("name").getValue(String::class.java)
                    if (collectionName != null) {
                        helpList.add(UserCircleChipModel(collectionName))
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.e("Database Error: ${databaseError.message}")
            }
        })

        return helpList
    }



    fun getUserCircleDetails() : DocumentReference{
        return FirebaseFirestore.getInstance().collection("userCircle")
            .document(currentUserId()?:"")
    }
}