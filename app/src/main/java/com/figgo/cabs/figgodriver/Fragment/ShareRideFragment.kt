package com.figgo.cabs.figgodriver.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.PrefManager
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.AddShareAdapter
import com.figgo.cabs.figgodriver.UI.LocationPickerActivity
import com.figgo.cabs.figgodriver.model.LIstModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.active_ride_layout.view.*
import kotlinx.android.synthetic.main.fragment_share_ride.*
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ShareRideFragment : Fragment() {
    var to_lat: Double? = 0.0
    var from_lat: Double? = 0.0
    var to_lng: Double? = 0.0
    var from_lng: Double? = 0.0
    var date:String=""

    lateinit var recyclerView: RecyclerView
    lateinit var start_edittext: TextView
    lateinit var destiantionMarker: LinearLayout
    lateinit var shareride_timepicker: LinearLayout
    lateinit var shareride_calendar: LinearLayout
    lateinit var timerOk: Button
    lateinit var sharetimePicker: TimePicker
    lateinit var destinationSetText: TextView
    lateinit var share_date: TextView
    lateinit var share_time: TextView
    lateinit var locationLL: LinearLayout
    lateinit var calender:Calendar
    private var param1: String? = null
    private var param2: String? = null
    private val ADDRESS_PICKER_REQUEST = 1
    private lateinit var db: FirebaseFirestore
    lateinit var createshareride:Button
    var selects: String? = ""

    val pref: PrefManager
        get() = PrefManager(requireContext())

    lateinit var dc_im: LinearLayout


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
        return inflater.inflate(R.layout.fragment_share_ride, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val arrCard = ArrayList<LIstModel>()
        super.onViewCreated(view, savedInstanceState)
        dc_im = view.findViewById(R.id.dc_im)
        var add_checkpoint = view.findViewById<LinearLayout>(R.id.add_checkpoint)
        var checkpoint_edittext = view.findViewById<TextView>(R.id.checkpoint_edittext)
        destinationSetText = view.findViewById<TextView>(R.id.destinationSetText)
        start_edittext = view.findViewById<TextView>(R.id.start_edittext)
        destiantionMarker = view.findViewById(R.id.destiantionMarker)
        shareride_timepicker = view.findViewById(R.id.shareride_timepicker)
        sharetimePicker = view.findViewById(R.id.sharetimePicker)
        shareride_calendar = view.findViewById(R.id.shareride_calendar)
        share_date = view.findViewById(R.id.share_date)
        share_time = view.findViewById(R.id.share_time)
        createshareride = view.findViewById(R.id.createshareride)
        timerOk = view.findViewById(R.id.timerOk)

        locationLL = view.findViewById(R.id.idLocationLL)
        db = Firebase.firestore

                locationLL.setOnClickListener {
            val internet: Boolean = isOnline(requireActivity())
            if (internet == true) {
                selects = ""
                pref.setType("3")
                val i = Intent(requireActivity(), LocationPickerActivity::class.java)
                startActivityForResult(i, ADDRESS_PICKER_REQUEST)
            } else {
                Toast.makeText(requireActivity(), "Please turn on internet", Toast.LENGTH_LONG)
                    .show()
            }


                if (checkpoint_edittext.text.toString().isNotEmpty()) {
                    arrCard.add(LIstModel(checkpoint_edittext.text.toString(),pref.getCheckLat(),pref.getCheckLng()))
                    recyclerView.adapter?.notifyDataSetChanged()

                    closeKeyboard(checkpoint_edittext)
                    checkpoint_edittext.setText("")
            }
        }
        add_checkpoint.setOnClickListener {
            try {
                if (checkpoint_edittext.text.toString().isNotEmpty()) {
                    arrCard.add(LIstModel(checkpoint_edittext.text.toString(),pref.getCheckLat(),pref.getCheckLng()))
                    recyclerView.adapter?.notifyDataSetChanged()

                    closeKeyboard(checkpoint_edittext)
                    checkpoint_edittext.setText("")


                }
            } catch (_: Exception) {

            }
        }
        destiantionMarker.setOnClickListener {
            val internet: Boolean = isOnline(requireActivity())
            if (internet == true) {
                selects = "dest"
                pref.setType("2")
                val i = Intent(requireActivity(), LocationPickerActivity::class.java)
                startActivityForResult(i, ADDRESS_PICKER_REQUEST)
            } else {
                Toast.makeText(requireActivity(), "Please turn on internet", Toast.LENGTH_LONG)
                    .show()

            }
        }
        dc_im.setOnClickListener {
            val internet: Boolean = isOnline(requireActivity())
            if (internet == true) {
                selects = "start"
                pref.setType("1")
                val i = Intent(requireActivity(), LocationPickerActivity::class.java)
                startActivityForResult(i, ADDRESS_PICKER_REQUEST)
            } else {
                Toast.makeText(requireActivity(), "Please turn on internet", Toast.LENGTH_LONG)
                    .show()
            }
        }

        shareride_calendar.setOnClickListener {
            calender= Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this.requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    date = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth.toString())
                    //n=date
                    Log.d("DATETIME","DATE"+date)
                    val dat1 = (dayOfMonth.toString()+"-" + (monthOfYear + 1) + "-" +year.toString())
                    share_date.setText(dat1)
                    //binding.insuranceNo.setBackgroundResource(R.drawable.input_boder_profile)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        shareride_timepicker.setOnClickListener {
            sharetimePicker.visibility=View.VISIBLE
            timerOk.visibility = View.VISIBLE
         sharetimePicker.setOnTimeChangedListener { timePicker, hour, minute ->
             var hour = hour
             var am_pm = ""
             // AM_PM decider logic
             when {hour == 0 -> {
                 hour += 12
                 am_pm = "AM"
             }
                 hour == 12 -> am_pm = "PM"
                 hour > 12 -> { hour -= 12
                     am_pm = "PM"
                 }
                 else -> am_pm = "AM"
             }
             if (share_time != null) {
                 val hour = if (hour < 10) "0" + hour else hour
                 val min = if (minute < 10) "0" + minute else minute
                 // display format of time
                 val msg = "$hour : $min $am_pm"
                 share_time.text = msg
             }
         }
        }
        timerOk.setOnClickListener {
            sharetimePicker.visibility=View.GONE
            timerOk.visibility = View.GONE
        }



        recyclerView = view.findViewById(R.id.add_share_list)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AddShareAdapter(requireContext(), arrCard)

        createshareride.setOnClickListener {
            //sendToFirebase()
            writeToFirebase()
        }

    }

    private fun sendToFirebase() {
        val driver = hashMapOf(
            "token" to "qwwwwwwrtyuiopsdfas[",
            "Module" to "ShareRide",
            "Location 1" to "Doiwala2",
            "Location 2" to "Jogiwala2",
            "Location 3" to "Rishikesh2",
            "Seats" to "4",
            "timing" to "5:30pm"
        )

        db.collection("drivers")
            .add(driver)
            .addOnSuccessListener {
                Log.d("Main","Data added succesfully $it")
            }
            .addOnFailureListener {
                Log.d("Main","Data failed to add $it")
            }
    }


    override fun onResume() {
        super.onResume()


        if (pref.getType().equals("1")) {

            if (pref.getToLatL().equals("")) {

            } else {
                getCurrentLoc()

            }
            if (pref.getToLatM().equals("")) {


            } else {
                if (pref.getToLatM().equals("")) {

                } else {
                    getDestinationLoc()
                }
            }


        } else if (pref.getType().equals("2")) {
            if (pref.getToLatL().equals("")) {

            } else {
                if (pref.getToLatL().equals("")) {

                } else {
                    getCurrentLoc()

                }
            }
            if (pref.getToLatM().equals("")) {

            } else {
                getDestinationLoc()
            }
            if (pref.getCheckLat().equals("")) {

            } else {
                getCheckPoint()
            }

        }
        else if (pref.getType().equals("3")){

            if (pref.getToLatL().equals("")) {

            } else {
                if (pref.getToLatL().equals("")) {

                } else {
                    getCurrentLoc()

                }
                if (pref.getCheckLat().equals("")) {

                } else {
                    getCheckPoint()
                }
            }
            if (pref.getToLatM().equals("")) {

            } else {
                if (pref.getToLatL().equals("")) {

                } else {
                    getDestinationLoc()

                }
            }
            if (pref.getCheckLat().equals("")) {

            } else {
                getCheckPoint()
            }

        }
    }

    private fun getCheckPoint() {

            to_lat = pref.getCheckLat().toDouble()
            to_lng = pref.getCheckLng().toDouble()

            var geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(requireActivity(), Locale.getDefault())


            var strAdd: String? = null
            try {
                val addresses = geocoder.getFromLocation(to_lat!!, to_lng!!, 1)
                if (addresses != null) {
                    val returnedAddress = addresses[0]
                    val strReturnedAddress = java.lang.StringBuilder("")
                    for (i in 0..returnedAddress.maxAddressLineIndex) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    }
                    strAdd = strReturnedAddress.toString()
                    Log.w(" Current loction address", strReturnedAddress.toString())
                } else {
                    Log.w(" Current loction address", "No Address returned!")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.w(" Current loction address", e.printStackTrace().toString())
            }
            checkpoint_edittext?.setText(strAdd)


    }

    private fun getCurrentLoc() {
        to_lat = pref.getToLatL().toDouble()
        to_lng = pref.getToLngL().toDouble()

        var geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(requireActivity(), Locale.getDefault())


        var strAdd: String? = null
        try {
            val addresses = geocoder.getFromLocation(to_lat!!, to_lng!!, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = java.lang.StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w(" Current loction address", strReturnedAddress.toString())
            } else {
                Log.w(" Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w(" Current loction address", e.printStackTrace().toString())
        }
        start_edittext?.setText(strAdd)
    }


    private fun getDestinationLoc() {

        from_lat = pref.getToLatM().toDouble()
        from_lng = pref.getToLngM().toDouble()

        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(requireActivity(), Locale.getDefault())

        var strAdd: String? = null
        try {
            val addresses = geocoder.getFromLocation(from_lat!!, from_lng!!, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = java.lang.StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Log.w(" Current loction address", strReturnedAddress.toString())
            } else {
                Log.w(" Current loction address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.w(" Current loction address", "Canont get Address!")
        }
        destinationSetText?.setText(strAdd)

    }

    @SuppressLint("MissingPermission")
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {

                    return true
                }
            }
        }
        return false
    }

    private fun closeKeyboard(view: View) {

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)


    }

    private fun writeToFirebase() {
        val driver = hashMapOf(
            "token" to "qwwwwwwrtyuiopsdfas[",
            "Module" to "ShareRide",
            "Location 1" to "Doiwala2",
            "Location 2" to "Jogiwala2",
            "Location 3" to "Rishikesh2",
            "Seats" to "4",
            "timing" to "5:30pm"
        )

        db.collection("drivers")
            .add(driver)
            .addOnSuccessListener {
                Log.d("Main","Data added succesfully $it")
            }
            .addOnFailureListener {
                Log.d("Main","Data failed to add $it")
            }
    }


}