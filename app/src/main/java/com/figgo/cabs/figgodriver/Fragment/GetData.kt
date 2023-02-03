package com.figgo.cabs.figgodriver.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GetData.newInstance] factory method to
 * create an instance of this fragment.
 */
class GetData : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_get_data, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var prefManager = PrefManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        val get_URL = "https://test.pearl-developer.com/figo/api/driver/get-all-details"
        val queue = Volley.newRequestQueue(requireContext())
        var profile_name = view.findViewById<EditText>(R.id.drivername)
        var profile_mobile = view.findViewById<EditText>(R.id.show_drivermobileno)
        var spinner_state = view.findViewById<Spinner>(R.id.show_spinner_state)
        var spinner_city = view.findViewById<Spinner>(R.id.show_spinner_city)
        var driver_dlNo = view.findViewById<EditText>(R.id.show_driverdlno)
        var driver_panNo = view.findViewById<EditText>(R.id.show_driverPanNo)
        var driver_adharNo = view.findViewById<EditText>(R.id.show_driverAdharNo)
        val jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.GET,get_URL,null,
            {
            if(it!=null)
            {
                Log.d("Get Response ",""+it.toString())
                profile_name.setText(it.getString("name"))
                profile_mobile.setText(it.getString("mobile"))
                driver_dlNo.setText(it.getString("dl_number"))
                driver_panNo.setText(it.getString("pan_number"))
                driver_adharNo.setText(it.getString("aadhar_number"))
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GetData.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GetData().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}