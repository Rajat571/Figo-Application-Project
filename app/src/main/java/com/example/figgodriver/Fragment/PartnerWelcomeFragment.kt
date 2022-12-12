package com.example.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentPartnerWelcomeBinding


class PartnerWelcomeFragment : Fragment() {
    lateinit var binding:FragmentPartnerWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_partner_welcome, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var next=view.findViewById<TextView>(R.id.next_button)
        next.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_partnerWelcomeFragment_to_partnerDetails)
        }
    }

}