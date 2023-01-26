package com.figgo.cabs.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.R


import kotlinx.android.synthetic.main.bottom_button_layout.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverRideSaleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DriverRideSaleFragment : Fragment() {
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

        
        return inflater.inflate(R.layout.fragment_driver_ride_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //var f1 = RequestDetailsOne()
        //setfragment(f1)
        val allrideData = ArrayList<AllRideData>();
        var main1 = view.findViewById<LinearLayout>(R.id.main_1)
        var topbutton = view.findViewById<LinearLayout>(R.id.top_buttons)
        var details = view.findViewById<LinearLayout>(R.id.details_view)
        var allride = view.findViewById<ConstraintLayout>(R.id.allrideview)
        var details_button = view.findViewById<Button>(R.id.details_button)
        var allride_button = view.findViewById<Button>(R.id.allride_button)
//        var details_view = view.findViewById<LinearLayout>(R.id.details_view)
//        var allride_view = view.findViewById<LinearLayout>(R.id.details_view)
        var submit = view.findViewById<Button>(R.id.sumit)
        var allRidefrag = allRideRS()
    //    var back = view.findViewById<TextView>(R.id.top_back)
        main1.visibility = View.VISIBLE
        topbutton.visibility = View.GONE
        details.visibility = View.GONE
        allride.visibility = View.GONE

        submit.setOnClickListener {
            main1.visibility = View.GONE
            topbutton.visibility = View.VISIBLE
            details.visibility = View.VISIBLE
            allride.visibility = View.GONE
        }

        details_button.setOnClickListener{
            details.visibility = View.VISIBLE
            allride.visibility = View.GONE
        }

        allride_button.setOnClickListener {
            details.visibility = View.GONE
            allride.visibility = View.VISIBLE
            //setfragment(allRidefrag)

            allrideData.add(AllRideData("6:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            allrideData.add(AllRideData("7:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            allrideData.add(AllRideData("8:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            val recyclerView1 = view.findViewById<RecyclerView>(R.id.frameRSall)
            //  val recyclerView2 = view.view.findViewById<RecyclerView>(R.id.drop_recycler)
            recyclerView1.adapter = AllRideAdapter(allrideData)
            recyclerView1.layoutManager = LinearLayoutManager(context)

        }

//        back.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_ride_sale_to_home)
//        }
    }

    private fun setfragment(frag: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.frameRS,frag)
            commit()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DriverRideSaleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}