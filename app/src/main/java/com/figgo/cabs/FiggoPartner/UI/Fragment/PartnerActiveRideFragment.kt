package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.figgo.cabs.FiggoPartner.Adapter.PartnerActiveRideAdapter
import com.figgo.cabs.FiggoPartner.Model.PartnerActiveRide
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentPartnerActiveRideBinding


class PartnerActiveRideFragment : Fragment() {
    lateinit var binding: FragmentPartnerActiveRideBinding
    lateinit var activeRideAdapter: PartnerActiveRideAdapter
    var datalist=ArrayList<PartnerActiveRide>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      binding=DataBindingUtil.inflate(inflater,
          R.layout.fragment_partner_active_ride, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.activeDriverList.layoutManager= LinearLayoutManager(requireContext())

        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))
        datalist.add(PartnerActiveRide("Oneway","20.10.2022","view"))

        activeRideAdapter=
         PartnerActiveRideAdapter(
                requireContext(),
                datalist
            )
        binding.activeDriverList.adapter=activeRideAdapter
        var back = view.findViewById<TextView>(R.id.top_back)
        back.setOnClickListener {
         Navigation.findNavController(view).navigate(R.id.action_active_ride_to_home2)
        }
    }
}