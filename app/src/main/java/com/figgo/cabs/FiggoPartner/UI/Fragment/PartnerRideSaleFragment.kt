package com.figgo.cabs.FiggoPartner.UI.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.FiggoPartner.Adapter.PartnerAllRideAdapter
import com.figgo.cabs.FiggoPartner.Model.AllRideData
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Fragment.allRideRS


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PartnerRideSaleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PartnerRideSaleFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_partner_ride_sale, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val allrideData = ArrayList<AllRideData>();
        var main1 = view.findViewById<LinearLayout>(R.id.main_1)
        var topbutton = view.findViewById<LinearLayout>(R.id.top_buttons)
        var details = view.findViewById<LinearLayout>(R.id.details_view)
        var allride = view.findViewById<ConstraintLayout>(R.id.allrideview)
        var details_button = view.findViewById<Button>(R.id.details_button)
        var allride_button = view.findViewById<Button>(R.id.allride_button)
//        var details_view = view.findViewById<LinearLayout>(R.id.details_view)
//        var allride_view = view.findViewById<LinearLayout>(R.id.details_view)
        var submit = view.findViewById<Button>(R.id.sumit)
        var allRidefrag = allRideRS()
        var back = view.findViewById<TextView>(R.id.top_back)
        main1.visibility = View.VISIBLE
        topbutton.visibility = View.GONE
        details.visibility = View.GONE
        allride.visibility = View.GONE

        submit.setOnClickListener {
            main1.visibility = View.GONE
            topbutton.visibility = View.VISIBLE
            details.visibility = View.VISIBLE
            allride.visibility = View.GONE
        }

        details_button.setOnClickListener{
            details.visibility = View.VISIBLE
            allride.visibility = View.GONE
        }

        allride_button.setOnClickListener {
            details.visibility = View.GONE
            allride.visibility = View.VISIBLE
            //setfragment(allRidefrag)

            allrideData.add(AllRideData("6:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            allrideData.add(AllRideData("7:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            allrideData.add(AllRideData("8:30 pm","PGI, Sector 12, Chandigarh","Chandigarh (Google location)"))
            val recyclerView1 = view.findViewById<RecyclerView>(R.id.frameRSall2)
            //  val recyclerView2 = view.view.findViewById<RecyclerView>(R.id.drop_recycler)
            recyclerView1.adapter = PartnerAllRideAdapter(allrideData)
            recyclerView1.layoutManager = LinearLayoutManager(context)

        }

        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_ride_sale_to_home2)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PartnerRideSaleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PartnerRideSaleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}