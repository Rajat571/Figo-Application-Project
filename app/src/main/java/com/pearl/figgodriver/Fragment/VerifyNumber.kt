package com.pearlorganisation.figgo.UI.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.JsonObject
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentVerifyNumberBinding
import kotlinx.android.synthetic.main.top_layout.*

class VerifyNumber : Fragment() {
    lateinit var binding: FragmentVerifyNumberBinding
    lateinit var queue:RequestQueue
    var base_url="https://test.pearl-developer.com/figo/api/create-driver"

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

//        var next=view.findViewById<TextView>(R.id.next)
//        next.setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_welcomeFragment_to_MPinGenerate2)
//        }


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

            val queue = Volley.newRequestQueue(requireContext())
            val url: String = "https://test.pearl-developer.com/figo/api/create-driver"

            var jsonObject=JsonObject()

            // Request a string response from the provided URL.
            val stringReq = JsonObjectRequest(Request.Method.GET, base_url,null,
                { response ->

                },
                {Log.d("API", "that didn't work") })
            queue.add(stringReq)




        }
    }


}