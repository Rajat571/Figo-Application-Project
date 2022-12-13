package com.example.figgodriver.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation
import com.example.figgodriver.R
import java.io.File
import java.io.FileOutputStream

class Figgo_Capton : Fragment() {
    lateinit var up_adharfront: ImageView;
    lateinit var up_adharback:ImageView;
    lateinit var imageuri: Uri;
    lateinit var selfiee:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_figgo__capton, container, false)
    }
    private val contract1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        up_adharfront.setImageURI(it)
        // upload()

    }
    private val contract2 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        up_adharback.setImageURI(it)
        // upload()

    }
    private val contract3 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        selfiee.setImageURI(it)
        // upload()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        up_adharfront = view.findViewById<ImageView>(R.id.up_adharfront)
        up_adharback = view.findViewById<ImageView>(R.id.up_adharback)
        selfiee = view.findViewById<ImageView>(R.id.selfiee)

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
            sharedPref(view)
            Navigation.findNavController(view).navigate(R.id.action_figgo_Capton_to_driverCabDetailsFragment)
        }
        var back=view.findViewById<TextView>(R.id.back_button)
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_figgo_Capton_to_driverWelcomeFragment)
        }
    }
    private fun sharedPref(view: View) {
        val name = view.findViewById<EditText>(R.id.drivername)
        val mobileno = view.findViewById<EditText>(R.id.mobileno)
        val dlno = view.findViewById<EditText>(R.id.dlno)
        val policeV = view.findViewById<EditText>(R.id.policev)
        val adharno = view.findViewById<EditText>(R.id.adharno)
        val pref = this.activity?.getPreferences(Context.MODE_PRIVATE)
        val edit =pref?.edit()
        edit?.putString("Name",name.text.toString())
        edit?.putString("MobileNo",mobileno.text.toString())
        edit?.putString("DL No",dlno.text.toString())
        edit?.putString("Police Verification",policeV.text.toString())
        edit?.putString("Adhar Number",adharno.text.toString())
        edit?.apply()

    }

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


