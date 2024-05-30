package com.hyunie.easychat.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {
    companion object{
        fun currentUserId(): String{
            return FirebaseAuth.getInstance().currentUser?.uid.toString()
        }
        fun currentUserDetails(): DocumentReference{
            return FirebaseFirestore.getInstance().collection("users").document(currentUserId())
        }
    }
}