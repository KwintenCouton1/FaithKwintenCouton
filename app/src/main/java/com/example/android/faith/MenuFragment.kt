package com.example.android.faith

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.android.faith.databinding.FragmentMenuBinding
import timber.log.Timber


/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  DataBindingUtil.inflate<FragmentMenuBinding>(inflater, R.layout.fragment_menu, container, false)
        binding.buttonMyPost.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_menuFragment_to_postFragment)
        }
        binding.buttonCreatePost.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_menuFragment_to_createPostFragment)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

}