package com.pearl.figgodriver.Fragment

import android.R.attr
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.pearl.figgodriver.BaseClass
import com.pearl.figgodriver.R
import com.pearl.figgodriver.databinding.FragmentFiggoCaptonBinding
import com.pearlorganisation.PrefManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class Figgo_Capton : Fragment() {

    lateinit var imageuri: Uri;
    lateinit var binding:FragmentFiggoCaptonBinding
     var  aadhar_verification_front:String=""
     var aadhar_verification_back :String=""
     var driverdp :String=""
    var police_verification:String=""

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

    private val contract1 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
       // up_adharfront.setImageURI(it)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it);
        binding.upAdharfront.setImageBitmap(bitmap)
        var base = BaseClass(requireContext())
           aadhar_verification_front = base.BitMapToString(bitmap).toString()
        // upload()
    }
    private val contract2 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
        //up_adharback.setImageURI(it)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it);
        binding.upAdharback.setImageBitmap(bitmap)
        var base = BaseClass(requireContext())
        aadhar_verification_back = base.BitMapToString(bitmap).toString()
        System.out.println("aadhar_verification_backo=="+aadhar_verification_back)
        // upload()
    }
    private val contract3 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!
      //  selfiee.setImageURI(it)
        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it);
        binding.selfiee.setImageBitmap(bitmap)
        var base = BaseClass(requireContext())
         driverdp = base.BitMapToString(bitmap).toString()

    }
    private val contract4 = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageuri = it!!

        val bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), it);
        binding.ivPoliceVerification.setImageBitmap(bitmap)
        var base = BaseClass(requireContext())
       police_verification= base.BitMapToString(bitmap).toString()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())


        binding.aadharFront.setOnClickListener {
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
            var driver_name=binding.drivername.text.toString()
            System.out.println("driver_name=="+driver_name)


            var driver_mobile_no=binding.drivermobileno.text.toString()

            var driver_dl_no=binding.driverdlno.text.toString()
            System.out.println("Driver DL no=="+driver_dl_no)


            prefManager.setDL_No(driver_dl_no)
            prefManager.setDriverName(driver_name)
            prefManager.setMobile_No(driver_mobile_no)
            prefManager.setDriverProfile(driverdp)
            Navigation.findNavController(view).navigate(R.id.action_figgo_Capton_to_driverCabDetailsFragment,args)
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

            var base = BaseClass(requireContext())
            aadhar_verification_front = base.BitMapToString(bitmap).toString()
            prefManager.setAadhar_verification_front(aadhar_verification_front)
            binding.upAdharfront.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }  else if (requestCode==2){
            var selectedImageUri2=data?.data
        var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri2)

        var base = BaseClass(requireContext())
        aadhar_verification_back = base.BitMapToString(bitmap).toString()
        prefManager.setAadhar_verification_back(aadhar_verification_back)
        binding.upAdharback.setImageBitmap(bitmap)
        }

        else if(requestCode==3){
        var selectedImageUri3=data?.data
        var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri3)

        var base = BaseClass(requireContext())
        police_verification= base.BitMapToString(bitmap).toString()
        prefManager.setPolice_verification(police_verification)
        binding.ivPoliceVerification.setImageBitmap(bitmap)
        }

    else if(requestCode==4){
        var selectedImageUri4=data?.data
        var bitmap=MediaStore.Images.Media.getBitmap(requireContext().contentResolver,selectedImageUri4)
        binding.selfiee.setImageBitmap(bitmap)
        var base = BaseClass(requireContext())
        driverdp = base.BitMapToString(bitmap).toString()
    }
}


    }


