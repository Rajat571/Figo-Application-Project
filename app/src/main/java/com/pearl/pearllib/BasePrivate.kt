package com.pearl.pearllib

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Criteria
import android.location.LocationManager
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pearl.figgodriver.LoginActivity
import org.json.JSONObject

abstract class BasePrivate : BaseClass() {
    var minteger = 0
    val REQUEST_LOCATION = 1
    var locationManager: LocationManager? = null
    var latitude: String? = null
    var longitude: String? = null
    var bestProvider: String? = null
    var criteria: Criteria? = null
    var coin_balance: TextView? = null
    var shiprocketToken: String? = null
    fun CheckSession(baseApbcContext: Context?, activityIn: AppCompatActivity) {
        session = Session(baseApbcContext!!)
        val token = session!!.token
        var session_available = false
        if (session!!.hasSession!!) {
            if (!token!!.isEmpty()) {
                session_available = true
                printLogs(
                    "BasePublic ",
                    "CheckSession ",
                    "is_session_available - $session_available"
                )
            } else {
                session_available = false
                printLogs(
                    "BasePublic ",
                    "CheckSession ",
                    "is_session_available - $session_available"
                )
            }
        } else {
            session_available = false
            printLogs("BasePublic ", "CheckSession ", "is_session_available - $session_available")
        }
        if (!session_available) {
            val intent1 = Intent(baseApbcContext, LoginActivity::class.java)
            startActivity(intent1)
            activityIn.finish()
        }
    }


    fun fetchStates(baseApbcContext: Context?,spinner: Spinner): Int {
        var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
        var state_id:Int = 0
       // statehashMap.clear()
        val URL = " https://test.pearl-developer.com/figo/api/get-state"
        val queue = Volley.newRequestQueue(baseApbcContext)
        val json = JSONObject()
       // var token= prefManager.getToken()
        json.put("country_id","101")

        Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                Log.d("SendData", "response===" + response)
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){
                        val jsonArray = response.getJSONArray("states")
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            hashMap.put(name,id.toInt())


                        }
                        //spinner
                        val stateadapter =  ArrayAdapter(baseApbcContext!!,R.layout.simple_spinner_item,hashMap.keys.toList());
                        // val stateadapter = com.pearl.figgodriver.Adapter.SpinnerAdapter( requireContext(),android.R.layout.simple_spinner_dropdown_item, statehashMap.keys.toList())
                        stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.setAdapter(stateadapter)
                        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                                state_id = hashMap.values.toList()[position]

                             //   fetchCity(id)

                            }

                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapter: AdapterView<*>?) {
                                //  (binding.spinnerState.getChildAt(0) as TextView).text = "Select Category"
                            }
                        })
                    }else{
                        Toast.makeText(baseApbcContext,"Something Went Wrong !!",Toast.LENGTH_SHORT).show()
                    }
                }
                // Get your json response and convert it to whatever you want.
            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)


        return state_id

    }


    /*fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }*/


}