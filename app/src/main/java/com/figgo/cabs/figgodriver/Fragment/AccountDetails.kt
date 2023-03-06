package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.RechargeLayoutAdapter
import com.figgo.cabs.figgodriver.model.Recharge
import com.figgo.cabs.pearllib.Helper
import org.json.JSONArray
import java.util.HashMap


class AccountDetails : Fragment() {
    var recharge_list=ArrayList<Recharge>()
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager= PrefManager(requireContext())
        getRecharges(view)

    }
    private fun getRecharges(view: View) {

        var URL= Helper.get_recharge
        var queue= Volley.newRequestQueue(requireContext())

        val jsonOblect =
            object : JsonArrayRequest(Method.GET, URL,null, Response.Listener<JSONArray> { response ->
                Log.d("RechargeFragment", "response===" + response)
                if (response!=null){
                    for (i in 0..response.length()-1){
                        var data=response.getJSONObject(i)
                        var id=data.getString("id")
                        var booking_limit=data.getString("booking_limit")
                        var ride_request=data.getString("request_limit")
                        var recharge_amount=data.getString("recharge_amount")
                        Log.d("RechargeFragment", "data===" + id+","+booking_limit+","+ride_request+","+recharge_amount)
                        recharge_list.add(Recharge(id,booking_limit,ride_request,recharge_amount))

                    }
                    var rechargeRecycler = view.findViewById<RecyclerView>(R.id.rechargeRecycler2)
                    rechargeRecycler.layoutManager = LinearLayoutManager(context)
                    rechargeRecycler.adapter = RechargeLayoutAdapter(requireContext(),recharge_list)
                }

            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("CityRideActivity", "error===" + error)
                    Toast.makeText(requireContext(), "Something went wrong!", Toast.LENGTH_LONG).show()
                }
            })

            {
                @SuppressLint("SuspiciousIndentation")
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + prefManager.getToken());
                    return headers
                }
            }

        queue.add(jsonOblect)
    }

}