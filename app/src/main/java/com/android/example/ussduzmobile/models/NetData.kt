package com.android.example.ussduzmobile.models

class NetData {
    var payment: String? = null
    var period: String? = null
    var traffic: String? = null
    var minute: String? = null
    var message: String? = null
    var mainName: String? = null
    var name: String? = null
    var toActivate: String? = null

    constructor()
    constructor(
        payment: String?,
        period: String?,
        traffic: String?,
        minute: String?,
        message: String?,
        mainName: String?,
        name: String?,
        toActivate: String?
    ) {
        this.payment = payment
        this.period = period
        this.traffic = traffic
        this.minute = minute
        this.message = message
        this.mainName = mainName
        this.name = name
        this.toActivate = toActivate
    }
}