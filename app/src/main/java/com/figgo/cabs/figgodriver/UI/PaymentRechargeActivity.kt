package com.figgo.cabs.figgodriver.UI

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.RechargeLayoutAdapter
import com.figgo.cabs.figgodriver.model.Recharge
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.Helper
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class PaymentRechargeActivity :BaseClass(),PaymentResultListener {
    lateinit var prefManager:PrefManager
    var recharge_id=0
    var rupees=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(R.color.AppColor)


        setLayoutXml()
        val isConnected = isNetworkConnected(this.applicationContext)
        if (isConnected) {
            //verifyVersion();
            internetChangeBroadCast()
            printLogs("LoginActivity", "onCreate", "initConnected")
            initializeViews()
            initializeClickListners()
            initializeLabels()
            initializeInputs()
            printLogs("LoginActivity", "onCreate", "exitConnected")
        }
        else{
            Toast.makeText(this,"Please connnect with internet",Toast.LENGTH_SHORT).show()
        }

    }
    override fun setLayoutXml() {
        setContentView(R.layout.activity_payment_recharge)
    }

    override fun initializeViews() {
        prefManager= PrefManager(this)
        recharge_id= intent.getStringExtra("id")!!.toInt()
         rupees=intent.getStringExtra("amount")!!.toInt()
    }

    override fun initializeClickListners() {

    }

    override fun initializeInputs() {

        val amount=Math.round(rupees?.toInt()?.toFloat()!! *100).toInt()
        var checkout= Checkout()
        checkout.setKeyID("rzp_test_capDM1KlnUhj5f")
        checkout.setImage(R.drawable.appicon)
        val obj = JSONObject()
        try {
            obj.put("name", "Figgo")
            obj.put("description", "Payment")
            obj.put("theme.color", "#000F3B")
            obj.put("send_sms_hash", true)
            obj.put("allow_rotation", true)
            obj.put("currency", "INR")
            obj.put("amount", amount)
            val preFill = JSONObject()
            preFill.put("email", "support@gmail.com")
            preFill.put("contact", "91" + "9876565455")
            obj.put("prefill", preFill)
            checkout.open(this, obj)
        } catch (e: JSONException) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_SHORT).show();
            e.printStackTrace()
        }
    }

    override fun initializeLabels() {

    }
    override fun onPaymentSuccess(p0: String?) {
        Log.d("DATA","Message"+p0)
        var URL= Helper.select_payment
        var queue= Volley.newRequestQueue(this)
        var json=JSONObject()
        json.put("recharge_id",recharge_id)

        val jsonOblect =
            object : JsonObjectRequest(Method.POST, URL,json, Response.Listener<JSONObject> { response ->
                Log.d("RechargeFragment", "response===" + response)
                if (response!=null){

                    Toast.makeText(this@PaymentRechargeActivity, ""+response.getString("message"), Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@PaymentRechargeActivity,DriverDashBoard::class.java))
                    }



            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("Payment Recharge", "error===" + error)
                    Toast.makeText(this@PaymentRechargeActivity, "Something went wrong!", Toast.LENGTH_LONG).show()
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

    override fun onPaymentError(p0: Int, p1: String?) {

    }
}