package com.pearlorganisation

import android.content.Context
import android.content.SharedPreferences




 class PrefManager(var context: Context) {
     // Shared preferences file name
     private val PREF_NAME = "welcome"
     private val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
     private val IS_VALID_LOGIN = "IsValidLogin"
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
         return pref?.getString("name","null").toString()
     }

     fun setMobile_No(mobile_no:String){
         editor?.putString("mobile_no",mobile_no)
         editor?.commit()
     }
     fun getMobileNo():String{
         return pref?.getString("mobile_no","").toString()
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

     fun setAadhar_no(aadhar_no:String){
         editor?.putString("aadhar_no",aadhar_no)
         editor?.commit()
     }

     fun getAadhar_no():String{
         return pref?.getString("aadhar_no","").toString()
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





}