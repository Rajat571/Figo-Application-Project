package com.figgo.cabs.pearllib

import android.content.Context
import android.content.SharedPreferences

class Session( val context: Context) {
    private val prefs: SharedPreferences
    var editor: SharedPreferences.Editor

    init {
        prefs = context.getSharedPreferences(Transofy, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    var hasSession: Boolean?
        get() = prefs.getBoolean(HAS_SESSION, false)
        set(value) {
            val edits = prefs.edit()
            edits.putBoolean(HAS_SESSION, value!!)
            edits.apply()
        }

    fun getData(id: String?): String? {
        return prefs.getString(id, "")
    }

    fun setData(id: String?, `val`: String?) {
        val edits = prefs.edit()
        edits.putString(id, `val`)
        edits.apply()
    }

    var token: String?
        get() = prefs.getString(U_TOKEN, null)
        set(value) {
            val edits = prefs.edit()
            edits.putString(U_TOKEN, value)
            edits.apply()
        }
    var isUpdateRequired: Boolean?
        get() = prefs.getBoolean(IS_UPDATED, false)
        set(value) {
            val edits = prefs.edit()
            edits.putBoolean(IS_UPDATED, value!!)
            edits.apply()
        }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    companion object {
        const val Transofy = "transofy"
        const val U_TOKEN = "token"
        const val U_ID = "user_id"
        const val BUSINESS_ID = "insert_id"
        const val U_Name = "user_name"
        const val U_Email = "user_email"
        const val U_Mobile = "user_mobile"
        const val U_Gender = "user_gender"
        const val U_DOB = "user_dob"
        const val U_Profile_Pic = "profile_picture"
        const val U_ADDRESS = "address"
        const val U_LANDMARK = "land_mark"
        const val U_PINCODE = "pin_code"
        const val U_reference_code = "user_refrence_id"
        const val HAS_SESSION = "has_session"
        const val IS_UPDATED = "is_updated"
        const val U_Role_type = "user_role_type"
        const val NewVersion = "newversion"
        const val businessName = "business"
        const val businessNumber = "phone"
        const val businessEmail = "email"
        const val businessAddress1 = "address1"
        const val businessAddress2 = "address2"
        const val businessPin = "pin_code"
        const val businessCityId = "businessCity"
        const val businessStateId = "businessState"
        const val businessStateName = "businessState"
        const val businessCityName = "businessState"
        const val isMerchant = "isMerchant"
        const val cartValue = "cartValue"
        const val totalCost = "totalCost"
        const val cartSize = "cartSize"
        const val priceList = "priceList"
        const val favStatus = "favStatus"
        const val shiprocketToken = "shiprocketToken"
    }
}