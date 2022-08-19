package com.example.android.faith.post

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.faith.FaithApplication
import com.example.android.faith.R
import com.example.android.faith.database.Comment
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.databinding.FragmentPostDetailBinding
import com.example.android.faith.post.comment.AddCommentListener
import com.example.android.faith.post.comment.CommentAdapter
import com.example.android.faith.post.comment.CommentViewModel
import com.example.android.faith.post.comment.CommentViewModelFactory
import com.example.android.faith.post.link.LinkAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailFragment : Fragment() {
    lateinit var binding : FragmentPostDetailBinding
    lateinit var postDetailViewModel : PostDetailViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = PostDetailFragmentArgs.fromBundle(requireArguments())

        val faithApp = application as FaithApplication
        val userId = faithApp.userProfile?.getId()!!


        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val userDao = FaithDatabase.getInstance(application).userDao

        val postDetailviewModelFactory = PostDetailViewModelFactory(arguments.postKey, userId, dataSource, userDao)

        postDetailViewModel =
            ViewModelProvider(this, postDetailviewModelFactory).get(PostDetailViewModel::class.java)

        binding.postDetailViewModel = postDetailViewModel

        binding.lifecycleOwner = this

        val linkAdapter = LinkAdapter()

        binding.postLinks.adapter = linkAdapter

        val commentViewModelFactory = CommentViewModelFactory(dataSource, application)
        val commentViewModel = ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel::class.java)



        val commentAdapter = CommentAdapter(clickListenerAdd = AddCommentListener {
            newComment ->
            commentViewModel.onSubmitComment(newComment)
        }, commentViewModel, userId)

        binding.comments.adapter = commentAdapter

        commentViewModel.getCommentsOfPost(arguments.postKey).observe(viewLifecycleOwner, Observer {
            it?.let{
                commentAdapter.submitList(it)
            }
        })

        postDetailViewModel.getPost().observe(viewLifecycleOwner, Observer{
            linkAdapter.submitList(it.links)
        })

        postDetailViewModel.navigateToPostList.observe(viewLifecycleOwner, Observer {
            if (it == true){
                this.findNavController().navigate(
                    PostDetailFragmentDirections.actionPostDetailFragmentToPostFragment()
                )
                postDetailViewModel.doneNavigating()
            }
        })


        binding.react.setOnClickListener { view: View ->
            val app : FaithApplication = requireActivity().applicationContext as FaithApplication
            val userId = app.userProfile?.getId()

                commentViewModel.onSubmitComment(

                Comment(postId = postDetailViewModel.getPost().value?.post?.postId!!, text = binding.topLevelComment.text.toString(), userId = userId!!)
            )
        }
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.post_detail_menu, menu)
        //var editPostMenuItem = menu.findItem(R.id.editPost)

        val app : FaithApplication = requireActivity().applicationContext as FaithApplication

        var toggleFavoriteMenuItem = menu.findItem(R.id.toggleFavorite)
        postDetailViewModel.getPost().observe(viewLifecycleOwner, Observer{
            toggleFavoriteMenuItem.setChecked(it?.post?.favorited!!)
        })


        //editPostMenuItem.setVisible(postDetailViewModel.getPost().value?.post?.userId == app.userProfile?.getId())


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.editPost -> {
                navigateToEditFragment()
                true
            }
            R.id.toggleFavorite -> {
                postDetailViewModel.onToggleFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun navigateToEditFragment(){
        val arguments = PostDetailFragmentArgs.fromBundle(requireArguments())

        var action = PostDetailFragmentDirections.actionPostDetailFragmentToCreatePostFragment()
        action.postKey = arguments.postKey
        view?.findNavController()?.navigate(action)
    }


}