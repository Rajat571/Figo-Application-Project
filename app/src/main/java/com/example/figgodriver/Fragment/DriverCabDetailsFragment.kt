package com.example.figgodriver.Fragment

import android.app.DatePickerDialog
import android.content.Intent
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
import com.example.figgodriver.DriverDashBoard
import com.example.figgodriver.R
import java.util.*


class DriverCabDetailsFragment : Fragment() {
    private lateinit var carDP: ImageView
    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        //imageuri = it!!
        carDP.setImageURI(it)
        // upload()
    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_cab_details, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var next=view.findViewById<TextView>(R.id.next_button)
        var back=view.findViewById<TextView>(R.id.back_button)
        var dateEdt = view.findViewById<EditText>(R.id.registration)
        var insurance = view.findViewById<EditText>(R.id.insurance)
        var tax_permit = view.findViewById<EditText>(R.id.tax_permit)
        carDP = view.findViewById(R.id.upload_car)
        carDP.setOnClickListener {
            contract.launch("image/*")
        }
        next.setOnClickListener {
            context?.startActivity(Intent( requireContext(),DriverDashBoard::class.java))
//            Navigation.findNavController(view).navigate(R.id.action_cabDetailsFragment_to_blankFragment)
        }

        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_driverCabDetailsFragment_to_figgo_Capton)
        }
        dateEdt.setOnClickListener {
            calendar(dateEdt)
        }
        insurance.setOnClickListener {
            calendar(insurance)
        }
        tax_permit.setOnClickListener {
            calendar(tax_permit)
        }
    }

    private fun calendar(edit:EditText) {
        val c = Calendar.getInstance()
        // on below line we are getting
        // our day, month and year.
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // on below line we are creating a
        // variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            this.requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                edit.setText(dat)
            },
            // on below line we are passing year, month
            // and day for the selected date in our date picker.
            year,
            month,
            day
        )
        // at last we are calling show
        // to display our date picker dialog.
        datePickerDialog.show()
    }
}