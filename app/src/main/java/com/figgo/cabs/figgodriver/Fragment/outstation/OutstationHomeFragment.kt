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
import com.android.volley.toolbox.JsonArrayRequest
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
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap



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
            JsonArrayRequest(Method.POST, url, null, Response.Listener<JSONArray> { response ->
                Log.d("OutstationHomeFragment", "Response===" + response)

                if (response != null) {
                    outstation_loadinggif.visibility = View.GONE
                    try {
                        Log.d("OutstationHomeFragment", "Response===" + response)

                        for (i in 0..response.length()-1){
                            var data = response.getJSONObject(i)
                            var booking_id = data.getString("booking_id")
                            Log.d(
                                "SEndData",
                                "DATA====" + booking_id)
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
                            ridelists.add(CityCurrentRidesList(
                                date, time, booking_id, to_name, from_name,
                                price, to_lat, to_long, from_lat,
                                from_long, ride_id, ride_request_id, 2))
                        }
                    }
                    catch (e:Exception){
                    }
                }
                ridelists.reverse()
                Log.d("Outstation", ridelists.toString())
                var recyclerView = view.findViewById<RecyclerView>(R.id.outstation_recyclerview)
                try{    recyclerView.layoutManager = LinearLayoutManager(requireContext())}
                catch (e:Exception){}

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
}
