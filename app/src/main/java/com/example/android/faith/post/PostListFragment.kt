package com.example.android.faith.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.faith.FaithApplication
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
        val userDao = FaithDatabase.getInstance(application).userDao

        val app = application as FaithApplication

        val viewModelFactory = PostViewModelFactory(dataSource, userDao, app.userProfile?.getId()!!, application)

        val postViewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel::class.java)

        binding.postViewModel = postViewModel

        val adapter = PostAdapter(PostListener {
            postId -> postViewModel.onPostClicked(postId)
        })


        binding.list.adapter = adapter

        postViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            postViewModel.posts.observe(viewLifecycleOwner, Observer {
                it.let{
                    adapter.submitList(it)
                }
            })
        })


//        postViewModel.posts.observe(viewLifecycleOwner, Observer {
//            it?.let{
//                adapter.submitList(it)
//            }
//        })


        binding.setLifecycleOwner(this)

        postViewModel.navigateToPostDetail.observe(viewLifecycleOwner, Observer{ post ->
            post?.let{
                this.findNavController().navigate(PostListFragmentDirections.actionPostFragmentToPostDetailFragment(post))
                postViewModel.onPostDetailNavigated()
            }
        })
        return binding.root

    }

}