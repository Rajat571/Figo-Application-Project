//  The MIT License (MIT)
//  Copyright (c) 2018 Intuz Solutions Pvt Ltd.
//  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
//  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify,
//  merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
//  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.figgo.cabs.R

object MapUtility {
    const val MAP_URL = "https://maps.googleapis.com"
    var apiKey = "AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU"
    var currentLocation: Location? = null
    var popupWindow: Dialog? = null
    var ADDRESS = "address"
    var LATITUDE = "lat"
    var LONGITUDE = "long"
    val paramMap = HashMap<Int, HashMap<String,String>>()
    fun isNetworkAvailable(context: Context): Boolean {
        var activeNetworkInfo: NetworkInfo? = null
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        } catch (ex: Exception) {
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showToast(context: Context?, message: String?) {
        try {
/*            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()*/
        } catch (ex: Exception) {
        }
    }

    fun showProgress(context: Context) {
        try {
            if (!(context as Activity).isFinishing) {
                val layout: View =
                    LayoutInflater.from(context).inflate(R.layout.pop_loading, null)
                popupWindow = Dialog(context, R.style.Theme_FiggoDriver)
                popupWindow!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                popupWindow!!.setContentView(layout)
                popupWindow!!.setCancelable(false)
                if (!context.isFinishing) {
                    popupWindow!!.show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgress() {
        try {
            if (popupWindow != null && popupWindow!!.isShowing) {
                popupWindow!!.dismiss()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }



}