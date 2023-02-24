package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentFiggoFamilyBinding
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject
import java.util.HashMap
import kotlin.jvm.Throws


class Figgo_FamilyFragment : Fragment() {
    lateinit var binding: FragmentFiggoFamilyBinding
    lateinit var prefManager: PrefManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_figgo__family, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        Log.d("onViewCreated","token == "+prefManager.getToken())
        Log.d("onViewCreated","id === "+ prefManager.getUserId())

        binding.taxiPartner.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                var args = Bundle()
                args.putString("Parent","TaxiPartner");
                //if(setUserType("Taxi"))
                setUserType("Partner",view)
            /*
                    Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_partnerDetails)
                else
                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_comingSoonFragment,args)*/

            }
        }
        binding.figgoDriver.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                var args = Bundle()
                args.putString("Parent","Driver");
                setUserType("Driver",view)

            }
        }
        binding.hotelPartner.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                //   var hotelPartner = PartnerWelcomeFragment()

//                hotelPartner.arguments = args;
                //childFragmentManager.beginTransaction().add(R.id.nav_controller,hotelPartner).commit()
                var args = Bundle()
                args.putString("Parent","Hotel");
                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_comingSoonFragment,args)
               // Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_partner_end_screen,args)



            }
        }
    }
    fun setUserType(userType:String,view: View):Boolean{
        var url = Helper.user_type
        var ret:Boolean = false
        var json = JSONObject()
        var queue = Volley.newRequestQueue(requireContext())
        json.put("user_type",userType)
        var jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.POST,url,json,
            {
                if(it!=null){
                    Log.d("Choose User Response ",it.toString())
                    if(it.getString("status").equals("true")){
                        ret = true
                        if (userType.equals("Partner")) {
                            var args = Bundle()
                            args.putString("Parent","Partner")
                            Navigation.findNavController(view)
                                .navigate(R.id.action_figgo_FamilyFragment_to_partnerWelcomeFragment,args)
                        }
                        else if (userType.equals("Driver")){
                            var args = Bundle()
                            args.putString("Parent","Driver");
                            Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_driverWelcomeFragment,args)
                        }
                        else{
                            var args = Bundle()
                            args.putString("Parent","HotelPartner");
                            Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_comingSoonFragment,args)
                            //Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_partner_end_screen,args)
                        }

                    }

                }
            },
            {
                var args = Bundle()
                args.putString("Parent","TaxiPartner");
                Navigation.findNavController(view).navigate(R.id.action_figgo_FamilyFragment_to_comingSoonFragment,args)


            })  {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("Authorization", "Bearer " + prefManager.getToken());
                return headers
            }
        }
        queue.add(jsonObject)
        return ret
    }


}