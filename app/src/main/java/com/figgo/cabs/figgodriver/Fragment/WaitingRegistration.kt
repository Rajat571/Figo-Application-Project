package com.figgo.cabs.figgodriver.Fragment

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class WaitingRegistration : Fragment() {
    lateinit var prefManager: PrefManager
    lateinit var dialog:ProgressDialog
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
        prefManager=PrefManager(requireContext())
        var number = view.findViewById<LinearLayout>(R.id.call_us)
        var email = view.findViewById<LinearLayout>(R.id.email_us)
        var exit = view.findViewById<Button>(R.id.exit)
        var intent_call = Intent(Intent.ACTION_DIAL)
        var pref = PrefManager(requireContext())
        var name:String = pref.getDriverName()
        var tv2 = view.findViewById<TextView>(R.id.textView2)
        val args = arguments
        val waiting_layout = view.findViewById<CardView>(R.id.waiting_cardview)
        if (args != null) {
            if (pref.getDashboard().equals("on"))
                waiting_layout.visibility = View.GONE
            else
                waiting_layout.visibility = View.VISIBLE
        }
        checkstatus(view)
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

    private fun checkstatus(view: View) {
        /*dialog.setTitle("please wait")
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);*/
        dialog = ProgressDialog.show(requireContext(), "", "Please wait for approval...")

       // var baseurl="https://test.pearl-developer.com/figo/api/check-status"
        var baseurl=Helper.check_status
        var queue=Volley.newRequestQueue(requireContext())
        var json=JSONObject()
        json.put("token", prefManager.getToken())

        var jsonObjectRequest=object :JsonObjectRequest(Method.POST,baseurl,json,Response.Listener<JSONObject>{
            response ->

            Log.d("WaitingRegistraion","RESPONSE=="+response)

            var user=response.getJSONObject("user")
           // var location = user.getJSONObject("location")
            var status=user.getString("status")
            var name=user.get("name")
            var num=user.getString("mobile")
            /*prefManager.setDriverName(name.toString())
            prefManager.setMobile_No(num.toString())*/

            Log.d("WaitingRegistraion","status=="+status)

            if (status.equals(0)||status.toInt()==0){

                dialog.setCancelable(true)
            }
            else{
                dialog.hide()
                prefManager.setRegistrationToken("Done")

               // prefManager.setToken(user.getString("token"))
                prefManager.setReferal(user.getString("referal_link"))
                startActivity(Intent( requireContext(), DriverDashBoard::class.java))
            }
        },object :Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
               Log.d("WaitingRegistraion","ERROR"+error)
            }
        })
        {}
        queue.add(jsonObjectRequest)

    }

}