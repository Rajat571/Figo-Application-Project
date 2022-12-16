package com.pearl.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.navigation.Navigation
import com.pearl.figgodriver.R

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
        return inflater.inflate(R.layout.fragment_driver_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var taxi_cab=view.findViewById<RadioButton>(R.id.taxi_cab)
       taxi_cab.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                Navigation.findNavController(view).navigate(R.id.action_driverWelcomeFragment_to_figgo_Capton)
            }
        }
        var back =view.findViewById<TextView>(R.id.back_button)
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_driverWelcomeFragment_to_figgo_FamilyFragment)
        }
    }
}