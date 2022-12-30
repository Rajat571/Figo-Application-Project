package com.pearlorganisation.figgo.UI.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.pearl.figgodriver.DriverDashBoard
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentVerifyNumberBinding
import com.pearl.pearllib.BaseClass
import com.pearlorganisation.PrefManager
import org.json.JSONObject


class VerifyNumber : Fragment(),GoogleApiClient.OnConnectionFailedListener  {
    lateinit var binding: FragmentVerifyNumberBinding
    lateinit var queue:RequestQueue
    var base_url="https://test.pearl-developer.com/figo/api/create-driver"
    lateinit var prefManager: PrefManager
    lateinit var baseClass: BaseClass
    lateinit var googleApiClient: GoogleApiClient
    private val RC_SIGN_IN = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding=DataBindingUtil.inflate(inflater, R.layout.fragment_verify_number, container, false)
        var view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prefManager = PrefManager(requireContext())
        baseClass=object :BaseClass(){
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleApiClient = GoogleApiClient.Builder(requireContext())
            .enableAutoManage(requireActivity(),this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        binding.signInButton.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, RC_SIGN_IN)
        }

        /*  binding.email.setOnClickListener{
            binding.inputEmail.isVisible=true
            binding.llNumber.isVisible=false
        }*/
        binding.number.setOnClickListener {
            binding.llNumber.isVisible=true
            binding.inputEmail.isVisible=false
        }

        binding.continuetv.setOnClickListener {

            if ( baseClass.validateNumber(binding.inputNumber)){


                binding.progress.isVisible = true
                binding.chooseUser.isVisible = false

                var mobile_num = binding.inputNumber.text.toString()

                val URL = "https://test.pearl-developer.com/figo/api/create-driver"
                val queue = Volley.newRequestQueue(requireContext())
                val json = JSONObject()
                json.put("mobile", mobile_num)

                val jsonOblect: JsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, URL, json, object :
                        Response.Listener<JSONObject?>               {
                        override fun onResponse(response: JSONObject?) {

                            Log.d("SendData", "response===" + response)
                            if (response != null) {
                                if (prefManager.getToken().equals("") || prefManager.getToken().equals("null")) {
                                    val token = response.getString("token")
                                    val profile_status = response.getString("profile_status").toInt()
                                    prefManager.setToken(token)
                                    prefManager.setisValidLogin(true)
                                   // prefManager.setFirstTimeLaunch(true)

                                    Toast.makeText(requireContext(), "Login Successfully", Toast.LENGTH_SHORT).show()
                                    Log.d("SendData", "token===" + token)

                                    if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")) {
                                             Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                                    } else {
                                        if(profile_status == 0){
                                            Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                                        }else{
                                            startActivity(Intent(requireContext(),DriverDashBoard::class.java))
                                        }
                                         }
                                } else {
                                    val token = response.getString("token")
                                    val profile_status = response.getString("profile_status").toInt()
                                    prefManager.setToken(token)
                                    prefManager.setisValidLogin(true)
                                    Toast.makeText( requireContext(), "Login Successfully", Toast.LENGTH_SHORT).show()
                                    if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")) {
                                        Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                                    } else {
                                        if(profile_status == 0){
                                            Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                                        }else{
                                            startActivity(Intent(requireContext(),DriverDashBoard::class.java))
                                        }
                                    }
                                }
                                binding.progress.isVisible = false
                                binding.chooseUser.isVisible = true

                            }
                            // Get your json response and convert it to whatever you want.
                        }
                    }, object : Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError?) {
                            // Error
                        }
                    }) {
                        /*     @Throws(AuthFailureError::class)
                         override fun getHeaders(): Map<String, String> {
                             val headers: MutableMap<String, String> = HashMap()
                             headers["Authorization"] = "TOKEN" //put your token here
                             return headers
                         }*/
                    }

                queue.add(jsonOblect)
            }
        }
        }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

 override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
           /* val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            handleSignInResult(result)*/
        }
    }

    private fun handleSignInResult(result: Task<GoogleSignInAccount>) {
        Log.d("TAG", "result====" +result);
        /* if (result!!.isSuccess) {
            Toast.makeText(requireContext(),"google signin successful",Toast.LENGTH_SHORT).show()


        } else {
            Toast.makeText(requireContext(),"Sign in cancel", Toast.LENGTH_LONG).show()
        }*/
        try {
            val account: GoogleSignInAccount = result.getResult(ApiException::class.java)
            if (result.isSuccessful){

                Toast.makeText(requireContext(),"google signin successful",Toast.LENGTH_SHORT).show()
            }

            // Signed in successfully, show authenticated UI.
            //updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }

    }
}
