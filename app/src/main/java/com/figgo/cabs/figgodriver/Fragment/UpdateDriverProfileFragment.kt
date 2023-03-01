package com.figgo.cabs.figgodriver.Fragment
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
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
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.SpinnerAdapter
import com.figgo.cabs.figgodriver.UI.DriverDashBoard
import com.figgo.cabs.figgodriver.model.SpinnerObj
import com.figgo.cabs.pearllib.BasePrivate
import com.figgo.cabs.pearllib.Helper
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_driver_cab_details.*
import kotlinx.android.synthetic.main.update_cab.*


class UpdateDriverProfileFragment : Fragment() {
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    val cab_category_list= ArrayList<SpinnerObj>()
    val cab_model_list=ArrayList<SpinnerObj>()
    var statehashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var modelHashMap  : HashMap<String, Int> = HashMap<String, Int> ()
    lateinit var spinnerState:Spinner
    lateinit var spinnerCity:Spinner
    var yearList= listOf<Int>()
    lateinit var updateButton:Button
    lateinit var updateCabButton:Button
    lateinit var updateWorkButton:Button
    lateinit var spinner_cabtype:Spinner
    lateinit var show_working_area_spinner:Spinner
    lateinit var prefManager:PrefManager
    lateinit var spinner_cabcategory:Spinner
    lateinit var work_view:ConstraintLayout
    lateinit var profile_pic_url:String
    lateinit var driver_cab_image_url:String
    lateinit var   stateadapter:SpinnerAdapter
    lateinit var year:Spinner
    var current_year:Int = 0
    lateinit var cab_view:ConstraintLayout
    lateinit var profile_view: LinearLayout
    lateinit var carModel:Spinner
    lateinit var bottom_nav:BottomNavigationView
    lateinit var local_working_areaLayout:LinearLayout
    lateinit var local_state:Spinner
    lateinit var local_city:Spinner
    lateinit var selfiee:ImageView
    lateinit var drivercabImage:ImageView
    lateinit var chooseWorkingArea:Spinner
    lateinit var outstationWorkinAreaLayout:LinearLayout
    lateinit var outstationState1:Spinner
    lateinit var outstationState2:Spinner
    lateinit var outstationState3:Spinner
    lateinit var outstationState4:Spinner
    lateinit var outstationState5:Spinner
    lateinit  var profile_name:EditText
    lateinit var profile_mobile:EditText
    lateinit var driver_dlNo:EditText
    lateinit var driver_adharNo:EditText
    lateinit var national_permit:EditText
    lateinit var local_permit:EditText
    lateinit var insurance_no:EditText
    lateinit var vehicle_num:EditText
    lateinit var driver_panNo:EditText
    var updatedStateList = java.util.ArrayList<String>()
    var stateList = kotlin.collections.ArrayList<String>()
    var selectedcity  = 0
    var selectedState = 0
    var driver_name=""
    var driver_adhar=""
    var driver_panno=""
    var driver_mobile=""
    var driver_dl=""
    lateinit var driver_email:EditText
    var driver_state=0
    var driver_city=0
    var id1=0
    var id2=0
    var cabCategory_id=0
    var driver_vechle_no=""
    var driver_insurance_no=""
    var driver_local_permit=""
    var driver_n_permit=""
    var v_category=0
    var cab_model=0
    var v_modal=0
    var yearval=""
    var cab_type_id=0
    var date:String=""
    var s=""
    var t=""
    var n=""
    var work_city=0
    var work_state=0
    var work_city_id=0
    var work_state_id=0

    lateinit var calender: Calendar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_get_data, container, false)
    }
    var baseprivate = object : BasePrivate(){
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefManager = PrefManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        initializeClickListners(view)
        bottomNavClickListener()

        var bundle = arguments
        getDriverDetails()
        work_view.visibility=View.GONE


        getCabDetails()
        getWorkDetails()
        var visible_view = bundle?.getString("Key")

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
    }

    private fun getWorkDetails() {

    }


    private fun initializeViews(view: View) {

        profile_name = view.findViewById<EditText>(R.id.show_drivername)
        profile_mobile = view.findViewById<EditText>(R.id.show_drivermobileno)
        driver_email= view.findViewById(R.id.show_driveremail)
        spinnerState = view.findViewById<Spinner>(R.id.show_spinner_state)
        spinnerCity = view.findViewById<Spinner>(R.id.show_spinner_city)
        driver_dlNo = view.findViewById<EditText>(R.id.show_driverdlno)
        driver_panNo = view.findViewById<EditText>(R.id.show_driverPanNo)
        driver_adharNo = view.findViewById<EditText>(R.id.show_driverAdharNo)
        var outstationHaspMap =   ArrayList<String>()
        profile_view = view.findViewById<LinearLayout>(R.id.get_profile_layout)
        drivercabImage = view.findViewById<ImageView>(R.id.show_car_image)
        cab_view = view.findViewById<ConstraintLayout>(R.id.get_drivercab_layout)
        work_view = view.findViewById(R.id.get_workarea_layout)
        selfiee = view.findViewById(R.id.show_selfiee)
        bottom_nav= view.findViewById<BottomNavigationView>(R.id.driverdetails_menu)
        updateButton=view.findViewById(R.id.updateButton)
        updateCabButton=view.findViewById(R.id.updatecabButton)
        updateWorkButton=view.findViewById(R.id.updateworkButton)
        show_working_area_spinner=view.findViewById(R.id.show_working_area_spinner)

        //DRIVER CAB AND WORK
        spinner_cabtype = view.findViewById<Spinner>(R.id.drivershow_cab_category)
        spinner_cabcategory = view.findViewById<Spinner>(R.id.show_cab_type)
        vehicle_num = view.findViewById<EditText>(R.id.show_vechle_no)
        carModel = view.findViewById<Spinner>(R.id.show_model_type)
        insurance_no = view.findViewById<EditText>(R.id.show_insurance_no)
        local_permit = view.findViewById<EditText>(R.id.show_tax_permit_no)
        national_permit = view.findViewById<EditText>(R.id.show_national_permit_date)
        year = view.findViewById<Spinner>(R.id.show_year_list)

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



    }
    private fun initializeClickListners(view: View) {
        updateButton.setOnClickListener {
            if (profile_name.text.toString()!=driver_name){
                driver_name=profile_name.text.toString()
                profile_name.setText(driver_name)
            }
            else if (profile_mobile.text.toString()!=driver_mobile){
                driver_mobile=profile_mobile.text.toString()
                profile_mobile.setText(driver_mobile)
            }
            else if (driver_dlNo.text.toString()!=driver_dl){
                driver_dl=driver_dlNo.text.toString()
                driver_dlNo.setText(driver_dl)
            }
            else if (driver_panNo.text.toString()!=driver_panno){
                driver_panno=driver_panNo.text.toString()
                driver_panNo.setText(driver_panno)
            }
            else if (driver_adharNo.text.toString()!=driver_adhar){
                driver_adhar=driver_adharNo.text.toString()
                driver_adharNo.setText(driver_adhar)
            }
            else if (driver_state!=id1){
                driver_state=id1
                spinnerState.setSelection(driver_state)

            }
            else if (driver_city!=id2){
                driver_city=id2
                //spinnerCity.setSelection(driver_city)
            }

            updateDriverDetailsForm(driver_name,driver_mobile,driver_dl,driver_panno,driver_adhar,driver_state,driver_city)

        }

        updateWorkButton.setOnClickListener {
            if (work_state!=work_state_id){
                work_state=work_state_id
                spinnerState.setSelection(work_state)

            }
            else if (work_city!=work_city_id){
                work_city=work_city_id
                //spinnerCity.setSelection(driver_city)
            }
            updateWorkArea(work_state,work_city)
        }

        updateCabButton.setOnClickListener {
            if (vehicle_num.text.toString()!=driver_vechle_no){
                driver_vechle_no=vehicle_num.text.toString()
                vehicle_num.setText(driver_vechle_no)
            }
            else if (insurance_no.text.toString()!=driver_insurance_no){
                driver_insurance_no=insurance_no.text.toString()
                insurance_no.setText(driver_insurance_no)
            }
            else if (local_permit.text.toString()!=driver_local_permit){
                driver_local_permit=local_permit.text.toString()
                local_permit.setText(driver_local_permit)
            }
            else if (national_permit.text.toString()!=driver_n_permit){
                driver_n_permit=national_permit.text.toString()
                national_permit.setText(driver_n_permit)
            }
            else if (cabCategory_id!=v_category){
                v_category=cabCategory_id

                // spinner_cabcategory.setSelection(v_category)

            }
            else if (cab_model!=v_modal){
                v_modal=cab_model
                // carModel.setSelection(v_modal)
            }
            updateDriverCabDetailsForm(driver_vechle_no,driver_insurance_no,driver_local_permit,driver_n_permit,v_category)
        }


        insurance_no.setOnClickListener {
            calender= Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    date = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString())
                    s=date
                    Log.d("DATETIME","DATE"+date)
                    val dat1 = (dayOfMonth.toString()+"-" + (monthOfYear + 1) + "-" +year.toString())
                  insurance_no.setText(dat1)
                    insurance_no.setBackgroundResource(R.drawable.input_boder_profile)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        local_permit.setOnClickListener {
            calender= Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                {
                        view,
                        year,
                        monthOfYear,
                        dayOfMonth ->

                    date = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString())
                    t=date
                    Log.d("DATETIME","DATE"+date)
                    val dat1 = (dayOfMonth.toString()+"-" + (monthOfYear + 1) + "-" +year.toString())
                   local_permit.setText(dat1)
                    local_permit.setBackgroundResource(R.drawable.input_boder_profile)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
       national_permit.setOnClickListener {
            calender= Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    date = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString())
                    n=date
                    Log.d("DATETIME","DATE"+date)
                    val dat1 = (dayOfMonth.toString()+"-" + (monthOfYear + 1) + "-" +year.toString())
                   national_permit.setText(dat1)
                    national_permit.setBackgroundResource(R.drawable.input_boder_profile)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

    }

    private fun updateWorkArea(work_state: Int, work_city: Int) {
        var get_URL=Helper.update_work_area
        Log.d("GetDATA","URL"+get_URL)

        val queue = Volley.newRequestQueue(requireContext())

        val jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.GET,get_URL,null,
            {
                if(it!=null)
                {
                    Log.d("Get Response ","GET RESPONSE work area"+it)

                    try {
                        var work=it.getJSONObject( "work")

                        var work_place=work.getString( "work_place").toInt()
                        this.work_city =work.getString( "city").toInt()
                        var work_state_array=work.getJSONArray("state")
                        for (i in 0..work_state_array.length()-1){
                            this.work_state =work_state_array.get(i).toString().toInt()
                            Log.d("work_state","work_state==="+work_state)
                        }
                    }
                    catch (e:Exception) {
                        fetchState(it.getString("state").toInt(), it.getString("city").toInt())
                    }

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
    }

    private fun getDriverDetails() {
        var get_URL=Helper.get_all_details
        Log.d("GetDATA","URL"+get_URL)

        val queue = Volley.newRequestQueue(requireContext())

        val jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.GET,get_URL,null,
            {
                if(it!=null)
                {
                    Log.d("Get Response ","GET RESPONSE"+it)


                    try {

                        driver_name=it.getString("name")
                        profile_name.setText(driver_name)
                    }catch (e:Exception){
                        profile_name.setText(" ")
                    }

                    try {
                        driver_mobile=it.getString("mobile")
                        profile_mobile.setText(driver_mobile)
                    }catch (e:Exception){
                        profile_mobile.setText(" ")
                    }

                    try {
                       var email=it.getString("email")
                       driver_email.setText(email)
                    }catch (e:Exception){
                       driver_email.setText(" ")
                    }
                    try {
                        driver_dl=it.getString("dl_number")
                        driver_dlNo.setText(driver_dl)
                    }catch (e:Exception){
                        driver_dlNo.setText(" ")
                    }

                    try {
                        driver_panno=it.getString("pan_number")
                        driver_panNo.setText(driver_panno)
                    }catch (e:Exception){
                        driver_panNo.setText(" ")
                    }

                    try {
                        driver_adhar=it.getString("aadhar_number")
                        driver_adharNo.setText(driver_adhar)
                    }catch (e:Exception){
                        driver_adharNo.setText(" ")
                    }

                    try {
                        profile_pic_url = it.getJSONObject("documents").getString("driver_image")
                        Picasso
                            .get()
                            .load(profile_pic_url)
                            .into(selfiee);

                    }catch (e:Exception){
                        profile_pic_url = " "
                    }
                    try {
                        driver_cab_image_url = it.getJSONObject("documents").getString("driver_image")
                        Picasso
                            .get()
                            .load(driver_cab_image_url)
                            .into(drivercabImage);

                    }catch (e:Exception){
                        driver_cab_image_url = " "
                    }


                    try {
                        driver_state=it.getString("state").toInt()
                        driver_city=it.getString("city").toInt()
                        fetchState(driver_state,driver_city)
                    }
                    catch (e:Exception) {
                        fetchState(it.getString("state").toInt(), it.getString("city").toInt())
                    }

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

    }
    private fun getCabDetails() {

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
                year.setSelection(dateadapter.getPosition(position))
                val i = yearList[position]
                yearval=i.toString()

                //prefManager.setDriverVechleYear(i)
                Log.d("Model year","Model year==="+i)
                Log.d("Model year","Model year==="+ prefManager.setDriverVechleType(position.toString()))

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        var URL2=Helper.get_cab_work_details
        Log.d("GetDATA", "URL$URL2")

        var queue2 = Volley.newRequestQueue(requireContext())
        var jsonObjectRequest2:JsonObjectRequest = object :JsonObjectRequest(Method.GET,URL2,null,
            {
                Log.d("UpdateDriverCab","Driver-Cab"+it)

                try {
                    driver_vechle_no=it.getString("v_number")
                    vehicle_num.setText(driver_vechle_no)
                }catch (e:Exception){
                    vehicle_num.setText(" ")
                }
                try {
                    driver_insurance_no=it.getString("insurance")
                    if (driver_insurance_no.equals("null")){
                        insurance_no.setText("")
                    }
                    insurance_no.setText(driver_insurance_no)
                }catch (e:Exception){
                    insurance_no.setText(" ")
                }
                try {
                    driver_local_permit=it.getString("l_permit")
                    if (driver_local_permit.equals("null")){
                        local_permit.setText("")
                    }
                    local_permit.setText(driver_local_permit)
                }catch (e:Exception){
                    insurance_no.setText(" ")
                }
                try {
                    driver_n_permit=it.getString("n_permit")
                    if (driver_n_permit.equals("null")){
                        national_permit.setText("")
                    }
                    national_permit.setText(driver_n_permit)
                }catch (e:Exception){
                    national_permit.setText("")
                }

          /*      try {
                    var my_circle_details=it.getJSONObject("my_circle_details")
                    cab_type_id=my_circle_details.getString("v_type").toString().toInt()
                    v_category=my_circle_details.getString("v_category").toInt()
                    v_modal=my_circle_details.getString("v_modal").toInt()
                    Log.d("CAbDATA","CAbDEtails==category and mode"+v_category+v_modal)
                    fetchCabCategory(2)

                }
                catch (e:Exception) {
                    fetchCabCategory(2)

                }*/
                try {
                    yearval = it.getString("year")
                    year.setSelection(dateadapter.getPosition(yearval.toInt()))
                }
                catch (e:Exception) {
                    year.setSelection(1)
                }
                var work=it.getJSONObject( "work")

                var work_place=work.getString( "work_place").toInt()
                 work_city=work.getString( "city").toInt()
                var work_state_array=work.getJSONArray("state")
                for (i in 0..work_state_array.length()-1){
                     work_state=work_state_array.get(i).toString().toInt()
                    Log.d("work_state","work_state==="+work_state)
                }



                Log.d("Year","Year==="+yearval)
                //year.setSelection(dateadapter.getPosition(yearval.toInt()))
                Log.d("UpdateDriverProfile",""+it.getJSONObject("work").getString("state").toList())

                var adapter = ArrayAdapter.createFromResource(requireContext(),R.array.CabType,android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                spinner_cabtype?.adapter = adapter
                // spinner_cabtype.setSelection(v_category)
                spinner_cabtype?.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()
                        spinner_cabtype.setSelection(2)
                        cab_type_id=position

                        fetchCabCategory(2)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // write code to perform some action
                    }
                }

                var adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.work_type,android.R.layout.simple_spinner_item);
                chooseWorkingArea.adapter = adapter2
                chooseWorkingArea.setSelection(work_place)
                chooseWorkingArea.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        if(p2 == 2){
                            updatedStateList.clear()
                            fetchState2(work_state,requireContext())
                            selectedState =0
                            selectedcity = 0
                            outstationWorkinAreaLayout.visibility=View.GONE
                            local_working_areaLayout.visibility=View.VISIBLE
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
                            updatedStateList = baseprivate.fetchStates(requireContext(), outstationState1, 1, outstationState1, stateList)
                            updatedStateList =  baseprivate.fetchStates(requireContext(),outstationState2,2,outstationState2,stateList)
                            updatedStateList =   baseprivate.fetchStates(requireContext(),outstationState3,3,outstationState3,stateList)
                            updatedStateList =  baseprivate.fetchStates(requireContext(),outstationState4,4,outstationState4,stateList)
                            updatedStateList = baseprivate.fetchStates(requireContext(),outstationState5,5,outstationState5,stateList)

                            outstationWorkinAreaLayout.visibility=View.VISIBLE
                            local_working_areaLayout.visibility=View.GONE

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

    private fun updateDriverCabDetailsForm(driver_vechle_no: String, driver_insurance_no: String, driver_local_permit: String, driver_n_permit: String, v_category: Int) {
        var URL=Helper.update_cab_details
        var queue=Volley.newRequestQueue(requireContext())
        var json=JSONObject()
        json.put("v_category",v_category)
        json.put("v_modal",v_modal)
        json.put("v_number",show_vechle_no.text.toString())
        json.put("insurance",driver_insurance_no)
        json.put("l_permit",driver_local_permit)
        json.put("n_permit",driver_n_permit)
        json.put("year",yearval)
        json.put("cab_img",driver_cab_image_url)
        json.put("cab_img_ext","jpg")

        Log.d("SEndDATA","Json==="+json)
        var jsonObjectRequest=object :JsonObjectRequest(Method.POST,URL,json,Response.Listener<JSONObject>{
                response ->
            context?.startActivity(Intent(requireContext(),DriverDashBoard::class.java))

            Log.d("UpdateDriverProfile","UpdateDriverCabDetails==="+response)
            Toast.makeText(context,"Your cab details update successfully",Toast.LENGTH_SHORT).show()

        },object:Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {

            }
        })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                return headers
            }}
        queue.add(jsonObjectRequest)
    }

    private fun  updateDriverDetailsForm(driver_name: String, driver_mobile: String, driver_dl: String, driver_panno: String, driver_adhar: String, driver_state: Int, driver_city: Int) {

        var URL=Helper.update_details
        var queue=Volley.newRequestQueue(requireContext())
        var json=JSONObject()
        json.put("name",driver_name)
        json.put("email",driver_email.text.toString())
        json.put("second_number",driver_mobile)
        json.put("dl_number",driver_dl)
        json.put("pan_number",driver_panno)
        json.put("aadhar_number",driver_adhar)
        json.put("state",driver_state)
        json.put("city",driver_city)

        Log.d("SEndDATA","Json==="+json)
        var jsonObjectRequest=object :JsonObjectRequest(Method.POST,URL,json,Response.Listener<JSONObject>{
           response ->
            driver_email.setText(driver_email.text.toString())

            Log.d("UpdateDriverProfile","UpdateDriverProfile==="+response)
            Toast.makeText(context,"Your profile update successfully",Toast.LENGTH_SHORT).show()
            context?.startActivity(Intent(requireContext(),DriverDashBoard::class.java))

        },object:Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {

            }
        })
        {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                return headers
        }}
                queue.add(jsonObjectRequest)
    }


    private fun fetchState(stateid:Int,cityid:Int) {
        statehashMap.clear()
        statelist.clear()
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

                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            if(id.toInt()==stateid)
                             x=i
                            statelist.add(SpinnerObj(name,id))
                            statehashMap.put(name,id.toInt())
                        }

                        Log.d("SendData", "statelist===" + statelist)
                         stateadapter = SpinnerAdapter(requireContext(),statelist)

                        spinnerState.setAdapter(stateadapter)
                        Toast.makeText(requireContext(),"StateId "+stateid.toString(),Toast.LENGTH_SHORT).show()
                        if(x!=-1)
                         spinnerState.setSelection(x)


                       spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                                 id1 = stateadapter.getItem(position)!!.id.toInt()



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
                                        id2  = stateadapter.getItem(position).id.toInt()
                                        prefManager.setDriveCity(id2!!.toInt())
                                        Log.d("SendData", "cityid===" + id2)
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


/*    private fun fetchCabCategory(position: Int, v_category: Int) {
        hashMap.clear()
        cab_category_list.clear()
        var x=-1
        val URL=Helper.f_category
        Log.d("SendData", "URL===" + URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("type_id",2)

        Log.d("SendData", "json===" + json)

        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    Log.d("SendData", "response===" + response)
                    if (response != null) {
                        val status = response.getString("status")
                        if(status.equals("1")){
                            val jsonArray = response.getJSONArray("categories")
                            for (i in 0..jsonArray.length()-1){
                                val rec: JSONObject = jsonArray.getJSONObject(i)
                                var name = rec.getString("name")
                                var id = rec.getString("id")
                                if(id.toInt()==v_category)
                                    x=i
                                cab_category_list.add(SpinnerObj(name,id))
                                hashMap.put(name,id.toInt())
                            }

                           // val cabcategoryadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,hashMap.keys.toList());
                            var cabcategoryadapter=SpinnerAdapter(requireContext(),cab_category_list)
                           // cabcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                            spinner_cabcategory.adapter = cabcategoryadapter
                            if(x!=-1)
                                spinnerState.setSelection(x)

                            spinner_cabcategory?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    //cabCategory_id=hashMap.values.toList()[position]
                                    cabCategory_id = cabcategoryadapter.getItem(position)!!.id.toInt()
                                    fetchModel(cabCategory_id)
                                    //spinner_cabcategory.setSelection(hashMap.values.toList()[position])
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    hashMap.clear()
                                }
                            }
                        }else{
                        }


                        Log.d("SendData", "json===" + json)


                    }

                }, Response.ErrorListener {

                }){}
        queue.add(jsonOblect)

    }
    private fun fetchModel(position: Int) {
        modelHashMap.clear()
        cab_model_list.clear()
        var URL=Helper.f_model
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()
        var tapNo:Int=0
        var y:Int=-1

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
                                Log.d("MODEL ","Model==="+name+id)
                                if(id.toInt()==v_modal) {
                                    y = i
                                    Log.d("v_modal",""+y)
                                }
                                cab_model_list.add(SpinnerObj(name,id))
                                modelHashMap.put(name,id.toInt())
                            }

                            // val cabModeladapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,modelHashMap.keys.toList());
                            //cabModeladapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                            var cabModeladapter=SpinnerAdapter(requireContext(),cab_model_list)
                            carModel.adapter = cabModeladapter
                            if(y!=-1) {
                                carModel.setSelection(cab_model)
                                tapNo += 1
                            }
                            carModel?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    //  fetchModel(hashMap.values.toList()[position])
                                    cab_model = cabModeladapter.getItem(position).id.toInt()

                                    // cab_model=modelHashMap.values.toList()[position]
                                    *//*          if(position<modelHashMap.size&&position>=0) {
                                                  tapNo += 1

                                                  Log.d("SendData", "cab_model===" + cab_model)
                                              }
          *//*
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

                }, Response.ErrorListener {

                }){}
        queue.add(jsonOblect)

    }*/

    private fun fetchCabCategory(position: Int) {
        hashMap.clear()
        //val URL = " https://test.pearl-developer.com/figo/api/f_category"
        val URL=Helper.f_category
        Log.d("SendData", "URL===" + URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("type_id",2)

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

    private fun fetchState2(work_state: Int, baseApbcContext: Context?) {
        statelist.clear()
        updatedStateList.clear()
        prefManager= PrefManager(baseApbcContext!!)
        var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
        var state_id:Int = 0
        var URL=Helper.get_state
        val queue = Volley.newRequestQueue(baseApbcContext)
        val json = JSONObject()
        var x=-1

        json.put("country_id","101")
        Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->

                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){
                        val jsonArray = response.getJSONArray("states")
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            if (id==work_state.toString()){
                                x=i
                            }
                            statelist.add(SpinnerObj(name,id))
                            hashMap.put(name,id.toInt())
                        }

                        val stateadapter = SpinnerAdapter(requireContext(),statelist)

                        local_state.setAdapter(stateadapter)


                        local_state.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {


                                if(position != 0){
                                    selectedState = statelist.get(position).id.toInt()
                                    updatedStateList.add(work_state.toString())
                                    //   prefManager.setdriverWorkState(state_id)
                                    Log.d("data","selectedState===="+selectedState)

                                    //fetchCity(selectedState,baseApbcContext)

                                     work_state_id=stateadapter.getItem(position)!!.id.toInt()
                                    fetchCity2(stateadapter.getItem(position)!!.id.toInt())
                                }

                            }

                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapter: AdapterView<*>?) {
                                //  (binding.spinnerState.getChildAt(0) as TextView).text = "Select Category"
                            }
                        })
                    }else{
                        Toast.makeText(baseApbcContext,"Something Went Wrong !!",Toast.LENGTH_SHORT).show()
                    }
                }

            }, Response.ErrorListener {

            }){}
        queue.add(jsonOblect)

    }
    private fun fetchCity2(localStateId: Int) {
        citylist.clear()

        var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()

        var URL=Helper.get_city
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()


        json.put("state_id",localStateId)

        Log.d("fetchCity", "json===" + json)

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
                            citylist.add(SpinnerObj(name,id))
                            cityhashMap.put(name,id.toInt())
                        }

                        val cityadapter = SpinnerAdapter(requireContext(),citylist)
                        local_city.setAdapter(cityadapter)
                        local_city.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {



                                if(position !=0){
                                    selectedcity = citylist.get(position).id.toInt()
                                     work_city_id=citylist.get(position).id.toInt()

                                    Log.d("SendData", "selectCityid===" + selectedcity)

                                }


                            }

                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapter: AdapterView<*>?) {
                                //  (binding.spinnerState.getChildAt(0) as TextView).text = "Select Category"
                            }
                        })

                    }else{

                    }


                    Log.d("SendData", "json===" + json)


                }

            }, Response.ErrorListener {

            }){}
        queue.add(jsonOblect)
    }


}
