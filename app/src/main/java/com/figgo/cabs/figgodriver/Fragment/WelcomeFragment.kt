package com.figgo.cabs.figgodriver.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.FiggoPartner.UI.Partner_Dashboard
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject


class WelcomeFragment : Fragment() {
    lateinit var prefManager: PrefManager

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
        /*dialog.setTitle("please wait")
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);*/
        //   dialog = ProgressDialog.show(requireContext(), "", "Please wait for approval...")

        // var baseurl="https://test.pearl-developer.com/figo/api/check-status"
        var baseurl= Helper.check_status
        var queue= Volley.newRequestQueue(requireContext())
        var json= JSONObject()
        json.put("token", prefManager.getToken())

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
            }
        })
        {}
        queue.add(jsonObjectRequest)

    }
}