package com.pearl.figgodriver.Fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pearl.PrefManager
import com.pearl.figgodriver.Adapter.SpinnerAdapter
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentFiggoCaptonBinding
import com.pearl.figgodriver.model.SpinnerObj
import com.pearl.pearllib.BaseClass
import com.pearl.pearllib.BasePrivate

import kotlinx.android.synthetic.main.fragment_driver_cab_details.view.*
import kotlinx.android.synthetic.main.fragment_figgo__capton.*
import org.json.JSONObject


class Figgo_Capton : Fragment(){

    lateinit var imageuri: Uri
    lateinit var binding:FragmentFiggoCaptonBinding
    var  aadhar_verification_front:String=""
    var aadhar_verification_back :String=""
    var driverdp :String=""
    var police_verification:String=""
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    lateinit var baseclass: BaseClass
    lateinit var basePrivate:  BasePrivate
    lateinit var spinner_state: Spinner
    // lateinit  var  backstr: String
    var args = Bundle()
    lateinit var prefManager: PrefManager
    var statehashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_figgo__capton, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        baseclass=object : BaseClass(){
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

        fetchState()
        // basePrivate=BasePrivate()


        binding.llAadharFront.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)


            //   contract1.launch("image/*")
        }
        binding.llAadharBack.setOnClickListener {
            var intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),2)
            // contract2.launch("image/*")
        }
        binding.selfiee.setOnClickListener {
            var intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),4)
            //contract3.launch("image/*")
        }
        binding.policeVerification.setOnClickListener {
            var intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),3)
            //contract4.launch("image/*")
        }
        var next=view.findViewById<TextView>(R.id.next_button)
        next.setOnClickListener {
            //  sharedPref(view)
            validateForm()

            var driver_name=binding.drivername.text.toString()
            var driver_mobile_no=binding.drivermobileno.text.toString()
            var driver_dl_no=binding.driverdlno.text.toString()
            var driver_pan_no=binding.driverPanNo.text.toString()
            var driver_aadhar_no=binding.driverAdharNo.text.toString()
            prefManager.setDL_No(driver_dl_no)
            prefManager.setDriverName(driver_name)
            prefManager.setMobile_No(driver_mobile_no)
            prefManager.setDriverAadhar_no(driver_aadhar_no)
            prefManager.setDriverPan_no(driver_pan_no)

        }

        var back=view.findViewById<TextView>(R.id.back_button)
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_figgo_Capton_to_driverWelcomeFragment)
        }

    }

    private fun fetchState() {
        statehashMap.clear()
        statelist.clear()
        val URL = " https://test.pearl-developer.com/figo/api/get-state"

        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

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
                        // statehashMap.put("Select State",0)
                        for (i in 0..jsonArray.length()-1){
                            val rec: JSONObject = jsonArray.getJSONObject(i)
                            var name = rec.getString("name")
                            var id = rec.getString("id")

                            statelist.add(SpinnerObj(name,id))
                            statehashMap.put(name,id.toInt())
                        }

                        //spinner
                        //  val stateadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,statehashMap.keys.toList());
                        Log.d("SendData", "statelist===" + statelist)
                        val stateadapter = SpinnerAdapter(requireContext(),statelist)

                        // stateadapter.setDropDownViewResource(R.layout.spinneritemlayout)
                        binding.spinnerState.setAdapter(stateadapter)


                        binding.spinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                                var id1 = stateadapter.getItem(position)!!.id.toInt()


                                Log.d("SendData", "id1===" + stateadapter.getItem(position)!!.id.toInt())

                                //binding.selectStateTV.text=statehashMap.keys.toList()[position]
                                prefManager.setDriveState(id1!!.toInt())
                                fetchCity(stateadapter.getItem(position)!!.id.toInt())
                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }
                        }

                    }else{

                    }

                }
                // Get your json response and convert it to whatever you want.
            }, Response.ErrorListener {
                // Error
            }){}
        queue.add(jsonOblect)

    }

    private fun fetchCity(id: Int) {
        cityhashMap.clear()
        citylist.clear()
        val URL = " https://test.pearl-developer.com/figo/api/get-city"
        val queue = Volley.newRequestQueue(requireContext())
        val json = JSONObject()
        var token= prefManager.getToken()

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
                            citylist.add(SpinnerObj(name,id))
                            cityhashMap.put(name,id.toInt())



                        }
                        //spinner
                        //  val stateadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,cityhashMap.keys.toList());
                        val stateadapter = SpinnerAdapter(requireContext(),citylist)
                        //   stateadapter.setDropDownViewResource(R.layout.spinneritemlayout)
                        binding.spinnerCity.setAdapter(stateadapter)





                        binding.spinnerCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {

                                val id1 = stateadapter.getItem(position)?.id
                                prefManager.setDriveCity(id1!!.toInt())
                                Log.d("SendData", "cityid===" + id1)
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

/*

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1){
            try {
                //Getting the Bitmap from Gallery
                val selectedImageUri = data?.getData()
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri)


                aadhar_verification_front = baseclass.BitMapToString(bitmap).toString()

                prefManager.setAadhar_verification_front(aadhar_verification_front)
                binding.upAdharfront.setImageBitmap(bitmap)
                val extension = baseclass.getExtension(selectedImageUri!!,requireContext())
                prefManager.setAadhar_front_ext(extension)
                binding.upAdharfront.visibility=View.VISIBLE
                binding.aadharfrontIV.visibility=View.GONE
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }  else if (requestCode==2){
            var selectedImageUri2=data?.data
            var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri2)
            val extension2 = baseclass.getExtension(selectedImageUri2!!,requireContext())
            prefManager.setAadhar_back_ext(extension2)
            aadhar_verification_back = baseclass.BitMapToString(bitmap).toString()
            prefManager.setAadhar_verification_back(aadhar_verification_back)
            binding.upAdharback.setImageBitmap(bitmap)
            binding.upAdharback.visibility=View.VISIBLE
            binding.aadharBackIV.visibility=View.GONE
        }

        else if(requestCode==3){
            var selectedImageUri3=data?.data
            var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri3)
            val extension3 = baseclass.getExtension(selectedImageUri3!!,requireContext())
            prefManager.setPolice_ext(extension3)
            police_verification= baseclass.BitMapToString(bitmap).toString()
            prefManager.setPolice_verification(police_verification)
            binding.ivPoliceVerification.setImageBitmap(bitmap)
            binding.policeVerification.setBackgroundResource(R.drawable.input_boder_profile)
        }

        else if(requestCode==4){
            var selectedImageUri4=data?.data
            val extension3 = baseclass.getExtension(selectedImageUri4!!,requireContext())
            prefManager.setDriverProfile_ext(extension3)
            var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri4)
            driverdp = baseclass.BitMapToString(bitmap).toString()
            binding.selfiee.setImageBitmap(bitmap)
            prefManager.setDriverProfile(driverdp)
        }
    }
*/

    private fun validateForm() {


        baseclass.validateName(binding.drivername)
        baseclass.validateNumber(binding.drivermobileno)
        //baseclass.validateState(binding.driverstate)
        //baseclass.validateCity(binding.drivercity)
        baseclass.validatedriverDLNo(binding.driverdlno)
        baseclass.validatedriverDLNo(binding.driverPanNo)
        baseclass.validatedriverDLNo(binding.driverAdharNo)


        if (binding.upAdharfront.drawable==null){
            ll_aadhar_front.setBackgroundResource(R.drawable.input_error_profile)
        }
        else{
            ll_aadhar_front.setBackgroundResource(R.drawable.input_boder_profile)
        }
        if (binding.upAdharback.drawable==null){
            ll_aadhar_back.setBackgroundResource(R.drawable.input_error_profile)
        }
        else{

            ll_aadhar_back.setBackgroundResource(R.drawable.input_boder_profile)
        }



        // binding.aadharfrontTV.setError("Please upload aadhar front image")

   /*     if (!binding.drivername.text.isEmpty()&&!binding.drivermobileno.text.isEmpty()&&!binding.driverdlno.text.isEmpty()&&binding.upAdharfront.drawable!=null&&binding.upAdharback.drawable!=null){



            // binding.aadharfrontIV.visibility=View.GONE
            binding.upAdharfront.visibility=View.VISIBLE
            binding.aadharBackIV.visibility=View.GONE
            binding.upAdharback.visibility=View.VISIBLE




            Navigation.findNavController(requireView()).navigate(R.id.action_figgo_Capton_to_driverCabDetailsFragment,args)

        }*/
        if (!binding.drivername.text.isEmpty()&&baseclass.validateNumber(binding.drivermobileno)&&!binding.driverdlno.text.isEmpty()&&!binding.driverPanNo.text.isEmpty()&&!binding.driverAdharNo.text.isEmpty()){


            // binding.aadharfrontIV.visibility=View.GONE
           /* binding.upAdharfront.visibility=View.VISIBLE
            binding.aadharBackIV.visibility=View.GONE
            binding.upAdharback.visibility=View.VISIBLE*/




            Navigation.findNavController(requireView()).navigate(R.id.action_figgo_Capton_to_driverCabDetailsFragment,args)

        }

    }

}


