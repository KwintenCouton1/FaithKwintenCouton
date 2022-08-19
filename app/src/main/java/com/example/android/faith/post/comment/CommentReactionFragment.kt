package com.example.android.faith.post.comment

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.faith.FaithApplication
import com.example.android.faith.R
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.databinding.FragmentCommentReactionBinding

class CommentReactionFragment : Fragment() {
    private lateinit var binding : FragmentCommentReactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate<FragmentCommentReactionBinding>(inflater, R.layout.fragment_comment_reaction, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = CommentReactionFragmentArgs.fromBundle(requireArguments())


        val commentViewModel = initializeViewModel(application, arguments)

        val commentAdapter = initializeAdapter(commentViewModel, application)

        initializeObservers(commentAdapter,commentViewModel, arguments)

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun initializeViewModel(
        application: Application,
        arguments: CommentReactionFragmentArgs
    ): CommentViewModel {
        val postDao = FaithDatabase.getInstance(application).postDatabaseDao

        val viewModelFactory = CommentViewModelFactory(arguments.commentId, postDao, application)

        return ViewModelProviders.of(this, viewModelFactory).get(CommentViewModel::class.java)
    }

    private fun initializeAdapter(viewModel: CommentViewModel, application: Application): CommentAdapter{
        val faithApp = application as FaithApplication
        val userId = faithApp.userProfile?.getId()!!

        val commentAdapter = CommentAdapter(
            clickListenerAdd = AddCommentListener {
                    newComment ->
                viewModel.onSubmitComment(newComment)
            },
            clickListenerReactions = ReactionsListener {
                    commentId ->
                viewModel.onDisplayReactions(commentId)
            }
            ,viewModel, userId)

        binding.commentReactions.adapter = commentAdapter
        return commentAdapter
    }

    private fun initializeObservers(adapter: CommentAdapter, commentViewModel: CommentViewModel, arguments: CommentReactionFragmentArgs ){
        commentViewModel.getComment().observe(viewLifecycleOwner, Observer {
            binding.mainComment = it
        }) //TODO check if removable with lifecycleowner set to this

        commentViewModel.getCommentsOfParent(arguments.commentId).observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)

        })

        commentViewModel.navigateToCommentReactions.observe(viewLifecycleOwner, Observer{comment ->
            comment?.let{
                this.findNavController().navigate(CommentReactionFragmentDirections.actionCommentReactionFragmentSelf(comment))
                commentViewModel.onReactionsDisplayed()
            }
        })
    }
}