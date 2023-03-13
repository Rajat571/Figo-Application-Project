package com.figgo.cabs.figgodriver.Fragment.outstation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.example.figgodriver.Fragment.SedanAdapter
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentOutstationHomeBinding
import com.figgo.cabs.figgodriver.Adapter.CityRideCurrentListAdapter
import com.figgo.cabs.figgodriver.Adapter.OutstationRideAdapter
import com.figgo.cabs.figgodriver.model.CityCurrentRidesList

import com.figgo.cabs.figgodriver.model.Sedan
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject
import java.util.HashMap

//import com.example.figgodriver.model.Sedan


class OutstationHomeFragment : Fragment() {
    lateinit var binding: FragmentOutstationHomeBinding
    lateinit var sedanAdapter: SedanAdapter
    var ridelists = java.util.ArrayList<CityCurrentRidesList>()
    lateinit var prefManager: PrefManager
    lateinit var outstation_loadinggif: LinearLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var data = ArrayList<Sedan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_outstation_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        outstation_loadinggif = view.findViewById(R.id.outstation_loadinggif)
        swipeRefreshLayout = view.findViewById(R.id.outstation_swipe_refresh)
        submitOnewayRide(view)
        //submitCurrentRideForm(view)
        swipeRefreshLayout.setOnRefreshListener {
            outstation_loadinggif.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
            submitOnewayRide(view)
            //submitCurrentRideForm(view)

        }

    }

    private fun submitOnewayRide(view: View) {
        var url = Helper.outstation_oneway_request
        var queue = Volley.newRequestQueue(requireContext())
        var jsonObject = object :
            JsonObjectRequest(Method.POST, url, null, Response.Listener<JSONObject> { response ->
                Log.d("OutstationHomeFragment", "Response===" + response)
                outstation_loadinggif.visibility = View.GONE
                if (response != null) {
                    try {
                        Log.d("OutstationHomeFragment", "Response===" + response)
                        var data = response.getJSONObject("493")
                        var booking_id = data.getString("booking_id")
                        var ride_id = data.getString("id")
                        var ride_request_id = data.getString("id")
                        var date = data.getString("date_only")
                        var time = data.getString("time_only")


                        var to_location = data.getJSONObject("to_location")
                        var to_lat = to_location.getString("lat")
                        var to_long = to_location.getString("lng")
                        var to_name = to_location.getString("name")

                        var from_location = data.getJSONObject("from_location")
                        var from_lat = from_location.getString("lat")
                        var from_long = from_location.getString("lng")
                        var from_name = from_location.getString("name")

                        var pricedata = data.getJSONObject("price")
                        var price = pricedata.getString("avg")
                        Log.d(
                            "SEndData",
                            "DATA====" + booking_id + "," + date + "," + time + "," + to_name +
                                    "," + from_name + "," + price
                        )
                        ridelists.add(
                            CityCurrentRidesList(
                                date, time, booking_id, to_name, from_name,
                                price, to_lat, to_long, from_lat,
                                from_long, ride_id, ride_request_id, 2
                            )
                        )
                    }
                    catch (e:Exception){

                    }
                }
                ridelists.reverse()
                Log.d("Outstation", ridelists.toString())
                var recyclerView = view.findViewById<RecyclerView>(R.id.outstation_recyclerview)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = OutstationRideAdapter(
                    requireContext().applicationContext,
                    ridelists
                )
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("SendData", "error===$error")
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Content-Type", "application/json; charset=UTF-8")
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                return headers
            }
        }
        queue.add(jsonObject)

    }

    private fun submitCurrentRideForm(view: View) {
        try {
            ridelists.clear()
            // val URL = "https://test.pearl-developer.com/figo/api/driver-ride/get-city-ride-request"
            var URL = Helper.customer_booking_list
            var recyclerView = view.findViewById<RecyclerView>(R.id.outstation_recyclerview)
            Log.d("CityRideFragment", "CITY_RIDE_FRAGMENT Current===$URL")
            val queue = Volley.newRequestQueue(requireContext())

            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(
                    Method.POST, URL, null,
                    Response.Listener<JSONObject?> { response ->
                        Log.d("CITY_RIDE_FRAGMENT Current", "response===$response")
                        if (response != null) {

                            ////Current
                            var y = 0
                            try {
                                var current = response.getJSONObject("current")
                                var ride_requests = current.getJSONArray("ride_requests").length()
                                if (ride_requests >= 1)
                                    outstation_loadinggif.visibility = View.GONE
                                for (i in 0 until ride_requests) {

                                    var data1 =
                                        response.getJSONObject("current")
                                            .getJSONArray("ride_requests")
                                            .getJSONObject(i)
                                    var ride_id = data1.getString("ride_id")
                                    var ride_request_id = data1.getString("id")
                                    // Log.d("SendData", "ride_request" + ride_request_id + "," + ride_id)

                                    var ride_detail =
                                        response.getJSONObject("current")
                                            .getJSONArray("ride_requests")
                                            .getJSONObject(i).getJSONObject("ride_detail")
                                    var booking_id = ride_detail.getString("booking_id")
                                    //Log.d("SendData", "booking_id" + booking_id)

                                    var to_location =
                                        response.getJSONObject("current")
                                            .getJSONArray("ride_requests")
                                            .getJSONObject(i).getJSONObject("ride_detail")
                                            .getJSONObject("to_location")
                                    var to_location_lat = to_location.getString("lat")
                                    var to_location_long = to_location.getString("lng")
                                    var address_name = to_location.getString("name")
                                    /*   Log.d(
                                    "SendData",
                                    "to_location" + to_location_lat + "\n" + to_location_long + "\n" + address_name
                                )*/

                                    var from_location =
                                        response.getJSONObject("current")
                                            .getJSONArray("ride_requests")
                                            .getJSONObject(i).getJSONObject("ride_detail")
                                            .getJSONObject("from_location")
                                    var from_location_lat = from_location.getString("lat")
                                    var from_location_long = from_location.getString("lng")
                                    var from_name = from_location.getString("name")
                                    Log.d(
                                        "SendData","to_location" + from_location_lat + "\n" + from_location_long + "\n" + from_name)

                                    var date_only = ride_detail.getString("date_only")
                                    var time_only = ride_detail.getString("time_only")
                                    Log.d("SendData", "date_only" + time_only)

                                    var price = data1.getString("price")
                                    Log.d("SendData", "price" + price)

                                    /////Advance
                                    ridelists.add(
                                        CityCurrentRidesList(
                                            date_only,
                                            time_only,
                                            booking_id,
                                            address_name,
                                            from_name,
                                            price,
                                            to_location_lat,
                                            to_location_long,
                                            from_location_lat,
                                            from_location_long,
                                            ride_id,
                                            ride_request_id,
                                            y
                                        )
                                    )
                                }
                                ridelists.reverse()
                                Log.d("Outstation", ridelists.toString())
                                recyclerView.adapter = OutstationRideAdapter(
                                    requireContext().applicationContext,
                                    ridelists
                                )
                            } catch (_: Exception) {
                            }
                            if (requireContext() != null)
                                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            //advanceData(response)


                        }
                        // Get your json response and convert it to whatever you want.
                    },
                    Response.ErrorListener { error -> Log.d("SendData", "error===$error") }) {
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
        } catch (e: Exception) {

        }
    }


}