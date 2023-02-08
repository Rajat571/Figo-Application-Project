package com.figgo.cabs.figgodriver.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
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
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentDriverCabDetailsBinding
import com.figgo.cabs.figgodriver.Adapter.SpinnerAdapter

import com.figgo.cabs.figgodriver.model.SpinnerObj
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.BasePrivate
import com.figgo.cabs.pearllib.Helper
import kotlinx.android.synthetic.main.cancel_ride_dialog.*
import kotlinx.android.synthetic.main.fragment_driver_cab_details.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*


class DriverCabDetailsFragment : Fragment() {
    private lateinit var carDP: ImageView
    private var  str: String? = null
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    var yearList= listOf<Int>()
    lateinit var binding: FragmentDriverCabDetailsBinding
    lateinit var prefManager: PrefManager
    lateinit var spinner_cabcategory: Spinner
    lateinit var carModel:Spinner
    var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var stateList = kotlin.collections.ArrayList<String>()
    var updatedStateList = ArrayList<String>()
    var modelHashMap  : HashMap<String, Int> = HashMap<String, Int> ()
    lateinit  var next  :TextView
    lateinit  var back  :TextView
    var current_year:Int = 0
    lateinit var spinner_cabtype   :Spinner
    lateinit  var workingarea      :Spinner
    var selectedcity  = 0
    var selectedState = 0

    /******* declaration of  images and extentions   *********/

    var driver_profile_ext:String=""
   var driving_license_ext:String=""
    var cab_insurance_ext:String=""
    var registration_certificate_ext:String=""
     var national_permit_ext:String=""
     var local_permit_ext:String=""
    var taxi_front_pic_ext:String=""
    var aadhar_front_ext:String=""
     var aadhar_back_ext:String=""
    var police_certification_ext:String=""
    var gps_certification_ext:String=""

   var driver_profile:String=""
   var driving_license:String=""
    var cab_insrance:String  =""
    var registration_certificate:String=""
   var national_permit:String=""
    var local_permit:String=""
   var driver_cab_image:String=""
     var aadhar_front:String=""
     var aadhar_back:String=""
   var police_certification:String=""
   var gps_certification:String=""
   var checked=0
    var date:String=""
    var s=""
    var t=""
    var n=""
    var temp=""
   lateinit var calender:Calendar
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

    var baseprivate = object : BasePrivate(){
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
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
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
                binding.uploadImage.visibility=View.GONE
                Navigation.findNavController(view).navigate(R.id.action_driverCabDetailsFragment_to_figgo_Capton)
            }
            else if(binding.uploadImage.visibility==View.VISIBLE){
                layout_cab.visibility=View.GONE
                next.text="NEXT"
                layout_work.visibility=View.VISIBLE
                gps_certification=""
                next.setOnClickListener {
                    validateForm()
                }
                binding.uploadImage.visibility=View.GONE
            }
            else if(binding.work.visibility==View.VISIBLE){

                layout_cab.visibility=View.VISIBLE
                binding.work.visibility=View.GONE
                next.setOnClickListener {
                    validateForm()
                }
               binding.uploadImage.visibility=View.GONE
            }else{
                //  binding.proceed.visibility=View.GONE
                layout_cab.visibility= View.VISIBLE
                layout_work.visibility = View.GONE
            }
        }

        binding.insuranceNo.setOnClickListener {
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
                    binding.insuranceNo.setText(dat1)
                    binding.insuranceNo.setBackgroundResource(R.drawable.input_boder_profile)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        binding.taxPermitNo.setOnClickListener {
            calender= Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    date = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString())
                    t=date
                    Log.d("DATETIME","DATE"+date)
                    val dat1 = (dayOfMonth.toString()+"-" + (monthOfYear + 1) + "-" +year.toString())
                   binding.taxPermitNo.setText(dat1)
                    binding.insuranceNo.setBackgroundResource(R.drawable.input_boder_profile)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        binding.nationalPermitDate.setOnClickListener {
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
                    binding.nationalPermitDate.setText(dat1)
                    binding.insuranceNo.setBackgroundResource(R.drawable.input_boder_profile)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        binding.selfiee.setOnClickListener {

         //   base.checkAndRequestPermissions(requireActivity())


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
                            var intent=Intent()
                            intent.type="image/*"
                            intent.action=Intent.ACTION_GET_CONTENT
                            startActivityForResult(Intent.createChooser(intent,"select Picture"),2)
                        } else if (optionsMenu[i] == "Exit") {
                            dialogInterface.dismiss()
                        }
                    })
                builder.show()

        }
        binding.drivingLicence.setOnClickListener {

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
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),4)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()

        }
        binding.cabInsurance.setOnClickListener {

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
                        startActivityForResult(camera_intent, 5)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),6)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()

        }
        binding.registrationCertificate.setOnClickListener {
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
                        startActivityForResult(camera_intent, 7)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),8)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        binding.nationalPermitImg.setOnClickListener {
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
                        startActivityForResult(camera_intent, 9)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),10)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        binding.localPermitImg.setOnClickListener {
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
                        startActivityForResult(camera_intent, 11)

                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),12)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        binding.taxiFrontPicImg.setOnClickListener {
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
                        startActivityForResult(camera_intent, 13)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),14)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        binding.aadharFrontImg.setOnClickListener {
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
                        startActivityForResult(camera_intent, 15)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),16)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        binding.aadharBackImg.setOnClickListener {
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
                        startActivityForResult(camera_intent, 17)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),18)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }

        binding.policeCertificationImg.setOnClickListener {
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
                        startActivityForResult(camera_intent, 19)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),20)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        binding.gpscertificte.setOnClickListener {
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
                        startActivityForResult(camera_intent, 21)



                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        var intent=Intent()
                        intent.type="image/*"
                        intent.action=Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),22)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
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
                    selectedcity  =0
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

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


    }

    private fun fetchState(baseApbcContext: Context?) {
        statelist.clear()
        updatedStateList.clear()
        prefManager= PrefManager(baseApbcContext!!)
        var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
        var state_id:Int = 0
        //val URL = " https://test.pearl-developer.com/figo/api/get-state"
        var URL=Helper.get_state
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
                        //  val stateadapter =  ArrayAdapter(baseApbcContext!!,android.R.layout.simple_spinner_item,hashMap.keys.toList());
                        val stateadapter = SpinnerAdapter(requireContext(),statelist)
                        //  stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.selectStateLocal.setAdapter(stateadapter)
                        binding.selectStateLocal.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {


                                if(position != 0){
                                    selectedState = statelist.get(position).id.toInt()
                                    updatedStateList.add(selectedState.toString())
                                    //   prefManager.setdriverWorkState(state_id)
                                    Log.d("data","selectedState===="+selectedState)

                                    //fetchCity(selectedState,baseApbcContext)
                                    fetchCity(stateadapter.getItem(position)!!.id.toInt())
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

    }

    private fun fetchCity(localStateId: Int) {
        citylist.clear()
        //prefManager=PrefManager(baseApbcContext!!)
        var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
       // val URL = " https://test.pearl-developer.com/figo/api/get-city"
        var URL=Helper.get_city
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        //    var token= prefManager.getToken()

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
                        //spinner
                        /* val cityadapter = baseApbcContext?.let { ArrayAdapter(it,android.R.layout.simple_spinner_item,cityhashMap.keys.toList()) };
                          if (cityadapter != null) {
                              cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                         }*/
                        val cityadapter = SpinnerAdapter(requireContext(),citylist)
                        binding.selectCity.setAdapter(cityadapter)
                        binding.selectCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {



                                if(position !=0){
                                    selectedcity = citylist.get(position).id.toInt()
                                    //  prefManager.setdriverWorkCity(selectedcity)
                                    Log.d("SendData", "selectCityid===" + selectedcity)
                                    //  fetchCity(id)
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
                // Get your json response and convert it to whatever you want.
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


  /*  private fun calendar(edit: EditText): String {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            { view, year, monthOfYear, dayOfMonth ->

                date = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString())


                Log.d("DATETIME","DATE"+date)


                val dat1 = (dayOfMonth.toString()+"-" + (monthOfYear + 1) + "-" +year.toString())
                edit.setText(dat1)
                binding.insuranceNo.setBackgroundResource(R.drawable.input_boder_profile)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()

        return temp
    }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
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
                    binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
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
                    binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    prefManager.setDriverProfile(driver_profile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 3) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    Log.d("onActivityResult", "response" + photo)
                    Log.d("onActivityResult", "" + selectedImageUri4.toString())
                    driving_license_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    driving_license = base.BitMapToString(bitmap).toString()
                    binding.drivingLicence.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    // prefManager.setDriverProfile(driver_profile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 4) {
                try {
                    var selectedImageUri4 = data?.data
                    Log.d("URI = ", selectedImageUri4.toString())
                    driving_license_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    driving_license = base.BitMapToString(bitmap).toString()
                    binding.drivingLicence.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 5) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    Log.d("onActivityResult", "response" + photo)
                    Log.d("onActivityResult", "" + selectedImageUri4.toString())
                    cab_insurance_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    cab_insrance = base.BitMapToString(bitmap).toString()
                    binding.cabInsurance.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    //prefManager.setDriverProfile(driver_profile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 6) {
                try {
                    var selectedImageUri4 = data?.data
                    cab_insurance_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    cab_insrance = base.BitMapToString(bitmap).toString()
                    binding.cabInsurance.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                }
            } else if (requestCode == 7) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    registration_certificate_ext =
                        base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    registration_certificate = base.BitMapToString(bitmap).toString()
                    binding.registrationCertificate.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 8) {
                try {
                    var selectedImageUri4 = data?.data
                    registration_certificate_ext =
                        base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    registration_certificate = base.BitMapToString(bitmap).toString()
                    binding.registrationCertificate.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 9) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    national_permit_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    national_permit = base.BitMapToString(bitmap).toString()
                    binding.nationalPermitImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 10) {
                try {
                    var selectedImageUri4 = data?.data
                    national_permit_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    national_permit = base.BitMapToString(bitmap).toString()
                    binding.nationalPermitImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 11) {
                try {
                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    local_permit_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    local_permit = base.BitMapToString(bitmap).toString()
                    binding.localPermitImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 12) {
                try {
                    var selectedImageUri4 = data?.data
                    local_permit_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    local_permit = base.BitMapToString(bitmap).toString()
                    binding.localPermitImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 13) {
                try {
                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    taxi_front_pic_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    driver_cab_image = base.BitMapToString(bitmap).toString()
                    binding.taxiFrontPicImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    prefManager.setDriverCab(driver_cab_image)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 14) {
                try {
                    val selectedImageUri = data?.getData()
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().getContentResolver(),
                        selectedImageUri
                    )
                    taxi_front_pic_ext = base.getExtension(selectedImageUri!!, requireContext())
                    prefManager.setDriverCab_ext(taxi_front_pic_ext!!)
                    driver_cab_image = base.BitMapToString(bitmap).toString()
                    binding.taxiFrontPicImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    prefManager.setDriverCab(driver_cab_image)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 15) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    aadhar_front_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_front = base.BitMapToString(bitmap).toString()
                    binding.aadharFrontImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 16) {
                try {
                    var selectedImageUri4 = data?.data
                    aadhar_front_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_front = base.BitMapToString(bitmap).toString()
                    binding.aadharFrontImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 17) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    aadhar_back_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_back = base.BitMapToString(bitmap).toString()
                    binding.aadharBackImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    prefManager.setAadhar_verification_back(aadhar_back)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 18) {
                try {
                    var selectedImageUri4 = data?.data
                    aadhar_back_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    prefManager.setAadhar_back_ext(aadhar_back_ext)
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_back = base.BitMapToString(bitmap).toString()
                    binding.aadharBackImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    prefManager.setAadhar_verification_back(aadhar_back)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 19) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    police_certification_ext =
                        base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    police_certification = base.BitMapToString(bitmap).toString()
                    binding.policeCertificationImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 20) {
                try {
                    var selectedImageUri4 = data?.data
                    police_certification_ext =
                        base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    police_certification = base.BitMapToString(bitmap).toString()
                    binding.policeCertificationImg.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 21) {
                try {

                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    gps_certification_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    Log.d(
                        "DriverActiveRideFragment",
                        "gps_certification_ext" + gps_certification_ext
                    )
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    gps_certification = base.BitMapToString(bitmap).toString()
                    Log.d("DriverActiveRideFragment", "gps_certification_ext" + gps_certification)
                    binding.gpscertificte.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    binding.trackerGPS.setBackgroundResource(R.drawable.input_boder_profile)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 22) {
                try {

                    var selectedImageUri4 = data?.data
                    gps_certification_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    Log.d(
                        "DriverActiveRideFragment1",
                        "gps_certification_ext" + gps_certification_ext
                    )
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    gps_certification = base.BitMapToString(bitmap).toString()
                    Log.d("DriverActiveRideFragment1", "gps_certification_ext" + gps_certification)
                    binding.gpscertificte.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )
                    binding.trackerGPS.setBackgroundResource(R.drawable.input_boder_profile)


                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
Toast.makeText(requireContext(),"Please upload images",Toast.LENGTH_SHORT).show()
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =   MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun validateForm() {

        var vechle_type=prefManager.getDriverVechleType()
        var  car_category=prefManager.getDriverCabCategory()
        var car_model=prefManager.getDriverVechleModel()
        var model_year=prefManager.getDriverVechleYear()
        var v_number=binding.vechleNo.text.toString()
        var registration_no=binding.registrationNo.text.toString()
        var insurance_valid_date=s
        Log.d("insurance_valid_date","insurance_valid_date"+insurance_valid_date)
        var local_permit_date=t
        var national_permit_date=n

        base.validatedriverRegistrationNo(binding.registrationNo)
        base.validatedriverRegistrationNo(binding.vechleNo)
        base.validateDriverInsuranceDate(binding.insuranceNo)
        base.validateDriverInsuranceDate(binding.taxPermitNo)
        base.validateDriverInsuranceDate(binding.nationalPermitDate)

        Log.d("Image","Image"+gps_certification)

        if (binding.radioBTNYes.isChecked)
        {
            checked=1

        }
        else if (binding.radioBTNNo.isChecked){
            checked=0

        }
        if (!binding.insuranceNo.text.toString().isEmpty()&&!binding.taxPermitNo.text.isEmpty()&&base.validatedriverRegistrationNo(binding.vechleNo)&&!binding.nationalPermitDate.text.isEmpty()){

            binding.cabDetailsLayout.visibility=View.GONE
            binding.work.visibility=View.VISIBLE

            if (binding.work.visibility==View.VISIBLE){
                next.setOnClickListener {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                        ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.CAMERA), 200);
                    binding.work.visibility=View.GONE
                    binding.cabDetailsLayout.visibility=View.GONE
                    binding.uploadImage.visibility=View.VISIBLE
                    next.text="Proceed"
                    if (binding.uploadImage.visibility==View.VISIBLE){
                        next.setOnClickListener {
                           // submitForm(insurance_valid_date,local_permit_date,national_permit_date,car_category,car_model,model_year,v_number)

                            if (checked==1){
                                if (gps_certification.isEmpty()){

                                    binding.trackerGPS.setBackgroundResource(R.drawable.input_error_profile)
                                }
                                else{
                                    binding.trackerGPS.setBackgroundResource(R.drawable.input_boder_profile)
                                    submitForm(insurance_valid_date,local_permit_date,national_permit_date,car_category,car_model,model_year,v_number)
                                }
                            }
                            else{
                                submitForm(insurance_valid_date,local_permit_date,national_permit_date,car_category,car_model,model_year,v_number)

                            }
                        }
                    }
                }
            }
        }
    }

    private fun submitForm(insurance_valid_date:String,local_permit_date:String,national_permit_date:String,car_category:String,car_model:Int,model_year:Int,v_number:String) {
        binding.progress.isVisible = true
        binding.cabDetailsLayout.isVisible = false
        binding.work.isVisible = false
        binding.uploadImage.isVisible = false
        Log.d("SendData", "updatedStateList==="+updatedStateList)
        //val URL = "https://test.pearl-developer.com/figo/api/regitser-driver"
        var URL=Helper.regitser_driver
        Log.d("SendData", "URL===" + URL)
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

        json.put("token",token)
        json.put("name",prefManager.getDriverName())
        // json.put("mobile",prefManager.getMobileNo())      //
        json.put("second_number",prefManager.getMobileNo())
        Log.d("Driver Cab Mobile",""+prefManager.getMobileNo())
        json.put("state",prefManager.getDriverState())
        json.put("city",prefManager.getDriverCity())
        json.put("dl_number",prefManager.getDL_No())
        json.put("pan_number",prefManager.getDriverPan_no())
        json.put("aadhar_number",prefManager.getDriverAadhar_no())

        // json.put("v_type","type")
        json.put("v_category",car_category)
        json.put("v_modal",prefManager.getDriverVechleModel())
        json.put("year",prefManager.getDriverVechleYear())
        json.put("v_number",v_number)
        //json.put("registration_no",registration_no)
        json.put("insurance",insurance_valid_date)
        json.put("l_permit",local_permit_date)
        json.put("n_permit",national_permit_date)
        json.put("work_place",prefManager.getDriverVechleType().toInt())
        json.put("work_state", updatedStateList)
        json.put("work_city",selectedcity)
        // json.put("selected_state",updatedStateList)

        //upload image
        json.put("selfie_ext",driver_profile_ext)
        json.put("dl_image_ext",driving_license_ext)
        json.put("cab_insurance_image_ext",cab_insurance_ext)
        json.put("registration_image_ext",registration_certificate_ext)
        json.put("n_permit_image_ext",national_permit_ext)
        json.put("l_permit_image_ext",local_permit_ext)
        json.put("taxi_front_image_ext",taxi_front_pic_ext)
        json.put("aadhar_front_ext",aadhar_front_ext)
        json.put("aadhar_back_ext",aadhar_back_ext)
        json.put("police_cer_image_ext",police_certification_ext)
        json.put("gps_tracking_ext",gps_certification_ext)
        json.put("gps_tracking",gps_certification)


        Log.d("SendData", "json===" + json)

        json.put("selfie",driver_profile)
        json.put("dl_image",driving_license)
        json.put("cab_insurance_image",cab_insrance)
        json.put("registration_image",registration_certificate)
        json.put("n_permit_image",national_permit)
        json.put("l_permit_image",local_permit)
        json.put("taxi_front_image",driver_cab_image)

        json.put("aadhar_front",aadhar_front)
        json.put("aadhar_back",aadhar_back)
        json.put("police_cer_image",police_certification)

        Log.d("SendData", "insurance===" + json)
     /*   Log.d("SendData", "aadharVerificationFront===" + aadhar_front)
        Log.d("SendData", "aadhar_back===" + aadhar_back)
        Log.d("SendData", "police_certification===" +police_certification)
        Log.d("SendData", "driver_profile===" + driver_profile)
        Log.d("SendData", "driving_license" + driving_license)
        Log.d("SendData", "police_certification_ext" + police_certification_ext)*/

         val jsonOblect=
             object : JsonObjectRequest(Method.POST, URL, json,
                 Response.Listener<JSONObject?> { response ->
                     Log.d("SendData", "response===" + response)

                     if (response != null) {

                       //  startActivity(Intent( requireContext(), DriverDashBoard::class.java))
                         binding.progress.isVisible = false
                         prefManager.setDashboard("off")
                         Toast.makeText(this.requireContext(), "Sucessfully sent data for verification.",Toast.LENGTH_SHORT).show()
                         prefManager.setRegistrationToken("Done")
                         val ip_address:String=myFunction()
                         sendreferal(ip_address)

                       //  prefManager.setCabFormToken("Submitted")
                     }else{
                        // Toast.makeText(this.requireContext(), "Sucessfully sent data for verification.",Toast.LENGTH_SHORT).show()
                         baseprivate.ErrorProgressDialog(requireContext(),"101",getString(R.string.server_error))
                     }
                     // Get your json response and convert it to whatever you want.
                 }, Response.ErrorListener {
                     baseprivate.ErrorProgressDialog(requireContext(),"101",getString(R.string.server_error))
                     Log.d("SendData", "response===" +it.message)
                 }){

                 @SuppressLint("SuspiciousIndentation")
                 @Throws(AuthFailureError::class)
                 override fun getHeaders(): Map<String, String> {
                     val headers: MutableMap<String, String> = HashMap()
                     headers.put("Content-Type", "application/json; charset=UTF-8");
                     headers.put("Authorization", "Bearer " + prefManager.getToken());
                     headers.put("Accept","application/vnd.api+json");
                     return headers
                 }
             }
         queue.add(jsonOblect)
    }

    private fun sendreferal(ipAddress: String) {
       // var baseurl="https://test.pearl-developer.com/figo/api/refer/create-referel"
        var baseurl=Helper.create_referel
        var queue= Volley.newRequestQueue(requireContext())
        var json= JSONObject()

        json.put("ip",ipAddress)
        var jsonObjectRequest=object : JsonObjectRequest(Method.POST,baseurl,json, Response.Listener<JSONObject>{
                response ->
            Log.d("Referal Response ",""+response)

            if(!response.equals("null")){
                var bundle = Bundle()
                bundle.putInt("Key",2)
                Navigation.findNavController(requireView()).navigate(R.id.action_driverCabDetailsFragment_to_waitingRegistration,bundle)

            }else{
                baseprivate.ErrorProgressDialog(requireContext(),"101",getString(R.string.server_error))
            }



        },{
            baseprivate.ErrorProgressDialog(requireContext(),"101",getString(R.string.server_error))

            Log.d("Error Response ",""+it)
        }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer " + prefManager.getToken())
                return headers
            }
        }
        queue.add(jsonObjectRequest);
    }

    private fun myFunction():String {
        /*     var ip:String=""
             CoroutineScope(Dispatchers.Main).launch {
                 val myPublicIp = getMyPublicIpAsync().await()
                 Toast.makeText(this@DriverDashBoard, myPublicIp, Toast.LENGTH_LONG).show()
                 ip=myPublicIp.toString()

             }
             return ip.toString()*/
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (true) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0,
                                    delim
                                ).uppercase(Locale.getDefault())
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return ""
    }

}