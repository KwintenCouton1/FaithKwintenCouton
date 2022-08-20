package com.example.android.faith.post.detail

import android.app.AlertDialog
import android.app.Application
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.android.faith.FaithApplication
import com.example.android.faith.MainActivity
import com.example.android.faith.R
import com.example.android.faith.database.Comment
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.PostDatabaseDao
import com.example.android.faith.databinding.FragmentPostDetailBinding
import com.example.android.faith.post.comment.*
import com.example.android.faith.post.link.LinkAdapter
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailFragment : Fragment() {
    lateinit var binding : FragmentPostDetailBinding
    lateinit var postDetailViewModel : PostDetailViewModel
    lateinit var commentViewModel : CommentViewModel

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

        initializePostDetailViewModel(application, dataSource, arguments, userId)

        binding.lifecycleOwner = this

        val linkAdapter = initializeLinkAdapter()

        commentViewModel = initializeCommentViewModel(application, dataSource)

        val commentAdapter = initializeCommentAdapter(commentViewModel, userId)

        initializeObservers(commentViewModel, commentAdapter, linkAdapter, arguments)

        setHasOptionsMenu(true)


        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        registerForContextMenu(binding.comments)
//    }
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
        alert.setTitle(R.string.edit_comment)
        val editText = EditText(requireActivity())
        editText.setText(comment.text)
        alert.setView(editText)

        alert.setPositiveButton(R.string.edit, DialogInterface.OnClickListener{
            dialog, buttonId ->
            comment.text = editText.text.toString()
            commentViewModel.onUpdateComment(comment)

        })

        alert.show()

    }

    private fun initializePostDetailViewModel(application: Application, postDao : PostDatabaseDao, arguments: PostDetailFragmentArgs, userId :String): PostDetailViewModel{
        val userDao = FaithDatabase.getInstance(application).userDao

        val postDetailViewModelFactory = PostDetailViewModelFactory(arguments.postKey, userId, postDao, userDao)

        postDetailViewModel =
            ViewModelProvider(this, postDetailViewModelFactory).get(PostDetailViewModel::class.java)

        binding.postDetailViewModel = postDetailViewModel
        return postDetailViewModel
    }

    private fun initializeCommentViewModel(application: Application, postDao: PostDatabaseDao): CommentViewModel{
        val commentViewModelFactory = CommentViewModelFactory(0L, postDao, application)
        return ViewModelProvider(this, commentViewModelFactory).get(CommentViewModel::class.java)
    }

    private fun initializeLinkAdapter(): LinkAdapter{
        val linkAdapter = LinkAdapter()

        binding.postLinks.adapter = linkAdapter
        return linkAdapter
    }

    private fun initializeCommentAdapter(commentViewModel: CommentViewModel, userId: String): CommentAdapter{
        val commentAdapter = CommentAdapter(
            clickListenerAdd = AddCommentListener {
                    newComment ->
                commentViewModel.onSubmitComment(newComment)
            },
            clickListenerReactions = ReactionsListener {
                    commentId ->
                commentViewModel.onDisplayReactions(commentId)
            },
            clickListenerPopup = PopupListener {
                    comment ->
                showCommentPopup(requireView(), comment)
            }
            , userId)

        binding.comments.adapter = commentAdapter
        return commentAdapter
    }

    private fun initializeObservers(commentViewModel : CommentViewModel, commentAdapter : CommentAdapter, linkAdapter: LinkAdapter, arguments: PostDetailFragmentArgs){
        commentViewModel.navigateToCommentReactions.observe(viewLifecycleOwner, Observer{comment ->
            comment?.let{
                this.findNavController().navigate(
                    PostDetailFragmentDirections.actionPostDetailFragmentToCommentReactionFragment(
                        comment
                    )
                )
                commentViewModel.onReactionsDisplayed()
            }
        })

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
        val arguments =
            PostDetailFragmentArgs.fromBundle(requireArguments())

        var action =
            PostDetailFragmentDirections.actionPostDetailFragmentToCreatePostFragment()
        action.postKey = arguments.postKey
        view?.findNavController()?.navigate(action)
    }




}