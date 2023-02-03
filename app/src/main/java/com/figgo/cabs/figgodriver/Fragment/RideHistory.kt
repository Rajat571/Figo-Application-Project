package com.figgo.cabs.figgodriver.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.figgo.cabs.R
import com.figgo.cabs.figgodriver.Adapter.RideHistoryAdapter
import com.figgo.cabs.figgodriver.Adapter.RideHistoryRowAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [RideHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class RideHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var header: RecyclerView = view.findViewById<RecyclerView>(R.id.ridehistoryheader)
        var headerData = listOf<String>("Sr.No","Name","Date","From","To","Time","Duration","Amount","Distance","Feedback");
        var contentdata = ArrayList<List<String>>()
        contentdata.add(listOf("Sr.No","Name","Date","From","To","Time","Duration","Amount","Distance","Feedback"))
        for (i in 0..40)
            contentdata.add(listOf("1","Sagar Bisht","01-02-2023","Chandigarh","Patiala","8:40am","50min","100","23KM","5star"))
        header.adapter= RideHistoryRowAdapter(contentdata,requireContext())
        header.layoutManager=LinearLayoutManager(requireContext())
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RideHistory.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): RideHistory {
            val fragment = RideHistory()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}