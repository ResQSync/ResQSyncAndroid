package com.uchi.resqsync.models

data class JoiningCode(
    val userId:String,
    val uniqueCode:String,
) {
    constructor() : this("","")
}