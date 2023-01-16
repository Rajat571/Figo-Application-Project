package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentPartnerWelcomeBinding


class PartnerWelcomeFragment : Fragment() {
    lateinit var binding: FragmentPartnerWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,
            R.layout.fragment_partner_welcome, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val check=binding.terms
        val accept = binding.accept
        val cancel = binding.cancel
        val value = arguments?.getString("Parent");
        if(value=="Hotel Partner"){
        accept.setOnClickListener {
            if (check.isChecked)
            Navigation.findNavController(view).navigate(R.id.action_partnerWelcomeFragment_to_figgoHotelPartner)
            else
                Toast.makeText(requireContext(),"Please accept the terms and conditions.",Toast.LENGTH_SHORT).show()
        }
        cancel.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_partnerWelcomeFragment_to_figgo_FamilyFragment)
        }
        }
        else{
        accept.setOnClickListener {
            if (check.isChecked)
                Navigation.findNavController(view).navigate(R.id.action_partnerWelcomeFragment_to_partnerDetails)
            else
                Toast.makeText(requireContext(),"Please accept the terms and conditions.",Toast.LENGTH_SHORT).show()
        }
            cancel.setOnClickListener {
                Navigation.findNavController(view).navigate(R.id.action_partnerWelcomeFragment_to_figgo_FamilyFragment)
            }
        } }

}