package com.example.android.faith.post.comment

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
import com.example.android.faith.post.PostDetailFragmentDirections

class CommentReactionFragment : Fragment() {
    private lateinit var binding : FragmentCommentReactionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding = DataBindingUtil.inflate<FragmentCommentReactionBinding>(inflater, R.layout.fragment_comment_reaction, container, false)


        val arguments = CommentReactionFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application

        val postDao = FaithDatabase.getInstance(application).postDatabaseDao

        val viewModelFactory = CommentViewModelFactory(arguments.commentId, postDao, application)

        val commentViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentViewModel::class.java)

        val faithApp = application as FaithApplication
        val userId = faithApp.userProfile?.getId()!!

        val commentAdapter = CommentAdapter(
            clickListenerAdd = AddCommentListener {
                    newComment ->
                commentViewModel.onSubmitComment(newComment)
            },
            clickListenerReactions = ReactionsListener {
                    commentId ->
                commentViewModel.onDisplayReactions(commentId)
            }
            ,commentViewModel, userId)

        binding.commentReactions.adapter = commentAdapter

        commentViewModel.getComment().observe(viewLifecycleOwner, Observer {
            binding.mainComment = it
        })

        commentViewModel.getCommentsOfParent(arguments.commentId).observe(viewLifecycleOwner, Observer {
            commentAdapter.submitList(it)

        })

        commentViewModel.navigateToCommentReactions.observe(viewLifecycleOwner, Observer{comment ->
            comment?.let{
                this.findNavController().navigate(CommentReactionFragmentDirections.actionCommentReactionFragmentSelf(comment))
                commentViewModel.onReactionsDisplayed()
            }
        })

        return binding.root
    }


}