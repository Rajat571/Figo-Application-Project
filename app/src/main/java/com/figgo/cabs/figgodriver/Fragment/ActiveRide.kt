package com.figgo.cabs.figgodriver.Fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R


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
        val allrideData = ArrayList<AllRideData>();
        prefManager = PrefManager(requireContext())
        val submit = view.findViewById<Button>(R.id.active_ride_submit)
        var local = view.findViewById<TextView>(R.id.ar_local)
        var outstation = view.findViewById<TextView>(R.id.ar_outstation)

        allrideData.add(AllRideData("6:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
        allrideData.add(AllRideData("7:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
        allrideData.add(AllRideData("8:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))

        prefManager.setActiveRide(0)
        val recyclerView1 = view.findViewById<RecyclerView>(R.id.activeride_recyclerview)
        recyclerView1.adapter = AllRideAdapter(allrideData)
        recyclerView1.layoutManager = LinearLayoutManager(context)
        local.setOnClickListener {
            outstation.setBackgroundColor(Color.LTGRAY)
            local.setBackgroundColor(Color.WHITE)
            recyclerView1.adapter = AllRideAdapter(allrideData)
            recyclerView1.layoutManager = LinearLayoutManager(context)
        }
        outstation.setOnClickListener {
            local.setBackgroundColor(Color.LTGRAY)
            outstation.setBackgroundColor(Color.WHITE)
            recyclerView1.adapter = OutstationAdapter(allrideData)
            recyclerView1.layoutManager = LinearLayoutManager(context)
        }
        submit.setOnClickListener {
            Toast.makeText(requireContext(),"Submitted", Toast.LENGTH_SHORT).show()
        }

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