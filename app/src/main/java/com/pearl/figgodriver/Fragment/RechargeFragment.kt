package com.pearl.figgodriver.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pearl.figgodriver.Adapter.RechargeLayoutAdapter
import com.pearl.figgodriver.R
import com.pearl.figgodriver.model.Recharge

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RechargeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RechargeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_recharge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var data = ArrayList<Recharge>();
        data.add(Recharge("5000",5,49))
        data.add(Recharge("12000",10,99))
        data.add(Recharge("25000",20,199))
        data.add(Recharge("75000",40,499))
        data.add(Recharge("200000",80,999))
        data.add(Recharge("200000",80,999))
        data.add(Recharge("200000",80,999))
        data.add(Recharge("Unlimited",150,1999))
        var rechargeRecycler = view.findViewById<RecyclerView>(R.id.rechargeRecycler2)
        rechargeRecycler.adapter = RechargeLayoutAdapter(data)
        rechargeRecycler.layoutManager = LinearLayoutManager(context)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RechargeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RechargeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}