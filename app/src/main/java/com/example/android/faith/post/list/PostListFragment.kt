package com.example.android.faith.post.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.faith.FaithApplication
import com.example.android.faith.R
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.user.UserType
import com.example.android.faith.databinding.FragmentPostListBinding
import com.example.android.faith.post.PostAdapter
import com.example.android.faith.post.PostListener

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

        val viewModelFactory = PostListViewModelFactory(dataSource, userDao, app.userProfile?.getId()!!, application)

        val postViewModel = ViewModelProviders.of(this, viewModelFactory).get(PostListViewModel::class.java)

        binding.postViewModel = postViewModel

        val adapter = PostAdapter(PostListener {
            postId -> postViewModel.onPostClicked(postId)
        })


        binding.list.adapter = adapter

        postViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            binding.favoritesSwitch.isVisible = it?.user?.userType == UserType.JONGERE
            postViewModel.showFavorites.observe(viewLifecycleOwner, Observer{
                var showFavorites = it
                postViewModel.posts.observe(viewLifecycleOwner, Observer {
                    it.let{
                        var filteredList = it.filter {
                            if (showFavorites) {
                                it?.post?.favorited!!
                            } else {
                                true
                            }
                        }
                        adapter.submitList(filteredList)
                    }
                })
            })

        })


        binding.lifecycleOwner = this

        binding.favoritesSwitch.setOnCheckedChangeListener{compoundButton, value ->
            postViewModel.toggleFavorites(value)

        }

        postViewModel.navigateToPostDetail.observe(viewLifecycleOwner, Observer{ post ->
            post?.let{
                this.findNavController().navigate(
                    PostListFragmentDirections.actionPostFragmentToPostDetailFragment(
                        post
                    )
                )
                postViewModel.onPostDetailNavigated()
            }
        })
        return binding.root

    }

}