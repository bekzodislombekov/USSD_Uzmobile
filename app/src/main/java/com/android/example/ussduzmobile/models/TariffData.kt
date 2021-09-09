package com.android.example.ussduzmobile.models

import java.io.Serializable

class TariffData : Serializable {
    var mainName: String? = null
    var name: String? = null
    var text: String? = null
    var minute: String? = null
    var traffic: String? = null
    var message: String? = null
    var payment: String? = null
    var toActivate: String? = null

    constructor()
    constructor(
        mainName: String?,
        name: String?,
        text: String?,
        minute: String?,
        traffic: String?,
        message: String?,
        payment: String?,
        toActivate: String?
    ) {
        this.mainName = mainName
        this.name = name
        this.text = text
        this.minute = minute
        this.traffic = traffic
        this.message = message
        this.payment = payment
        this.toActivate = toActivate
    }
}