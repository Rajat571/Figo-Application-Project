package com.pearlorganisation.figgo.UI.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.figgodriver.R
import com.example.figgodriver.databinding.FragmentMPinGenerateBinding
import kotlinx.android.synthetic.main.fragment_m_pin_generate.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MPinGenerate.newInstance] factory method to
 * create an instance of this fragment.
 */
class MPinGenerate : Fragment() {
    lateinit var binding: FragmentMPinGenerateBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_m_pin_generate, container, false)
        var view=binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mpin = view.findViewById<EditText>(R.id.mPin)
        val confirm_mpin = view.findViewById<EditText>(R.id.confirm_mpin)
        binding.continuetv.setOnClickListener{
            val pref = this.activity?.getPreferences(Context.MODE_PRIVATE)
            val edit =pref?.edit()
            if (pref?.getString("mpin","")==null){
              binding.mPin.setError("please fill the box")
//                edit?.putString("mpin",binding.mPin.text.toString())
//                edit?.putString("Confirm mpin",binding.confirmMpin.text.toString())
//                edit?.apply()

            }
         else{
            if (pref?.getString("mpin","123").equals(binding.mPin.text.toString())){
                Navigation.findNavController(view).navigate(R.id.action_MPinGenerate_to_figgo_FamilyFragment)
            }}

          /*  if(mpin.text.toString()== "123" && confirm_mpin.text.toString()=="123"){
                Navigation.findNavController(view).navigate(R.id.action_MPinGenerate_to_figgo_FamilyFragment)
            }else{
                Toast.makeText(context,"Wrong MPIN",Toast.LENGTH_LONG).show()
            }*/





        }
        binding.exit.setOnClickListener {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }







    }

}