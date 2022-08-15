package com.example.android.faith.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_detail, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = PostDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val postDetailviewModelFactory = PostDetailViewModelFactory(arguments.postKey, dataSource)

        val postDetailViewModel =
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
        }, commentViewModel)

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
            commentViewModel.onSubmitComment(
                Comment(postId = postDetailViewModel.getPost().value?.post?.postId!!, text = binding.topLevelComment.text.toString())
            )
        }

        return binding.root
    }


}