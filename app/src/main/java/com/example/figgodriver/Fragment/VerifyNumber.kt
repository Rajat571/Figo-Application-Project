package com.pearlorganisation.figgo.UI.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.FiggoPartner.Model.RegisterNumber
import com.example.figgodriver.Network.RegisterApiService
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentVerifyNumberBinding
import kotlinx.android.synthetic.main.top_layout.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Objects

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VerifyNumber.newInstance] factory method to
 * create an instance of this fragment.
 */
class VerifyNumber : Fragment() {
    lateinit var binding: FragmentVerifyNumberBinding

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
        var mobile_num=binding.inputNumber.text.toString()

         binding.email.setOnClickListener{
            binding.inputEmail.isVisible=true
            binding.inputNumber.isVisible=false
        }
        binding.number.setOnClickListener {
            binding.inputNumber.isVisible=true
            binding.inputEmail.isVisible=false
        }
        binding.continuetv.setOnClickListener {
            var retrofit=Retrofit.Builder()
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
            })
        }
    }


}