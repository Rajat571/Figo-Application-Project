package com.example.FiggoPartner.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FiggoPartner.Adapter.PartnerActiveRideAdapter
import com.example.FiggoPartner.Model.PartnerActiveRide
import com.example.figgodriver.Adapter.ActiveRideAdapter
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentPartnerActiveRideBinding
import com.example.figgodriver.model.ActiveRide

class PartnerActiveRideFragment : Fragment() {
    lateinit var binding:FragmentPartnerActiveRideBinding
    lateinit var activeRideAdapter: PartnerActiveRideAdapter
    var datalist=ArrayList<PartnerActiveRide>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      binding=DataBindingUtil.inflate(inflater,R.layout.fragment_partner_active_ride, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.activeDriverList.layoutManager= LinearLayoutManager(requireContext())

        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))

        activeRideAdapter=PartnerActiveRideAdapter(requireContext(),datalist)
        binding.activeDriverList.adapter=activeRideAdapter
    }
}