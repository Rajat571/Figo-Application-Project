package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentCityRideBinding
import com.figgo.cabs.figgodriver.Adapter.CityRideAdvanceListAdapter
import com.figgo.cabs.figgodriver.Adapter.CityRideCurrentListAdapter
import com.figgo.cabs.figgodriver.model.CityAdvanceRideList
import com.figgo.cabs.figgodriver.model.CityCurrentRidesList
import com.figgo.cabs.pearllib.Helper
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import java.io.File
import java.util.Collections


class CityRideFragment : Fragment() {
    lateinit var binding: FragmentCityRideBinding
    lateinit var cityRideCurrentListAdapter: CityRideCurrentListAdapter
    lateinit var cityRideAdvanceListAdapter: CityRideAdvanceListAdapter
    lateinit var progressBar: ProgressBar
    lateinit var relativeLayout_data:RelativeLayout
    var ridelists=ArrayList<CityCurrentRidesList>()
    var advanceRidelists=ArrayList<CityAdvanceRideList>()
    lateinit var prefManager: PrefManager
     lateinit var currentdata: CityCurrentRidesList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_city_ride, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager= PrefManager(requireContext())
        progressBar=view.findViewById<ProgressBar>(R.id.city_ride_progressbar)
        relativeLayout_data = view.findViewById<RelativeLayout>(R.id.city_ride_relative_layout)
        //ridelists.add()
        //submitCurrentRideForm(view,"advance")
            submitAdvanceRideForm(view)
            submitCurrentRideForm(view)
        progressBar.visibility=View.VISIBLE
        relativeLayout_data.visibility=View.GONE
            binding.cityRideAdvanceRecylerview.layoutManager = LinearLayoutManager(requireContext())
            binding.cityRideCurrentRecylerview.layoutManager = LinearLayoutManager(requireContext())

      /*  ridelists.add(CityCurrentRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100","","","","","","",0))
        ridelists.add(CityCurrentRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100","","","","","","",0))
        ridelists.add(CityCurrentRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100","","","","","","",0))
        ridelists.add(CityCurrentRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100","","","","","","",0))
        ridelists.add(CityCurrentRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100","","","","","","",0))
        cityRideCurrentListAdapter= CityRideCurrentListAdapter(requireContext(),ridelists)
        binding.cityRideCurrentRecylerview.adapter=cityRideCurrentListAdapter*/


       /* binding.current.setOnClickListener {
            binding.current.setBackgroundResource(R.drawable.change_background)
            binding.current.setTextColor(Color.WHITE)
            binding.advance.setTextColor(Color.BLACK)
            binding.advance.setBackgroundResource(R.drawable.background_card)
            binding.cityRideCurrentRecylerview.visibility=View.VISIBLE
            binding.cityRideAdvanceRecylerview.visibility=View.GONE
        }

        binding.advance.setOnClickListener {
            binding.advance.setTextColor(Color.WHITE)
            binding.current.setTextColor(Color.BLACK)
            binding.advance.setBackgroundResource(R.drawable.change_background)
            binding.current.setBackgroundResource(R.drawable.background_card)
            binding.cityRideCurrentRecylerview.visibility=View.GONE
            binding.cityRideAdvanceRecylerview.visibility=View.VISIBLE
        }*/

    }

    private fun submitCurrentRideForm(view: View){
       // val URL = "https://test.pearl-developer.com/figo/api/driver-ride/get-city-ride-request"
        var URL=Helper.customer_booking_list
        Log.d("CityRideFragment", "CITY_RIDE_FRAGMENT Current===" + URL)
        val queue = Volley.newRequestQueue(requireContext())

        val jsonOblect: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, URL,null,
                Response.Listener<JSONObject?> { response ->
                    Log.d("CITY_RIDE_FRAGMENT Current", "response===" + response)
                    if (response != null) {
                        progressBar.visibility=View.GONE
                        relativeLayout_data.visibility=View.VISIBLE
                        ////Current
                        var y=0
                        try {
                            var current = response.getJSONObject("current")
                            var ride_requests = current.getJSONArray("ride_requests").length()

                            for (i in 0..ride_requests - 1) {

                                var data1 =
                                    response.getJSONObject("current").getJSONArray("ride_requests")
                                        .getJSONObject(i)
                                var ride_id = data1.getString("ride_id")
                                var ride_request_id = data1.getString("id")
                                Log.d("SendData", "ride_request" + ride_request_id + "," + ride_id)

                                var ride_detail =
                                    response.getJSONObject("current").getJSONArray("ride_requests")
                                        .getJSONObject(i).getJSONObject("ride_detail")
                                var booking_id = ride_detail.getString("booking_id")
                                Log.d("SendData", "booking_id" + booking_id)

                                var to_location =
                                    response.getJSONObject("current").getJSONArray("ride_requests")
                                        .getJSONObject(i).getJSONObject("ride_detail")
                                        .getJSONObject("to_location")
                                var to_location_lat = to_location.getString("lat")
                                var to_location_long = to_location.getString("lng")
                                var address_name = to_location.getString("name")
                                Log.d(
                                    "SendData",
                                    "to_location" + to_location_lat + "\n" + to_location_long + "\n" + address_name
                                )

                                var from_location =
                                    response.getJSONObject("current").getJSONArray("ride_requests")
                                        .getJSONObject(i).getJSONObject("ride_detail")
                                        .getJSONObject("from_location")
                                var from_location_lat = from_location.getString("lat")
                                var from_location_long = from_location.getString("lng")
                                var from_name = from_location.getString("name")
                                Log.d(
                                    "SendData",
                                    "to_location" + from_location_lat + "\n" + from_location_long + "\n" + from_name
                                )

                                var date_only = ride_detail.getString("date_only")
                                var time_only = ride_detail.getString("time_only")
                                Log.d("SendData", "date_only" + time_only)

                                var price = data1.getString("price")
                                Log.d("SendData", "price" + price)

                                /////Advance

                                ridelists.add(
                                    CityCurrentRidesList(date_only, time_only, booking_id, address_name, from_name, price, to_location_lat, to_location_long, from_location_lat, from_location_long, ride_id, ride_request_id, y
                                    )
                                )
                            }
                            Collections.reverse(ridelists)
                            cityRideCurrentListAdapter= CityRideCurrentListAdapter(requireContextX().applicationContext,ridelists)
                            binding.cityRideCurrentRecylerview.adapter=cityRideCurrentListAdapter
                        }
                        catch (e:Exception){
                            Toast.makeText(requireContext(),"Server Problem",Toast.LENGTH_SHORT).show()
                        }
                        //advanceData(response)


                    }
                    // Get your json response and convert it to whatever you want.
                }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("SendData", "error===" + error)
                    Toast.makeText(requireActivity(), "Something went wrong!", Toast.LENGTH_LONG).show()
                }
            }) {
                @SuppressLint("SuspiciousIndentation")
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers.put("Content-Type", "application/json; charset=UTF-8")
                    headers.put("Authorization", "Bearer " + prefManager.getToken())
                    return headers
                }
            }

        queue.add(jsonOblect)
        //return currentdata
    }
    fun requireContextX(): Context {
        return this.context
            ?: throw IllegalStateException("Fragment $this not attached to a context.")
    }

    private fun submitAdvanceRideForm(view: View) {

        //val URL = " https://test.pearl-developer.com/figo/api/driver-ride/get-city-ride-request"
        var URL=Helper.customer_booking_list
        val queue = Volley.newRequestQueue(requireContext())
        Log.d("CityRideFragment", "CITY_RIDE_FRAGMENT Current===" + URL)
        val jsonOblect: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, URL,null,
                Response.Listener<JSONObject?> { response ->
                    Log.d("CITY_RIDE_FRAGMENT Advance", "response===" + response)
                    if (response != null) {
                        try {
                            var data = response.getJSONArray("advance").length()
                            progressBar.visibility = View.GONE
                            relativeLayout_data.visibility = View.VISIBLE
                            for (i in 0 until data) {

                                var data1 = response.getJSONArray("advance").getJSONObject(i)
                                var advance_booking_id = data1.getString("booking_id")
                                var ride_id = data1.getString("id")
                                Log.d("SendData", "advance_booking_id" + advance_booking_id)

                                var to_location = response.getJSONArray("advance").getJSONObject(i)
                                    .getJSONObject("to_location")
                                var to_location_lat = to_location.getString("lat")
                                var to_location_long = to_location.getString("lng")
                                var address_name = to_location.getString("name")
                                Log.d(
                                    "SendData",
                                    "to_location" + to_location_lat + "\n" + to_location_long + "\n" + address_name
                                )

                                var from_location =
                                    response.getJSONArray("advance").getJSONObject(i)
                                        .getJSONObject("from_location")
                                var from_location_lat = from_location.getString("lat")
                                var from_location_long = from_location.getString("lng")
                                var from_name = from_location.getString("name")

                                Log.d(
                                    "SendData",
                                    "to_location" + from_location_lat + "\n" + from_location_long + "\n" + from_name
                                )
                                var date_only = data1.getString("date_only")
                                var time_only = data1.getString("time_only")
                                advanceRidelists.add(
                                    CityAdvanceRideList(
                                        date_only,
                                        time_only,
                                        advance_booking_id,
                                        address_name,
                                        from_name,
                                        "",
                                        from_location_lat,
                                        from_location_long,
                                        to_location_lat,
                                        to_location_long,
                                        ride_id
                                    )
                                )
                            }

                            Collections.reverse(advanceRidelists)
                            cityRideAdvanceListAdapter =
                                CityRideAdvanceListAdapter(requireContextX(), advanceRidelists)
                            binding.cityRideAdvanceRecylerview.adapter = cityRideAdvanceListAdapter
                        }catch (e:Exception){
                            Toast.makeText(requireContext(),"Server Problem",Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Get your json response and convert it to whatever you want.
                }, object : Response.ErrorListener {
                    override fun onErrorResponse(error: VolleyError?) {
                        Log.d("SendData", "error===" + error)
                        Toast.makeText(requireActivity(), "Something went wrong!", Toast.LENGTH_LONG).show()
                    }
                }) {
                @SuppressLint("SuspiciousIndentation")
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + prefManager.getToken());
                    return headers
                }
            }

        queue.add(jsonOblect)

    }

/*
    private fun advanceData(response: JSONObject) {
        var y=1

        var advance=response.getJSONArray( "advance").length()
        for (i in 0 until advance){
            var data1=response.getJSONArray("advance").getJSONObject(i)
            var booking_id=data1.getString( "booking_id")
            var ride_id=data1.getString( "ride_id")
            Log.d("SendData", "advance_booking_id" + booking_id+ride_id)

            var to_location=response.getJSONArray("advance").getJSONObject(i).getJSONObject("to_location")
            var to_location_lat=to_location.getString("lat")
            var to_location_long=to_location.getString("lng")
            var address_name=to_location.getString("name")
            Log.d("SendData", "to_location" + to_location_lat+"\n"+to_location_long+"\n"+address_name)

            var from_location=response.getJSONArray("advance").getJSONObject(i).getJSONObject("from_location")
            var from_location_lat=from_location.getString("lat")
            var from_location_long=from_location.getString("lng")
            var from_name=from_location.getString("name")

            Log.d("SendData", "to_location" + from_location_lat+"\n"+from_location_long+"\n"+from_name)
            var date_only=data1.getString("date_only")
            var time_only=data1.getString( "time_only")
            ridelists.add(CityCurrentRidesList(date_only,time_only,booking_id,address_name,from_name,"200",to_location_lat,to_location_long,from_location_lat,from_location_long,ride_id,"3",y))

        }
        //return currentdata
    }
*/

    override fun onDestroyView() {
        super.onDestroyView()
       advanceRidelists.clear()
        ridelists.clear()
    }
    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }
}
