package com.figgo.cabs.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.figgo.cabs.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DriverWelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DriverWelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var taxi_cab=view.findViewById<CheckBox>(R.id.terms)
        var accept = view.findViewById<Button>(R.id.accept)
       accept.setOnClickListener {
            if (taxi_cab.isChecked){
                Navigation.findNavController(view).navigate(R.id.action_driverWelcomeFragment_to_figgo_Capton)
            }
           else{
               Toast.makeText(requireContext(),"Please agree to terms and conditions first",Toast.LENGTH_SHORT).show()
            }
        }
        var cancel =view.findViewById<TextView>(R.id.cancel)
        cancel.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_driverWelcomeFragment_to_figgo_FamilyFragment)
        }
    }
}