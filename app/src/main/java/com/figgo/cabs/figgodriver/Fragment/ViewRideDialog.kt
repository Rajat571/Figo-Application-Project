package com.figgo.cabs.figgodriver.Fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.figgo.cabs.R

import com.figgo.cabs.figgodriver.UI.PriceDetailsActivity


class ViewRideDialog:DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.view_ride_layout,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var cancel=view.findViewById<Button>(R.id.cancel)
        var done=view.findViewById<Button>(R.id.done)
        done.setOnClickListener {
            startActivity(Intent(context, PriceDetailsActivity::class.java))
        }
        cancel.setOnClickListener {
           var d=Dialog(requireContext())
            d.setContentView(R.layout.cancel_ride_dialog)
            var no=d.findViewById<Button>(R.id.no)
            var yes=d.findViewById<Button>(R.id.yes)
            no.setOnClickListener {
                d.dismiss()
            }
            yes.setOnClickListener {
                d.dismiss()
            }
            d.show()
            d.setCancelable(true)
            dialog?.dismiss()
        }
        var changeDriver=view.findViewById<TextView>(R.id.changeDriver)
        changeDriver.setOnClickListener {
         var changedriver= ChangeDriverFragmentDialog()
            changedriver.show(requireActivity().supportFragmentManager,"change driver")
        }
    }
}