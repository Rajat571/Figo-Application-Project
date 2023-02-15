package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentPartnerDetailsBinding
import com.figgo.cabs.pearllib.BaseClass
import com.figgo.cabs.pearllib.BasePrivate
import com.figgo.cabs.pearllib.Helper
import kotlinx.android.synthetic.main.fragment_partner_details.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.HashMap
import kotlin.jvm.Throws


class PartnerDetails : Fragment() {

    lateinit var up_adharfront: ImageView;
    lateinit var up_adharback: ImageView;
    lateinit var imageuri: Uri;
    lateinit var binding: FragmentPartnerDetailsBinding
    lateinit var prefManager:PrefManager
    var aadhar_front_ext:String=""
    var aadhar_back_ext:String=""
    var aadhar_front:String=""
    var aadhar_back:String=""
    lateinit var progressBar:ProgressBar
    lateinit var defLayout:ConstraintLayout

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,
            R.layout.fragment_partner_details, container, false)
        return binding.root
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefManager = PrefManager(requireContext())
        var next=view.findViewById<TextView>(R.id.next_button)
        var back=view.findViewById<TextView>(R.id.back_button)
        var name = view.findViewById<TextView>(R.id.partner_name)
        var panno = view.findViewById<TextView>(R.id.partner_panNo)
        var adharno = view.findViewById<TextView>(R.id.partner_adharNo)

        up_adharback = view.findViewById(R.id.adhar_back)
        up_adharfront = view.findViewById(R.id.adhar_front)
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        up_adharback.setOnClickListener {
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
                        var intent= Intent()
                        intent.type="image/*"
                        intent.action= Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),2)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }
        up_adharfront.setOnClickListener {
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
                        var intent= Intent()
                        intent.type="image/*"
                        intent.action= Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent,"select Picture"),4)
                    } else if (optionsMenu[i] == "Exit") {
                        dialogInterface.dismiss()
                    }
                })
            builder.show()
        }



progressBar = view.findViewById(R.id.partnerprogressbar)
        defLayout = view.findViewById(R.id.choose_user)
        progressBar.visibility=View.GONE
        defLayout.visibility=View.VISIBLE
        next.setOnClickListener {
            progressBar.visibility=View.VISIBLE
            defLayout.visibility=View.GONE
            var url = Helper.register_partner
            var json = JSONObject()
            var queue = Volley.newRequestQueue(requireContext())
            json.put("name",name.text)
            json.put("pan_number",panno.text)
            json.put("aadhar_number",adharno.text)
            json.put("aadhar_front",aadhar_front)
            json.put("aadhar_back",aadhar_back)
            json.put("aadhar_front_ext",aadhar_front_ext)
            json.put("aadhar_back_ext",aadhar_back_ext)

            var jsonObject: JsonObjectRequest = object : JsonObjectRequest(
                Method.POST,url,json,
                {
                    if(it!=null){
                        Log.d("Partner Register Response ",it.toString())
                        progressBar.visibility=View.GONE
                        defLayout.visibility=View.VISIBLE
                        Navigation.findNavController(view).navigate(R.id.action_partnerDetails_to_driverDetailsFragment)

                    }
                },
                {
                })  {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers: MutableMap<String, String> = HashMap()
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    headers.put("Authorization", "Bearer " + prefManager.getToken());
                    return headers
                }
            }
            queue.add(jsonObject)

        }
        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_partnerDetails_to_partnerWelcomeFragment)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == 1) {
                try {
                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    Log.d("onActivityResult", "response" + photo)
                    Log.d("onActivityResult", "" + selectedImageUri4.toString())
                    aadhar_back_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_back = base.BitMapToString(bitmap).toString()
        /*            binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )*/
                    prefManager.setAadhar_verification_back(aadhar_back)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 2) {
                try {
                    var selectedImageUri4 = data?.data
                    Log.d("URI = ", selectedImageUri4.toString())
                    aadhar_back_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_back = base.BitMapToString(bitmap).toString()
              /*      binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                        0,
                        0,
                        R.drawable.check_circle,
                        0
                    )*/
                    prefManager.setAadhar_verification_back(aadhar_back)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else if (requestCode == 3) {
                try {
                    val photo = data!!.extras!!["data"] as Bitmap?
                    var selectedImageUri4 = getImageUri(requireContext(), photo!!)
                    Log.d("onActivityResult", "response" + photo)
                    Log.d("onActivityResult", "" + selectedImageUri4.toString())
                    aadhar_front_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_front = base.BitMapToString(bitmap).toString()
                    /*            binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                                    0,
                                    0,
                                    R.drawable.check_circle,
                                    0
                                )*/
                    prefManager.setAadhar_verification_front(aadhar_front)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (requestCode == 4) {
                try {
                    var selectedImageUri4 = data?.data
                    Log.d("URI = ", selectedImageUri4.toString())
                    aadhar_front_ext = base.getExtension(selectedImageUri4!!, requireContext())
                    var bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        selectedImageUri4
                    )
                    aadhar_front = base.BitMapToString(bitmap).toString()
                    /*      binding.selfiee.setCompoundDrawablesWithIntrinsicBounds(
                              0,
                              0,
                              R.drawable.check_circle,
                              0
                          )*/
                    prefManager.setAadhar_verification_back(aadhar_front)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }


    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =   MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

}