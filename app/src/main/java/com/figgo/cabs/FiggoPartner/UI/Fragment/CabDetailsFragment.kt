package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import com.figgo.cabs.FiggoPartner.UI.Partner_Dashboard
import com.figgo.cabs.R


class CabDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cab_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var next=view.findViewById<TextView>(R.id.next_button)
        var back=view.findViewById<TextView>(R.id.back_button)
        next.setOnClickListener {
            startActivity(Intent(context, Partner_Dashboard::class.java))
        }
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_cabDetailsFragment_to_driverDetailsFragment)
        }


    }

}