package com.pearl.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.pearl.figgodriver.Adapter.ActiveRideAdapter
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentDriverActiveRideBinding
import com.pearl.figgodriver.model.ActiveRide
import com.pearl.pearllib.BaseClass


class DriverActiveRideFragment : Fragment() {
    lateinit var binding: FragmentDriverActiveRideBinding
    lateinit var activeRideAdapter: ActiveRideAdapter
    var datalist=ArrayList<ActiveRide>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=DataBindingUtil.inflate(inflater,R.layout.fragment_driver_active_ride, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var back = view.findViewById<TextView>(R.id.top_back)
        super.onViewCreated(view, savedInstanceState)

        binding.activeDriverList.layoutManager= LinearLayoutManager(requireContext())
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))

        activeRideAdapter= ActiveRideAdapter(requireContext(),datalist)
        binding.activeDriverList.adapter=activeRideAdapter
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_active_ride_to_home)
        }
    }
}