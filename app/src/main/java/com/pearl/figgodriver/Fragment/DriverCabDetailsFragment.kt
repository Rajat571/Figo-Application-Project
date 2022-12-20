package com.pearl.figgodriver.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pearl.figgodriver.BaseClass
import com.pearl.figgodriver.DriverDashBoard

import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentDriverCabDetailsBinding
import com.pearlorganisation.PrefManager
import org.json.JSONObject
import java.util.*


class DriverCabDetailsFragment : Fragment() {
    private lateinit var carDP: ImageView
    private var  str: String? = null
    lateinit var binding:FragmentDriverCabDetailsBinding
    lateinit var prefManager: PrefManager
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        //imageuri = it!!
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it!!);
        var base = BaseClass(requireContext())
       str =  base.BitMapToString(bitmap)
        carDP.setImageURI(it)
        // upload()
    }
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
        var base = BaseClass(requireContext())
        var next=view.findViewById<TextView>(R.id.next_button)
        var back=view.findViewById<TextView>(R.id.back_button)


        carDP = view.findViewById(R.id.upload_car)
        carDP.setOnClickListener {
            contract.launch("image/*")

        }
        next.setOnClickListener {
            var driver_name = prefManager.getDriverName()
            var driver_mobile_no= prefManager.getMobileNo()
            var driver_dl_no = prefManager.getDL_No()
            System.out.println("Driver_DL_NO"+driver_dl_no)
            var driver_police_verification_no = prefManager.getPolice_verification()
            var driver_adhar_no = prefManager.getAadhar_no()
            var aadhar_verification_front = prefManager.getAadhar_verification_front()
            var aadhar_verification_back= prefManager.getAadhar_verification_back()
            System.out.println("Aadhar Verification====="+driver_dl_no)
            var driver_profile=prefManager.getDriverProfile()
            var  car_category=binding.carCategory.text.toString()
            var car_model=binding.carModel.text.toString()
            var model_year=binding.modelYear.text.toString()
            var registration_no=binding.registrationNo.text.toString()
            var insurance_no=binding.insuranceNo.text.toString()
            var permit_no=binding.taxPermitNo.text.toString()

                submitForm(driver_name,driver_mobile_no,driver_dl_no,driver_police_verification_no,driver_adhar_no, aadhar_verification_front, aadhar_verification_back,driver_profile,car_category,car_model,model_year,registration_no,insurance_no,permit_no)


//
        //    Navigation.findNavController(view).navigate(R.id.action_cabDetailsFragment_to_blankFragment)
        }

        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_driverCabDetailsFragment_to_figgo_Capton)
        }
       binding.registrationNo.setOnClickListener {
            calendar(binding.registrationNo)
        }
       binding.insuranceNo .setOnClickListener {
            calendar(binding.insuranceNo)
        }
       binding.taxPermitNo.setOnClickListener {
            calendar(binding.taxPermitNo)
        }
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

        json.put("category",car_category)
        json.put("model",car_model)
        json.put("year",model_year)
        json.put("registration_no",registration_no)
        json.put("insurance",insurance_no)
        json.put("permit",permit_no)
        json.put("police_verification",driverPoliceVerificationNo)
        json.put("aadhar_verification_front",aadharVerificationFront)
        json.put("aadhar_verification_back",aadharVerificationBack)
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
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
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
}