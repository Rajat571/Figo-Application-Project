package com.pearl.figgodriver.Fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
//import androidx.transition.AutoTransition
//import androidx.transition.TransitionManager
import com.pearl.FiggoPartner.Model.AllRideData
import com.pearl.figgodriver.R
import kotlinx.android.synthetic.main.fragment_all_ride_r_s.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class allRideRS : Fragment() {

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
        return inflater.inflate(R.layout.fragment_all_ride_r_s, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allrideData = ArrayList<AllRideData>();
        val submit = view.findViewById<Button>(R.id.submit)

       // val cardView=view.findViewById<CardView>(R.id.card)
        allrideData.add(AllRideData("6:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
        allrideData.add(AllRideData("7:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
        allrideData.add(AllRideData("8:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))

//        submit.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_allRideRS_to_requestDetails)
//        }



        val recyclerView1 = view.findViewById<RecyclerView>(R.id.allride_recyclerview)
      //  val recyclerView2 = view.findViewById<RecyclerView>(R.id.drop_recycler)
        recyclerView1.adapter = AllRideAdapter(allrideData)
        recyclerView1.layoutManager = LinearLayoutManager(context)
//        submit.setOnClickListener {
//            startActivity(Intent(context,RequestAllride::class.java))
//        }
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            allRideRS().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}