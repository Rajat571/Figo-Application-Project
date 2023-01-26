package com.figgo.cabs.pearllib

import android.content.SharedPreferences

public abstract class SharedPreferenceData :SharedPreferences{
    var userType: String=""
    get() = field
    set(value) {
        field=value
              }

    var isValid: String=""
    get()=field
    set(value) {
        field=value
               }

    var isLogin:String=""
    get() = field
    set(value) {
        field=value
    }

    var partner_name:String=""
    get()=field
    set(value){
        field=value
    }

    var partner_mobile_no:String=""
    get()=field
    set(value){
        field=value
    }

    var partner_pan_no:String=""
        get()=field
        set(value){
            field=value
        }

    var partner_Adhar_no:String=""
        get()=field
        set(value){
            field=value
        }

}