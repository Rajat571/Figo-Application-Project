package com.pearl.figgodriver.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pearl.figgodriver.DriverDashBoard
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentDriverCabDetailsBinding
import com.pearl.figgodriver.model.CabCategoryObj
import com.pearl.figgodriver.model.SpinnerObj
import com.pearl.pearllib.BaseClass
import com.pearl.pearllib.BasePrivate
import com.pearlorganisation.PrefManager
import kotlinx.android.synthetic.main.cancel_ride_dialog.*
import kotlinx.android.synthetic.main.fragment_driver_cab_details.*
import org.json.JSONObject
import java.io.IOException
import java.time.Year
import java.util.*
import kotlin.collections.HashMap


class DriverCabDetailsFragment : Fragment() {
    private lateinit var carDP: ImageView
    private var  str: String? = null
    var  driver_cab_image:String=""

    lateinit var binding:FragmentDriverCabDetailsBinding
    lateinit var prefManager: PrefManager
    lateinit var spinner_cabcategory: Spinner
    //   var categorylist: List<CabCategoryObj> = listOf<CabCategoryObj>(CabCategoryObj("",""))
    lateinit var carModel:Spinner
    //val categorylist: ArrayList<SpinnerObj> = ArrayList()
    var hashMap : HashMap<String, Int>
            = HashMap<String, Int> ()
    var modelHashMap  : HashMap<String, Int> = HashMap<String, Int> ()

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


    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        //imageuri = it!!
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it!!);

       str = base.BitMapToString(bitmap)
        carDP.setImageURI(it)
        // upload()
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
        var next=view.findViewById<TextView>(R.id.next_button)
        var back=view.findViewById<TextView>(R.id.back_button)
        var current_year:Int = Calendar.getInstance().get(Calendar.YEAR)
        spinner_cabcategory = view.findViewById<Spinner>(R.id.spinner_cabtype)
        var spinner_cabtype = view?.findViewById<Spinner>(R.id.spinner_cabcategory)!!
        var workingarea = view.findViewById<Spinner>(R.id.working_area_spinner)
        var yearList= listOf<Int>()
         carModel = view.findViewById<Spinner>(R.id.vechle_model)
        var adapter = ArrayAdapter.createFromResource(requireContext(),R.array.CabType,android.R.layout.simple_spinner_item);
        var adapter2 = ArrayAdapter.createFromResource(requireContext(),R.array.work_type,android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        spinner_cabtype?.adapter = adapter
        for(i in 2000..current_year)
        {
            yearList+=i
        }
        val dateadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,yearList);
        dateadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        binding.modelYear.adapter = dateadapter
        spinner_cabtype?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()

                fetchCabCategory(position)



            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

//
//        hashMap.put("IronMan" , 3000)
//        hashMap.put("Thor" , 100)
//        hashMap.put("SpiderMan" , 1100)



        binding.proceed.visibility=View.GONE
        workingarea.adapter = adapter2
        workingarea.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
               if(position==2){
                   binding.workingStateLayout.visibility=View.GONE
                   binding.workingLocalLayout.visibility=View.VISIBLE
               }
                else{
                   binding.workingStateLayout.visibility=View.VISIBLE
                   binding.workingLocalLayout.visibility=View.GONE
               }


            }



            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


        var layout_cab = binding.chooseUser
        var layout_work = binding.work

        layout_cab.visibility= View.VISIBLE
        layout_work.visibility = View.GONE

        carDP = view.findViewById(R.id.upload_car)
        carDP.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        next.setOnClickListener {
            layout_cab.visibility= View.GONE
            layout_work.visibility = View.VISIBLE
            var driver_name = prefManager.getDriverName()
            var driver_mobile_no= prefManager.getMobileNo()
            var driver_dl_no = prefManager.getDL_No()
            binding.proceed.visibility=View.VISIBLE
            System.out.println("Driver_DL_NO"+driver_dl_no)
            var driver_police_verification_no = prefManager.getPolice_verification()
            var driver_adhar_no = prefManager.getAadhar_no()
            var aadhar_verification_front = prefManager.getAadhar_verification_front()
            var aadhar_verification_back= prefManager.getAadhar_verification_back()
            System.out.println("Aadhar Verification====="+driver_dl_no)
            var driver_profile=prefManager.getDriverProfile()
           // var  car_category=binding.carCategory.text.toString()
            var  car_category="binding.carCategory.text.toString()"
            var car_model="binding.carModel.text.toString()"
            var model_year="binding.modelYear.text.toString()"
            var registration_no=binding.registrationNo.text.toString()
            var insurance_no=binding.insuranceNo.text.toString()
            var permit_no=binding.taxPermitNo.text.toString()
            var proceed = binding.proceed


        proceed.setOnClickListener {
                submitForm(driver_name,driver_mobile_no,driver_dl_no,driver_police_verification_no,driver_adhar_no, aadhar_verification_front, aadhar_verification_back,driver_profile,car_category,car_model,model_year,registration_no,insurance_no,permit_no)
               // context?.startActivity(Intent(requireContext(), DriverDashBoard::class.java))
            }
        }

        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_driverCabDetailsFragment_to_figgo_Capton)

            layout_cab.visibility= View.VISIBLE
            layout_work.visibility = View.GONE
        }
//       binding.registrationNo.setOnClickListener {
//            //calendar(binding.registrationNo)
//        }
       binding.insuranceNo .setOnClickListener {
            calendar(binding.insuranceNo)
        }
       binding.taxPermitNo.setOnClickListener {
            calendar(binding.taxPermitNo)
        }


        baseprivate.fetchStates(requireContext(),binding.selectState1)
        baseprivate.fetchStates(requireContext(),binding.selectState2)
        baseprivate.fetchStates(requireContext(),binding.selectState3)
        baseprivate.fetchStates(requireContext(),binding.selectState4)
        baseprivate.fetchStates(requireContext(),binding.selectState5)



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



    private fun submitForm(driverName: String, driverMobileNo: String, driverDlNo: String, driverPoliceVerificationNo: String, driverAdharNo: String, aadharVerificationFront: String, aadharVerificationBack: String,driver_profile:String,car_category:String,car_model:String,model_year:String,registration_no:String,insurance_no:String,permit_no:String) {
        val URL = " https://test.pearl-developer.com/figo/api/regitser-driver"
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
       var token= prefManager.getToken()

        json.put("token",token)
        json.put("name",driverName)
        json.put("email","madhuri@gmail.com")
        json.put("password","123456")
        json.put("dl_number",driverDlNo)
        json.put("aadhar_front_ext",prefManager.getAadhar_front_ext())
        json.put("aadhar_back_ext",prefManager.getAadhar_back_ext())
        json.put("police_ext",prefManager.getPolice_ext())
        json.put("driver_ext",prefManager.getDriverProfile_ext())
        json.put("cab_ext",prefManager.getDriverCab_ext())

        json.put("category",car_category)
        json.put("model",car_model)
        json.put("year",model_year)
        json.put("registration_no",registration_no)
        json.put("insurance",insurance_no)
        json.put("permit",permit_no)
        json.put("police_verification",driverPoliceVerificationNo)
        json.put("aadhar_verification_front",aadharVerificationFront)
        json.put("aadhar_verification_back",aadharVerificationBack)
        json.put("cab_image",prefManager.getDriverCab())
        Log.d("SendData", "json===" + json)
        Log.d("SendData", "json===" + aadharVerificationFront)
        Log.d("SendData", "json===" + aadharVerificationBack)

        val jsonOblect=
            object : JsonObjectRequest(Method.POST, URL, json,
                Response.Listener<JSONObject?> { response ->
                    Log.d("SendData", "response===" + response)
                    Toast.makeText(this.requireContext(), "response===" + response,Toast.LENGTH_SHORT).show()
                    if (response != null) {
                        context?.startActivity(Intent( requireContext(), DriverDashBoard::class.java))
                        prefManager.setCabFormToken("Submitted")
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
                val extension = base.getExtension(selectedImageUri)
                prefManager.setDriverCab_ext(extension!!)
                driver_cab_image = base.BitMapToString(bitmap).toString()
                prefManager.setDriverCab(driver_cab_image)
                binding.uploadCar.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }



}