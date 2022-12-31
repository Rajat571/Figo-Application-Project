
package com.pearl.figgodriver
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.maps.route.extensions.drawRouteOnMap
import com.maps.route.extensions.moveCameraOnMap

import okhttp3.OkHttpClient
import com.android.volley.Request
import com.google.android.gms.maps.model.*
import org.json.JSONException


class Maps : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
    GoogleMap.OnPolygonClickListener {

    private lateinit var mMap: GoogleMap
    private var originLatitude: Double = 28.5021359
    private var originLongitude: Double = 77.4054901
    private var destinationLatitude: Double = 28.5151087
    lateinit var data:String
    val path =  ArrayList<LatLng>()
    var result =  ArrayList<List<LatLng>>()
    private var destinationLongitude: Double = 77.3932163
    private var requestQueue: RequestQueue? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
 setContentView(R.layout.activity_maps)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapXX) as SupportMapFragment

        mapFragment.getMapAsync(this)



        val urll = "https://maps.googleapis.com/maps/api/directions/json?origin=30.28791336599283, 77.99502643295264&destination=30.2835219559435, 77.99350293828664&key=AIzaSyA695FzYYRDkyrvue7VCb-kZeOlfbfG22w"
        val queue = Volley.newRequestQueue(baseContext)

        val request = JsonObjectRequest(Request.Method.GET, urll, null, {

                response ->try {
            data = response.toString()
            Log.d("Response === ", "Res" + data)
        }
        catch (e: JSONException) {
            Log.d("Response === ","REs"+e.stackTrace.toString())
        }
        }, { error -> error.printStackTrace() })

        queue?.add(request)

        val gd = findViewById<Button>(R.id.directions)
        gd.setOnClickListener{
            mapFragment.getMapAsync {
                mMap = it
                val originLocation = LatLng(originLatitude, originLongitude)
                mMap.addMarker(MarkerOptions().position(originLocation))
                val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
                mMap.addMarker(MarkerOptions().position(destinationLocation))
                val urll = "https://maps.googleapis.com/maps/api/directions/json?origin=30.28791336599283, 77.99502643295264&destination=30.2835219559435, 77.99350293828664&key=AIzaSyA695FzYYRDkyrvue7VCb-kZeOlfbfG22w"
               GetDirection(urll,data).execute()
                Log.d("Path == ", " =$path")
                val polyline1 = mMap.addPolyline(PolylineOptions()
                    .clickable(true)
                    .addAll(path))

                mMap.addMarker(MarkerOptions().position(originLocation))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
                mMap.setOnPolylineClickListener(this)
                mMap.setOnPolygonClickListener(this)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
            }
        }
}


    override fun onMapReady(p0: GoogleMap) {
        mMap = p0!!
        val originLocation = LatLng(originLatitude, originLongitude)
        mMap.clear()
        Log.d("Path == ", " =$path")
        val polyline1 = p0.addPolyline(PolylineOptions()
            .clickable(true)
            .addAll(path))

        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
        p0.setOnPolylineClickListener(this)
        p0.setOnPolygonClickListener(this)
    }
    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String,val data:String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            Log.d("ssss","rrrr")



            val respObj = Gson().fromJson(data,MapData::class.java)
                Log.d("Response X == ", " =$data")

                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            Log.d("LatLNG X == ", " =$result")
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

    override fun onPolylineClick(p0: Polyline) {
        TODO("Not yet implemented")
    }

    override fun onPolygonClick(p0: Polygon) {
        TODO("Not yet implemented")
    }


}


