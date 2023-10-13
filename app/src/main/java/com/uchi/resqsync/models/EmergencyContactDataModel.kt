package com.uchi.resqsync.models

data class EmergencyContactDataModel(
    val contacts : ArrayList<ContactDetails>
    ){
    constructor(): this(ArrayList())
}

data class ContactDetails(
    val name: String,
    val phone: String,
    var primary: Boolean
)


