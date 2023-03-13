package com.figgo.cabs.figgodriver.Fragment
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.BasePrivate
import com.figgo.cabs.pearllib.Helper
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_driver_cab_details.*
import kotlinx.android.synthetic.main.update_cab.*
import java.io.ByteArrayOutputStream
import java.io.IOException


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
    var updatedStateList2 = java.util.ArrayList<String>()
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
    var work_place=0
    var work_city=0
    var work_state=0
    var stateList2 =ArrayList<Int>()
    var work_city_id=0
    var work_state_id=0
    var cab_id=0
    var isProfileChanged:Boolean = false
    var isCabImageChanged:Boolean = false
    var driver_profile:String=""
    var driver_cab_image:String=""
    var driver_profile_ext:String=""
    var driver_cab_image_ext:String=""

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
    var base = object : BaseClass(){
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefManager = PrefManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        initializeClickListners(view)

        var bundle = arguments
        try {
            getDriverDetails()
            getCabDetails()

        }catch (_:Exception){}

        work_view.visibility=View.GONE
        bottomNavClickListener()

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
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        selfiee.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.CAMERA), 200);
            // set the items in builder

            builder.setItems(optionsMenu,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    if (optionsMenu[i] == "Take Photo") {
                        // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                        val camera_intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        } else {
                            TODO("VERSION.SDK_INT < CUPCAKE")
                        }
                        // Start the activity with camera_intent, and request pic id
                        startActivityForResult(camera_intent, 1)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent= Intent()
                        intent.type="image/*"
                        intent.action= Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),2)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        drivercabImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.CAMERA), 200);
            // set the items in builder

            builder.setItems(optionsMenu,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    if (optionsMenu[i] == "Take Photo") {
                        // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
                        val camera_intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        } else {
                            TODO("VERSION.SDK_INT < CUPCAKE")
                        }
                        // Start the activity with camera_intent, and request pic id
                        startActivityForResult(camera_intent, 3)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent= Intent()
                        intent.type="image/*"
                        intent.action= Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),4)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        updateButton.setOnClickListener {
            if (profile_name.text.toString()!=driver_name){
                driver_name=profile_name.text.toString()
                //profile_name.setText(driver_name)
            }
            if (profile_mobile.text.toString()!=driver_mobile){
                driver_mobile=profile_mobile.text.toString()
                //profile_mobile.setText(driver_mobile)
            }
            if (driver_dlNo.text.toString()!=driver_dl){
                driver_dl=driver_dlNo.text.toString()
               // driver_dlNo.setText(driver_dl)
            }
            if (driver_panNo.text.toString()!=driver_panno){
                driver_panno=driver_panNo.text.toString()
               // driver_panNo.setText(driver_panno)
            }
            if (driver_adharNo.text.toString()!=driver_adhar){
                driver_adhar=driver_adharNo.text.toString()
                //driver_adharNo.setText(driver_adhar)
            }
            if (driver_state!=id1){
                try {
                    driver_state = id1
                  //  spinnerState.setSelection(driver_state)
                }catch (_:Exception){

                }
            }
            if (driver_city!=id2){
                driver_city=id2
                //spinnerCity.setSelection(driver_city)
            }

            updateDriverDetailsForm(driver_name,driver_mobile,driver_dl,driver_panno,driver_adhar,driver_state,driver_city)
            getDriverDetails()

        }

        updateWorkButton.setOnClickListener {
            if (work_state!=work_state_id){
                work_state=work_state_id
                //local_state.setSelection(work_state)

            }
            if (work_city!=work_city_id){
                work_city=work_city_id
                //spinnerCity.setSelection(driver_city)
            }
            if(work_place==1) {
                updatedStateList = prefManager.getOutstationlist()
                updateWorkArea(updatedStateList, work_city, cab_id, work_place)
                Log.d("PrefManagerList ",updatedStateList.toString())
            }
            else{
            if (updatedStateList.size>1) {
                for (i in 1 until updatedStateList.size) {
                    updatedStateList2.add(updatedStateList[i])
                }
                updateWorkArea(updatedStateList2, work_city, cab_id, work_place)
            }else{
                updateWorkArea(updatedStateList, work_city, cab_id, work_place)
            }
            }
            updatedStateList2.clear()
            updatedStateList.clear()
        }

        updateCabButton.setOnClickListener {
            if (vehicle_num.text.toString()!=driver_vechle_no){
                driver_vechle_no=vehicle_num.text.toString()
                vehicle_num.setText(driver_vechle_no)
            }
            if (insurance_no.text.toString()!=driver_insurance_no){
                driver_insurance_no=insurance_no.text.toString()
                insurance_no.setText(driver_insurance_no)
            }
             if (local_permit.text.toString()!=driver_local_permit){
                driver_local_permit=local_permit.text.toString()
                local_permit.setText(driver_local_permit)
            }
             if (national_permit.text.toString()!=driver_n_permit){
                driver_n_permit=national_permit.text.toString()
                national_permit.setText(driver_n_permit)
            }
             if (cabCategory_id!=v_category){
                v_category=cabCategory_id
                // spinner_cabcategory.setSelection(v_category)
            }
             if (cab_model!=v_modal){
                v_modal=cab_model
                // carModel.setSelection(v_modal)
            }
            updateDriverCabDetailsForm(driver_vechle_no,driver_insurance_no,driver_local_permit,driver_n_permit,prefManager.getDriverCabCategory().toInt(),prefManager.getDriverVechleModel())
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
                    //Log.d("DATETIME","DATE"+date)
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
                    //Log.d("DATETIME","DATE"+date)
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
                    //Log.d("DATETIME","DATE"+date)
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



    private fun getDriverDetails() {
        var get_URL=Helper.get_all_details
        //Log.d("GetDATA","URL"+get_URL)

        val queue = Volley.newRequestQueue(requireContext())

        val jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.GET,get_URL,null,
            {
                if(it!=null)
                {
                    //Log.d("Get Response ","GET RESPONSE"+it)


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
                        driver_cab_image_url = it.getJSONObject("documents").getString("taxi_image")
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
                        //fetchState(it.getString("state").toInt(), it.getString("city").toInt())
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
        var adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.CabType,
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner_cabtype?.adapter = adapter
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
                //Log.d("Model year","Model year==="+i)
                //Log.d("Model year","Model year==="+ prefManager.setDriverVechleType(position.toString()))

            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


                    var URL2=Helper.get_cab_work_details
        //Log.d("GetDATA", "URL$URL2")

        var queue2 = Volley.newRequestQueue(requireContext())
        var jsonObjectRequest2:JsonObjectRequest = object :JsonObjectRequest(Method.GET,URL2,null,
            {
                //Log.d("UpdateDriverCab","Driver-Cab"+it)

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

                try {
                    var x=0
                    val my_circle_details=it.getJSONObject("my_circle_details")
                    cab_type_id=my_circle_details.getString("v_type").toInt()
                    v_category=my_circle_details.getString("v_category").toInt()
                    spinner_cabtype.setSelection(2,false)
                    prefManager.setDriverCabCategory(v_category.toString())
                    v_modal=my_circle_details.getString("v_modal").toInt()
                    prefManager.setDriverVechleModel(v_modal)
                    Log.d("Parameters", "1 2 3 === $cab_type_id $v_category $v_modal")

                    // spinner_cabtype.setSelection(v_category)

                    spinner_cabtype?.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()
                            //spinner_cabtype.setSelection(2)
                            cab_type_id = position
                            x+=1
                            if(x>1)
                            fetchCabCategory2(cab_type_id)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }

                    }

                    fetchCabCategory(cab_type_id, v_category,v_modal)
                }
                catch (e:Exception) {
                    //fetchCabCategory(0, v_category,-1)

                    spinner_cabtype?.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()
                            //spinner_cabtype.setSelection(2)
                            cab_type_id = position
                            fetchCabCategory2(2)

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }

                    }
                }
                try {
                    yearval = it.getString("year")
                    year.setSelection(dateadapter.getPosition(yearval.toInt()))
                }
                catch (e:Exception) {
                    year.setSelection(1)
                }
                var work=it.getJSONObject( "work")

                work_place=work.getString( "work_place").toInt()
                 work_city=work.getString( "city").toInt()
                cab_id = work.getString("cab_id").toInt()
                work_city_id=work_city
                try {
                    var work_state_array = work.getJSONArray("state")
                    for (i in 0..work_state_array.length()-1){
                        work_state=work_state_array.get(i).toString().toInt()
                        work_state_id=work_state
                        stateList2.add(work_state)
                        prefManager.setOutstationlist(work_state,i)
                        Log.d("UpdateWorkstate","work_state==="+work_state)
                    }
                }catch(_:Exception){
                    var work_state_array = work.getString("state")
                    var work_state = work_state_array.subSequence(1,work_state_array.length-1).split(",").toList()[0]
                    work_state_id=work_state.toInt()
                    Log.d("work_state2","work_state==="+work_state)

                }





                //Log.d("Year","Year==="+yearval)
                //year.setSelection(dateadapter.getPosition(yearval.toInt()))
                //Log.d("UpdateDriverProfile",""+it.getJSONObject("work").getString("state").toList())
try {

    var adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.work_type,android.R.layout.simple_spinner_item);
    chooseWorkingArea.adapter = adapter2
    chooseWorkingArea.setSelection(work_place)
    chooseWorkingArea.onItemSelectedListener = object :
        AdapterView.OnItemSelectedListener{
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            if(p2!=work_place)
                work_place=p2
            if(p2 == 2){

                updatedStateList.clear()
                try {
                    Log.d("StateItemSelection", " Selected === ${stateList2[0]}")
                    fetchState2(stateList2[0], work_city, requireContext())
                }catch(_:Exception){
                    Log.d("StateItemSelectionString", " Selected === ${work_state_id}")
                    fetchState2(work_state_id, work_city, requireContext())
                }

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
                Log.d("outstationState1",updatedStateList.toString())
                updatedStateList =  baseprivate.fetchStates(requireContext(),outstationState2,2,outstationState2,stateList)
                Log.d("outstationState2",updatedStateList.toString())
                updatedStateList =   baseprivate.fetchStates(requireContext(),outstationState3,3,outstationState3,stateList)
                Log.d("outstationState3",updatedStateList.toString())
                updatedStateList =  baseprivate.fetchStates(requireContext(),outstationState4,4,outstationState4,stateList)
                Log.d("outstationState4",updatedStateList.toString())
                updatedStateList = baseprivate.fetchStates(requireContext(),outstationState5,5,outstationState5,stateList)
                Log.d("outstationState5",updatedStateList.toString())

                Log.d("UpdateDriverProfile","$updatedStateList")
/*
                outstationState1.onItemSelectedListener = object:
               AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        updatedStateList = baseprivate.fetchStates(requireContext(), outstationState1, 1, outstationState1, stateList)
                        Log.d("outstationState11",updatedStateList.toString())
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {

                }}
*/

                outstationWorkinAreaLayout.visibility=View.VISIBLE
                local_working_areaLayout.visibility=View.GONE
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }
}catch (_:Exception){

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

    private fun updateDriverCabDetailsForm(driver_vechle_no: String, driver_insurance_no: String, driver_local_permit: String, driver_n_permit: String, v_category: Int,vmodel:Int){
        var URL=Helper.update_cab_details
        var queue=Volley.newRequestQueue(requireContext())
        var json=JSONObject()
        json.put("v_category",v_category)
        json.put("v_modal",vmodel)
        json.put("v_number",driver_vechle_no)
        json.put("insurance",driver_insurance_no)
        json.put("l_permit",driver_local_permit)
        json.put("n_permit",driver_n_permit)
        json.put("year",yearval)
        if(isCabImageChanged){
            json.put("cab_img",driver_cab_image )
            json.put("cab_img_ext", driver_cab_image_ext)
        }else {
            json.put("cab_img", driver_cab_image_url)
            json.put("cab_img_ext", "jpg")
        }

        //Log.d("SEndDATA","Json==="+json)
        var jsonObjectRequest=object :JsonObjectRequest(Method.POST,URL,json,Response.Listener<JSONObject>{
                response ->
            context?.startActivity(Intent(requireContext(),DriverDashBoard::class.java))

            //Log.d("UpdateDriverProfile","UpdateDriverCabDetails==="+response)
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
        if(isProfileChanged)
        {
            json.put("selfie",driver_profile)
            json.put("selfie_ext",driver_profile_ext)
            Log.d("SelfieeEXT",driver_profile_ext.toString())

        }
        else{
            json.put("selfie",profile_pic_url)
            json.put("selfie_ext",".jpg")
        }
        Log.d("UpdateProfile",json.toString())
       /* json.put()*/

        //Log.d("SEndDATA","Json==="+json)
        var jsonObjectRequest=object :JsonObjectRequest(Method.POST,URL,json,Response.Listener<JSONObject>{
           response ->
            driver_email.setText(driver_email.text.toString())

            //Log.d("UpdateDriverProfile","UpdateDriverProfile==="+response)
            Toast.makeText(context,"Your profile update successfully",Toast.LENGTH_SHORT).show()
            context?.startActivity(Intent(requireContext(),DriverDashBoard::class.java))

        },object:Response.ErrorListener{
            override fun onErrorResponse(error: VolleyError?) {
Log.d("ErrorResponseUpdateProfile",error.toString())
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

    private fun updateWorkArea(work_state: java.util.ArrayList<String>, work_city: Int,cab_id:Int,work_place:Int) {
        var get_URL=Helper.update_work_area
        //Log.d("GetDATA","URL"+get_URL)

        val queue = Volley.newRequestQueue(requireContext())
        var json=JSONObject()
        json.put("cab_id",cab_id)
        json.put("work_place",work_place)
        json.put("state",work_state)
        json.put("city",work_city)

        Log.d("UpdateDataSend","$cab_id $work_place $work_state $work_city")

        val jsonObject:JsonObjectRequest = object :JsonObjectRequest(Method.POST,get_URL,json,
            {
                if(it!=null)
                {
                    Log.d("WorkUpdateApI", "RESPONSE work area ==== $it")

                    context?.startActivity(Intent(requireContext(),DriverDashBoard::class.java))

                    Toast.makeText(context,"Your work area details are update successfully",Toast.LENGTH_SHORT).show()
                    //baseclass.fetchStates(requireContext(),spinner_state,it.getString("state").toInt(),spinner_state,outstationHaspMap)
                }
            },{

                    Log.d("WorkUpdateApI", "RESPONSE work area ==== $it")
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

    private fun fetchState(stateid:Int,cityid:Int) {
        statehashMap.clear()
        statelist.clear()
        val URL=Helper.get_state
        //Log.d("GetDATA","URL"+URL)
        try {
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        json.put("country_id","101")

        //Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                //Log.d("SendData", "response===" + response)
                var x:Int=-1
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){
                        val jsonArray = response.getJSONArray("states")

                        for (i in 0 until jsonArray.length()){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            if(id.toInt()==stateid)
                             x=i
                            statelist.add(SpinnerObj(name,id))
                            statehashMap.put(name,id.toInt())
                        }

                        //Log.d("SendData", "statelist===" + statelist)
                         stateadapter = SpinnerAdapter(requireContext(),statelist)

                        spinnerState.setAdapter(stateadapter)
                        //Toast.makeText(requireContext(),"StateId "+stateid.toString(),Toast.LENGTH_SHORT).show()
                        if(x!=-1)
                         spinnerState.setSelection(x)


                       spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
try {
    id1 = stateadapter.getItem(position).id.toInt()
    Log.d("SelectedID", "id1===" + stateadapter.getItem(position)!!.id.toInt())
    prefManager.setDriveState(id1!!.toInt())
    fetchCity(stateadapter.getItem(position)!!.id.toInt(),cityid)
}
catch (_:Exception){

}

                                //outstationStateTV.text=statehashMap.keys.toList()[position]


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

    }catch (_:Exception){
    }
    }

    private fun fetchCity(id: Int,cityID:Int) {
        cityhashMap.clear()
        citylist.clear()
        //val URL = " https://test.pearl-developer.com/figo/api/get-city"
        var URL=Helper.get_city
        //Log.d("GetDATA","URL"+URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var tapNo:Int=0
        var token= prefManager.getToken()
        var y:Int=-1
        json.put("state_id",id)

        //Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                //Log.d("SendData", "response===" + response)
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
                                //Log.d("CityID",""+y)
                            }
                            citylist.add(SpinnerObj(name,id))
                            cityhashMap.put(name,id.toInt())

                        }
try {
    val stateadapter = SpinnerAdapter(requireContext(), citylist)

    spinnerCity.setAdapter(stateadapter)
    if (y != -1) {
        spinnerCity.setSelection(y)
        tapNo += 1
    }
}catch (_:java.lang.Exception){

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
                                        Log.d("SelectedCityID", "id1===" + stateadapter.getItem(position).id.toInt())
                                        //Log.d("SendData", "cityid===" + id2)
                                    }

                                }
                                override fun onNothingSelected(adapter: AdapterView<*>?) {
                                }
                            }
                        }
                        catch (e:Exception){
                            //Log.d("Spinner Problem", "Position null")
                        }

                    }else{

                    }
                    //Log.d("SendData", "json===" + json)

                }

            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)

    }


    private fun fetchCabCategory2(position: Int) {
        hashMap.clear()
        //val URL = " https://test.pearl-developer.com/figo/api/f_category"
        val URL=Helper.f_category
        //Log.d("SendData", "URL===" + URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("type_id",position)

        //Log.d("SendData", "json===" + json)
        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    //Log.d("SendData", "response===" + response)
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
                                    var id = hashMap.values.toList()[position]
                                    cabCategory_id=id
                                    Log.d("FetchCabCategoryListner","Selected_CATEGORY === "+id.toString())
                                    fetchModel(id)
                                    prefManager.setDriverCabCategory(id.toString())
                                    //Log.d("DriverCabCategory","DriverCabCategory==="+ prefManager.setDriverCabCategory(hashMap.values.toList()[position].toString()))


                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    hashMap.clear()
                                }
                            }
                        }else{
                        }


                        //Log.d("SendData", "json===" + json)


                    }
                    // Get your json response and convert it to whatever you want.
                }, Response.ErrorListener {
                    // Error
                }){}
        queue.add(jsonOblect)

    }

    private fun fetchCabCategory(cab_type: Int, v_category: Int,model:Int) {
        hashMap.clear()
        cab_category_list.clear()
        var x=-1
        try {
        val URL=Helper.f_category
        //Log.d("SendData", "URL===" + URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("type_id",cab_type)

        //Log.d("SendData", "json===" + json)

        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    //Log.d("SendData", "response===" + response)
                    if (response != null) {
                        val status = response.getString("status")
                        if(status.equals("1")){
                            val jsonArray = response.getJSONArray("categories")
                            for (i in 0..jsonArray.length()-1){
                                val rec: JSONObject = jsonArray.getJSONObject(i)
                                var name = rec.getString("name")
                                var id = rec.getString("id")
                                if(id.toInt()==v_category) {
                                    Log.d("FetchCabCategory","V_CATEGORY === "+i.toString())
                                    x = i
                                }
                                cab_category_list.add(SpinnerObj(name,id))
                                hashMap.put(name,id.toInt())
                            }

                           // val cabcategoryadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,hashMap.keys.toList());
                            var cabcategoryadapter=SpinnerAdapter(requireContext(),cab_category_list)
                           // cabcategoryadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                            spinner_cabcategory.adapter = cabcategoryadapter
                            if(x!=-1) {
                                spinner_cabcategory.setSelection(x)
                                /*fetchModel(v_category,model)*/
                            }

                            spinner_cabcategory?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    //cabCategory_id=hashMap.values.toList()[position]
                                    try {
                                        cabCategory_id =
                                            cabcategoryadapter.getItem(position)!!.id.toInt()
                                        prefManager.setDriverCabCategory(cabCategory_id.toString())
                                        Log.d("FetchCabCategory",
                                            "Selected Category === $cabCategory_id"
                                        )
                                        fetchModel(cabCategory_id)

                                    }catch (_:Exception){

                                    }
                                    //spinner_cabcategory.setSelection(hashMap.values.toList()[position])

                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    hashMap.clear()
                                }
                            }
                        }else{
                        }
                        //Log.d("SendData", "json===" + json)
                    }

                }, Response.ErrorListener {

                }){}
        queue.add(jsonOblect)

    }catch (_:Exception){ }
    }


    private fun fetchModel(position: Int,v_modal1:Int) {
        modelHashMap.clear()
        cab_model_list.clear()
        var URL=Helper.f_model
        try{
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()
        var tapNo:Int=0
        var y:Int=-1
if(position!=-1) {
    json.put("category_id", position)
    //Log.d("SendData", "json===" + URL)
    //Log.d("SendData", "json===" + json)

    val jsonOblect =
        object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                //Log.d("SendData", "response===" + response)
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if (status.equals("1")) {
                        val jsonArray = response.getJSONArray("models")
                        for (i in 0..jsonArray.length() - 1) {
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            //Log.d("MODEL ", "Model===" + name + id)
                            if (id.toInt() == v_modal1) {
                                y = i
                                //Log.d("v_modal", "" + y)
                            }
                            cab_model_list.add(SpinnerObj(name, id))
                            modelHashMap.put(name, id.toInt())
                        }

                        // val cabModeladapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,modelHashMap.keys.toList());
                        //cabModeladapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                        var cabModeladapter = SpinnerAdapter(requireContext(), cab_model_list)
                        carModel.adapter = cabModeladapter
                        if (y != -1) {
                            carModel.setSelection(y)
                            tapNo += 1
                        }
/*                        carModel?.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View,
                                    position: Int,
                                    id: Long
                                ) {
                                    //  fetchModel(hashMap.values.toList()[position])
                                    try {
                                        cab_model = cabModeladapter.getItem(position).id.toInt()


                                        prefManager.setDriverVechleModel(modelHashMap.values.toList()[position])
                                        //Log.d(
                                            "DriverVechleModel",
                                            "DriverVechleModel===" + prefManager.setDriverVechleModel(
                                                modelHashMap.values.toList()[position]
                                            )
                                        )
                                    } catch (_: Exception) {

                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    modelHashMap.clear()
                                }
                            }*/
                    } else {

                    }


                    //Log.d("SendData", "json===" + json)


                }

            }, Response.ErrorListener {

            }) {}
    queue.add(jsonOblect)
}
    }catch (_:Exception){
    }
    }

    private fun fetchModel(position: Int) {
        modelHashMap.clear()
        //val URL = "https://test.pearl-developer.com/figo/api/f_model"
        var URL=Helper.f_model
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("category_id",position)
        //Log.d("SendData", "json===" + URL)
        //Log.d("SendData", "json===" + json)

        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    //Log.d("SendData", "response===" + response)
                    // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                    if (response != null) {
                        val status = response.getString("status")
                        if(status.equals("1")){
                            val jsonArray = response.getJSONArray("models")
                            for (i in 0 until jsonArray.length()){
                                val rec: JSONObject = jsonArray.getJSONObject(i)
                                var name = rec.getString("name")
                                var id = rec.getString("id")
                                modelHashMap.put(name,id.toInt())
                            }
                            try {

                                val cabModeladapter = ArrayAdapter(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    modelHashMap.keys.toList()
                                );
                                cabModeladapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                                carModel.adapter = cabModeladapter
                                carModel?.onItemSelectedListener =
                                    object : AdapterView.OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>,
                                            view: View,
                                            position: Int,
                                            id: Long
                                        ) {
                                            //  fetchModel(hashMap.values.toList()[position])
                                            try {
                                                //Log.d( "SendData","modelHashMap.values.toList()[position]===" + modelHashMap.values.toList()[position])
                                                prefManager.setDriverVechleModel(modelHashMap.values.toList()[position])
                                                //Log.d( "DriverVechleModel","DriverVechleModel===" + prefManager.setDriverVechleModel(modelHashMap.values.toList()[position]) )
                                            } catch (_: Exception) {

                                            }
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>) {
                                            modelHashMap.clear()
                                        }
                                    }
                            }catch (_:Exception){

                            }
                        }else{

                        }


                        //Log.d("SendData", "json===" + json)


                    }
                    // Get your json response and convert it to whatever you want.
                }, Response.ErrorListener {
                    // Error
                }){}
        queue.add(jsonOblect)

    }

    private fun fetchState2(work_state: Int, work_city:Int, baseApbcContext: Context?) {
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
        //Log.d("SendData", "json===" + json)

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
                            Log.d("SpinnerState","Loop ==== $id")
                            if (id==work_state.toString()){
                                x=i
                            }
                            statelist.add(SpinnerObj(name,id))
                            hashMap.put(name,id.toInt())
                        }

                        val stateadapter = SpinnerAdapter(requireContext(),statelist)

                        local_state.setAdapter(stateadapter)
                        if(x!=-1) {
                            local_state.setSelection(x)
                            Log.d("SpinnerState","Selection ==== $x")
                        }

                        local_state.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {


                                if(position != 0){
                                    selectedState = statelist.get(position).id.toInt()
                                    updatedStateList.add(selectedState.toString())
                                    //   prefManager.setdriverWorkState(state_id)
                                    //Log.d("data","selectedState===="+selectedState)

                                    //fetchCity(selectedState,baseApbcContext)
                                    Log.d("SelectedStateID", "id1===" + selectedState)
                                     work_state_id=stateadapter.getItem(position)!!.id.toInt()
                                    fetchCity2(stateadapter.getItem(position)!!.id.toInt(),work_city)
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
    private fun fetchCity2(localStateId: Int,cityID:Int) {
        citylist.clear()

        var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
        var x=-1
        var URL=Helper.get_city
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()


        json.put("state_id",localStateId)

        //Log.d("fetchCity", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                //Log.d("SendData", "response===" + response)
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){
                        val jsonArray = response.getJSONArray("cities")
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            if(cityID==id.toInt())
                                x=i
                            citylist.add(SpinnerObj(name,id))
                            cityhashMap.put(name,id.toInt())
                        }

                        val cityadapter = SpinnerAdapter(requireContext(),citylist)
                        local_city.setAdapter(cityadapter)

                        if(x!=-1){
                            local_city.setSelection(x)
                        }
                        local_city.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {
                                if(position !=0){
                                    selectedcity = citylist.get(position).id.toInt()
                                     work_city_id=citylist.get(position).id.toInt()
                                    Log.d("SelectedCityID", "id1===" + citylist.get(position).id.toInt())
                                    //Log.d("SendData", "selectCityid===" + selectedcity)

                                }


                            }

                            @SuppressLint("SetTextI18n")
                            override fun onNothingSelected(adapter: AdapterView<*>?) {
                                //  (binding.spinnerState.getChildAt(0) as TextView).text = "Select Category"
                            }
                        })

                    }else{

                    }


                    //Log.d("SendData", "json===" + json)


                }

            }, Response.ErrorListener {

            }){}
        queue.add(jsonOblect)
    }

    fun fetchStates3(
        baseApbcContext: Context?, spinner: Spinner, loc: Int,
        spinner2: Spinner,
        outstationStateHashMap: ArrayList<String>
    ): ArrayList<String> {
        //outstationStateHashMap.clear()
        prefManager= PrefManager(baseApbcContext!!)
        var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
        var state_id:Int = 0
        val URL = " https://test.pearl-developer.com/figo/api/get-state"
        val queue = Volley.newRequestQueue(baseApbcContext)
        val json = JSONObject()

        json.put("country_id","101")
        Log.d("SendData", "json===" + json)

        val jsonOblect=  object : JsonObjectRequest(Method.POST, URL, json,
            Response.Listener<JSONObject?> { response ->
                Log.d("SendData", "response===" + response)
                // Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                if (response != null) {
                    val status = response.getString("status")
                    if(status.equals("1")){

                        val jsonArray = response.getJSONArray("states")
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")
                            statelist.add(SpinnerObj(name,id))
                            hashMap.put(name,id.toInt())
                        }
                        //spinner
                        val stateadapter = SpinnerAdapter(baseApbcContext,statelist)
                        // val stateadapter =  ArrayAdapter(baseApbcContext!!,R.layout.simple_spinner_item,hashMap.keys.toList());
                        //stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.setAdapter(stateadapter)
                        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                                if(position !=0){
                                    state_id = statelist.get(position).id.toInt()
                                    //    prefManager.setdriverWorkState(state_id)
                                    Log.d("data","State_id===="+state_id+loc)
                                    outstationStateHashMap.add(state_id.toString())
                                    // outstationStateHashMap.put("state"+loc,state_id)
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
                // Get your json response and convert it to whatever you want.
            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)


        return outstationStateHashMap
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1) {
                try {
                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    Log.d("onActivityResult", "response" + photo)
                    Log.d("onActivityResult", "" + selectedImageUri4.toString())
                    driver_profile_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    driver_profile = base.BitMapToString(bitmap).toString()
                    /*            binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.check_circle,
                                    0
                                )*/
                    selfiee.setImageBitmap(bitmap)
                    isProfileChanged=true
                    prefManager.setDriverProfile(driver_profile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 2) {
                try {
                    var selectedImageUri4 = data?.data
                    Log.d("URI = ", selectedImageUri4.toString())
                    driver_profile_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    driver_profile = base.BitMapToString(bitmap).toString()
                    /*      binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                              0,
                              0,
                              R.drawable.check_circle,
                              0
                          )*/
                    selfiee.setImageBitmap(bitmap)
                    isProfileChanged=true
                    prefManager.setAadhar_verification_back(driver_profile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else if (requestCode == 3) {
                try {
                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    Log.d("onActivityResult", "response" + photo)
                    Log.d("onActivityResult", "" + selectedImageUri4.toString())
                    driver_cab_image_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    driver_cab_image = base.BitMapToString(bitmap).toString()
                    /*            binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.check_circle,
                                    0
                                )*/
                    drivercabImage.setImageBitmap(bitmap)
                    isCabImageChanged=true
                    prefManager.setAadhar_verification_front(driver_cab_image)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 4) {
                try {
                    var selectedImageUri4 = data?.data
                    Log.d("URI = ", selectedImageUri4.toString())
                    driver_cab_image_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    driver_cab_image = base.BitMapToString(bitmap).toString()
                    drivercabImage.setImageBitmap(bitmap)
                    isCabImageChanged=true
                    prefManager.setAadhar_verification_back(driver_cab_image)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }


    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =   MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


}
