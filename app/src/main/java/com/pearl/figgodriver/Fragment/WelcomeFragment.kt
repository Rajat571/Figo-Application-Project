package com.pearl.figgodriver.Fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.pearl.PrefManager
import com.pearl.figgodriver.R



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

            if(prefManager.getToken().equals("") || prefManager.getToken().equals("null")){
                Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_verifyNumber2)
            }else{
                if (prefManager.getRegistrationToken().equals("") || prefManager.getRegistrationToken().equals("null"))
                {
                    Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_figgo_FamilyFragment)
                }else{
                    //startActivity(Intent(requireContext(),DriverDashBoard::class.java))
                    Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_waitingRegistration)
                }

            }

        },2000)
    }


}