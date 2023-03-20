package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Adapter.PartnerActiveRideAdapter
import com.figgo.cabs.FiggoPartner.Adapter.PartnerPaymentHistoryAdapter
import com.figgo.cabs.FiggoPartner.Model.PartnerActiveRide
import com.figgo.cabs.R
import com.figgo.cabs.databinding.FragmentPartnerActiveRideBinding
import com.figgo.cabs.figgodriver.Adapter.PayHistoryAdapter
import com.figgo.cabs.figgodriver.model.PaymentHistoryModel


class PartnerActiveRideFragment : Fragment() {
    lateinit var binding: FragmentPartnerActiveRideBinding
    lateinit var activeRideAdapter: PartnerActiveRideAdapter
    var datalist=ArrayList<PartnerActiveRide>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      binding=DataBindingUtil.inflate(inflater,
          R.layout.fragment_partner_active_ride, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentHistoryAdapter(view)

    }

    private fun paymentHistoryAdapter(view: View) {
        var history = view.findViewById<RecyclerView>(R.id.partRechargeHistoryRecycler)
        var data = ArrayList<PaymentHistoryModel>()/*
        data.add(PaymentHistoryModel("2.10.2022","300","Mr. XYZ XYZ","15minute 6:25pm","10 KM",1))
        data.add(PaymentHistoryModel("2.10.2022","1000","Mr. XYZ XYZ","15minute 6:25pm","10 KM",0))
        data.add(PaymentHistoryModel("2.10.2022","1500","Mr. XYZ XYZ","15minute 6:25pm","10 KM",0))
        data.add(PaymentHistoryModel("2.10.2022","23","Mr. XYZ XYZ","15minute 6:25pm","10 KM",1))
        data.add(PaymentHistoryModel("2.10.2022","2300","Mr. XYZ XYZ","15minute 6:25pm","10 KM",1))*/
        history.adapter= PartnerPaymentHistoryAdapter(data)
        history.layoutManager=LinearLayoutManager(requireContext())
    }
}