package com.example.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentFiggoFamilyBinding

class Figgo_FamilyFragment : Fragment() {
  lateinit var binding: FragmentFiggoFamilyBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_figgo__family, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
     binding.taxiPartner.setOnCheckedChangeListener { buttonView, isChecked ->
         if (isChecked){

             Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_partnerWelcomeFragment)
         }
     }
        binding.figgoDriver.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_driverWelcomeFragment)
            }
        }
        binding.hotelPartner.setOnCheckedChangeListener { buttonView, isChecked ->
        }
    }
}