package com.figgo.cabs.figgodriver.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.RideHistoryAdapter
import com.figgo.cabs.figgodriver.Adapter.RideHistoryRowAdapter
import com.figgo.cabs.pearllib.Helper
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

/**
 * A simple [Fragment] subclass.
 * Use the [RideHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class RideHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var prefManager=PrefManager(requireContext())
        var header: RecyclerView = view.findViewById<RecyclerView>(R.id.ridehistoryheader)
       // var url3 = "https://test.pearl-developer.com/figo/api/driver/ride-history"
        var url3=Helper.ride_history
        Log.d("RideHistory","URL"+url3)
        var progressbar = view.findViewById<ProgressBar>(R.id.ridehistory_progressbar)
        var data_view = view.findViewById<HorizontalScrollView>(R.id.ridehisory_data)
        progressbar.visibility=View.VISIBLE
        data_view.visibility=View.GONE
        val queue = Volley.newRequestQueue(requireContext())

        var headerData = listOf<String>("Booking ID","To","From","Status","Distance","View");
        var contentdata = ArrayList<List<String>>()
       /* for (i in 0..40)
            contentdata.add(listOf("1","Sagar Bisht","01-02-2023","8:40am","50min","Chandigarh","Patiala","Pending","View"))*/

        val jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.POST,url3,null,
            {
                if(it!=null) {
                    try {


                        progressbar.visibility = View.GONE
                        data_view.visibility = View.VISIBLE
                        Log.d("Data Response", "" + it)
                        var allride: JSONObject = it.getJSONObject("data")
                        var allrideArray: JSONArray = allride.getJSONArray("all_rides")
                        var ride_details: JSONObject
                        var booking_id: String
                        var destination: String
                        var from: String
                        var status: String
                        var actual_distance: String
                        var price: String
                        contentdata.add(
                            listOf(
                                "Booking ID",
                                "To",
                                "From",
                                "Status",
                                "Distance",
                                "View"
                            )
                        )
                        //  ride_details=allrideArray.optJSONObject(1).getJSONObject("ride_detail")
                        //Log.d("Ride Detail ",""+ride_details.toString())
                        for (i in 0..allrideArray.length() - 1) {
                            ride_details =
                                allrideArray.optJSONObject(i).getJSONObject("ride_detail")
                            booking_id = ride_details.getString("booking_id")
                            destination =
                                ride_details.getJSONObject("to_location").getString("name")
                            from = ride_details.getJSONObject("from_location").getString("name")
                            status = ride_details.getString("status")
                            actual_distance = ride_details.getString("actual_distance")
                            Log.d(
                                "Ride Detail ",
                                "book - " + booking_id + " des - " + destination + "from - " + from + "status - " + status + " dist - " + actual_distance
                            )
                            contentdata.add(
                                listOf(
                                    booking_id,
                                    destination,
                                    from,
                                    status,
                                    actual_distance,
                                    "View"
                                )
                            )
                        }
                        header.adapter = RideHistoryRowAdapter(contentdata, requireContext())
                        header.layoutManager = LinearLayoutManager(requireContext())
                    }
                    catch (e:Exception){
                        Toast.makeText(requireContext(),"Server Problem", Toast.LENGTH_SHORT).show()
                    }
                }

            },{


            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                return headers
            }
        }
        queue.add(jsonObject)


    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RideHistory.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): RideHistory {
            val fragment = RideHistory()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}