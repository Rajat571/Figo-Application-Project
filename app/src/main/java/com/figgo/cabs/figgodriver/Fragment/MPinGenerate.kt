package com.figgo.cabs.figgodriver.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentMPinGenerateBinding

import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.pearllib.BaseClass

//import com.pearl.figgodriver.R
//import com.example.figgodriver.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MPinGenerate : Fragment() {
    lateinit var dialog: ProgressDialog
    lateinit var binding: FragmentMPinGenerateBinding

    var baseClass=object :BaseClass(){
        override fun setLayoutXml() {
            TODO("Not yet implemented")
        }

        override fun initializeViews() {
            TODO("Not yet implemented")
        }

        override fun initializeClickListners() {
            TODO("Not yet implemented")
        }

        override fun initializeInputs() {
            TODO("Not yet implemented")
        }

        override fun initializeLabels() {
            TODO("Not yet implemented")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_m_pin_generate, container, false)
        var view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mpin = view.findViewById<EditText>(R.id.mPin)
        var pref = PrefManager(requireContext())

        val confirm_mpin = view.findViewById<EditText>(R.id.confirm_mpin)

        dialog= ProgressDialog(requireContext())
        binding.continuetv.setOnClickListener {
            dialog.show()
            baseClass.validateMpin(mpin)
            baseClass.validateMpin(confirm_mpin)
            if (baseClass.validateMpin(mpin)&&baseClass.validateMpin(confirm_mpin)){
            if (mpin.text.toString().equals(confirm_mpin.text.toString())) {
                dialog.hide()
                pref.setMpin(mpin.text.toString())
               /* if(pref.getRegistrationToken().equals("") || pref.getRegistrationToken().equals("null")){
                    Navigation.findNavController(view).navigate(R.id.action_MPinGenerate_to_figgo_FamilyFragment)
                }else{*/
                    startActivity(Intent(requireContext(), DriverDashBoard::class.java))
                    //Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_waitingRegistration)
                    // }
            } else {
                dialog.hide()
                Toast.makeText(this.context, "MPin not match", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            dialog.hide()
        }}

        binding.exit.setOnClickListener {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
    }
}