package com.figgo.cabs.figgodriver.Fragment
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.SpinnerAdapter
import com.figgo.cabs.figgodriver.model.SpinnerObj
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.BasePrivate
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject
import retrofit2.http.GET
import java.util.HashMap

class GetData : Fragment() {
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    var statehashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
    lateinit var spinnerState:Spinner
    lateinit var spinnerCity:Spinner
    lateinit var prefManager:PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_data, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefManager = PrefManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        var baseclass:BasePrivate
        var bundle = arguments
        baseclass=object : BasePrivate(){
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

       // val get_URL = "https://test.pearl-developer.com/figo/api/driver/get-all-details"
        var get_URL=Helper.get_all_details
        Log.d("GetDATA","URL"+get_URL)
        val queue = Volley.newRequestQueue(requireContext())
        var profile_name = view.findViewById<EditText>(R.id.show_drivername)
        var profile_mobile = view.findViewById<EditText>(R.id.show_drivermobileno)
         spinnerState = view.findViewById<Spinner>(R.id.show_spinner_state)
         spinnerCity = view.findViewById<Spinner>(R.id.show_spinner_city)
        var driver_dlNo = view.findViewById<EditText>(R.id.show_driverdlno)
        var driver_panNo = view.findViewById<EditText>(R.id.show_driverPanNo)
        var driver_adharNo = view.findViewById<EditText>(R.id.show_driverAdharNo)
        var outstationHaspMap =   ArrayList<String>()
        var profile_view = view.findViewById<LinearLayout>(R.id.get_profile_layout)
        var cab_view = view.findViewById<ConstraintLayout>(R.id.get_drivercab_layout)

        //DRIVER CAB AND WORK
        var cab_category = view.findViewById<Spinner>(R.id.show_cab_category)
        var cab_type = view.findViewById<Spinner>(R.id.show_cab_type)
        var vehicle_no = view.findViewById<EditText>(R.id.show_vechle_no)
        var model_type = view.findViewById<Spinner>(R.id.show_model_type)
        var insurance_no = view.findViewById<EditText>(R.id.show_insurance_no)
        var local_permit = view.findViewById<EditText>(R.id.show_tax_permit_no)
        var national_permit = view.findViewById<EditText>(R.id.show_national_permit_date)
        //var URL2 = "https://test.pearl-developer.com/figo/api/driver/get-cab-work-details"
        var URL2=Helper.get_cab_work_details
        Log.d("GetDATA","URL"+URL2)

        var queue2 = Volley.newRequestQueue(requireContext())
        var visible_view = bundle?.getString("Key")
        Toast.makeText(requireContext(),"Key "+visible_view,Toast.LENGTH_SHORT).show()
        profile_view.visibility=View.GONE
        cab_view.visibility=View.GONE
        if(visible_view.equals("Profile"))
        {
            profile_view.visibility=View.VISIBLE
            cab_view.visibility=View.GONE
        }
        else if(visible_view.equals("Cab")){
            cab_view.visibility=View.VISIBLE
            profile_view.visibility=View.GONE
        }


        val jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.GET,get_URL,null,
            {
            if(it!=null)
            {
                Log.d("Get Response ",""+it.toString())
                profile_name.setText(it.getString("name"))
                profile_mobile.setText(it.getString("mobile"))
                driver_dlNo.setText(it.getString("dl_number"))
                driver_panNo.setText(it.getString("pan_number"))
                driver_adharNo.setText(it.getString("aadhar_number"))
                fetchState(it.getString("state").toInt(),it.getString("city").toInt())
                //baseclass.fetchStates(requireContext(),spinner_state,it.getString("state").toInt(),spinner_state,outstationHaspMap)
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
        var jsonObjectRequest2:JsonObjectRequest = object :JsonObjectRequest(Method.GET,URL2,null,
            {
                vehicle_no.setText(it.getString("v_number"))
                insurance_no.setText(it.getString("insurance"))
                local_permit.setText(it.getString("l_permit"))
                national_permit.setText(it.getString("n_permit"))

            },{

            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                return headers
            }
        }
        queue2.add(jsonObjectRequest2)

    }

    private fun fetchState(stateid:Int,cityid:Int) {
        statehashMap.clear()
        statelist.clear()
        //val URL = "https://test.pearl-developer.com/figo/api/get-state"
        var URL=Helper.get_state
        Log.d("GetDATA","URL"+URL)


        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        json.put("country_id","101")

        Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                Log.d("SendData", "response===" + response)
                var x:Int=-1
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){
                        val jsonArray = response.getJSONArray("states")
                        // statehashMap.put("Select State",0)
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
if(id.toInt()==stateid)
    x=i
                            statelist.add(SpinnerObj(name,id))
                            statehashMap.put(name,id.toInt())
                        }

                        //spinner
                        //  val stateadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,statehashMap.keys.toList());
                        Log.d("SendData", "statelist===" + statelist)
                        val stateadapter = SpinnerAdapter(requireContext(),statelist)

                        // stateadapter.setDropDownViewResource(R.layout.spinneritemlayout)
                        spinnerState.setAdapter(stateadapter)
                        Toast.makeText(requireContext(),"StateId "+stateid.toString(),Toast.LENGTH_SHORT).show()
                        if(x!=-1)
                        spinnerState.setSelection(x)

                        fetchCity(stateid,cityid)
                       spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                                var id1 = stateadapter.getItem(position)!!.id.toInt()


                                Log.d("SendData", "id1===" + stateadapter.getItem(position)!!.id.toInt())

                                //binding.selectStateTV.text=statehashMap.keys.toList()[position]
                                prefManager.setDriveState(id1!!.toInt())
                                fetchCity(stateadapter.getItem(position)!!.id.toInt(),-1)

                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }
                        }

                    }else{

                    }

                }

            }, Response.ErrorListener {

            }){}
        queue.add(jsonOblect)

    }

    private fun fetchCity(id: Int,cityID:Int) {
        cityhashMap.clear()
        citylist.clear()
        //val URL = " https://test.pearl-developer.com/figo/api/get-city"
        var URL=Helper.get_city
        Log.d("GetDATA","URL"+URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()
        var y:Int=-1
        json.put("state_id",id)

        Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                Log.d("SendData", "response===" + response)
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){
                        val jsonArray = response.getJSONArray("cities")
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            if(id.toInt()==cityID) {
                                y = i
                                Log.d("CityID",""+y)
                            }
                            citylist.add(SpinnerObj(name,id))
                            cityhashMap.put(name,id.toInt())

                        }

                        val stateadapter = SpinnerAdapter(requireContext(),citylist)

                        spinnerCity.setAdapter(stateadapter)
                        if(y!=-1)
                            spinnerCity.setSelection(y)


                        spinnerCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                                val id1 = stateadapter.getItem(position)?.id
                                prefManager.setDriveCity(id1!!.toInt())
                                Log.d("SendData", "cityid===" + id1)

                            }
                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapter: AdapterView<*>?) {
                            }
                        })
                    }else{

                    }
                    Log.d("SendData", "json===" + json)
                }

            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)


    }

}