package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.BuisnessAdapter
import com.figgo.cabs.figgodriver.Adapter.PayHistoryAdapter
import com.figgo.cabs.figgodriver.model.BuisnessAd
import com.figgo.cabs.figgodriver.model.PaymentHistoryModel
import com.figgo.cabs.pearllib.BaseClass
import kotlinx.android.synthetic.main.change_mpin.view.*
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SupportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SupportFragment : Fragment() {
    lateinit var prefManager: PrefManager
    lateinit var cab_type:Spinner
    lateinit var model_type:Spinner
    lateinit var year_list:Spinner
    var hashMap : HashMap<String, Int> = HashMap<String, Int> ()
    var modelHashMap  : HashMap<String, Int> = HashMap<String, Int> ()
    var yearList= listOf<Int>()
    var current_year:Int=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false)
    }
    private lateinit var profile_pic:ImageView
    lateinit var imageuri: Uri;
    lateinit var pref: PrefManager
    private val contract1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        profile_pic.setImageURI(it)
        // upload()

    }
    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager= PrefManager(requireContext())
        var bundle = arguments
        var number = view.findViewById<LinearLayout>(R.id.call_us)
        var email = view.findViewById<LinearLayout>(R.id.email_us)
        var baseClass = BaseClass
        //var exit = view.findViewById<Button>(R.id.exit)
        var intent_call = Intent(Intent.ACTION_DIAL)
        var pref = PrefManager(requireContext())
        // var name:String = pref.getDriverName()
        var tv2 = view.findViewById<TextView>(R.id.textView2)
        tv2.text = "The mission of our company is to provide the most clean and safe cab/taxi services throughout India by ensuring the security of our customers during the whole duration of their trip. We have a mission to provide a comfortable travel/journey experience in the most cost-effective manner possible. We also aim to be known as one of the most renowned and trusted cab/taxi company and become role model when it comes to the concept of operations and giving services to customers."
        intent_call.data = Uri.parse("tel:"+"+919715597855")
        number.setOnClickListener {
            startActivity(intent_call)
        }
        var intent_email = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+"support@figgocabs.com"))
        email.setOnClickListener{
            startActivity(intent_email)
        }
        var support = view.findViewById<ConstraintLayout>(R.id.Support_Nav)
        var about = view.findViewById<ConstraintLayout>(R.id.About_Nav)
        var terms = view.findViewById<ConstraintLayout>(R.id.Terms_Nav)
        var cancel = view.findViewById<ConstraintLayout>(R.id.Cancel_Nav)

        var profile = view.findViewById<LinearLayout>(R.id.Profile_Nav)
        val change_mpin_nav = view.findViewById<LinearLayout>(R.id.Change_MPIN_Nav)
        val cab_nav = view.findViewById<LinearLayout>(R.id.Cab_Nav)
        var str = bundle?.getString("Key")
        var buisness_recylcer = view.findViewById<RecyclerView>(R.id.Buisness_Nav_Recycler)
        var history = view.findViewById<LinearLayout>(R.id.historyofrecharge)


        profile_pic = view.findViewById<ImageView>(R.id.change_profile_pic_image)
        if(str.equals("About")){
        val myWebView: WebView = view.findViewById(R.id.about_wv)
            support.visibility=View.GONE
            about.visibility=View.VISIBLE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
            history.visibility=View.GONE
        myWebView.loadUrl("https://figgocabs.com/about/")
    }else if (str.equals("Support")){
            support.visibility=View.VISIBLE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            cab_nav.visibility=View.GONE
            history.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
        }else if (str.equals("Terms")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.VISIBLE
            cancel.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            profile.visibility=View.GONE
            history.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
        }else if (str.equals("Cancel")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            profile.visibility=View.GONE
            cab_nav.visibility=View.GONE
            history.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
            cancel.visibility=View.VISIBLE
        }else if (str.equals("Profile")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.VISIBLE
            history.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
        }
        else if(str.equals("Change_Mpin")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            cab_nav.visibility=View.GONE
            profile.visibility=View.GONE
            history.visibility=View.GONE
            change_mpin_nav.visibility = View.VISIBLE
            buisness_recylcer.visibility=View.GONE
        }
        else if(str.equals("Cab")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            history.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.VISIBLE
            buisness_recylcer.visibility=View.GONE
        }
        else if(str.equals("Buisness")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            history.visibility=View.GONE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            buisness_recylcer.visibility=View.VISIBLE
        }
        else if(str.equals("History")){
            support.visibility=View.GONE
            about.visibility=View.GONE
            terms.visibility=View.GONE
            cancel.visibility=View.GONE
            profile.visibility=View.GONE
            history.visibility=View.VISIBLE
            change_mpin_nav.visibility = View.GONE
            cab_nav.visibility=View.GONE
            buisness_recylcer.visibility=View.GONE
        }

    pref = PrefManager(requireContext())
       updateProfile(view)
        updateMpin(view)
        updateDriverCabDetails(view)
        figgoBuisness(view)
        paymentHistoryAdapter(view)
    }

    private fun paymentHistoryAdapter(view: View) {
        var history = view.findViewById<RecyclerView>(R.id.RechargeHistoryRecycler)
        var data = ArrayList<PaymentHistoryModel>()
        data.add(PaymentHistoryModel("2.10.2022","300","Mr. XYZ XYZ","15minute 6:25pm","10 KM",1))
        data.add(PaymentHistoryModel("2.10.2022","1000","Mr. XYZ XYZ","15minute 6:25pm","10 KM",0))
        data.add(PaymentHistoryModel("2.10.2022","1500","Mr. XYZ XYZ","15minute 6:25pm","10 KM",0))
        data.add(PaymentHistoryModel("2.10.2022","23","Mr. XYZ XYZ","15minute 6:25pm","10 KM",1))
        data.add(PaymentHistoryModel("2.10.2022","2300","Mr. XYZ XYZ","15minute 6:25pm","10 KM",1))
        history.adapter= PayHistoryAdapter(data)
        history.layoutManager=LinearLayoutManager(requireContext())
    }


    private fun updateMpin(view: View) {
        var mpin_done = view.findViewById<TextView>(R.id.mpin_done)
        var mpin_exit = view.findViewById<TextView>(R.id.mpin_exit)
        var mpin_old = view.findViewById<EditText>(R.id.old_mpin)
        var mpin_new = view.findViewById<EditText>(R.id.mPin_new)
        var mpin_confirm = view.findViewById<EditText>(R.id.confirm_mpin)

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

    private fun updateProfile(view: View) {
   /*     var change = view.findViewById<ConstraintLayout>(R.id.change_profile_pic)
        var name = view.findViewById<EditText>(R.id.update_name)
        var vehicle_no = view.findViewById<EditText>(R.id.update_state)
        var mobile_number = view.findViewById<EditText>(R.id.update_number)
        var email = view.findViewById<EditText>(R.id.update_email)
        var update = view.findViewById<Button>(R.id.update_button)

      update.setOnClickListener {
//            pref.setDriverName(name.text.toString())
//            pref.setMobile_No(mobile_number.text.toString())
//            //pref.setDriveState(1)
        }

        change.setOnClickListener {
           // contract1.launch("image/*")
            val optionsMenu = arrayOf<CharSequence>(
                "Take Photo",
                "Choose from Gallery",
                "Exit"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            // set the items in builder
            builder.setItems(optionsMenu,
                DialogInterface.OnClickListener { dialogInterface, i ->
                    if (optionsMenu[i] == "Take Photo") {
                        // Open the camera and get the photo
                        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePicture, 0)
                    } else if (optionsMenu[i] == "Choose from Gallery") {
                        // choose from  external storage
                        val pickPhoto =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(pickPhoto, 1)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }*/
    */
    }


    private fun figgoBuisness(view: View) {
        var buisness_recylcer = view.findViewById<RecyclerView>(R.id.Buisness_Nav_Recycler)
        var data = ArrayList<BuisnessAd>()
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        data.add(BuisnessAd("https://www.youtube.com/watch?v=5twT0x6cvcg",R.drawable.figgodriverad))
        buisness_recylcer.adapter= BuisnessAdapter(data,view)
        buisness_recylcer.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun updateDriverCabDetails(view: View) {
        var cab_category=view.findViewById<Spinner>(R.id.show_cab_category)
       cab_type=view.findViewById(R.id.show_cab_type)
        model_type=view.findViewById(R.id.show_model_type)
        year_list=view.findViewById<Spinner>(R.id.show_year_list)





        var adapter = ArrayAdapter.createFromResource(requireContext(),R.array.CabType,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        cab_category?.adapter = adapter
        cab_category?.onItemSelectedListener = object :
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

            Log.d("Model year","Model year==="+i)
        }

        val dateadapter =  ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,yearList);
        dateadapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        year_list.adapter = dateadapter
        year_list.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Toast.makeText(requireContext(),""+position, Toast.LENGTH_SHORT).show()

                val i = yearList[position]

                //prefManager.setDriverVechleYear(i)
                Log.d("Model year","Model year==="+i)
                // Log.d("Model year","Model year==="+ prefManager.setDriverVechleType(position.toString()))


            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }


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
                            cab_type.adapter = cabcategoryadapter
                            cab_type?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                                    fetchModel(hashMap.values.toList()[position])
                                   // prefManager.setDriverCabCategory(hashMap.values.toList()[position].toString())
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
                            model_type.adapter = cabModeladapter
                            model_type?.onItemSelectedListener = object :   AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                                    //  fetchModel(hashMap.values.toList()[position])
                                    Log.d("SendData", "modelHashMap.values.toList()[position]===" + modelHashMap.values.toList()[position])
                                   // prefManager.setDriverVechleModel(modelHashMap.values.toList()[position])
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
}