package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
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
        var number = view.findViewById<LinearLayout>(R.id.partcall_us)
        var email = view.findViewById<LinearLayout>(R.id.partemail_us)
        var tv2 = view.findViewById<TextView>(R.id.parttextView2)
        tv2.text = "The mission of our company is to provide the most clean and safe cab/taxi services throughout India by ensuring the security of our customers during the whole duration of their trip. We have a mission to provide a comfortable travel/journey experience in the most cost-effective manner possible. We also aim to be known as one of the most renowned and trusted cab/taxi company and become role model when it comes to the concept of operations and giving services to customers."
        var intent_call = Intent(Intent.ACTION_DIAL)
        intent_call.data = Uri.parse("tel:"+"+919715597855")
        number.setOnClickListener {
            startActivity(intent_call)
        }
        var intent_email = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"support@figgocabs.com"))
        email.setOnClickListener{
            startActivity(intent_email)
        }
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