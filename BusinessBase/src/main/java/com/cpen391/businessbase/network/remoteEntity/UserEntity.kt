package com.cpen391.businessbase.network.remoteEntity

import com.cpen391.businessbase.network.remoteDatabase.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

object UserEntity {
    val mUserRef: DatabaseReference = FirebaseDatabase.mDatabase.getReference("User")
}