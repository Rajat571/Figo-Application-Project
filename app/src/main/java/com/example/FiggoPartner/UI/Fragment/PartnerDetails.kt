package com.example.FiggoPartner.UI.Fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.figgodriver.R


class PartnerDetails : Fragment() {

    lateinit var up_adharfront: ImageView;
    lateinit var up_adharback: ImageView;
    lateinit var imageuri: Uri;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partner_details, container, false)
    }
    private val contract1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        up_adharfront.setImageURI(it)
        // upload()

    }
    private val contract2 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        up_adharback.setImageURI(it)
        // upload()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var next=view.findViewById<TextView>(R.id.next_button)
        var back=view.findViewById<TextView>(R.id.back_button)

        up_adharback = view.findViewById(R.id.adhar_back)
        up_adharfront = view.findViewById(R.id.adhar_front)

        up_adharfront.setOnClickListener {
            contract1.launch("image/*")
        }
        up_adharback.setOnClickListener {
            contract2.launch("image/*")
        }

        next.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_partnerDetails_to_driverDetailsFragment)
        }
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_partnerDetails_to_partnerWelcomeFragment)
        }

    }

}