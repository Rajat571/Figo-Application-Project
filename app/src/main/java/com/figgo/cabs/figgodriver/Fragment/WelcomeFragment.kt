package com.figgo.cabs.figgodriver.Fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.FiggoPartner.UI.Partner_Dashboard
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject


class WelcomeFragment : Fragment() {
    lateinit var prefManager: PrefManager
    lateinit var dialog:Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_driver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager= PrefManager(requireContext())
        dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.badconnectionlayout)
        dialog.getWindow()?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        var reconnectNow = dialog.findViewById<TextView>(R.id.reconnect)
        var reconnectlater = dialog.findViewById<TextView>(R.id.reconnectlater)
        reconnectNow.setOnClickListener {
            dialog.dismiss()
            checkstatus(view)
        }
        reconnectlater.setOnClickListener {
            dialog.dismiss()
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
        Handler().postDelayed({
            if(prefManager.getToken().equals("") || prefManager.getToken().equals("null")){
                Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_verifyNumber2)
            }else{

                if (prefManager.getRegistrationToken().equals("") || prefManager.getRegistrationToken().equals("null"))
                {
                    Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_figgo_FamilyFragment)
                }else{
                    checkstatus(requireView())
                   /* val bundle = Bundle()
                    bundle.putInt("Key",1)

                // startActivity(Intent(requireContext(), DriverDashBoard::class.java))
                    if(prefManager.getUserType().equals("Partner"))
                        startActivity(Intent(requireContext(),Partner_Dashboard::class.java))
                    else
                  Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_waitingRegistration,bundle)*/
                }
            }
       // }
        },2000)
    }
    private fun checkstatus(view: View) {
        // var baseurl="https://test.pearl-developer.com/figo/api/check-status"
        var baseurl= Helper.check_status
        var queue= Volley.newRequestQueue(requireContext())
        var json= JSONObject()
        //json.put("token", prefManager.getToken())

        var jsonObjectRequest=object :
            JsonObjectRequest(Method.POST,baseurl,json, Response.Listener<JSONObject>{
                response ->

            Log.d("WaitingRegistraion","RESPONSE=="+response)

            var user=response.getJSONObject("user")

            // var location = user.getJSONObject("location")
            var status=user.getString("status")
            var name=user.get("name")

            var num=user.getString("mobile")
            var user_type=user.getString("user_type")
            /*prefManager.setDriverName(name.toString())
            prefManager.setMobile_No(num.toString())*/
            prefManager.setDriverName(name.toString())
            Log.d("WaitingRegistraion","status=="+status)

            if (status.equals(0)||status.toInt()==0){
                val bundle = Bundle()
                bundle.putInt("Key",1)
                Navigation.findNavController(view).navigate(R.id.action_welcomeDriverFragment_to_waitingRegistration,bundle)
                //  dialog.setCancelable(true)
            } else{

                prefManager.setRegistrationToken("Done")
                prefManager.setReferal(user.getString("referal_link"))

                if (user_type=="Partner"){
                    //  dialog.hide()
                    startActivity(Intent(requireContext(),Partner_Dashboard::class.java))
                }
                else  if (user_type=="Driver"){
                    // dialog.hide()
                    startActivity(Intent( requireContext(), DriverDashBoard::class.java))
                }

            }
        },object : Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                Log.d("WaitingRegistraion","ERROR"+error)
                dialog.show()
            }
        })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers
            }
            }
        queue.add(jsonObjectRequest)

/*        var string=""
        val stringRequest: StringRequest = object : StringRequest(Method.POST, baseurl,
            Response.Listener {
                              Log.d("StringResponse","$it")
            }, Response.ErrorListener { }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer" + prefManager.getToken())
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Accept", "application/vnd.api+json");
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return "Your JSON body".toByteArray()
            }
        }
        queue.add(stringRequest)*/

    }
}