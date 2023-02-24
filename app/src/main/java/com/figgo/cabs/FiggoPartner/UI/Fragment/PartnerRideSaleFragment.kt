package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.FiggoPartner.Adapter.PartnerAllRideAdapter
import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentFiggoCaptonBinding
import com.figgo.cabs.databinding.FragmentPartnerRideSaleBinding
import com.figgo.cabs.figgodriver.Adapter.SpinnerAdapter
import com.figgo.cabs.figgodriver.Fragment.DriverCabDetailsFragment
import com.figgo.cabs.figgodriver.Fragment.Figgo_Capton
import com.figgo.cabs.figgodriver.Fragment.allRideRS
import com.figgo.cabs.figgodriver.model.SpinnerObj
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.Helper
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerRideSaleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PartnerRideSaleFragment : Fragment() {

    lateinit var binding: FragmentPartnerRideSaleBinding
    lateinit var baseclass: BaseClass
    lateinit var prefManager: PrefManager
    var args = Bundle()
    val statelist: ArrayList<SpinnerObj> = ArrayList()
    val citylist: ArrayList<SpinnerObj> = ArrayList()
    var statehashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var cityhashMap : HashMap<String, Int> = HashMap<String, Int> ()
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_partner_ride_sale, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var arg = arguments
        var user_type = arg?.getString("Parent")

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
        try {
            fetchState()
        }catch(e:Exception){

        }

        binding.partllAadharFront.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }

        binding.partllAadharBack.setOnClickListener {
            var intent= Intent()
            intent.type="image/*"
            intent.action= Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),2)
        }
        binding.partselfiee.setOnClickListener {
            var intent= Intent()
            intent.type="image/*"
            intent.action= Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),4)
        }
        /*   binding.partverifyReferalcode.setOnClickListener {
               baseclass.validateReferalCode()
           }*/
        binding.partpoliceVerification.setOnClickListener {
            var intent= Intent()
            intent.type="image/*"
            intent.action= Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Picture"),3)
        }

            var next=view.findViewById<Button>(R.id.partner_next_button)

        next.setOnClickListener {

            validateForm()

            var driver_name=binding.partdrivername.text.toString()
            var driver_mobile_no=binding.partdrivermobileno.text.toString()
            var driver_dl_no=binding.partdriverdlno.text.toString()
            var driver_pan_no=binding.partdriverPanNo.text.toString()
            var driver_aadhar_no=binding.partdriverAdharNo.text.toString()
            Log.d("Second Number check",""+driver_mobile_no)
            prefManager.setDL_No(driver_dl_no)
            prefManager.setDriverName(driver_name)
            prefManager.setMobile_No(driver_mobile_no)
            prefManager.setDriverAadhar_no(driver_aadhar_no)
            prefManager.setDriverPan_no(driver_pan_no)
        }

        /*var back=view.findViewById<TextView>(R.id.back_button)

        back.visibility = View.GONE*/
        /*back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_ride_sale_to_home)
        }*/
    }

    private fun fetchState() {
        statehashMap.clear()
        statelist.clear()
        // val URL = "https://test.pearl-developer.com/figo/api/get-state"
        var URL= Helper.get_state
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
                        try{
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
                        binding.partspinnerState.setAdapter(stateadapter)


                        binding.partspinnerState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                                var id1 = stateadapter.getItem(position)!!.id.toInt()


                                Log.d("SendData", "id1===" + stateadapter.getItem(position)!!.id.toInt())

                                //binding.partselectStateTV.text=statehashMap.keys.toList()[position]
                                prefManager.setDriveState(id1!!.toInt())
                                fetchCity(stateadapter.getItem(position)!!.id.toInt())
                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                            }
                        }

                    }catch (e:Exception) {
                        }
                        }
                        else
                        {

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
        var URL= Helper.get_city
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

                        binding.partspinnerCity.setAdapter(stateadapter)


                        binding.partspinnerCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
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

        baseclass.validateName(binding.partdrivername)
        baseclass.validateNumber(binding.partdrivermobileno)
        baseclass.validateAadharNo(binding.partdriverAdharNo)
        baseclass.validatePanNo(binding.partdriverPanNo)
        baseclass.validateDLNo(binding.partdriverdlno)

        if (baseclass.validateName(binding.partdrivername)&&baseclass.validateNumber(binding.partdrivermobileno)&&baseclass.validateDLNo(binding.partdriverdlno)&&baseclass.validatePanNo(binding.partdriverPanNo)&&baseclass.validateAadharNo(binding.partdriverAdharNo)){

            Navigation.findNavController(requireView()).navigate(R.id.action_ride_sale_to_partner_drivercab,args)
            //parentFragmentManager.beginTransaction().add(DriverCabDetailsFragment(),"").commit()
        }
    }

    override fun onResume() {
        super.onResume()

        /*binding.partdrivername.setText(prefManager.getDriverName())
        binding.partdrivermobileno.setText(prefManager.getMobileNo())
        binding.partdriverdlno.setText(prefManager.getDL_No())
        binding.partdriverPanNo.setText(prefManager.getDriverPan_no())
        binding.partdriverAdharNo.setText(prefManager.getDriverAadhar_no())*/
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerRideSaleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerRideSaleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }





}