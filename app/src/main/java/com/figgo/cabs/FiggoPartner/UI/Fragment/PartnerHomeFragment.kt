package com.figgo.cabs.FiggoPartner.UI.Fragment

import PartnerHomeAdapter
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.figgodriver.model.HomeData
import com.figgo.cabs.FiggoPartner.Adapter.PartnerDriverDetailsScrollAdapter
import com.figgo.cabs.FiggoPartner.Adapter.PartnerDriversDetailAdapter
import com.figgo.cabs.FiggoPartner.Model.PartnerDriverView
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentPartnerHomeBinding
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class PartnerHomeFragment : Fragment() {
    lateinit var binding: FragmentPartnerHomeBinding
    var dataList=ArrayList<PartnerDriverView>()
    var sorttedList=ArrayList<PartnerDriverView>()
    lateinit var partnerHomeAdapter:PartnerHomeAdapter
    lateinit var adapter: PartnerDriversDetailAdapter
    lateinit var adapter2: PartnerDriverDetailsScrollAdapter
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
        var recyclerView2 = view.findViewById<RecyclerView>(R.id.partner_home_scroll_recyclerView)
        var searchField = view.findViewById<EditText>(R.id.partner_driver_searchfield)
        var url = Helper.partner_getDrivers
        var queue = Volley.newRequestQueue(requireContext())
        var Dname:String
        var Dmobileno:String
        var Ddlno:String

        dataList.clear()
        sorttedList.clear()
        Log.d(TAG,"TOKEN "+ "4586|aNz5HXwotAMADp9SpP28AMmVqQpM6xqS2BbJ4EYg")
        var json = JSONObject()
        var jsonObjectRequest = object : JsonObjectRequest(Method.POST,url, json,{
            if(it!=null) {
                Log.d(TAG, "GET DRIVER RES = $it")
       try {


           var driverArray = it.getJSONArray("drivers")
           for (i in 0 until driverArray.length()) {
               try {
                   Log.d(TAG, "GET DRIVER RES = " + driverArray.optJSONObject(i))
                   Dname = driverArray.optJSONObject(i).getString("name")
                   Dmobileno = driverArray.optJSONObject(i).getString("mobile")
                   Ddlno = driverArray.optJSONObject(i).getString("dl_number")
                   dataList.add(PartnerDriverView(Dname,Dmobileno,Ddlno))
                   sorttedList.add(PartnerDriverView(Dname,Dmobileno,Ddlno))
               }catch (e:Exception){

               }
           }
           adapter = PartnerDriversDetailAdapter(dataList)

           recyclerView.adapter = adapter

           Log.d(TAG,"DataList"+dataList)
           recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
           Collections.reverse(sorttedList)
           recyclerView2.adapter = PartnerDriverDetailsScrollAdapter(sorttedList)
           adapter2 = PartnerDriverDetailsScrollAdapter(sorttedList)
           recyclerView2.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

           searchField.addTextChangedListener(object : TextWatcher {
               override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                   // TODO Auto-generated method stub
               }

               override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                   // TODO Auto-generated method stub
               }

               override fun afterTextChanged(s: Editable) {

                   // filter your list from your input
                   val temp = ArrayList<PartnerDriverView>()
                   val temp2 = ArrayList<PartnerDriverView>()
                   for (d in dataList) {
                       //or use .equal(text) with you want equal match
                       //use .toLowerCase() for better matches
                       if (d.dl.contains(s.toString())||d.name.contains(s.toString())||d.dl.contains(s.toString())) {
                           temp.add(d)
                       }
                       else if (d.dl.uppercase(Locale.ROOT).contains(s.toString().uppercase(Locale.ROOT))||d.name.uppercase(Locale.ROOT).contains(s.toString().uppercase(Locale.ROOT))||d.dl.uppercase(Locale.ROOT).contains(s.toString().uppercase(Locale.ROOT))) {
                           temp.add(d)
                       }
                       else if (d.dl.contains(s.toString().lowercase(Locale.ROOT))||d.name.contains(s.toString().lowercase(Locale.ROOT))||d.dl.contains(s.toString().lowercase(Locale.ROOT))) {
                           temp.add(d)
                       }
                   }
                   for (d in sorttedList) {
                       //or use .equal(text) with you want equal match
                       //use .toLowerCase() for better matches
                       if (d.dl.contains(s.toString())||d.name.contains(s.toString())||d.dl.contains(s.toString())) {
                           temp2.add(d)
                       }
                       else if (d.dl.uppercase(Locale.ROOT).contains(s.toString().uppercase(Locale.ROOT))||d.name.uppercase(Locale.ROOT).contains(s.toString().uppercase(Locale.ROOT))||d.dl.uppercase(Locale.ROOT).contains(s.toString().uppercase(Locale.ROOT))) {
                           temp2.add(d)
                       }
                       else if (d.dl.contains(s.toString().lowercase(Locale.ROOT))||d.name.contains(s.toString().lowercase(Locale.ROOT))||d.dl.contains(s.toString().lowercase(Locale.ROOT))) {
                           temp2.add(d)
                       }
                   }
                   //update recyclerview
                   recyclerView.adapter = PartnerDriversDetailAdapter(temp)
                   recyclerView2.adapter = PartnerDriverDetailsScrollAdapter(temp2)
                   adapter.notifyDataSetChanged()
                   adapter2.notifyDataSetChanged()


                   //you can use runnable postDelayed like 500 ms to delay search text
               }
           })

       }catch (_:Exception){

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
                headers["Authorization"] = "Bearer " +prefManager.getToken() ;
                return headers
            }
        }
        queue.add(jsonObjectRequest)


    }

    fun filter(text: String?) {

    }



}