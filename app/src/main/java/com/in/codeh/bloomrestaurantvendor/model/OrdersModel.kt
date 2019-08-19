package com.`in`.codeh.bloomrestaurantvendor.model

import com.google.firebase.Timestamp

class OrdersModel(var orderID: String = "",var date: Timestamp? = null, var total: Int = 0, var userUID: String= ""
, var pickupTime: String = "",var orderNumber: Int = 0){}