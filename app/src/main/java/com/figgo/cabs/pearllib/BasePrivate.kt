package com.figgo.cabs.pearllib

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.figgodriver.Adapter.SpinnerAdapter
import com.figgo.cabs.figgodriver.UI.LoginActivity
import com.figgo.cabs.figgodriver.model.SpinnerObj
import org.json.JSONObject

abstract class BasePrivate : BaseClass() {
    var minteger = 0
    val REQUEST_LOCATION = 1
    var locationManager: LocationManager? = null
    var latitude: String? = null
    var longitude: String? = null
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    var bestProvider: String? = null
    var criteria: Criteria? = null
    var coin_balance: TextView? = null
    var shiprocketToken: String? = null
    lateinit var prefManager: PrefManager
    fun CheckSession(baseApbcContext: Context?, activityIn: AppCompatActivity) {
        session = Session(baseApbcContext!!)
        prefManager= PrefManager(baseApbcContext)
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

    fun fetchStates(
        baseApbcContext: Context?, spinner: Spinner, loc: Int,
        spinner2: Spinner,
        outstationStateHashMap: ArrayList<String>
    ): ArrayList<String> {
        outstationStateHashMap.clear()
        prefManager= PrefManager(baseApbcContext!!)
        var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
        var state_id:Int = 0
        val URL = " https://test.pearl-developer.com/figo/api/get-state"
        val queue = Volley.newRequestQueue(baseApbcContext)
        val json = JSONObject()

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
                            statelist.add(SpinnerObj(name,id))
                            hashMap.put(name,id.toInt())
                        }
                        //spinner
                        val stateadapter = SpinnerAdapter(baseApbcContext,statelist)
                        // val stateadapter =  ArrayAdapter(baseApbcContext!!,R.layout.simple_spinner_item,hashMap.keys.toList());
                        //stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.setAdapter(stateadapter)
                        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                                if(position !=0){
                                    state_id = statelist.get(position).id.toInt()
                                    //    prefManager.setdriverWorkState(state_id)
                                    Log.d("data","State_id===="+state_id+loc)
                                    outstationStateHashMap.add(state_id.toString())
                                    // outstationStateHashMap.put("state"+loc,state_id)
                                }


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


        return outstationStateHashMap
    }

    private fun fetchCity(baseApbcContext: Context?,spinnerCity: Spinner,id: Int) {
        prefManager= PrefManager(baseApbcContext!!)
        var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
        val URL = " https://test.pearl-developer.com/figo/api/get-city"
        val queue = Volley.newRequestQueue(baseApbcContext)
        val json = JSONObject()
        //    var token= prefManager.getToken()

        json.put("state_id",id)

        Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                Log.d("SendData", "response===" + response)
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){
                        val jsonArray = response.getJSONArray("cities")
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            cityhashMap.put(name,id.toInt())


                        }
                        //spinner
                        val stateadapter = baseApbcContext?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item,cityhashMap.keys.toList()) };
                        // val stateadapter = com.pearl.figgodriver.Adapter.SpinnerAdapter( requireContext(),android.R.layout.simple_spinner_dropdown_item, statehashMap.keys.toList())
                        if (stateadapter != null) {
                            stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                        spinnerCity.setAdapter(stateadapter)
                        spinnerCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                                val id = cityhashMap.values.toList()[position]
                                prefManager.setdriverWorkCity(id)
                                Log.d("SendData", "cityid===" + id)
                                //  fetchCity(id)
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapter: AdapterView<*>?) {
                                //  (binding.spinnerState.getChildAt(0) as TextView).text = "Select Category"
                            }
                        })




                    }else{

                    }


                    Log.d("SendData", "json===" + json)


                }
                // Get your json response and convert it to whatever you want.
            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)

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



    fun ErrorProgressDialog(context:Context,id:String,message:String){
        val alertDialog2 = AlertDialog.Builder(
            context
        )
        alertDialog2.setTitle("Error : "+id)
        alertDialog2.setMessage(message)
        alertDialog2.setPositiveButton(
            "Yes"
        ) { dialog: DialogInterface?, which: Int ->
            finishAffinity()
            System.exit(0)
        }
        alertDialog2.setNegativeButton(
            "Cancel"
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        alertDialog2.show()
        //       finish();
    }


}