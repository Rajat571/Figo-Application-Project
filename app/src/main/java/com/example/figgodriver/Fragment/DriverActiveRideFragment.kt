package com.example.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.figgodriver.Adapter.ActiveRideAdapter
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentDriverActiveRideBinding
import com.example.figgodriver.model.ActiveRide


class DriverActiveRideFragment : Fragment() {
    lateinit var binding:FragmentDriverActiveRideBinding
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
        super.onViewCreated(view, savedInstanceState)
        binding.activeDriverList.layoutManager= LinearLayoutManager(requireContext())
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))
        datalist.add(ActiveRide("Oneway","20.10.2022","view"))

        activeRideAdapter= ActiveRideAdapter(requireContext(),datalist)
        binding.activeDriverList.adapter=activeRideAdapter
    }
}