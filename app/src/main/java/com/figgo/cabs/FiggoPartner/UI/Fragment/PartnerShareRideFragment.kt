package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.RequestDetails
import com.figgo.cabs.figgodriver.Fragment.allRideRS
import com.figgo.cabs.figgodriver.Fragment.createRS


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerShareRideFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PartnerShareRideFragment : Fragment() {
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

        return inflater.inflate(R.layout.fragment_partner_share_ride, container, false)
    }


    private lateinit var back: TextView;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var create: Button = view.findViewById<Button>(R.id.rideshare_create)
        var allride = view.findViewById<Button>(R.id.rideshare_allride)
        back = view.findViewById(R.id.top_back)
        var createFrag = createRS()
        var allRidefrag = allRideRS()
        var driverdetails = RequestDetails()
        var details = view.findViewById<Button>(R.id.Request_Detals)
        setfragment(createFrag)
        //back = view.findViewById<TextView>(R.id.back_button)
        create.setOnClickListener{
            setfragment(createFrag)
            allride.setBackgroundResource(R.color.purple_200)
            allride.setTextColor(resources.getColor(R.color.white))
            create.setBackgroundColor(resources.getColor(R.color.white))
            create.text = "CREATE"
            create.setTextColor(resources.getColor(R.color.orange))
        }
        allride.setOnClickListener {
            setfragment(allRidefrag)
            create.setBackgroundResource(R.color.purple_200)
            create.setTextColor(resources.getColor(R.color.white))
            allride.setBackgroundColor(resources.getColor(R.color.white))
            allride.setTextColor(resources.getColor(R.color.orange))
        }
        back.setOnClickListener {
Navigation.findNavController(view).navigate(R.id.action_share_ride_to_home2)
        }
//        details.setOnClickListener {
//            setfragment(driverdetails)
//            create.setBackgroundColor(resources.getColor(R.color.white))
//            create.setTextColor(resources.getColor(R.color.orange))
//            create.text = "DETAILS"
//            allride.setBackgroundResource(R.color.purple_200)
//            allride.setTextColor(resources.getColor(R.color.white))
//        }

    }

    private fun setfragment(frag: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.frameRS, frag)
            commit()
        }
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerShareRideFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerShareRideFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}