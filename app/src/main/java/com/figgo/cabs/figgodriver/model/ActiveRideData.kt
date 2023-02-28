package com.figgo.cabs.figgodriver.model

data class ActiveRideData(
    var booking_id:String,
    var type:String,
    var to_location_lat:String,
    var to_location_long:String,
    var to_name:String,
    var from_location_lat:String,
    var from_location_long:String,
    var from_name:String,
    var date_only:String,
    var time_only:String,
    var price:String,
    var ride_id:String

)