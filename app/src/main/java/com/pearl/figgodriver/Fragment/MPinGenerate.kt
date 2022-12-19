package com.pearlorganisation.figgo.UI.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentMPinGenerateBinding
import com.google.android.gms.tasks.Task
import com.pearlorganisation.PrefManager
//import com.pearl.figgodriver.R
//import com.example.figgodriver.R

import kotlinx.android.synthetic.main.fragment_m_pin_generate.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MPinGenerate : Fragment() {
    lateinit var binding: FragmentMPinGenerateBinding


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
        binding.continuetv.setOnClickListener {
            if (mpin.text.toString().equals(confirm_mpin.text.toString())) {
                pref.setMpin(mpin.text.toString())
                Navigation.findNavController(view)
                    .navigate(R.id.action_MPinGenerate_to_figgo_FamilyFragment)
            } else {
                Toast.makeText(this.context, "MPin not match", Toast.LENGTH_SHORT).show()
            }
        }
        binding.exit.setOnClickListener {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
    }
}