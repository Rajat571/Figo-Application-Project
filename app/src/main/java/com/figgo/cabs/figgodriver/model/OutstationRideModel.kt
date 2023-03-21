package com.figgo.cabs.figgodriver.model

data class OutstationRideModel(var date: String,
                          var time:String,
                          var cutomer_name:String,
                          var to:String,
                          var from:String,
                          var fare_price:String,
                          var current_lat:String,
                          var current_long:String,
                          var des_lat:String,
                          var des_long:String,
                          var ride_id:String,
                          var actual_distance:String,
                          var status: String,
                          var y:Int,) {

}