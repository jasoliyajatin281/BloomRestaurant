package com.`in`.codeh.bloomrestaurantvendor.model

import java.sql.Timestamp

class ReservationModel(var reservationID: String = "", var name: String = "", var quantity: Int = 0, var timestamp: com.google.firebase.Timestamp? = null,
                       var email: String = ""
                       , var date: String = "",
                       var time: String = "",
                       var confirmation: String = "",
                       var userUID: String = "",
                       var reservationNumber: Int = 0) {

}