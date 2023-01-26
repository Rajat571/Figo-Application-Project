package com.figgo.cabs.FiggoPartner.UI.Fragment

import PartnerHomeAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.figgodriver.model.HomeData
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentPartnerHomeBinding




class PartnerHomeFragment : Fragment() {
    lateinit var binding: FragmentPartnerHomeBinding
    lateinit var partnerHomeAdapter:PartnerHomeAdapter
    var data=ArrayList<HomeData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_partner_home, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sedanRecycler.layoutManager= LinearLayoutManager(requireContext())
        data.add(HomeData("Sedan","265","Etios,Dzire or similar","25.10.2022","8:00am","Chandigarh, Sector 35, India","Ambala","Panipath","Delhi","Chandigarh",15))
        data.add(HomeData("Sedan","265","Etios,Dzire or similar","25.10.2022","8:00am","Chandigarh, Sector 35, India","Ambala","Panipath","Delhi","Chandigarh",15))
      partnerHomeAdapter= PartnerHomeAdapter(data)
        binding.sedanRecycler.adapter=partnerHomeAdapter
        var back = view.findViewById<TextView>(R.id.top_back)
        back.setOnClickListener {
            activity?.finish();
        }

    }

}