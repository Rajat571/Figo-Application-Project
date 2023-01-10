package com.pearl.figgodriver.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pearl.PrefManager
import com.pearl.figgodriver.Adapter.BuisnessAdapter
import com.pearl.figgodriver.R
import com.pearl.figgodriver.model.BuisnessAd
import kotlinx.android.synthetic.main.change_mpin.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SupportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SupportFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_support, container, false)
    }
    private lateinit var profile_pic:ImageView
    lateinit var imageuri: Uri;
    lateinit var pref:PrefManager
    private val contract1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        profile_pic.setImageURI(it)
        // upload()

    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var bundle = arguments
        var number = view.findViewById<LinearLayout>(R.id.call_us)
        var email = view.findViewById<LinearLayout>(R.id.email_us)
        //var exit = view.findViewById<Button>(R.id.exit)
        var intent_call = Intent(Intent.ACTION_DIAL)
        var pref = PrefManager(requireContext())
        // var name:String = pref.getDriverName()
        var tv2 = view.findViewById<TextView>(R.id.textView2)
        tv2.text = "The mission of our company is to provide the most clean and safe cab/taxi services throughout India by ensuring the security of our customers during the whole duration of their trip. We have a mission to provide a comfortable travel/journey experience in the most cost-effective manner possible. We also aim to be known as one of the most renowned and trusted cab/taxi company and become role model when it comes to the concept of operations and giving services to customers."
        intent_call.data = Uri.parse("tel:"+"+919715597855")
        number.setOnClickListener {
            startActivity(intent_call)
        }
        var intent_email = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"support@figgocabs.com"))
        email.setOnClickListener{
            startActivity(intent_email)
        }
        var support = view.findViewById<ConstraintLayout>(R.id.Support_Nav)
        var about = view.findViewById<ConstraintLayout>(R.id.About_Nav)
        var terms = view.findViewById<ConstraintLayout>(R.id.Terms_Nav)
        var cancel = view.findViewById<ConstraintLayout>(R.id.Cancel_Nav)
        var profile = view.findViewById<LinearLayout>(R.id.Profile_Nav)
        val change_mpin_nav = view.findViewById<LinearLayout>(R.id.Change_MPIN_Nav)
        val cab_nav = view.findViewById<LinearLayout>(R.id.Cab_Nav)
        var buisness_recylcer = view.findViewById<RecyclerView>(R.id.Buisness_Nav_Recycler)
        var str = bundle?.getString("Key")


        profile_pic = view.findViewById<ImageView>(R.id.change_profile_pic_image)
        if(str.equals("About")){
        val myWebView: WebView = view.findViewById(R.id.about_wv)
            support.visibility=View.GONE
            about.visibility=View.VISIBLE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
        myWebView.loadUrl("https://figgocabs.com/about/")
    }else if (str.equals("Support")){
            support.visibility=View.VISIBLE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            cab_nav.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
        }else if (str.equals("Terms")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.VISIBLE
            cancel.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            profile.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
        }else if (str.equals("Cancel")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            profile.visibility=View.GONE
            cab_nav.visibility=View.GONE
            cancel.visibility=View.VISIBLE
            buisness_recylcer.visibility=View.GONE
        }else if (str.equals("Profile")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.VISIBLE
            buisness_recylcer.visibility=View.GONE
        }
        else if(str.equals("Change_Mpin")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            cab_nav.visibility=View.GONE
            profile.visibility=View.GONE
            change_mpin_nav.visibility = View.VISIBLE
            buisness_recylcer.visibility=View.GONE
        }
        else if(str.equals("Cab")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.VISIBLE
            buisness_recylcer.visibility=View.GONE
        }
        else if(str.equals("Buisness")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            buisness_recylcer.visibility=View.VISIBLE
        }


        pref = PrefManager(requireContext())
        profileValidations(view)
        mpinValidations(view)
        figgoBuisness(view)
    }

    private fun figgoBuisness(view: View) {
        var buisness_recylcer = view.findViewById<RecyclerView>(R.id.Buisness_Nav_Recycler)
        var data = ArrayList<BuisnessAd>()
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        buisness_recylcer.adapter=BuisnessAdapter(data,view)
        buisness_recylcer.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun mpinValidations(view: View) {
        var mpin_done = view.findViewById<TextView>(R.id.mpin_done)
        var mpin_exit = view.findViewById<TextView>(R.id.mpin_exit)
        var mpin_old = view.findViewById<EditText>(R.id.old_mpin)
        var mpin_new = view.findViewById<EditText>(R.id.mPin_new)
        var mpin_confirm = view.findViewById<EditText>(R.id.confirm_mpin)

    }

    private fun profileValidations(view: View) {
        var change = view.findViewById<ConstraintLayout>(R.id.change_profile_pic)
        var name = view.findViewById<EditText>(R.id.update_name)
        var vehicle_no = view.findViewById<EditText>(R.id.update_state)
        var mobile_number = view.findViewById<EditText>(R.id.update_number)
        var email = view.findViewById<EditText>(R.id.update_email)
        var update = view.findViewById<Button>(R.id.update_button)

        update.setOnClickListener {
            pref.setDriverName(name.text.toString())
            pref.setMobile_No(mobile_number.text.toString())
            //pref.setDriveState(1)
        }

        change.setOnClickListener {
            contract1.launch("image/*")
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SupportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SupportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}