package com.pearl.figgodriver.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.figgodriver.Fragment.PartnerSedanAdapter

import com.pearl.FiggoPartner.Model.PartnerSedan
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentDriverHomeBinding

//import com.example.figgodriver.model.Sedan


class DriverHomeFragment : Fragment() {
    lateinit var binding: FragmentDriverHomeBinding
    lateinit var sedanAdapter: PartnerSedanAdapter
    var data=ArrayList<PartnerSedan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding=DataBindingUtil.inflate(inflater,R.layout.fragment_driver_home,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sedanRecycler.layoutManager= LinearLayoutManager(requireContext())
        data.add(PartnerSedan("Sedan","265","Etios,Dzire or similar","25.10.2022","8:00am","Chandigarh, Sector 35, India","Ambala","Panipath","Delhi","Chandigarh",15))
        data.add(PartnerSedan("Sedan","265","Etios,Dzire or similar","25.10.2022","8:00am","Chandigarh, Sector 35, India","Ambala","Panipath","Delhi","Chandigarh",15))
        sedanAdapter= PartnerSedanAdapter(data)
        binding.sedanRecycler.adapter=sedanAdapter
        var back = view.findViewById<TextView>(R.id.top_back)
        back.setOnClickListener {
            //activity?.moveTaskToBack(true);
            activity?.finish();
        }
//
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            activity?.moveTaskToBack(true);
//            activity?.finish();
//        }
    }


}