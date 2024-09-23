package com.revoio.kinfly.presentation.auth

data class UserData (
    val userId : String,
    val email : String,
    val phoneNumber : String,
    val userName : String,
    val contacts: List<String>,
)