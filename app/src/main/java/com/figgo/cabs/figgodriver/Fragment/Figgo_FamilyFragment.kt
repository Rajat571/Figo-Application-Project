package com.figgo.cabs.figgodriver.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentFiggoFamilyBinding


class Figgo_FamilyFragment : Fragment() {
  lateinit var binding: FragmentFiggoFamilyBinding
    lateinit var prefManager: PrefManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_figgo__family, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        Log.d("onViewCreated","token == "+prefManager.getToken())
        Log.d("onViewCreated","id === "+ prefManager.getUserId())

     binding.taxiPartner.setOnCheckedChangeListener { buttonView, isChecked ->
         if (isChecked){
             var args = Bundle()
             args.putString("Parent","TaxiPartner");

             Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_comingSoonFragment,args)
         }
     }
        binding.figgoDriver.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                var args = Bundle()
                args.putString("Parent","Driver");
                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_driverWelcomeFragment,args)
            }
        }
        binding.hotelPartner.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
             //   var hotelPartner = PartnerWelcomeFragment()
                var args = Bundle()
                args.putString("Parent","HotelPartner");
//                hotelPartner.arguments = args;
                //childFragmentManager.beginTransaction().add(R.id.nav_controller,hotelPartner).commit()

                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_comingSoonFragment,args)

            }
        }
    }
}