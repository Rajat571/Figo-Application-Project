package com.pearl.figgodriver.Fragment


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentFiggoCaptonBinding
import com.pearl.pearllib.BaseClass
import com.pearl.pearllib.BasePrivate
import com.pearlorganisation.PrefManager
import kotlinx.android.synthetic.main.fragment_driver_cab_details.view.*
import kotlinx.android.synthetic.main.fragment_figgo__capton.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.regex.Pattern


class Figgo_Capton : Fragment(){

    lateinit var imageuri: Uri
    lateinit var binding:FragmentFiggoCaptonBinding
     var  aadhar_verification_front:String=""
     var aadhar_verification_back :String=""
     var driverdp :String=""
    var police_verification:String=""

    lateinit var baseclass: BaseClass
    lateinit var basePrivate:  BasePrivate
   // lateinit  var  backstr: String
    var args = Bundle()
    lateinit var prefManager: PrefManager

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
        binding.llPoliceVerification.setOnClickListener {
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

        }

        var back=view.findViewById<TextView>(R.id.back_button)
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_figgo_Capton_to_driverWelcomeFragment)
        }

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
            binding.upAdharfront.visibility=View.VISIBLE
           // binding.aadharfrontIV.visibility=View.GONE
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }  else if (requestCode==2){
            var selectedImageUri2=data?.data
        var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri2)


        aadhar_verification_back = baseclass.BitMapToString(bitmap).toString()
        prefManager.setAadhar_verification_back(aadhar_verification_back)
        binding.upAdharback.setImageBitmap(bitmap)
        binding.upAdharback.visibility=View.VISIBLE
        binding.aadharBackIV.visibility=View.GONE
        }

        else if(requestCode==3){
        var selectedImageUri3=data?.data
        var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri3)


        police_verification= baseclass.BitMapToString(bitmap).toString()
        prefManager.setPolice_verification(police_verification)
        binding.ivPoliceVerification.setImageBitmap(bitmap)
        binding.llPoliceVerification.setBackgroundResource(R.drawable.input_boder_profile)
        }

    else if(requestCode==4){
        var selectedImageUri4=data?.data
        var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri4)
        binding.selfiee.setImageBitmap(bitmap)
        driverdp = baseclass.BitMapToString(bitmap).toString()
    }
}

    private fun validateForm() {
           baseclass.validateName(binding.drivername)
           baseclass.validateNumber(binding.drivermobileno)
           baseclass.validateState(binding.driverstate)
           baseclass.validateCity(binding.drivercity)
           baseclass.validatedriverDLNo(binding.driverdlno)

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

        if (!binding.drivername.text.isEmpty()&&!binding.drivermobileno.text.isEmpty()&&!binding.driverstate.text.isEmpty()&&!binding.drivercity.text.isEmpty()&&!binding.driverdlno.text.isEmpty()&&binding.upAdharfront.drawable!=null&&binding.upAdharback.drawable!=null){

            var driver_name=binding.drivername.text.toString()
            var driver_mobile_no=binding.drivermobileno.text.toString()
            var driver_dl_no=binding.driverdlno.text.toString()

           // binding.aadharfrontIV.visibility=View.GONE
            binding.upAdharfront.visibility=View.VISIBLE
            binding.aadharBackIV.visibility=View.GONE
            binding.upAdharback.visibility=View.VISIBLE

            prefManager.setDL_No(driver_dl_no)
            prefManager.setDriverName(driver_name)
            prefManager.setMobile_No(driver_mobile_no)
            prefManager.setDriverProfile(driverdp)

            Navigation.findNavController(requireView()).navigate(R.id.action_figgo_Capton_to_driverCabDetailsFragment,args)

        }

    }


    }


