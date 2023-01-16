package com.figgo.cabs.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import com.figgo.cabs.R
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaleExtraBooking.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaleExtraBooking : Fragment() {
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
        return inflater.inflate(R.layout.fragment_sale_extra_booking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var nav_bar = view.findViewById<BottomNavigationView>(R.id.saleExtra_navBar)
        var upload = view.findViewById<ScrollView>(R.id.upload_booking_include)
        var status =view.findViewById<FrameLayout>(R.id.status_include)
        upload.visibility=View.VISIBLE
        status.visibility=View.GONE
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.upload_booking->{
                    upload.visibility=View.VISIBLE
                    status.visibility=View.GONE
                    true
                }
                R.id.sale_status->{
                    upload.visibility=View.GONE
                    status.visibility=View.VISIBLE
                    childFragmentManager.beginTransaction().replace(R.id.status_include,
                        allRideRS()
                    ).commit()
                    true
                }


                else -> {
                    true
                }
            }

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaleExtraBooking.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaleExtraBooking().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}