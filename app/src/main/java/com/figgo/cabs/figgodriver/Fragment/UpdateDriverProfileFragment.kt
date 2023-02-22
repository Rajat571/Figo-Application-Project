package com.figgo.cabs.figgodriver.Fragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.SpinnerAdapter
import com.figgo.cabs.figgodriver.model.SpinnerObj
import com.figgo.cabs.pearllib.BasePrivate
import com.figgo.cabs.pearllib.Helper
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class UpdateDriverProfileFragment : Fragment() {
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    var statehashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var modelHashMap  : HashMap<String, Int> = HashMap<String, Int> ()
    lateinit var spinnerState:Spinner
    lateinit var spinnerCity:Spinner
    var yearList= listOf<Int>()
    lateinit var spinner_cabtype:Spinner
    lateinit var prefManager:PrefManager
    lateinit var spinner_cabcategory:Spinner
    lateinit var work_view:ConstraintLayout
    lateinit var year:Spinner
    var current_year:Int = 0
    lateinit var cab_view:ConstraintLayout
    lateinit var profile_view: LinearLayout
    lateinit var carModel:Spinner
    lateinit var bottom_nav:BottomNavigationView
    lateinit var local_working_areaLayout:LinearLayout
    lateinit var local_state:Spinner
    lateinit var local_city:Spinner
    lateinit var chooseWorkingArea:Spinner
    lateinit var outstationWorkinAreaLayout:LinearLayout
    lateinit var outstationState1:Spinner
    lateinit var outstationState2:Spinner
    lateinit var outstationState3:Spinner
    lateinit var outstationState4:Spinner
    lateinit var outstationState5:Spinner
    var updatedStateList = java.util.ArrayList<String>()
    var stateList = kotlin.collections.ArrayList<String>()
    var selectedcity  = 0
    var selectedState = 0

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

            }

            override fun initializeViews() {

            }

            override fun initializeClickListners() {

            }

            override fun initializeInputs() {

            }

            override fun initializeLabels() {

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
        profile_view = view.findViewById<LinearLayout>(R.id.get_profile_layout)
        cab_view = view.findViewById<ConstraintLayout>(R.id.get_drivercab_layout)
        work_view = view.findViewById(R.id.get_workarea_layout)
        bottom_nav= view.findViewById<BottomNavigationView>(R.id.driverdetails_menu)


        chooseWorkingArea = view.findViewById(R.id.show_working_area_spinner)

        //OUTSTATION
        outstationWorkinAreaLayout = view.findViewById(R.id.show_working_state_layout)
        outstationState1 = view.findViewById(R.id.show_select_state1)
        outstationState2 = view.findViewById(R.id.show_select_state2)
        outstationState3 = view.findViewById(R.id.show_select_state3)
        outstationState4 = view.findViewById(R.id.show_select_state4)
        outstationState5 = view.findViewById(R.id.show_select_state5)

        //LOCAL
        local_working_areaLayout = view.findViewById(R.id.show_working_local_layout)
        local_state = view.findViewById(R.id.show_select_state_local)
        local_city = view.findViewById(R.id.show_select_city)

        work_view.visibility=View.GONE
        bottomNavClickListener()

        //DRIVER CAB AND WORK
        spinner_cabtype = view.findViewById<Spinner>(R.id.drivershow_cab_category)
         spinner_cabcategory = view.findViewById<Spinner>(R.id.show_cab_type)
        var vehicle_no = view.findViewById<EditText>(R.id.show_vechle_no)
        carModel = view.findViewById<Spinner>(R.id.show_model_type)
        var insurance_no = view.findViewById<EditText>(R.id.show_insurance_no)
        var local_permit = view.findViewById<EditText>(R.id.show_tax_permit_no)
        var national_permit = view.findViewById<EditText>(R.id.show_national_permit_date)
        year = view.findViewById<Spinner>(R.id.show_year_list)
        //var URL2 = "https://test.pearl-developer.com/figo/api/driver/get-cab-work-details"
        var URL2=Helper.get_cab_work_details
        Log.d("GetDATA", "URL$URL2")

        current_year   = Calendar.getInstance().get(Calendar.YEAR)
        for(i in 2000..current_year)
        {
            yearList+=i

        }
        val dateadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,yearList);
        dateadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        year.adapter=dateadapter
        year.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()

                val i = yearList[position]

                //prefManager.setDriverVechleYear(i)
                Log.d("Model year","Model year==="+i)
                Log.d("Model year","Model year==="+ prefManager.setDriverVechleType(position.toString()))


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

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
                if(it.getString("city")!=null)
                 fetchState(it.getString("state").toInt(),it.getString("city").toInt())
                else
                 fetchState(it.getString("state").toInt(),0)

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

                var adapter = ArrayAdapter.createFromResource(requireContext(),R.array.CabType,android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                spinner_cabtype?.adapter = adapter
                spinner_cabtype?.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()
                        fetchCabCategory2(position)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }
                var yearval = it.getString("year")
                year.setSelection(dateadapter.getPosition(yearval.toInt()))
                
                //work Area

                var adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.work_type,android.R.layout.simple_spinner_item);
                chooseWorkingArea.adapter = adapter2
                chooseWorkingArea.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        if(p2 == 2){
                            updatedStateList.clear()
                        }
                        else if(p2==0){
                            selectedState =0
                            selectedcity = 0
                            outstationWorkinAreaLayout.visibility=View.GONE
                            local_working_areaLayout.visibility=View.GONE
                        }
                        else{
                            selectedState =0
                            selectedcity = 0
/*                            updatedStateList = baseprivate.fetchStates(requireContext(), outstationState1, 1, outstationState1, stateList)
                            updatedStateList =  baseprivate.fetchStates(requireContext(),outstationState2,2,outstationState2,stateList)
                            updatedStateList =   baseprivate.fetchStates(requireContext(),outstationState3,3,outstationState3,stateList)
                            updatedStateList =  baseprivate.fetchStates(requireContext(),outstationState4,4,outstationState4,stateList)
                            updatedStateList = baseprivate.fetchStates(requireContext(),outstationState5,5,outstationState5,stateList)

                            binding.workingStateLayout.visibility=View.VISIBLE
                            binding.workingLocalLayout.visibility=View.GONE*/
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

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
        queue2.add(jsonObjectRequest2)

    }

    private fun bottomNavClickListener() {
        bottom_nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.detailsdriver ->{
                    profile_view.visibility = View.VISIBLE
                    work_view.visibility=View.GONE
                    cab_view.visibility = View.GONE
                }
                R.id.detailsCab->{
                    profile_view.visibility = View.GONE
                    cab_view.visibility = View.VISIBLE
                    work_view.visibility=View.GONE
                }
                R.id.detailswork->{
                    work_view.visibility=View.VISIBLE
                    profile_view.visibility = View.GONE
                    cab_view.visibility = View.GONE

                    true
                }
            }
            true

        }
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

                        //fetchCity(stateid,cityid)
                       spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                                var id1 = stateadapter.getItem(position)!!.id.toInt()


                                Log.d("SendData", "id1===" + stateadapter.getItem(position)!!.id.toInt())

                                //outstationStateTV.text=statehashMap.keys.toList()[position]
                                prefManager.setDriveState(id1!!.toInt())
                                fetchCity(stateadapter.getItem(position)!!.id.toInt(),cityid)

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
        var tapNo:Int=0
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
                        if(y!=-1) {
                            spinnerCity.setSelection(y)
                            tapNo += 1
                        }


try{
                            spinnerCity.onItemSelectedListener = object :
                                AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    adapterView: AdapterView<*>?,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    if(position<cityhashMap.size&&position>=0) {
                                        tapNo += 1
                                        val id1 = stateadapter.getItem(position).id
                                        prefManager.setDriveCity(id1!!.toInt())
                                        Log.d("SendData", "cityid===" + id1)
                                    }

                                }
                                override fun onNothingSelected(adapter: AdapterView<*>?) {
                                }
                            }
}
catch (e:Exception){
    Log.d("Spinner Problem", "Position null")
}

                    }else{

                    }
                    Log.d("SendData", "json===" + json)

                }

            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)


    }

    private fun fetchCabCategory(position: Int) {
        hashMap.clear()
        //val URL = " https://test.pearl-developer.com/figo/api/f_category"
        val URL=Helper.f_category
        Log.d("SendData", "URL===" + URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("type_id",position)

        Log.d("SendData", "json===" + json)

        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    Log.d("SendData", "response===" + response)
                    // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                    if (response != null) {
                        val status = response.getString("status")
                        if(status.equals("1")){
                            val jsonArray = response.getJSONArray("categories")
                            for (i in 0..jsonArray.length()-1){
                                val rec: JSONObject = jsonArray.getJSONObject(i)
                                var name = rec.getString("name")
                                var id = rec.getString("id")
                                hashMap.put(name,id.toInt())
                            }

                            val cabcategoryadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,hashMap.keys.toList());
                            cabcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                            spinner_cabcategory.adapter = cabcategoryadapter
                            spinner_cabcategory?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                                    fetchModel(hashMap.values.toList()[position])
                                    prefManager.setDriverCabCategory(hashMap.values.toList()[position].toString())
                                    Log.d("DriverCabCategory","DriverCabCategory==="+ prefManager.setDriverCabCategory(hashMap.values.toList()[position].toString()))


                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    hashMap.clear()
                                }
                            }
                        }else{
                        }


                        Log.d("SendData", "json===" + json)


                    }
                    // Get your json response and convert it to whatever you want.
                }, Response.ErrorListener {
                    // Error
                }){}
        queue.add(jsonOblect)

    }


    private fun fetchModel(position: Int) {
        modelHashMap.clear()
        //val URL = "https://test.pearl-developer.com/figo/api/f_model"
        var URL=Helper.f_model
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("category_id",position)
        Log.d("SendData", "json===" + URL)
        Log.d("SendData", "json===" + json)

        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    Log.d("SendData", "response===" + response)
                    // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                    if (response != null) {
                        val status = response.getString("status")
                        if(status.equals("1")){
                            val jsonArray = response.getJSONArray("models")
                            for (i in 0..jsonArray.length()-1){
                                val rec: JSONObject = jsonArray.getJSONObject(i)
                                var name = rec.getString("name")
                                var id = rec.getString("id")
                                modelHashMap.put(name,id.toInt())
                            }

                            val cabModeladapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,modelHashMap.keys.toList());
                            cabModeladapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                            carModel.adapter = cabModeladapter
                            carModel?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    //  fetchModel(hashMap.values.toList()[position])
                                    Log.d("SendData", "modelHashMap.values.toList()[position]===" + modelHashMap.values.toList()[position])
                                    prefManager.setDriverVechleModel(modelHashMap.values.toList()[position])
                                    Log.d("DriverVechleModel","DriverVechleModel==="+  prefManager.setDriverVechleModel(modelHashMap.values.toList()[position]))
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    modelHashMap.clear()
                                }
                            }
                        }else{

                        }


                        Log.d("SendData", "json===" + json)


                    }
                    // Get your json response and convert it to whatever you want.
                }, Response.ErrorListener {
                    // Error
                }){}
        queue.add(jsonOblect)

    }
    private fun fetchCabCategory2(position: Int) {
        hashMap.clear()
        //val URL = " https://test.pearl-developer.com/figo/api/f_category"
        val URL=Helper.f_category
        Log.d("SendData", "URL===" + URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("type_id",position)

        Log.d("SendData", "json===$json")

        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    Log.d("SendData", "response===$response")
                    // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                    if (response != null) {
                        val status = response.getString("status")
                        if(status.equals("1")){
                            val jsonArray = response.getJSONArray("categories")
                            for (i in 0..jsonArray.length()-1){
                                val rec: JSONObject = jsonArray.getJSONObject(i)
                                var name = rec.getString("name")
                                var id = rec.getString("id")
                                hashMap.put(name,id.toInt())
                            }

                            val cabcategoryadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,hashMap.keys.toList());
                            cabcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                            spinner_cabcategory.adapter = cabcategoryadapter
                            spinner_cabcategory?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                                    fetchModel(hashMap.values.toList()[position])
                                    prefManager.setDriverCabCategory(hashMap.values.toList()[position].toString())
                                    Log.d("DriverCabCategory","DriverCabCategory==="+ prefManager.setDriverCabCategory(hashMap.values.toList()[position].toString()))


                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    hashMap.clear()
                                }
                            }
                        }else{
                        }


                        Log.d("SendData", "json===" + json)


                    }
                    // Get your json response and convert it to whatever you want.
                }, Response.ErrorListener {
                    // Error
                }){}
        queue.add(jsonOblect)

    }
    
    fun setWorkingArea(){

    }

}