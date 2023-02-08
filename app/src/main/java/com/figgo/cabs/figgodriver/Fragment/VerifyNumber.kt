package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentVerifyNumberBinding
import com.figgo.cabs.figgodriver.UI.DriverDashBoard

import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.Helper
import kotlinx.android.synthetic.main.fragment_m_pin_generate.*

import org.json.JSONObject


class VerifyNumber : Fragment(),GoogleApiClient.OnConnectionFailedListener  {
    lateinit var binding: FragmentVerifyNumberBinding
    lateinit var queue:RequestQueue
    var base_url="https://test.pearl-developer.com/figo/api/create-driver"
    lateinit var prefManager: PrefManager
    lateinit var baseClass: BaseClass
    lateinit var vieW: View
    lateinit var googleApiClient: GoogleApiClient
    private val RC_SIGN_IN = 1

    var driver_id:String=""
    var profile_status=0
    var token:String=""
    var mobile_num=""
    var getotp=0
    var status=0
   lateinit var view_timer:TextView
    lateinit var cTimer:CountDownTimer
    var googleSignInClient: GoogleSignInClient? = null
    lateinit var dialog:ProgressDialog
    //var firebaseAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

         binding=DataBindingUtil.inflate(inflater, R.layout.fragment_verify_number, container, false)
        var view=binding.root

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog= ProgressDialog(requireContext())
        startTimer()

      vieW = view
        var otp_screen = view.findViewById<CardView>(R.id.otp_screen)
        prefManager = PrefManager(requireContext())
        binding.chooseUser.isVisible = true
        otp_screen.visibility=View.GONE
        baseClass=object : BaseClass(){
            override fun setLayoutXml() {
                TODO("Not yet implemented")
            }

            override fun initializeViews() {
                TODO("Not yet implemented")
            }

            override fun initializeClickListners() {
                TODO("Not yet implemented")
            }

            override fun initializeInputs() {
                TODO("Not yet implemented")
            }

            override fun initializeLabels() {
                TODO("Not yet implemented")
            }
        }
       var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        val account = GoogleSignIn.getLastSignedInAccount(requireContext())

     /*   val signInButton: SignInButton = view.findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            val signInIntent: Intent = googleSignInClient!!.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }*/
        binding.number.setOnClickListener {
            binding.llNumber.isVisible=true
            binding.inputEmail.isVisible=false
        }
        var verify_btn=view.findViewById<Button>(R.id.otp_Verify_button)
         view_timer=view.findViewById<TextView>(R.id.view_timer)
        var resentOTP=view.findViewById<Button>(R.id.resent_otp)


         verify_btn.setOnClickListener {
             verifyOTP(view)
        }

        resentOTP.setOnClickListener {
            verifyOTP(view)
         /*   val otp1 = "https://test.pearl-developer.com/figo/api/otp/send-otp"

            val queue2 = Volley.newRequestQueue(requireContext())
            val json2 = JSONObject()
            json2.put("type", "driver")
            json2.put("type_id", driver_id.toInt())
            json2.put("contact_no", mobile_num)

            Log.d("OTP", "json2===" + json2)

            var jsonObjectRequest=object :JsonObjectRequest(Method.POST,otp1,json2,Response.Listener<JSONObject>
            {response ->

                Log.d("VerifyNumber","OTPresponse"+response)
            },object :Response.ErrorListener{
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("VerifyNumber","ERROR"+error)
                    Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                }
            }){

            }
            queue2.add(jsonObjectRequest)*/

        }
        binding.continuetv.setOnClickListener {
            dialog.show()
            if ( baseClass.validateNumber(binding.inputNumber)){

                //binding.progress.isVisible = true
                binding.chooseUser.isVisible = false
                otp_screen.visibility=View.VISIBLE

                 mobile_num = binding.inputNumber.text.toString()
               // val URL = "https://test.pearl-developer.com/figo/api/create-driver"
                var URL=Helper.create_driver
               // val otp1 = "https://test.pearl-developer.com/figo/api/otp/send-otp"
                val otp1=Helper.send_otp
                val queue = Volley.newRequestQueue(requireContext())
                val json = JSONObject()
                json.put("mobile", mobile_num)
                Log.d("VerifyNumber", "response===" + URL)
                val jsonOblect: JsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, URL, json, object :
                        Response.Listener<JSONObject?>               {
                        override fun onResponse(response: JSONObject?) {

                            Log.d("SendData", "response===" + response)
                            if (response != null) {
                                token = response.getString("token")
                                Log.d("SendData", "token===" + token)

                                profile_status = response.getString("profile_status").toInt()
                                 driver_id = response.getJSONObject("user").getString("id")
                               // var user=response.getJSONObject("user")
                              //   status=user.getString("status").toInt()
                               // Log.d("VerifyNumber","STATUS"+status)

                                Log.d("SendData", "check_otp===" + otp1)
                                val queue2 = Volley.newRequestQueue(requireContext())
                                val json2 = JSONObject()
                                json2.put("type", "driver")
                                json2.put("type_id", driver_id.toInt())
                                json2.put("contact_no", mobile_num)

                                Log.d("OTP", "json2===" + json2)

                                var jsonObjectRequest=object :JsonObjectRequest(Method.POST,otp1,json2,Response.Listener<JSONObject>
                                {response ->
                                    getotp=response.getString("otp").toInt()
                                    Log.d("VerifyNumber","getotp"+getotp)
                                    dialog.hide()

                                    Log.d("VerifyNumber","OTPresponse"+response)
                                },object :Response.ErrorListener{
                                    override fun onErrorResponse(error: VolleyError?) {
                                        dialog.hide()
                                        Log.d("VerifyNumber","ERROR"+error)
                                        Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
                                    }
                                }){

                                }
                                queue2.add(jsonObjectRequest)


                            }
                            // Get your json response and convert it to whatever you want.
                        }
                    }, object : Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            // Error
                        }
                    }) {
                             @SuppressLint("SuspiciousIndentation")
                             @Throws(AuthFailureError::class)
                         override fun getHeaders(): Map<String, String> {
                             val headers: MutableMap<String, String> = HashMap()
                                 headers.put("Content-Type", "application/json; charset=UTF-8");
                                 headers.put("Accept","application/vnd.api+json");
                             return headers
                         }
                    }

                queue.add(jsonOblect)
            }
        }
        }

    private fun verifyOTP(view: View) {
        var dialog=ProgressDialog(requireContext())
        dialog.show()
      //  var otp_check_url="https://test.pearl-developer.com/figo/api/otp/check-otp"
        var otp_check_url=Helper.check_otp
        Log.d("VerifyNumber","otp_check"+otp_check_url)

        var input_otp=view.findViewById<TextView>(R.id.enteredOTP).text.toString()
        Log.d("VerifyNumber","input_otp"+input_otp)

        var queue=Volley.newRequestQueue(requireContext())
        var json2=JSONObject()
        json2.put("type", "driver")
        json2.put("type_id", driver_id.toInt())
        json2.put("otp", input_otp.toInt())
        var jsonObjectRequest= @SuppressLint("SuspiciousIndentation")
        object :JsonObjectRequest(Method.POST,otp_check_url,json2,Response.Listener<JSONObject>
        {response ->

            Log.d("VerifyNumber","OTP Check RESPONSE"+response)

                if (input_otp.toInt()!=getotp) {
                    dialog.hide()
                    Toast.makeText(requireContext(), "incorrect otp"+response.getString("message"), Toast.LENGTH_SHORT).show()
                } else{
                    dialog.hide()
                    prefManager.setToken(token)

                    prefManager.setisValidLogin(true)
                    var user=response.getJSONObject("user")
                    var  statuss=user.getString("status").toInt()

                    if (statuss.equals(0)) {
                    dialog.hide()
                /*    if (prefManager.getToken().equals("") || prefManager.getToken().equals("null")) {*/
                     /*   if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")) {
                            Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                        } else {*/
                            if (profile_status==0 || prefManager.getRegistrationToken().equals("")) {
                                Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                            }
                           /* else if (status.equals(0)||status==0){
                                Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                            }*/
                            else {
                                startActivity(Intent(requireContext(), DriverDashBoard::class.java))
                            }
                        //}
                 //   }
               /*     else {
                      *//*  if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")
                        ) {
                            Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                        } else {*//*
                            if (profile_status == 0 || prefManager.getRegistrationToken().equals("")
                            ) {
                                Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                            } else {
                                startActivity(Intent(requireContext(),DriverDashBoard::class.java)) }
                      //  }
                    }*/
                    // Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                }
                    else{
                        /*   dialog.hide()
                         if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")) {
                               Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                           }
                          else{*/
                        dialog.hide()
                        startActivity(Intent(requireContext(), DriverDashBoard::class.java))
                    }
                }


        },object :Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
                dialog.hide()
                Log.d("VerifyNumber","ERROR"+error)
                Toast.makeText(requireContext(),"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }){}

        queue.add(jsonObjectRequest)

    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }


    private fun displayToast(s: String) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("Account ","tasktask"+task)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d("Account ",""+account.account)
            if (completedTask.isSuccessful){

                prefManager.setAccountDetails(account.account.toString(),account.displayName.toString(),account.photoUrl.toString())
                // prefManager.setToken("")
                Toast.makeText(requireContext(),"Signed In :"+account.account.toString(),Toast.LENGTH_LONG).show()
                if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")) {
                    Navigation.findNavController(vieW).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                } else {
                    Navigation.findNavController(vieW).navigate(R.id.action_verifyNumber2_to_waitingRegistration)
                    //startActivity(Intent(requireContext(),DriverDashBoard::class.java))
                }
            }
            else{
                Toast.makeText(requireContext(),"failed",Toast.LENGTH_SHORT).show()
            }

            // Signed in successfully, show authenticated UI.
           // updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("SignIN = ", "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }

    fun startTimer() {

        cTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view_timer.setText("seconds remaining: " + (millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                view_timer.setText("Re send OTP!")

            }
        }
        cTimer.start()
    }
}
