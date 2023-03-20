package com.figgo.cabs.pearllib

 object Helper {

     /**************** Driver Section ******************/

    var baseurl = "https://test.pearl-developer.com/figo/api/"
    var  create_driver = baseurl + "create-driver"
    var new_create_driver = baseurl+"register-user"
    var new_check_OTP = baseurl+"verify-otp"
     var  send_otp = baseurl + "otp/send-otp"
     //var user_type = baseurl+"check-user-type"
     var user_type = baseurl+"set-type"
     var check_otp= baseurl+"otp/check-otp"
     var get_state= baseurl+"get-state"
     var get_city= baseurl+"get-city"
     var f_model= baseurl+"f_model"
     var f_category= baseurl+"f_category"
     var regitser_driver= baseurl+"regitser-driver"
     var create_referel= baseurl+"refer/create-referel"
     var check_status= baseurl+"check-status"
     var customer_booking_list= baseurl+"driver-ride/get-city-ride-request"
     var reject_city_ride_request= baseurl+"driver-ride/reject-city-ride-request"
     var accept_city_ride_request= baseurl+"driver-ride/accept-city-ride-request"
     var check_ride_otp= baseurl+"driver-ride/check-ride-otp"
     var get_all_details= baseurl+"driver/get-all-details"
     var get_cab_work_details= baseurl+"driver/get-cab-work-details"
     var ride_history= baseurl+"driver/ride-history"
     var ride_details = baseurl+"ride/ride-details"
     var ride_complete = baseurl+"ride/ride-complete"
     var active_ride = baseurl+"driver/active-rides"
     var update_details= baseurl+"driver/update-details"
    var update_cab_details= baseurl+"driver/update-cab-details"
     var update_work_area= baseurl+"driver/update-work-details"
     var get_recharge= baseurl+"recharge/get-recharges"
     var select_payment= baseurl+"recharge/select-payment"
     var recharge_history= baseurl+"recharge/recharge-history"
    var outstation_oneway_request= baseurl+"Oneway/getRequest"
     var get_user_recharge= baseurl+"recharge/get-user-recharge"
var all_transactions = baseurl+"wallet/get-transaction-history"
    var accept_advance_ride_request= baseurl+"advance-ride/accept-advance-ride-request"

     /**************** Partner Section ******************/
     var register_partner = baseurl+"partner/register_partner"
     var create_driverbypartner = baseurl+"partner/create-driver-by-partner"
     var partner_getDrivers = baseurl+"partner/get-drivers"

}