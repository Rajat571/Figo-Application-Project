package com.pearl.figgodriver.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pearl.figgodriver.DriverDashBoard
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentDriverCabDetailsBinding
import com.pearl.pearllib.BaseClass
import com.pearl.pearllib.BasePrivate
import com.pearlorganisation.PrefManager
import kotlinx.android.synthetic.main.cancel_ride_dialog.*
import kotlinx.android.synthetic.main.fragment_driver_cab_details.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DriverCabDetailsFragment : Fragment() {
    private lateinit var carDP: ImageView
    private var  str: String? = null
    var  driver_cab_image:String=""
    var yearList= listOf<Int>()
    lateinit var binding:FragmentDriverCabDetailsBinding
    lateinit var prefManager: PrefManager
    lateinit var spinner_cabcategory: Spinner
    lateinit var carModel:Spinner
    var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var stateList = kotlin.collections.ArrayList<String>()
    var updatedStateList = ArrayList<String>()
    var outstationStateHashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var modelHashMap  : HashMap<String, Int> = HashMap<String, Int> ()
    lateinit  var next  :TextView
    lateinit  var back  :TextView
    var current_year:Int = 0
    lateinit var spinner_cabtype   :Spinner
    lateinit  var workingarea      :Spinner
    var selectedcity  = 0
    var selectedState = 0
    var base = object :BaseClass(){
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

    var baseprivate = object :BasePrivate(){
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

        @SuppressLint("SuspiciousIndentation")
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
             ): View? {
        // Inflate the layout for this fragment
      binding=DataBindingUtil.inflate(inflater,R.layout.fragment_driver_cab_details, container, false)
            return binding.root
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        initializeViews(view)
        initializeClickListners(view)
        initializeInputs()


    }

     fun initializeViews(view: View) {
          next               =view.findViewById<TextView>(R.id.next_button)
          back               =view.findViewById<TextView>(R.id.back_button)
          current_year   = Calendar.getInstance().get(Calendar.YEAR)
         spinner_cabcategory    = view.findViewById<Spinner>(R.id.spinner_cabtype)
          spinner_cabtype    = view?.findViewById<Spinner>(R.id.spinner_cabcategory)!!
          workingarea        = view.findViewById<Spinner>(R.id.working_area_spinner)
         carDP                  = view.findViewById(R.id.upload_car)
         carModel               = view.findViewById<Spinner>(R.id.vechle_model)
        // binding.proceed.visibility=View.GONE
    }

     fun initializeClickListners(view: View) {
         var layout_cab = binding.cabDetailsLayout
         var layout_work = binding.work

         layout_cab.visibility= View.VISIBLE
         layout_work.visibility = View.GONE


         carDP.setOnClickListener {
             val intent = Intent()
             intent.type = "image/*"
             intent.action = Intent.ACTION_GET_CONTENT
             startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
         }
         next.setOnClickListener {
             validateForm()

         }

         back.setOnClickListener {
             if (layout_cab.visibility==View.VISIBLE){
                // binding.proceed.visibility=View.GONE
                 Navigation.findNavController(view).navigate(R.id.action_driverCabDetailsFragment_to_figgo_Capton)
             }else{
               //  binding.proceed.visibility=View.GONE
                 layout_cab.visibility= View.VISIBLE
                 layout_work.visibility = View.GONE
             }
         }
    }

    fun initializeInputs() {
        stateList.clear()
        /*Cab Year Spinner*/
        var adapter = ArrayAdapter.createFromResource(requireContext(),R.array.CabType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner_cabtype?.adapter = adapter
        spinner_cabtype?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()
                fetchCabCategory(position)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }




        for(i in 2000..current_year)
        {
            yearList+=i

        }
        val dateadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,yearList);
        dateadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.modelYear.adapter = dateadapter
            binding.modelYear?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()

                val i = yearList[position]

                prefManager.setDriverVechleYear(i)
                Log.d("Model year","Model year==="+i)
                Log.d("Model year","Model year==="+ prefManager.setDriverVechleType(position.toString()))


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        var adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.work_type,android.R.layout.simple_spinner_item);
        workingarea.adapter = adapter2
        workingarea.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                prefManager.setDriverVechleType(position.toString())



                if(position==2){
                    updatedStateList.clear()
                    selectedState =0
                    selectedcity = 0
                    fetchState(requireContext())
                    binding.workingStateLayout.visibility=View.GONE
                    binding.workingLocalLayout.visibility=View.VISIBLE
                }else if(position == 0){
                    selectedState =0
                    selectedcity = 0
                    binding.workingStateLayout.visibility=View.GONE
                    binding.workingLocalLayout.visibility=View.GONE
                }
                else{

                    selectedState =0
                    selectedcity = 0
                    updatedStateList = baseprivate.fetchStates(requireContext(), binding.selectState1, 1, binding.selectState1, stateList)
                    updatedStateList =  baseprivate.fetchStates(requireContext(),binding.selectState2,2,binding.selectState1,stateList)
                    updatedStateList =   baseprivate.fetchStates(requireContext(),binding.selectState3,3,binding.selectState1,stateList)
                    updatedStateList =  baseprivate.fetchStates(requireContext(),binding.selectState4,4,binding.selectState1,stateList)
                    updatedStateList = baseprivate.fetchStates(requireContext(),binding.selectState5,5,binding.selectState1,stateList)

                    binding.workingStateLayout.visibility=View.VISIBLE
                    binding.workingLocalLayout.visibility=View.GONE
                    updatedStateList.clear()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        binding.insuranceNo .setOnClickListener {
            calendar(binding.insuranceNo)
        }
        binding.taxPermitNo.setOnClickListener {
            calendar(binding.taxPermitNo)
        }

       // baseprivate.fetchStates(requireContext(),binding.selectStateLocal,2,binding.selectCity)

    }

    private fun fetchState(baseApbcContext: Context?) {
        prefManager=PrefManager(baseApbcContext!!)
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
                            hashMap.put(name,id.toInt())
                        }
                        //spinner
                        val stateadapter = ArrayAdapter(baseApbcContext!!, android.R.layout.simple_spinner_item,hashMap.keys.toList());
                          stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.selectStateLocal.setAdapter(stateadapter)
                        binding.selectStateLocal.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                               selectedState = hashMap.values.toList()[position]
                             //   prefManager.setdriverWorkState(state_id)
                                Log.d("data","selectedState===="+selectedState)

                                    fetchCity(selectedState,baseApbcContext)

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

    }

    private fun fetchCity(localStateId: Int, baseApbcContext: Context) {
        prefManager=PrefManager(baseApbcContext!!)
        var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
        val URL = " https://test.pearl-developer.com/figo/api/get-city"
        val queue = Volley.newRequestQueue(baseApbcContext)
        val json = JSONObject()
        //    var token= prefManager.getToken()

        json.put("state_id",localStateId)

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
                            cityhashMap.put(name,id.toInt())
                        }
                        //spinner
                        val cityadapter = baseApbcContext?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item,cityhashMap.keys.toList()) };
                         if (cityadapter != null) {
                             cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                        binding.selectCity.setAdapter(cityadapter)
                        binding.selectCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                                selectedcity = cityhashMap.values.toList()[position]
                             //  prefManager.setdriverWorkCity(selectedcity)
                                Log.d("SendData", "selectCityid===" + selectedcity)
                                //  fetchCity(id)

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
                // Get your json response and convert it to whatever you want.
            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)


    }


    private fun fetchCabCategory(position: Int) {
        hashMap.clear()
        val URL = " https://test.pearl-developer.com/figo/api/f_category"
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
                                   /* prefManager.setDriverCabCategory(fetchModel(hashMap.values.toList()[position]).toString())
                                    Log.d("DriverCabCategory","DriverCabCategory==="+ prefManager.setDriverCabCategory(fetchModel(hashMap.values.toList()[position]).toString()))*/


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
        val URL = "https://test.pearl-developer.com/figo/api/f_model"
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("category_id",position)

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





    private fun calendar(edit:EditText) {
        val c = Calendar.getInstance()
        // on below line we are getting
        // our day, month and year.
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // on below line we are creating a
        // variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            this.requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString() )
                edit.setText(dat)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            try {
                //Getting the Bitmap from Gallery
                val selectedImageUri = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri)
                val extension = base.getExtension(selectedImageUri!!,requireContext())
                prefManager.setDriverCab_ext(extension!!)
                driver_cab_image = base.BitMapToString(bitmap).toString()
                prefManager.setDriverCab(driver_cab_image)
                binding.uploadCar.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun validateForm() {


        var vechle_type=prefManager.getDriverVechleType()
        var  car_category=prefManager.getDriverCabCategory()
        var car_model=prefManager.getDriverVechleModel()
        var model_year=prefManager.getDriverVechleYear()
        var v_number=binding.vechleNo.text.toString()
        var registration_no=binding.registrationNo.text.toString()
        var insurance_valid_date=binding.insuranceNo.text.toString()
        var permit_valid_date=binding.taxPermitNo.text.toString()


        base.validateDriverInsuranceDate(binding.registrationNo)
        base.validateDriverInsuranceDate(binding.insuranceNo)
        base.validateDriverInsuranceDate(binding.taxPermitNo)

        if (!binding.registrationNo.text.toString().isEmpty()&&!binding.insuranceNo.text.toString().isEmpty()&&!binding.taxPermitNo.text.toString().isEmpty()){

                    binding.cabDetailsLayout.visibility=View.GONE
                    binding.work.visibility=View.VISIBLE
            next.text="Proceed"
           // binding.proceed.visibility = View.VISIBLE
            if (binding.work.visibility==View.VISIBLE){
                next.setOnClickListener {
                    context?.startActivity(Intent( requireContext(), DriverDashBoard::class.java))
                    submitForm(registration_no,insurance_valid_date,permit_valid_date,car_category,car_model,model_year,v_number)

                }
            }

                    //var proceed = binding.proceed

                    /*proceed.setOnClickListener {
                        *//*if (binding.firstWorkState.text.toString().isEmpty()){
                            baseClass.validateWorkState(binding.firstWorkState)
                        }
                        else{*//*



                       // }

                        // context?.startActivity(Intent(requireContext(), DriverDashBoard::class.java))
                    }*/
                }
    }

    private fun submitForm(registration_no:String,insurance_valid_date:String,permit_valid_date:String,car_category:String,car_model:Int,model_year:Int,v_number:String) {

        Log.d("SendData", "updatedStateList==="+updatedStateList)

        val URL = "https://test.pearl-developer.com/figo/api/regitser-driver"
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("token",token)
        json.put("name",prefManager.getDriverName())
        json.put("mobile",prefManager.getMobileNo())
        json.put("state",prefManager.getDriverState())
        json.put("city",prefManager.getDriverCity())
        json.put("dl_number",prefManager.getDL_No())
       // json.put("aadhar_no","8349889375")
        json.put("v_type","type")
        json.put("v_cat","category")
        json.put("v_modal",prefManager.getDriverVechleModel())
        json.put("year",prefManager.getDriverVechleYear())
        json.put("v_no","v_no")
        json.put("registration_no",registration_no)
        json.put("insurance",insurance_valid_date)
        json.put("permit",permit_valid_date)
        json.put("work_place",prefManager.getDriverVechleType().toInt())
        json.put("work_state", selectedState)
        json.put("work_city",selectedcity)
        json.put("selected_state",updatedStateList)
        json.put("driver_ext",prefManager.getDriverProfile_ext())
        json.put("police_ext",prefManager.getPolice_ext())
        json.put("aadhar_front_ext",prefManager.getAadhar_front_ext())
        json.put("aadhar_back_ext",prefManager.getAadhar_back_ext())
        json.put("cab_ext",prefManager.getDriverCab_ext())
        Log.d("SendData", "json===" + json)


        json.put("driver_image",prefManager.getDriverProfile())
        json.put("police_verification",prefManager.getDriverRegistrationNo())
        json.put("aadhar_verification_front",prefManager.getAadhar_verification_front())
        json.put("aadhar_verification_back",prefManager.getAadhar_verification_back())
        json.put("cab_image",prefManager.getDriverCab())

        Log.d("SendData", "aadharVerificationFront===" + prefManager.getDriverProfile())
        Log.d("SendData", "aadharVerificationFront===" + prefManager.getDriverRegistrationNo())
        Log.d("SendData", "aadharVerificationFront===" + prefManager.getAadhar_verification_front())
        Log.d("SendData", "aadharVerificationFront===" + prefManager.getAadhar_verification_back())
        Log.d("SendData", "aadharVerificationBack" + prefManager.getDriverCab())


         val jsonOblect=
             object : JsonObjectRequest(Method.POST, URL, json,
                 Response.Listener<JSONObject?> { response ->
                     Log.d("SendData", "response===" + response)
                     Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                     if (response != null) {

                       //  Navigation.findNavController(requireView()).navigate(R.id.action_driverCabDetailsFragment_to_waitingRegistration)
                         prefManager.setRegistrationToken("Done")
                         prefManager.setCabFormToken("Submitted")
                     }
                     // Get your json response and convert it to whatever you want.
                 }, Response.ErrorListener {
                     // Error
                 }){}
         queue.add(jsonOblect)


    }

    }