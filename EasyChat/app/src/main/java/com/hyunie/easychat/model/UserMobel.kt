package com.hyunie.easychat.model

import com.google.firebase.Timestamp

class UserMobel {
    var phone : String = ""
        get() = field
        set(value){
            field = value
        }

    var userName : String = ""
        get() = field
        set(value){
            field = value
        }
    var creatdTimestamp : Timestamp = Timestamp.now()
        get() = field
        set(value){
            field = value
        }

    constructor()
    constructor(phone: String, userName: String, creatdTimestamp: Timestamp) {
        this.phone = phone
        this.userName = userName
        this.creatdTimestamp = creatdTimestamp
    }

}