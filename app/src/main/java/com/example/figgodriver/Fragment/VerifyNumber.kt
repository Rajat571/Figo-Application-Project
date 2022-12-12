package com.pearlorganisation.figgo.UI.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentVerifyNumberBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyNumber.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyNumber : Fragment() {
    lateinit var binding: FragmentVerifyNumberBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=DataBindingUtil.inflate(inflater, R.layout.fragment_verify_number, container, false)
        var view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var next=view.findViewById<TextView>(R.id.next)
//        next.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_MPinGenerate2)
//        }

         binding.email.setOnClickListener{
            binding.inputEmail.isVisible=true
            binding.inputNumber.isVisible=false
        }
        binding.number.setOnClickListener {
            binding.inputNumber.isVisible=true
            binding.inputEmail.isVisible=false
        }
        binding.continuetv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
        }


    }
}