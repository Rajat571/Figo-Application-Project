package com.example.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.FiggoPartner.UI.Fragment.PartnerWelcomeFragment
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentFiggoFamilyBinding
import com.example.hotelPartner.UI.Fragment.FiggoHotelPartner

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
             var args = Bundle()
             args.putString("Parent","TaxiPartner");
             Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_partnerWelcomeFragment,args)
         }
     }
        binding.figgoDriver.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                var args = Bundle()
                args.putString("Parent","Driver");
                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_partnerWelcomeFragment,args)
            }
        }
        binding.hotelPartner.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
             //   var hotelPartner = PartnerWelcomeFragment()
                var args = Bundle()
                args.putString("Parent","HotelPartner");
//                hotelPartner.arguments = args;
                //childFragmentManager.beginTransaction().add(R.id.nav_controller,hotelPartner).commit()

                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_partnerWelcomeFragment,args)

            }
        }
    }
}