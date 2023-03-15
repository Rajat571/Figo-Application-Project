package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.databinding.internal.org.antlr.v4.runtime.misc.MurmurHash.finish
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.figgodriver.model.CityAdvanceRideList
import com.figgo.cabs.figgodriver.model.CityCurrentRidesList
import com.figgo.cabs.pearllib.GlobalVariables
import com.figgo.cabs.pearllib.Helper
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import java.io.File
import java.util.*


class CityRideFragment : Fragment() {
    lateinit var binding: FragmentCityRideBinding
    lateinit var cityRideCurrentListAdapter: CityRideCurrentListAdapter
    lateinit var cityRideAdvanceListAdapter: CityRideAdvanceListAdapter
    lateinit var progressBar: ProgressBar
    lateinit var loading: LinearLayout
    lateinit var card: CardView
    lateinit var riderequestno: TextView
    lateinit var bookinglimit: TextView
    lateinit var rechargeNow: TextView

    var count = 0
    lateinit var relativeLayout_data: RelativeLayout
    var ridelists = ArrayList<CityCurrentRidesList>()
    var advanceRidelists = ArrayList<CityAdvanceRideList>()
    lateinit var prefManager: PrefManager
    lateinit var currentdata: CityCurrentRidesList
    lateinit var swiperefresh: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city_ride, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        progressBar = view.findViewById<ProgressBar>(R.id.city_ride_progressbar)
        loading = view.findViewById(R.id.loadinggif)
        relativeLayout_data = view.findViewById<RelativeLayout>(R.id.city_ride_relative_layout)
        swiperefresh = view.findViewById(R.id.pulldownforrefresh)
        card = view.findViewById(R.id.cityride_card)
        bookinglimit = view.findViewById(R.id.citybookingLimit)
        riderequestno = view.findViewById(R.id.ride_requestlimit)
        rechargeNow = view.findViewById(R.id.rechargenow)
        cityRideCurrentListAdapter = CityRideCurrentListAdapter(
            requireContext().applicationContext,
            ridelists)



        getUserRecharge()
        //ridelists.add()
        //submitCurrentRideForm(view,"advance")
/*        swiperefresh.setOnRefreshListener {
            swiperefresh.isRefreshing=false

            cityRideAdvanceListAdapter.notifyDataSetChanged()
            cityRideCurrentListAdapter.notifyDataSetChanged()

        }*/
        submitAdvanceRideForm(view)
        submitCurrentRideForm(view)
/*        if(count<1){
            loading.visibility=View.VISIBLE
        }
        else{
            loading.visibility=View.GONE
        }*/
        progressBar.visibility = View.VISIBLE
        relativeLayout_data.visibility = View.GONE
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



        swiperefresh.setOnRefreshListener {
            count = 0
            loading.visibility = View.VISIBLE
            swiperefresh.isRefreshing = false
            cityRideCurrentListAdapter.notifyDataSetChanged()
            cityRideAdvanceListAdapter.notifyDataSetChanged()
            submitCurrentRideForm(view)
            submitAdvanceRideForm(view)
        }


        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                try {
                    handler.postDelayed(this, 10000)
                    cityRideCurrentListAdapter.notifyDataSetChanged()
                    cityRideAdvanceListAdapter.notifyDataSetChanged()
                    submitCurrentRideForm(view)
                    submitAdvanceRideForm(view)
                } catch (e: Exception) {

                }
            }
        }
        handler.postDelayed(runnable, 10000)

/*        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //   Log.d(TAG, "Fragment back pressed invoked")
                    // Do custom work here
                    startActivity(Intent(requireContext(), DriverDashBoard::class.java))


                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
            )*/

    }

    private fun getUserRecharge() {
        var url=Helper.get_user_recharge
        var queue=Volley.newRequestQueue(requireContext())
        var jsonobjectRequest=object :JsonObjectRequest(Method.POST,url,null,Response.Listener<JSONObject>{
            response ->
            Log.d("get_user_recharge","response==="+response)
            if (response!=null){
                try {
                    riderequestno.text = response.getString("request_limit")
                    bookinglimit.text = response.getString("booking_limit")
                } catch (_: Exception) {
                    riderequestno.text = "0"
                    bookinglimit.text = "0"
                }
                rechargeNow.setOnClickListener {
                    if (response.getString("request_limit").toInt()==0){
                        Toast.makeText(requireContext(),"Your balance is  over please recharge ",Toast.LENGTH_SHORT).show()
                        parentFragment?.let { it1 ->
                            parentFragmentManager.beginTransaction().replace(
                                it1.id,
                                AccountDetails()
                            ).commit()
                            GlobalVariables.dashboardBackCount += 1
                        }
                    }
                    else{
                        Toast.makeText(requireContext(),"Your balance is not over yet",Toast.LENGTH_SHORT).show()

                    }

                }

            }
        },object :Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {

            }
        })
        {
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Content-Type", "application/json; charset=UTF-8")
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                return headers
            }
        }
        queue.add(jsonobjectRequest)

    }

    private fun submitCurrentRideForm(view: View) {
        try {
            ridelists.clear()
            // val URL = "https://test.pearl-developer.com/figo/api/driver-ride/get-city-ride-request"
            var URL = Helper.customer_booking_list
            Log.d("CityRideFragment", "CITY_RIDE_FRAGMENT Current===$URL")
            val queue = Volley.newRequestQueue(requireContext())

            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(Method.POST, URL, null,
                    Response.Listener<JSONObject?> { response ->
                        Log.d("CITY_RIDE_FRAGMENT Current", "response===$response")
                        if (response != null) {
                            progressBar.visibility = View.GONE
                            relativeLayout_data.visibility = View.VISIBLE
                            ////Current
                            var y = 0
                            try {
                                var current=response.getJSONArray("current")
                                if (current.length()!=0){
                                    loading.visibility = View.GONE
                                }
                                else{
                                    loading.visibility=View.VISIBLE
                                }
                                for (i in 0..current.length()-1){

                                    var data=current.getJSONObject(i)
                                    var booking_id= data.getString("booking_id")
                                    Log.d("SendData","Booking_id"+booking_id)
                                    var ride_id = data.getString("id")
                                    var to_location =
                                        data.getJSONObject("to_location")
                                    var to_location_lat = to_location.getString("lat")
                                    var to_location_long = to_location.getString("lng")

                                    var address_name = to_location.getString("name")
                                    var from_location = data.getJSONObject("from_location")
                                    var from_location_lat = from_location.getString("lat")
                                    var from_location_long = from_location.getString("lng")
                                    var from_name = from_location.getString("name")

                                    var date_only = data.getString("date_only")
                                    var time_only = data.getString("time_only")
                                    var price1 = data.getJSONObject("price")
                                    var price=price1.getString("avg")
                                    var rideeRequest=data.getJSONObject("ride_request")
                                    var ride_request_id=rideeRequest.getString("id")
                                    Log.d("SendDATA","Ride-Request api"+ride_request_id)

                                    ridelists.add(CityCurrentRidesList(date_only, time_only, booking_id, address_name, from_name, price, to_location_lat, to_location_long, from_location_lat, from_location_long, ride_id, y,ride_request_id))

                                }
                                ridelists.reverse()
                                cityRideCurrentListAdapter = CityRideCurrentListAdapter(
                                    requireContext().applicationContext,
                                    ridelists
                                )
                                binding.cityRideCurrentRecylerview.adapter =
                                    cityRideCurrentListAdapter

                            /*    var current = response.getJSONObject("current")
                                var ride_requests = current.getJSONArray("ride_requests").length()
                                count += ride_requests
                                try {
                                    riderequestno.text = current.getString("request_limit")
                                    bookinglimit.text = current.getString("booking_limit")
                                } catch (_: Exception) {
                                    riderequestno.text = "0"
                                    bookinglimit.text = "0"
                                }
                                if (ride_requests >= 1)
                                    loading.visibility = View.GONE
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
                                    *//*   Log.d(
                                    "SendData",
                                    "to_location" + to_location_lat + "\n" + to_location_long + "\n" + address_name
                                )*//*
                                    var from_location =
                                        response.getJSONObject("current")
                                            .getJSONArray("ride_requests")
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
                                cityRideCurrentListAdapter = CityRideCurrentListAdapter(
                                    requireContext().applicationContext,
                                    ridelists
                                )
                                binding.cityRideCurrentRecylerview.adapter =
                                    cityRideCurrentListAdapter*/
                            } catch (_: Exception) {
                            }
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
        //return currentdata
    }

    private fun submitAdvanceRideForm(view: View) {
        try {
            advanceRidelists.clear()
            //val URL = " https://test.pearl-developer.com/figo/api/driver-ride/get-city-ride-request"
            var URL = Helper.customer_booking_list
            val queue = Volley.newRequestQueue(requireContext())
            Log.d("CityRideFragment", "CITY_RIDE_FRAGMENT Current===" + URL)
            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(Method.POST, URL, null,
                    Response.Listener<JSONObject?> { response ->
                        Log.d("CITY_RIDE_FRAGMENT Advance", "response===" + response)
                        if (response != null) {
                            try {
                                var data = response.getJSONArray("advance").length()
                                count += data
                                if (data >= 1){
                                    loading.visibility = View.GONE
                                    progressBar.visibility = View.GONE}
                                else{
                                    loading.visibility = View.VISIBLE
                                }
                                relativeLayout_data.visibility = View.VISIBLE

                                for (i in 0 until data) {

                                    var data1 = response.getJSONArray("advance").getJSONObject(i)
                                    var advance_booking_id = data1.getString("booking_id")
                                    var ride_id = data1.getString("id")
                                    Log.d("SendData", "advance_booking_id" + advance_booking_id)

                                    var to_location =
                                        response.getJSONArray("advance").getJSONObject(i)
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
                                    var price = data1.getString("price")
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
                                            price,
                                            from_location_lat,
                                            from_location_long,
                                            to_location_lat,
                                            to_location_long,
                                            ride_id,
                                            "advance"
                                        )
                                    )
                                }

                                Collections.reverse(advanceRidelists)
                                cityRideAdvanceListAdapter =
                                    CityRideAdvanceListAdapter(requireContext(), advanceRidelists)

                                binding.cityRideAdvanceRecylerview.adapter =
                                    cityRideAdvanceListAdapter
                            } catch (_: Exception) {
                            }
                        }
                        // Get your json response and convert it to whatever you want.
                    }, Response.ErrorListener { error ->
                        Log.d("SendData", "error===" + error)
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
        } catch (e: Exception) {

        }
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

}
