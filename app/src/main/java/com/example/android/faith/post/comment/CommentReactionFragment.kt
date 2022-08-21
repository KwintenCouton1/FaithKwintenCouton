package com.example.android.faith.post.comment

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.android.faith.FaithApplication
import com.example.android.faith.R
import com.example.android.faith.database.post.Comment
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.databinding.FragmentCommentReactionBinding
import com.example.android.faith.setActivityTitle
import timber.log.Timber

class CommentReactionFragment : Fragment() {
    private lateinit var binding : FragmentCommentReactionBinding
    private lateinit var commentViewModel: CommentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setActivityTitle(R.string.reactions_title)

        binding = DataBindingUtil.inflate<FragmentCommentReactionBinding>(inflater, R.layout.fragment_comment_reaction, container, false)

        val application = requireNotNull(this.activity).application
        val arguments = CommentReactionFragmentArgs.fromBundle(requireArguments())


        commentViewModel = initializeViewModel(application, arguments)

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
            },
            clickListenerPopup = PopupListener {
                    comment ->

                showCommentPopup(requireView(), comment)
            }
            , userId)

        binding.commentReactions.adapter = commentAdapter
        return commentAdapter
    }

    fun showCommentPopup(v: View, comment: Comment){
        PopupMenu(requireActivity(), v).apply{
            setOnMenuItemClickListener{
                Timber.i(it.itemId.toString())
                when (it.itemId){
                    R.id.deleteComment -> {
                        commentViewModel.onDeleteComment(comment)
                        true
                    }
                    R.id.editComment -> {
                        showEditCommentPopup(comment)
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.comment_context_menu)
            show()
        }
    }

    fun showEditCommentPopup(comment: Comment){
        val alert = AlertDialog.Builder(requireActivity())
        alert.setTitle("TODO: change to string")
        val editText = EditText(requireActivity())
        editText.setText(comment.text)
        alert.setView(editText)

        alert.setPositiveButton("Bewerk", DialogInterface.OnClickListener{
                dialog, buttonId ->
            comment.text = editText.text.toString()
            commentViewModel.onUpdateComment(comment)

        })

        alert.show()

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