package com.figgo.cabs.FiggoPartner.UI.Fragment

import PartnerHomeAdapter
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.figgodriver.model.HomeData
import com.figgo.cabs.FiggoPartner.Adapter.PartnerDriversDetailAdapter
import com.figgo.cabs.FiggoPartner.Model.PartnerDriverView
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentPartnerHomeBinding
import com.figgo.cabs.figgodriver.Fragment.prefManager
import com.figgo.cabs.pearllib.Helper
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap


class PartnerHomeFragment : Fragment() {
    lateinit var binding: FragmentPartnerHomeBinding
    lateinit var partnerHomeAdapter:PartnerHomeAdapter
    var data=ArrayList<HomeData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_partner_home, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            var prefManager = PrefManager(requireContext())
        var recyclerView = view.findViewById<RecyclerView>(R.id.partner_home_recycler)
        var url = Helper.partner_getDrivers
        var queue = Volley.newRequestQueue(requireContext())
        var Dname:String
        var Dmobileno:String
        var Ddlno:String
        var dataList=ArrayList<PartnerDriverView>()
        Log.d(TAG,"TOKEN "+ "4586|aNz5HXwotAMADp9SpP28AMmVqQpM6xqS2BbJ4EYg")
        var json = JSONObject()
        var jsonObjectRequest = object : JsonObjectRequest(Method.POST,url, json,{
            if(it!=null) {
                Log.d(TAG, "GET DRIVER RES = " + it)
       try {


           var driverArray = it.getJSONArray("drivers")
           for (i in 0 until driverArray.length()) {
               try {
                   Log.d(TAG, "GET DRIVER RES = " + driverArray.optJSONObject(i))
                   Dname = driverArray.optJSONObject(i).getString("name")
                   Dmobileno = driverArray.optJSONObject(i).getString("mobile")
                   Ddlno = driverArray.optJSONObject(i).getString("dl_number")
                   dataList.add(PartnerDriverView(Dname,Dmobileno,Ddlno))
               }catch (e:Exception){

               }
           }
           recyclerView.adapter = PartnerDriversDetailAdapter(dataList)
           Log.d(TAG,"DataList"+dataList)
           recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

       }catch (e:Exception){

       }
            }
            else
                Log.d(TAG,"Response Null")
        },{
            Log.d(TAG,"Error Response $it")
        }){

            @SuppressLint("SuspiciousIndentation")
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["Content-Type"] = "application/json; charset=UTF-8";
                headers["Authorization"] = "Bearer " + "4586|aNz5HXwotAMADp9SpP28AMmVqQpM6xqS2BbJ4EYg";
                return headers
            }
        }
        queue.add(jsonObjectRequest)


    }

}