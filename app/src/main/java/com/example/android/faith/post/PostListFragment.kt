package com.example.android.faith.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.faith.R
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.databinding.FragmentPostListBinding

/**
 * A fragment representing a list of Items.
 */
class PostListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  DataBindingUtil.inflate<FragmentPostListBinding>(inflater,
            R.layout.fragment_post_list, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao

        val viewModelFactory = PostViewModelFactory(dataSource, application)

        val postViewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel::class.java)

        binding.postViewModel = postViewModel

        val adapter = PostAdapter()

        binding.list.adapter = adapter

        postViewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })


        binding.setLifecycleOwner(this)

        return binding.root

    }

}