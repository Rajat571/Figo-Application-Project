package com.pearl.figgodriver.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.LinearGradient
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.pearl.figgodriver.Adapter.CityRideListAdapter
import com.pearl.figgodriver.MapData
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentCityRideBinding
import com.pearl.figgodriver.model.CityRidesList
import com.pearlorganisation.PrefManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.w3c.dom.Text


class CityRideFragment : Fragment(),OnMapReadyCallback {
    lateinit var binding:FragmentCityRideBinding
    lateinit var cityRideListAdapter: CityRideListAdapter
    var ridelists=ArrayList<CityRidesList>()
    private lateinit var mMap: GoogleMap

    //private lateinit var mMap: GoogleMap



    var customerLatLng:HashMap<Double,Double> = HashMap()
    var mCurrLocationMarker: Marker? = null

    lateinit var originLocation:LatLng
    lateinit var destinationLocation:LatLng



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_city_ride, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
       // submitForm(view)
        binding.cityRideRecylerview.layoutManager=LinearLayoutManager(requireContext())

        ridelists.add(CityRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100"))
        ridelists.add(CityRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100"))
        ridelists.add(CityRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100"))
        ridelists.add(CityRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100"))
        ridelists.add(CityRidesList("02-01-2023","12:31pm","Vivek","ISBT","premnagar","Rs 100"))
        cityRideListAdapter=CityRideListAdapter(requireContext(),ridelists)
        binding.cityRideRecylerview.adapter=cityRideListAdapter



        //get current location






 /*       var accept_city_ride_btn=view.findViewById<TextView>(R.id.accept_city_ride_btn)
        var reject_city_ride_btn=view.findViewById<TextView>(R.id.reject_city_ride_btn)

        accept_city_ride_btn.setOnClickListener {
            getCityRideDetails(view)
            Toast.makeText(requireContext(),"Accepted",Toast.LENGTH_SHORT).show()
        }
        reject_city_ride_btn.setOnClickListener {
            Toast.makeText(requireContext(),"Reject",Toast.LENGTH_SHORT).show()
        }*/


/*
        customerLatLng.put(30.288747155809858, 77.99142154400673)
        customerLatLng.put(30.282224877386177, 77.9951122634793)
        customerLatLng.put(30.280927781694515, 77.98987659166937)
        customerLatLng.put(30.28355901506611, 77.98661502562382)
        customerLatLng.put(30.292934526443705, 77.99579890896257)*/



       /* mapFragment.getMapAsync {
            mMap = it
            originLocation = LatLng(prefManager.getlatitude().toDouble(), prefManager.getlongitude().toDouble())
            Log.d("originLocation","originLocation"+originLocation)
            mMap.addMarker(MarkerOptions().position(originLocation))
         destinationLocation = LatLng(30.288793853142632, 77.99709732183523)//30.288793853142632, 77.99709732183523
            mMap.addMarker(MarkerOptions().position(destinationLocation))
            for(i in 0..customerLatLng.size-1)
                mMap.addMarker(MarkerOptions().position(LatLng(customerLatLng.keys.toList()[i],customerLatLng.values.toList()[i]))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
            val urll = getDirectionURL(originLocation, destinationLocation, "AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
            GetDirection(urll).execute()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
        }*/
    }

    private fun submitForm(view: View) {
        val URL = "https://test.pearl-developer.com/figo/api/ride/get-booking-details"
        val queue = Volley.newRequestQueue(requireContext())

        val jsonOblect: JsonObjectRequest =
            object : JsonObjectRequest(Method.GET, URL,null,
                Response.Listener<JSONObject?> { response ->
                    Log.d("SendData", "response===" + response)
                    if (response != null) {
                        var data=response.getJSONArray("rides").length()
                        for (i in 0 until data){
                            Log.d("SendData", "data===" + data)
                            var data1=response.getJSONArray("rides").getJSONObject(i)
                            var booking_id=data1.get("booking_id").toString()

                            var to_location=response.getJSONArray("rides").getJSONObject(i).getJSONObject( "to_location")
                            var to_location_lat=to_location.getString("lat")
                            var to_location_long=to_location.getString("lng")
                            var address_name=to_location.getString("name")

                            var from_location=response.getJSONArray("rides").getJSONObject(i).getJSONObject(  "from_location")
                            var from_location_lat=from_location.getString("lat")
                            var from_location_long=from_location.getString("lng")
                            var from_name=from_location.getString("name")

                            var dateTime=data1.getString("date_time")
                            ridelists.add(CityRidesList(dateTime,dateTime,booking_id,address_name,from_name,"300"))

                        }
                        cityRideListAdapter=CityRideListAdapter(requireContext(),ridelists)
                        binding.cityRideRecylerview.adapter=cityRideListAdapter


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
                    headers.put("Authorization", "Bearer " + "1379|yVFExCRmDPYlgSHoPx56EtpSbzBdAqIUqTgYJP6D");
                    return headers
                }
            }

        queue.add(jsonOblect)


    }

/*

    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }
*/


    override fun onMapReady( googleMap: GoogleMap) {
      /*  mMap = googleMap
        // Add a marker in Sydney and move the camera
      //  val sydney = LatLng(prefManager.getlatitude().toDouble(), prefManager.getlongitude().toDouble())
        val sydney=LatLng(prefManager.getlatitude().toDouble(),prefManager.getlongitude().toDouble())
        mMap.clear()
        mCurrLocationMarker =   mMap.addMarker(
            MarkerOptions()
            .position(sydney)
            .title("Marker in Uttarakhand"))
        val markerOptions = MarkerOptions()
        // mMap.addMarker(markerOptions);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,18f))*/

    }





   /* private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body().toString()
Log.d("Response == ","ResponseLatLANg == "+data)
            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                Log.d("response == ",""+e.stackTrace)
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.GREEN)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }*/



    private fun getCityRideDetails(view: View) {
        var city_ride_date=view.findViewById<TextView>(R.id.city_ride_date)
        var city_ride_time=view.findViewById<TextView>(R.id.city_ride_time)
        var city_ride_address=view.findViewById<TextView>(R.id.city_ride_address)

    }


}