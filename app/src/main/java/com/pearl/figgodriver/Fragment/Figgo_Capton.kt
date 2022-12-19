package com.pearl.figgodriver.Fragment

import android.content.Context
import android.net.Uri
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pearl.figgodriver.BaseClass
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentFiggoCaptonBinding
import com.pearlorganisation.PrefManager
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

class Figgo_Capton : Fragment() {
    lateinit var up_adharfront: ImageView;
    lateinit var up_adharback:ImageView;
    lateinit var imageuri: Uri;
    lateinit var selfiee:ImageView
    lateinit var binding:FragmentFiggoCaptonBinding
     var  aadhar_verification_front:String=""
     var aadhar_verification_back :String=""
    lateinit var driverdp :String
    lateinit  var  backstr: String
    var args = Bundle()
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       binding=DataBindingUtil.inflate(inflater,R.layout.fragment_figgo__capton, container, false)
        return binding.root
    }
    private val contract1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        up_adharfront.setImageURI(it)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it);
        var base = BaseClass(requireContext())
           aadhar_verification_front = base.BitMapToString(bitmap).toString()
        // upload()
    }
    private val contract2 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        up_adharback.setImageURI(it)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it);
        var base = BaseClass(requireContext())
        aadhar_verification_back = base.BitMapToString(bitmap).toString()
        // upload()

    }
    private val contract3 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        selfiee.setImageURI(it)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it);
        var base = BaseClass(requireContext())
         driverdp = base.BitMapToString(bitmap).toString()
        // upload()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())

        up_adharfront = view.findViewById<ImageView>(R.id.up_adharfront)
        up_adharback = view.findViewById<ImageView>(R.id.up_adharback)
        selfiee = view.findViewById<ImageView>(R.id.selfiee)
        var driver_name=binding.drivername.text.toString()

        var driver_mobile_no=binding.drivermobileno.text.toString()
        prefManager.setMobile_No(driver_mobile_no)
        var driver_dl_no=binding.driverdlno.text.toString()
        prefManager.setDL_No(driver_dl_no)
        var driver_police_verification_no=binding.driverpolicev.text.toString()

        var driver_adhar_no=binding.driveradharno.text.toString()
     //   var aadhar_verification_front=binding.upAdharfront.resources.toString()
      //  var aadhar_verification_back=binding.upAdharback.resources.toString()

        args.putString("name",driver_name)
        args.putString("mobile_no",driver_mobile_no)
        args.putString("dl_number",driver_dl_no)
        args.putString("police_verification",driver_police_verification_no)
        args.putString("aadhar_no",driver_adhar_no)
        args.putString("aadhar_verification_front",aadhar_verification_front)
        args.putString("aadhar_verification_back", aadhar_verification_back)

        up_adharfront.setOnClickListener {
            contract1.launch("image/*")
        }
        up_adharback.setOnClickListener {
            contract2.launch("image/*")
        }
        selfiee.setOnClickListener {
            contract3.launch("image/*")
        }
        var next=view.findViewById<TextView>(R.id.next_button)
        next.setOnClickListener {
          //  sharedPref(view)

            Navigation.findNavController(view).navigate(R.id.action_figgo_Capton_to_driverCabDetailsFragment,args)
        }
        var back=view.findViewById<TextView>(R.id.back_button)
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_figgo_Capton_to_driverWelcomeFragment)
        }


    }


//    private fun sharedPref(view: View) {
//        val name = view.findViewById<EditText>(R.id.drivername)
//        val mobileno = view.findViewById<EditText>(R.id.mobileno)
//        val dlno = view.findViewById<EditText>(R.id.dlno)
//        val policeV = view.findViewById<EditText>(R.id.policev)
//        val adharno = view.findViewById<EditText>(R.id.adharno)
//        val pref = this.activity?.getPreferences(Context.MODE_PRIVATE)
//        val edit =pref?.edit()
//        edit?.putString("Name",name.text.toString())
//        edit?.putString("MobileNo",mobileno.text.toString())
//        edit?.putString("DL No",dlno.text.toString())
//        edit?.putString("Police Verification",policeV.text.toString())
//        edit?.putString("Adhar Number",adharno.text.toString())
//        edit?.apply()
//    }

    private fun upload() {
        val fileDir = this.activity?.applicationContext?.filesDir
        val file = File(fileDir,"adhar_front.png")
        val inputStream = this.activity?.contentResolver?.openInputStream(imageuri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

//        val requestbody = file.asRequestBody("image/*".toMediaTypeOrNull())
//        val part = MultipartBody.Part.createFormData("profile",file.name,requestbody)

//        val retrofit =
//            Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/posts")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(Upload::class.java)
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = retrofit.uploadImage(part)
//            Log.d("Upload Fragment",response.toString())
//        }
        }
    }


