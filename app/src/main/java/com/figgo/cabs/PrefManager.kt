package com.figgo.cabs

import android.content.Context
import android.content.SharedPreferences

 class PrefManager(var context: Context) {
     // Shared preferences file name
     private val PREF_NAME = "welcome"
     private val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
     private val IS_VALID_LOGIN = "IsValidLogin"
     var updatedStateList = java.util.ArrayList<String>()
     // shared pref mode
     var PRIVATE_MODE = 0
    var pref: SharedPreferences? = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref!!.edit()
    var _context: Context? = null





 /*   fun PrefManager(context: Context?) {
        pref = _context
        editor =
    }*/

     fun setMpin(mpin:String){
         editor?.putString("mpin",mpin)
         editor?.commit()
     }
     fun getMpin():String{
         return pref?.getString("mpin","null").toString()
     }
    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor!!.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor!!.commit()
    }



    fun isFirstTimeLaunch(): Boolean {
        return pref!!.getBoolean(IS_FIRST_TIME_LAUNCH, true)
    }
     fun setisValidLogin(isFirstTime: Boolean) {
         editor!!.putBoolean(IS_VALID_LOGIN, isFirstTime)
         editor!!.commit()
     }

     fun isValidLogin(): Boolean {
         return pref!!.getBoolean(IS_VALID_LOGIN, false)
     }
     fun setToken(token:String){
         editor?.putString("token",token)
         editor?.commit()
     }
     fun getToken():String{
         return pref?.getString("token","null").toString()
     }
     fun setUserId(userid:String){
         editor?.putString("userid",userid)
         editor?.commit()
     }
     fun setCabFormToken(formID:String){
         editor?.putString("formID",formID)
         editor?.commit()
     }
     fun getCabFormToken():String{
         return pref?.getString("formID","").toString()
     }

     fun getUserId():String{
         return pref?.getString("userid","").toString()
     }
     fun setDriverName(name:String){
         editor?.putString("name",name)
         editor?.commit()
     }
     fun getDriverName():String{
         return pref?.getString("name","").toString()
     }

     fun setMobile_No(mobile_no:String){
         editor?.putString("mobile_no",mobile_no)
         editor?.commit()
     }
     fun getMobileNo():String{
         return pref?.getString("mobile_no","").toString()
     }

     fun setemail(email:String){
         editor?.putString("email",email)
         editor?.commit()
     }
     fun getemail():String{
         return pref?.getString("email","").toString()
     }
     fun setDashboard(dash:String){
         editor?.putString("dash",dash)
         editor?.commit()
     }
     fun getDashboard():String{
         return pref?.getString("dash","").toString()
     }

     fun setDL_No(DL_no:String){
         editor?.putString("DL_no",DL_no)
         editor?.commit()
     }

     fun getDL_No():String{
         return pref?.getString("DL_no","").toString()
     }

     fun setPolice_verification(police_ver:String){
         editor?.putString("police_ver",police_ver)
         editor?.commit()
     }

     fun getPolice_verification():String{
         return pref?.getString("police_ver","").toString()
     }
     fun setPolice_ext(police_ver:String){
         editor?.putString("police_ext",police_ver)
         editor?.commit()
     }

     fun getPolice_ext():String{
         return pref?.getString("police_ext","").toString()
     }

     fun getReferal():String{
         return pref?.getString("referal_code","").toString()
     }
    fun setReferal(ref:String){
        editor?.putString("referal_code",ref).toString()
        editor?.commit()
    }

     fun setDriverAadhar_no(aadhar_no:String){
         editor?.putString("aadhar_no",aadhar_no)
         editor?.commit()
     }

     fun getDriverAadhar_no():String{
         return pref?.getString("aadhar_no","").toString()
     }
     fun setDriverPan_no(pan_no:String){
         editor?.putString("pan_no",pan_no)
         editor?.commit()
     }

     fun getDriverPan_no():String{
         return pref?.getString("pan_no","").toString()
     }
     fun setAadhar_verification_front(aadhar_verification_front:String){
         editor?.putString("aadhar_verification_front",aadhar_verification_front)
         editor?.commit()
     }
     fun getAadhar_verification_front():String{
         return pref?.getString("aadhar_verification_front","").toString()
     }
     fun setAadhar_front_ext(aadhar_verification_front:String){
         editor?.putString("aadhar_front_ext",aadhar_verification_front)
         editor?.commit()
     }
     fun getAadhar_front_ext():String{
         return pref?.getString("aadhar_front_ext","").toString()
     }
     fun setAadhar_verification_back(aadhar_verification_back:String){
         editor?.putString("aadhar_verification_back",aadhar_verification_back)
         editor?.commit()
     }

     fun getAadhar_verification_back():String{
         return pref?.getString("aadhar_verification_back","").toString()
     }

     fun setAadhar_back_ext(aadhar_verification_back:String){
         editor?.putString("aadhar_back_ext",aadhar_verification_back)
         editor?.commit()
     }

     fun getAadhar_back_ext():String{
         return pref?.getString("aadhar_back_ext","").toString()
     }


     fun setDriverProfile(DriverProfile:String){
         editor?.putString("DriverProfile",DriverProfile)
         editor?.commit()
     }

     fun getDriverProfile():String{
         return pref?.getString("DriverProfile","").toString()
     }

     fun setDriverProfile_ext(DriverProfile:String){
         editor?.putString("DriverProfile_ext",DriverProfile)
         editor?.commit()
     }

     fun getDriverProfile_ext():String{
         return pref?.getString("DriverProfile_ext","").toString()
     }

     fun setDriverCab(DriverProfile:String){
         editor?.putString("DriverCab",DriverProfile)
         editor?.commit()
     }

     fun getDriverCab():String{
         return pref?.getString("DriverCab","").toString()
     }

     fun setDriverCab_ext(DriverProfile:String){
         editor?.putString("DriverCab_ext",DriverProfile)
         editor?.commit()
     }

     fun getDriverCab_ext():String{
         return pref?.getString("DriverCab_ext","").toString()
     }

     fun setDriveCity(DriverCity:Int){
         editor?.putInt("DriverCity",DriverCity)
         editor?.commit()
     }

     fun getDriverCity():Int{
         return pref?.getInt("DriverCity",0)!!
     }
     fun setDriveState(DriverState:Int){
         editor?.putInt("DriverState",DriverState)
         editor?.commit()
     }

     fun getDriverState():Int{
         return pref?.getInt("DriverState",0)!!
     }
     fun setDriverVechleType(VechleType:String){
         editor?.putString("VechleType",VechleType)
         editor?.commit()
     }

     fun getDriverVechleType():String{
         return pref?.getString("VechleType","")!!
     }
     fun setDriverCabCategory(cab_category:String){
         editor?.putString("cab_category",cab_category)
         editor?.commit()
     }

     fun getDriverCabCategory():String{
         return pref?.getString("cab_category","")!!
     }
     fun setDriverVechleModel(vechle_model:Int){
         editor?.putInt("vechle_model",vechle_model)
         editor?.commit()
     }

     fun getDriverVechleModel():Int{
         return pref?.getInt("vechle_model",0)!!
     }
     fun setDriverVechleYear(vechle_year:Int){
         editor?.putInt("vechle_year",vechle_year)
         editor?.commit()
     }

     fun getDriverVechleYear():Int{
         return pref?.getInt("vechle_year",0)!!
     }

     fun setDriverRegistrationNo(registration_no:String){
         editor?.putString("registration_no",registration_no)
         editor?.commit()
     }

     fun getDriverRegistrationNo():String{
         return pref?.getString("registration_no","")!!
     }
     fun setDriverInsuranceDate(insurance_date:String){
         editor?.putString("insurance_date",insurance_date)
         editor?.commit()
     }

     fun getDriverInsuranceDate():String{
         return pref?.getString("insurance_date","")!!
     }
     fun setDriverTaxiPermitDate(permit_valid_date:String){
         editor?.putString("permit_valid_date",permit_valid_date)
         editor?.commit()
     }

     fun getDriverTaxiPermitDate():String{
         return pref?.getString("permit_valid_date","")!!
     }

     fun getRegistrationToken():String{
         return  pref?.getString("RegistrationW","").toString()
     }
     fun setRegistrationToken(reg:String){
         editor?.putString("RegistrationW",reg)
         editor?.commit()
     }
     fun getdriverWorkState():Int{
         return  pref?.getInt("work_city",0)!!
     }
     fun setdriverWorkState(work_state:Int){
         editor?.putInt("work_state",work_state)
         editor?.commit()
     }
     fun getdriverWorkCity():Int{
         return  pref?.getInt("work_city",0)!!
     }
     fun setdriverWorkCity(work_city:Int){
         editor?.putInt("work_state",work_city)
         editor?.commit()
     }

     fun setlatitude(lat:Float){
         editor?.putFloat("lat",lat)
         editor?.commit()
     }
     fun getlatitude():Float{
         return  pref?.getFloat("lat",0.0f)!!
     }
     fun setlongitude(long:Float){
         editor?.putFloat("long",long)
         editor?.commit()
     }
     fun getlongitude():Float{
         return  pref?.getFloat("long",0.0f)!!
     }

     fun setActiveRide(active_ride:Int){
         editor?.putInt("active_ride",active_ride)
         editor?.commit()
     }
     fun getActiveRide():Int{
         return  pref?.getInt("active_ride",0)!!
     }
     fun setAccountDetails(email:String,name:String,photoURL:String){
         editor?.putString("EmailID",email)
         editor?.putString("EmailName",name)
         editor?.putString("PhotoUrl",photoURL)
         editor?.commit()
     }
     fun getAccountMail():String{
         return pref?.getString("EmailID","")!!
     }
     fun getAccountName():String{
         return pref?.getString("EmailName","")!!
     }
     fun getAccountPhotoURL():String{
         return pref?.getString("PhotoUrl","")!!
     }

     fun getRideID():Int{
         return pref?.getInt("RideID",0)!!
     }
     fun setRideID(rideID:Int){
         editor?.putInt("RideID",rideID)
         editor?.commit()
     }
     fun setCustomerlocation(lat:Float,lon:Float){
         editor?.putFloat("CLat",lat)
         editor?.putFloat("CLon",lon)
         editor?.commit()
     }
     fun getCustLat():Float{
         return pref?.getFloat("CLat",0.0f)!!
     }
     fun getCustLon():Float{
         return pref?.getFloat("CLon",0.0f)!!
     }

     fun setUserType(user:String){
         editor?.putString("UserType",user)
         editor?.commit()
     }
     fun getUserType():String{
         return pref?.getString("UserType","")!!
     }
     fun setdestinationlocation(lat:Float,lon:Float){
         editor?.putFloat("DestLat",lat)
         editor?.putFloat("DestLon",lon)
         editor?.commit()
     }
     fun getDestLat():Float{
         return pref?.getFloat("DestLat",0.0f)!!
     }
     fun getDestLon():Float{
         return pref?.getFloat("DestLon",0.0f)!!
     }
     fun setridehistoryCount(count:Int){
         editor?.putInt("RideHistoryCount",count)
     }
     fun getridehistoryCount():Int{
         return pref?.getInt("RideHistoryCount",0)!!
     }

     fun setOutstationlist(id:Int,num:Int){
         editor?.putInt("State$num",id)
         editor?.commit()
     }
     fun getOutstationlist():ArrayList<String>{
         for(i in 0 until 5){
             updatedStateList.add(pref?.getInt("State$i",0)!!.toString())
         }
         return updatedStateList
     }



     fun setToLatL(lat:String){
         editor?.putString("lat_to_l",lat)
         editor?.commit()
     }

     fun getToLatL():String{
         return pref?.getString("lat_to_l","null").toString()
     }
     fun setToLngL(lat:String){
         editor?.putString("lng_to_l",lat)
         editor?.commit()
     }

     fun getToLngL():String{
         return pref?.getString("lng_to_l","null").toString()
     }
     fun setToLngM(lat:String){
         editor?.putString("lng_to_m",lat)
         editor?.commit()
     }

     fun getToLngM():String{
         return pref?.getString("lng_to_m","null").toString()
     }
     fun setToLatM(lat:String){
         editor?.putString("lat_to_m",lat)
         editor?.commit()
     }

     fun getToLatM():String{
         return pref?.getString("lat_to_m","null").toString()
     }


     fun setType(type:String){
         editor?.putString("type",type)
         editor?.commit()
     }

     fun getType():String{
         return pref?.getString("type","null").toString()
     }
     fun setTypeC(type:String){
         editor?.putString("typeC",type)
         editor?.commit()
     }

     fun getTypeC():String{
         return pref?.getString("typeC","null").toString()
     }

     fun setToLatMC(lat:String){
         editor?.putString("lat_to_mc",lat)
         editor?.commit()
     }

     fun getToLatMC():String{
         return pref?.getString("lat_to_mc","null").toString()
     }

     fun setToLngMC(lat:String){
         editor?.putString("lng_to_mc",lat)
         editor?.commit()
     }
     fun setToLatLC(lat:String){
         editor?.putString("lat_to_lc",lat)
         editor?.commit()
     }

     fun getToLatLC():String{
         return pref?.getString("lat_to_lc","null").toString()
     }

     fun setToLngLC(lat:String){
         editor?.putString("lng_to_lc",lat)
         editor?.commit()
     }
     fun getToLngLC():String{
         return pref?.getString("lng_to_lc","null").toString()
     }
     fun setCheckLng(lng:String){
         editor?.putString("check_lng",lng)
         editor?.commit()
     }

     fun getCheckLng(): String {

         return pref?.getString("check_lng","null").toString()
     }

     fun setCheckLat(lat:String){
         editor?.putString("check_lat",lat)
         editor?.commit()
     }

     fun getCheckLat(): String {

         return pref?.getString("check_lat","null").toString()
     }
     fun setServiceStatus(status:Boolean){
         editor?.putBoolean("service",status)
         editor?.commit()
     }
     fun isServiceRunning():Boolean{
         return pref?.getBoolean("service",true)!!
     }
 }