package com.pearl.figgodriver.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pearl.figgodriver.R
import com.pearlorganisation.PrefManager

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WaitingRegistration.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaitingRegistration : Fragment() {
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
        return inflater.inflate(R.layout.fragment_waiting_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var number = view.findViewById<LinearLayout>(R.id.call_us)
        var email = view.findViewById<LinearLayout>(R.id.email_us)
        var exit = view.findViewById<Button>(R.id.exit)
        var intent_call = Intent(Intent.ACTION_DIAL)
        var pref = PrefManager(requireContext())
        var name:String = pref.getDriverName()
        var tv2 = view.findViewById<TextView>(R.id.textView2)
        tv2.text = "Hello $name\n" +
                "You registered an account on Figgo Driver App .Your Document Verification is under process, we will connect with you shortly"
        intent_call.data = Uri.parse("tel:"+"+919715597855")
        number.setOnClickListener {
            startActivity(intent_call)
        }
        var intent_email = Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+"support@figgocabs.com"))
        email.setOnClickListener{
            startActivity(intent_email)
        }
        exit.setOnClickListener {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WaitingRegistration.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WaitingRegistration().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}