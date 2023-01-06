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
import androidx.test.core.app.ApplicationProvider.getApplicationContext
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
import com.google.android.gms.common.SignInButton
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
    lateinit var vieW: View
    lateinit var googleApiClient: GoogleApiClient
    private val RC_SIGN_IN = 1
    var googleSignInClient: GoogleSignInClient? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*FirebaseApp.initializeApp(requireContext())
        firebaseAuth = FirebaseAuth.getInstance()
*/      vieW = view
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



//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken("830098883348-8upo0dha04flmjuu9p1ikqp5t4kk8h34.apps.googleusercontent.com").requestEmail().build()
//
//        googleSignInClient= GoogleSignIn.getClient(requireContext(),gso)

      /*  googleApiClient = GoogleApiClient.Builder(requireContext())
            .enableAutoManage(requireActivity(),this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()*/
  /*      binding.signInButton.setOnClickListener {
            val intent = googleSignInClient!!.signInIntent
            //val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent,100);
           // startActivityForResult(intent, RC_SIGN_IN)
        }
*/

        // Initialize firebase user
        // Initialize firebase user
       /* val firebaseUser = firebaseAuth!!.currentUser
        // Check condition
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in
            // redirect to profile activity
            Toast.makeText(requireContext(),"successfully login",Toast.LENGTH_SHORT).show()
        }
*/
        /*  binding.email.setOnClickListener{
            binding.inputEmail.isVisible=true
            binding.llNumber.isVisible=false
        }*/


        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
       var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        //updateUI(account)

// Set the dimensions of the sign-in button.
        // Set the dimensions of the sign-in button.
        val signInButton: SignInButton = view.findViewById(com.pearl.figgodriver.R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            val signInIntent: Intent = googleSignInClient!!.getSignInIntent()
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }



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

/* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
     if (requestCode === 100) {
         // When request code is equal to 100
         // Initialize task
         val signInAccountTask = GoogleSignIn
             .getSignedInAccountFromIntent(data)

         // check condition
         if (signInAccountTask.isSuccessful) {
             // When google sign in successful
             // Initialize string
             val s = "Google sign in successful"
             // Display Toast
             displayToast(s)
             // Initialize sign in account
             try {
                 // Initialize sign in account
                 val googleSignInAccount = signInAccountTask
                     .getResult(ApiException::class.java)
                 // Check condition
                 if (googleSignInAccount != null) {
                     // When sign in account is not equal to null
                     // Initialize auth credential
                     val authCredential = GoogleAuthProvider
                         .getCredential(
                             googleSignInAccount.idToken, null
                         )
                     // Check credential
                     firebaseAuth!!.signInWithCredential(authCredential)
                         .addOnCompleteListener(OnCompleteListener {
                             if (it.isSuccessful){
                                 displayToast("Firebase authentication successful")
                             }
                             else{
                                 displayToast("Authentication Failed :"+it.getException()?.message)
                             }
                         })
                 }
             } catch (e: ApiException) {
                 e.printStackTrace()
             }
         }
     }
    }*/

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
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d("Account ",""+account.account)

            prefManager.setAccountDetails(account.account.toString(),account.displayName.toString(),account.photoUrl.toString())
           // prefManager.setToken("")
            Toast.makeText(requireContext(),"Signed In :"+account.account.toString(),Toast.LENGTH_LONG).show()
            if (prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")) {
                Navigation.findNavController(vieW).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
            } else {
Navigation.findNavController(vieW).navigate(R.id.action_verifyNumber2_to_waitingRegistration)
                    //startActivity(Intent(requireContext(),DriverDashBoard::class.java))
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

}
