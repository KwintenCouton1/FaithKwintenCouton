package com.example.android.faith.joke

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.android.faith.R
import com.example.android.faith.databinding.FragmentJokeBinding


class JokeFragment : Fragment() {
    private lateinit var binding : FragmentJokeBinding

    private val viewModel : JokeViewModel by lazy{
        ViewModelProvider(this).get(JokeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentJokeBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.buttonNext.setOnClickListener { view: View ->
            viewModel.loadJoke()
        }

        //binding = DataBindingUtil.inflate<FragmentJokeBinding>(inflater, R.layout.fragment_joke, container, false)
        return binding.root

    }

}