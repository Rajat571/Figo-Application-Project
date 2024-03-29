package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import com.figgo.cabs.FiggoPartner.UI.Partner_Dashboard
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.Figgo_Capton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerEndScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class PartnerEndScreen : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_partner_end_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var addmore = view.findViewById<CardView>(R.id.addmore_drivers)
        var partnerdash = view.findViewById<CardView>(R.id.partner_dashboard)
        var data = Bundle()
        data.putString("Parent","Partner");
        addmore.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_partner_end_screen_to_figgo_Capton)
        }
        partnerdash.setOnClickListener {
            startActivity(Intent(requireContext(),Partner_Dashboard::class.java))
        }
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //   Log.d(TAG, "Fragment back pressed invoked")
                    // Do custom work here
                    Navigation.findNavController(view).navigate(R.id.action_partner_end_screen_to_figgo_FamilyFragment)
                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
            )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerEndScreen.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerEndScreen().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}