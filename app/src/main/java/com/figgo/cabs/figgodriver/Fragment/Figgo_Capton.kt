package com.figgo.cabs.figgodriver.Fragment


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
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentFiggoCaptonBinding
import com.figgo.cabs.figgodriver.Adapter.SpinnerAdapter
import com.figgo.cabs.figgodriver.model.SpinnerObj
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.BasePrivate
import com.figgo.cabs.pearllib.Helper

import kotlinx.android.synthetic.main.fragment_driver_cab_details.view.*
import kotlinx.android.synthetic.main.fragment_figgo__capton.*
import org.json.JSONObject


class Figgo_Capton : Fragment(){


    lateinit var binding: FragmentFiggoCaptonBinding
    lateinit var baseclass: BaseClass
    lateinit var prefManager: PrefManager
    var args = Bundle()
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    var statehashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_figgo__capton, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*****************************    PrefManager initialization  *******************************************/

        prefManager = PrefManager(requireContext())

        /*****************************    BaseClass initialization  *******************************************/
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

        binding.llAadharFront.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        binding.llAadharBack.setOnClickListener {
            var intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),2)
        }
        binding.selfiee.setOnClickListener {
            var intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),4)

        }
     /*   binding.verifyReferalcode.setOnClickListener {
            baseclass.validateReferalCode()
        }*/
        binding.policeVerification.setOnClickListener {
            var intent=Intent()
            intent.type="image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),3)
        }

        var next=view.findViewById<TextView>(R.id.next_button)

        next.setOnClickListener {

            validateForm()

            var driver_name=binding.drivername.text.toString()
            var driver_mobile_no=binding.drivermobileno.text.toString()
            var driver_dl_no=binding.driverdlno.text.toString()
            var driver_pan_no=binding.driverPanNo.text.toString()
            var driver_aadhar_no=binding.driverAdharNo.text.toString()
            Log.d("Second Number check",""+driver_mobile_no)
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
       // val URL = "https://test.pearl-developer.com/figo/api/get-state"
        var URL=Helper.get_state
        Log.d("GetDATA","URL"+URL)

        val queue = Volley.newRequestQueue(requireContext())
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

            }, Response.ErrorListener {

            }){}
        queue.add(jsonOblect)

    }

    private fun fetchCity(id: Int) {
        cityhashMap.clear()
        citylist.clear()
        //val URL = " https://test.pearl-developer.com/figo/api/get-city"
        var URL=Helper.get_city
        Log.d("Figgo_Capton","URL "+URL)
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

                        val stateadapter = SpinnerAdapter(requireContext(),citylist)

                        binding.spinnerCity.setAdapter(stateadapter)


                        binding.spinnerCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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

    private fun validateForm() {

        baseclass.validateName(binding.drivername)
        baseclass.validateNumber(binding.drivermobileno)
        baseclass.validateAadharNo(binding.driverAdharNo)
        baseclass.validatePanNo(binding.driverPanNo)
        baseclass.validateDLNo(binding.driverdlno)

        if (baseclass.validateName(binding.drivername)&&baseclass.validateNumber(binding.drivermobileno)&&baseclass.validateDLNo(binding.driverdlno)&&baseclass.validatePanNo(binding.driverPanNo)&&baseclass.validateAadharNo(binding.driverAdharNo)){

            Navigation.findNavController(requireView()).navigate(R.id.action_figgo_Capton_to_driverCabDetailsFragment,args)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.drivername.setText(prefManager.getDriverName())
        binding.drivermobileno.setText(prefManager.getMobileNo())
        binding.driverdlno.setText(prefManager.getDL_No())
        binding.driverPanNo.setText(prefManager.getDriverPan_no())
        binding.driverAdharNo.setText(prefManager.getDriverAadhar_no())
    }

}


