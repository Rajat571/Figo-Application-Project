package com.pearl.figgodriver.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.pearl.figgodriver.DriverDashBoard
import com.pearl.figgodriver.R
import com.pearlorganisation.PrefManager


class WelcomeFragment : Fragment() {
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_driver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager= PrefManager(requireContext())
        Handler().postDelayed({
            if (!prefManager.getRegistrationToken().equals(""))
            {
                startActivity(Intent(requireContext(),DriverDashBoard::class.java))
                //Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_waitingRegistration)
            }
            else if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null"))
         {
             Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_verifyNumber2)
         } else{
                Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_figgo_FamilyFragment)
            }
           /* if (prefManager.isFirstTimeLaunch()){
                                  Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_verifyNumber2)
                              }
            else   {
                Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_figgo_FamilyFragment)
                       }*/

        },2000)
    }


}