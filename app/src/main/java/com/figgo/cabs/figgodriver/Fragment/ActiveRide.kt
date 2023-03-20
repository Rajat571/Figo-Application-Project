package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.ActiveRideAdapter
import com.figgo.cabs.figgodriver.Adapter.AllRideAdapter
import com.figgo.cabs.figgodriver.Adapter.OutstationAdapter
import com.figgo.cabs.figgodriver.model.ActiveRideData
import com.figgo.cabs.pearllib.Helper
import org.json.JSONArray
import java.util.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var prefManager: PrefManager

/**
 * A simple [Fragment] subclass.
 * Use the [ActiveRide.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActiveRide : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var recyclerView: RecyclerView
    lateinit var loading: LinearLayout
    lateinit var pulldown:SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_ride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        recyclerView = view.findViewById(R.id.active_ride_recycler)
        loading = view.findViewById(R.id.active_loadinggif)
        pulldown = view.findViewById(R.id.active_ride_pulldown)
        apiCall(view)
        pulldown.setOnRefreshListener {
            loading.visibility=View.VISIBLE
            pulldown.isRefreshing=false
            apiCall(view)
        }
    }

    private fun apiCall(view: View) {
        var activeURL = Helper.active_ride
        var queue = Volley.newRequestQueue(requireContext())
        var json = JSONArray()
        var dataList:ArrayList<ActiveRideData> = ArrayList<ActiveRideData>()
        var booking_id:String="- - -"
        var type:String="- - -"
        var to_location_lat:String="- - - "
        var to_location_long:String="- - -"
        var price:String = "- - - "
        var to_name:String="- - -"
        var from_location_lat:String="- - -"
        var from_location_long:String="- - -"
        var from_name:String="- - -"
        var date_only:String="- - -"
        var time_only:String="- - - "
        var rideID:String="- - - "

        var jsonArrayRequest:JsonArrayRequest = object :JsonArrayRequest(Method.POST,activeURL,json,{
if(it!=null){
    for(i in 0 until it.length()){
        if(it.length()>0)
            loading.visibility=View.GONE
        var data = it.getJSONObject(i)
        try {
            booking_id = data.getString("booking_id")
        }catch (_:Exception){
            booking_id = " - - - "
        }

        try {
            to_location_lat = data.getJSONObject("to_location").getString("lat")
            to_location_long = data.getJSONObject("to_location").getString("lng")
            to_name = data.getJSONObject("to_location").getString("name")
        }catch (_:Exception){ }

        try {
            from_location_lat = data.getJSONObject("from_location").getString("lat")
            from_location_long = data.getJSONObject("from_location").getString("lng")
            from_name = data.getJSONObject("from_location").getString("name")


        }catch (_:Exception){ }

        try {
            type = data.getString("type")
        }catch (_:Exception){

        }
        try{
            date_only = data.getString("date_only")
            time_only = data.getString("time_only")
        }catch (_:Exception){

        }

        try{
            price = data.getJSONObject("ride_details").getString("price")
            rideID = data.getJSONObject("ride_details").getString("ride_id")
        }catch (_:Exception){

        }
        dataList.add(ActiveRideData(booking_id, type, to_location_lat, to_location_long, to_name, from_location_lat, from_location_long, from_name, date_only, time_only,price,rideID))

    }
    try {
        recyclerView.adapter = ActiveRideAdapter(requireContext(), dataList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }catch (_:Exception){

    }



}
        },{

        }){
            @SuppressLint("SuspiciousIndentation")
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", "Bearer " + prefManager.getToken());
                return headers
            }
        }
        queue.add(jsonArrayRequest)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ActiveRide.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActiveRide().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}