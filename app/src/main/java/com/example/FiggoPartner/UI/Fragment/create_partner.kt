package com.example.FiggoPartner.UI.Fragment

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
import com.example.figgodriver.Fragment.CreateFragAdapter
import com.example.figgodriver.R
import com.example.figgodriver.model.Location

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [create_partner.newInstance] factory method to
 * create an instance of this fragment.
 */
class create_partner : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_create_partner, container, false)
        var city_spinner = view?.findViewById<Spinner>(R.id.cityname_spinner2)
        var adapter = ArrayAdapter.createFromResource(requireContext(),R.array.spin_cityname,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        city_spinner?.adapter = adapter

        var city_list=ArrayList<com.example.figgodriver.model.Location>()
        city_list.add(
            com.example.figgodriver.model.Location(
                R.drawable.ic_baseline_location_on_24,
                "Chandigarh"
            )
        )
        city_list.add(
            com.example.figgodriver.model.Location(
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment create_partner.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            create_partner().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}