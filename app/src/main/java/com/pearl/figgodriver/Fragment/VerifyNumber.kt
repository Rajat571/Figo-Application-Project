package com.pearlorganisation.figgo.UI.Fragments

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
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentVerifyNumberBinding
import com.pearlorganisation.PrefManager
import kotlinx.android.synthetic.main.top_layout.*
import org.json.JSONException
import org.json.JSONObject

class VerifyNumber : Fragment() {
    lateinit var binding: FragmentVerifyNumberBinding
    lateinit var queue:RequestQueue
    var base_url="https://test.pearl-developer.com/figo/api/create-driver"
    lateinit var prefManager: PrefManager


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


         binding.email.setOnClickListener{
            binding.inputEmail.isVisible=true
            binding.inputNumber.isVisible=false
        }
        binding.number.setOnClickListener {
            binding.inputNumber.isVisible=true
            binding.inputEmail.isVisible=false
        }
        binding.continuetv.setOnClickListener {
        /*    var retrofit=Retrofit.Builder()
                .baseUrl("https://test.pearl-developer.com/figo/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            var registerApi=retrofit.create(RegisterApiService::class.java)
            var map=HashMap<String,String>()
            map.put("contact_no",mobile_num)
            var callApi=registerApi.registerNum(map)

            callApi.enqueue(object :Callback<RegisterNumber>{
                override fun onResponse(
                    call: Call<RegisterNumber>,
                    response: Response<RegisterNumber>
                ) {
                   if (response.isSuccessful)
                   {
                       Toast.makeText(context,"Registration successfully done",Toast.LENGTH_SHORT).show()
                       Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                   }

                    if (call.isExecuted){
                        Toast.makeText(context,"Registration successfully done",Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                    }
                }
                override fun onFailure(call: Call<RegisterNumber>, t: Throwable) {
                    Log.d("main","error")
                    Toast.makeText(context,"Registration failed",Toast.LENGTH_SHORT).show()
                }
            })*/
            var mobile_num=binding.inputNumber.text.toString()
            val URL = "https://test.pearl-developer.com/figo/api/create-driver"
            val queue = Volley.newRequestQueue(requireContext())
            val json = JSONObject()
            json.put("mobile", mobile_num)

            val jsonOblect: JsonObjectRequest =
                object : JsonObjectRequest(Method.POST, URL, json, object :
                    Response.Listener<JSONObject?> {
                    override fun onResponse(response: JSONObject?) {

                        Log.d("SendData", "response===" + response)
                        if (response != null) {
                            if(prefManager.getToken().equals("") || prefManager.getToken().equals("null")){
//                                val jsonObject = response.getJSONObject("data")
                                val profile_status = response.getString("profile_status")
                             //   val userid = jsonObject1.getString("id")
                                val token = response.getString("token")
                                prefManager.setToken(token)
                                Toast.makeText(requireContext(),"Login Successfully",Toast.LENGTH_SHORT).show()
                                Log.d("SendData", "token===" + token)
                                if(prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")){
                                    Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                                }else{
                                    Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                                }
                            }else{
                                Toast.makeText(requireContext(),"Login Successfully",Toast.LENGTH_SHORT).show()
                                if(prefManager.getMpin().equals("") || prefManager.getMpin().equals("null")){
                                    Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_MPinGenerate)
                                }else{
                                    Navigation.findNavController(view).navigate(R.id.action_verifyNumber2_to_figgo_FamilyFragment)
                                }
                            }

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
