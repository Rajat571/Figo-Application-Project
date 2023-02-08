package com.figgo.cabs.figgodriver.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.pearllib.Helper
import org.json.JSONArray
import org.json.JSONObject

class RideHistoryAdapter(var data: List<String>,var x:Int,var context:Context):RecyclerView.Adapter<RideHistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideHistoryHolder {
        return RideHistoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.ridehistory_table_adapter,parent,false))

    }

    override fun onBindViewHolder(holder: RideHistoryHolder, position: Int) {
        var prefManager=PrefManager(context)
        if (data[position].equals("View")&&x!=0)
        {
            holder.boxTV.setBackgroundColor(context.getColor(R.color.app_color))
            holder.boxTV.setTextColor(context.getColor(R.color.white))
            holder.boxTV.setOnClickListener {
                //var url3 = "https://test.pearl-developer.com/figo/api/driver/ride-history"
                var url3=Helper.ride_history
                Log.d("RideHistory","URL"+url3)

                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.historyview_details)
                var submit = dialog.findViewById<Button>(R.id.view_ok)
                var cancel = dialog.findViewById<ImageView>(R.id.view_cancel)
                var dialog_booking = dialog.findViewById<TextView>(R.id.history_booking_id)
                var dialog_to = dialog.findViewById<TextView>(R.id.history_destination)
                var dialog_name=dialog.findViewById<TextView>(R.id.history_customer_name)
                var dialog_date=dialog.findViewById<TextView>(R.id.history_date)
                var dialog_distance=dialog.findViewById<TextView>(R.id.history_distance)
                var dialog_from=dialog.findViewById<TextView>(R.id.history_from)
                var dialog_transaction=dialog.findViewById<TextView>(R.id.history_transaction)
                var dialog_vehicle=dialog.findViewById<TextView>(R.id.history_vehicle)
                var dialog_status=dialog.findViewById<TextView>(R.id.history_status)

                var ride_details:JSONObject
                var booking_id:String="-"
                var destination:String="-"
                var from:String="-"
                var status:String="-"
                var actual_distance:String
                var transactionID:String="-"
                var vehicle_name:String="-"
                var date:String="-"
                var user_name:String="-"
                val queue = Volley.newRequestQueue(context)
                val jsonObject: JsonObjectRequest = object :JsonObjectRequest(Method.POST,url3,null,
                    {
                        if(it!=null) {
                            try {
                                Log.d("Data Response", "" + it)
                                var allride: JSONObject = it.getJSONObject("data")
                                var allrideArray: JSONArray = allride.getJSONArray("all_rides")

                                //contentdata.add(listOf("Booking ID","To","From","Status","Distance","View"))
                                //  ride_details=allrideArray.optJSONObject(1).getJSONObject("ride_detail")
                                //Log.d("Ride Detail ",""+ride_details.toString())
                                ride_details =
                                    allrideArray.optJSONObject(x - 1).getJSONObject("ride_detail")
                                if (!ride_details.getString("booking_id").equals(null))
                                    booking_id = ride_details.getString("booking_id")
                                else
                                    booking_id = ""

                                if (!ride_details.getJSONObject("to_location").getString("name")
                                        .equals(null)
                                )
                                    destination =
                                        ride_details.getJSONObject("to_location").getString("name")
                                else
                                    destination = "-"

                                if (!ride_details.getJSONObject("from_location").getString("name")
                                        .equals(null)
                                )
                                    from = ride_details.getJSONObject("from_location")
                                        .getString("name")
                                else
                                    from = "-"

                                if (!ride_details.getString("status").equals(null))
                                    status = ride_details.getString("status")
                                else
                                    status = "-"

                                if (!ride_details.getString("actual_distance").equals(null))
                                    actual_distance = ride_details.getString("actual_distance")
                                else
                                    actual_distance = "-"

                                if (!ride_details.getJSONArray("model_details").getJSONObject(0)
                                        .getString("name").equals(null)
                                )
                                    vehicle_name =
                                        ride_details.getJSONArray("model_details").getJSONObject(0)
                                            .getString("name")
                                else
                                    vehicle_name = "-"

                                if (!ride_details.getString("date_only").equals(null))
                                    date = ride_details.getString("date_only")
                                else
                                    date = ""
                                if ((ride_details.getString("transaction_id")) != null)
                                    transactionID = ride_details.getString("transaction_id")
                                else
                                    transactionID = "-"
                                if (!ride_details.getJSONObject("user").getString("name")
                                        .equals(null)
                                ) {
                                    user_name = ride_details.getJSONObject("user").getString("name")
                                    dialog_name.text = user_name
                                } else {
                                    dialog_name.text = "-"
                                }
                                //vechicle_name = ride_details.getJSONObject("model_details")
                                Log.d(
                                    "Ride Detail ",
                                    "book - " + booking_id + " des - " + destination + "from - " + from + "status - " + status + " dist - " + actual_distance
                                )
                                // contentdata.add(listOf(booking_id,destination,from,status,actual_distance,"View"))

                                dialog_booking.text = booking_id
                                dialog_to.text = destination
                                dialog_date.text = date
                                dialog_distance.text = actual_distance
                                dialog_from.text = from
                                dialog_transaction.text = transactionID
                                dialog_vehicle.text = vehicle_name
                                dialog_status.text = status


                            }
                            catch (e:Exception){
                                Toast.makeText(context,"Server Problem",Toast.LENGTH_SHORT).show()
                            }
                        }

                    },{


                    }){
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): Map<String, String> {
                        val headers: MutableMap<String, String> = HashMap()
                        headers.put("Authorization", "Bearer " + prefManager.getToken())
                        return headers
                    }
                }
                queue.add(jsonObject)




                submit.setOnClickListener {
                    //Toast.makeText(view.context,"OTP SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()

                //Toast.makeText(context,"VIEW",Toast.LENGTH_SHORT).show()
            }
        }
        holder.boxTV.text= data[position]
        if (x==0){
            //holder.boxTV.setTextColor(Color.WHITE)
            holder.block.setBackgroundColor(context.getColor(R.color.exel))
            holder.boxTV.setTypeface(null, Typeface.BOLD);
            holder.boxTV.setTextColor(context.getColor(R.color.white))
        }else if(x%2==0) {
            holder.block.setBackgroundColor(Color.LTGRAY)
        }
    }



    override fun getItemCount(): Int {
        return data.size
    }

}
class RideHistoryHolder(itemView: View):ViewHolder(itemView){
    var boxTV=itemView.findViewById<TextView>(R.id.tablecontentTV)
    var block = itemView.findViewById<LinearLayout>(R.id.blocklinear)
}