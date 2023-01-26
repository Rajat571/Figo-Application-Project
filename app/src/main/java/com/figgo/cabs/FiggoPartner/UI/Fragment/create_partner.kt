package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
//import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Model.Location
import com.figgo.cabs.R


class create_partner : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_create_partner, container, false)
        var city_spinner = view?.findViewById<Spinner>(R.id.cityname_spinner2)
        var adapter = ArrayAdapter.createFromResource(requireContext(),R.array.spin_cityname,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        city_spinner?.adapter = adapter

        var city_list=ArrayList<Location>()
        city_list.add(
       Location(
                R.drawable.ic_baseline_location_on_24,
                "Chandigarh"
            )
        )
        city_list.add(
     Location(
                R.drawable.ic_baseline_my_location_24,
                "Rishikesh"
            )
        )
        city_list.add(Location(R.drawable.ic_baseline_my_location_24,"Vikasnagar"))
        city_list.add(Location(R.drawable.ic_baseline_my_location_24,"Haridwar"))
        city_list.add(Location(R.drawable.ic_baseline_my_location_24,"Uttarakhand"))
        city_list.add(Location(R.drawable.ic_baseline_location_on_24,"Jalandar"))
        val city = listOf<String>("Dehradun","Rishikesh","Vikasnagar","Haridwar","Uttarkashi")
        var recyclerView = view?.findViewById<RecyclerView>(R.id.partner_create_recyvlerview)
        recyclerView?.adapter = PartnerCreateFragAdapter(city_list)
        recyclerView?.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)


        return view

    }

}