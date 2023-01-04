package com.pearl.figgodriver.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
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
import com.pearl.figgodriver.MapData
import com.pearl.figgodriver.R
import com.pearlorganisation.PrefManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.w3c.dom.Text


class CityRideFragment : Fragment(),OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var prefManager: PrefManager
    //private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var lat:Double = 0.0
    var long:Double = 0.0
    var customerLatLng:HashMap<Double,Double> = HashMap()
    var mCurrLocationMarker: Marker? = null

    lateinit var originLocation:LatLng
    lateinit var destinationLocation:LatLng

    private var originLatitude: Double =30.28401063526107
    private var originLongitude: Double = 77.99210085398012
    private var destinationLatitude: Double =  30.35335500972683
    private var destinationLongitude: Double = 78.02461312748794


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_ride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager=PrefManager(requireContext())

        //get current location

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    lat = location.latitude
                    prefManager.setlatitude(lat.toFloat())
                    long = location.longitude
                    prefManager.setlongitude(long.toFloat())
                    Toast.makeText(requireContext(),"Lat :"+lat+"\nLong: "+long, Toast.LENGTH_SHORT).show()
                }
            }



        // Fetching API_KEY which we wrapped
        val ai: ApplicationInfo = activity?.applicationContext?.packageManager
            ?.getApplicationInfo(activity?.applicationContext!!.packageName, PackageManager.GET_META_DATA)!!

        val value = ai.metaData["com.google.android.geo.${R.string.Google_maps_key}"]
        val apiKey = value.toString()
        // Initializing the Places API with the help of our API_KEY
        if (!Places.isInitialized()) {
            Places.initialize(requireActivity().applicationContext,apiKey)
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)



        var accept_city_ride_btn=view.findViewById<TextView>(R.id.accept_city_ride_btn)
        var reject_city_ride_btn=view.findViewById<TextView>(R.id.reject_city_ride_btn)

        accept_city_ride_btn.setOnClickListener {
            getCityRideDetails(view)
            Toast.makeText(requireContext(),"Accepted",Toast.LENGTH_SHORT).show()
        }
        reject_city_ride_btn.setOnClickListener {
            Toast.makeText(requireContext(),"Reject",Toast.LENGTH_SHORT).show()
        }


/*
        customerLatLng.put(30.288747155809858, 77.99142154400673)
        customerLatLng.put(30.282224877386177, 77.9951122634793)
        customerLatLng.put(30.280927781694515, 77.98987659166937)
        customerLatLng.put(30.28355901506611, 77.98661502562382)
        customerLatLng.put(30.292934526443705, 77.99579890896257)*/

        mapFragment.getMapAsync {
            mMap = it
            val originLocation = LatLng(originLatitude, originLongitude)
            mMap.addMarker(MarkerOptions().position(originLocation))
            val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
            mMap.addMarker(MarkerOptions().position(destinationLocation))
            val urll = getDirectionURL(originLocation, destinationLocation, "AIzaSyCbd3JqvfSx0p74kYfhRTXE7LZghirSDoU")
            GetDirection(urll).execute()
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
        }


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
        mMap =googleMap
        val originLocation = LatLng(originLatitude, originLongitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
    }

    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()?.string()
            Log.d("SEND DATA"," data ===="+ data )

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data,MapData::class.java)
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
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

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }


    private fun getCityRideDetails(view: View) {
        var city_ride_date=view.findViewById<TextView>(R.id.city_ride_date)
        var city_ride_time=view.findViewById<TextView>(R.id.city_ride_time)
        var city_ride_address=view.findViewById<TextView>(R.id.city_ride_address)

    }


}