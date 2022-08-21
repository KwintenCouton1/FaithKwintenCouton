package com.example.android.faith.post.create

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService


import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.android.faith.FaithApplication
import com.example.android.faith.R
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.post.Link
import com.example.android.faith.database.post.Post
import com.example.android.faith.databinding.FragmentCreatePostBinding
import com.example.android.faith.post.detail.PostDetailFragmentArgs
import com.example.android.faith.post.link.LinkAdapter
import com.example.android.faith.setActivityTitle
import kotlinx.android.synthetic.main.link_view.*
import timber.log.Timber
import java.io.ByteArrayOutputStream

/**
 * A simple [Fragment] subclass.
 * Use the [CreatePostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatePostFragment : Fragment() {
    private lateinit var viewModel : CreatePostViewModel
    private lateinit var binding : FragmentCreatePostBinding


    private val REQUEST_CODE = 100
    private var imageData: ByteArray? = null

    private val links : MutableList<Link> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setActivityTitle(R.string.create_post_title)
        val application = requireNotNull(this.activity).application

        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao
        val userDao = FaithDatabase.getInstance(application).userDao

        val arguments = PostDetailFragmentArgs.fromBundle(requireArguments())

        val app = application as FaithApplication

        val createPostViewModelFactory = CreatePostViewModelFactory(arguments.postKey,dataSource,  application)


        viewModel = ViewModelProviders.of(this, createPostViewModelFactory).get(CreatePostViewModel::class.java)

        val linkAdapter = LinkAdapter()

        viewModel.post.observe(viewLifecycleOwner, Observer {
            it?.let{
                links.clear()
                links.addAll(it.links)
                linkAdapter.submitList(links)
                linkAdapter.notifyDataSetChanged()
                imageData = it.post.image
            }

        })


        binding =  DataBindingUtil.inflate<FragmentCreatePostBinding>(inflater,
            R.layout.fragment_create_post, container, false)
        binding.createPostViewModel = viewModel



        binding.buttonSave.setOnClickListener{view: View ->
            savePost(view)
        }

        binding.textPostText.setOnFocusChangeListener {
                view, value ->
            if (!value){
                hideKeyboard(view)
            }


        }



        binding.buttonAddLink.setOnClickListener{view: View ->
        links.add(Link(linkString = binding.textUrlText.text.toString()))
            binding.textUrlText.setText("")
            linkAdapter.submitList(links)
            linkAdapter.notifyDataSetChanged()
            hideKeyboard(view)

        }
        binding.buttonAddImage.setOnClickListener{view: View ->
            openGalleryForImage()

        }
        binding.links.adapter = linkAdapter

        binding.setLifecycleOwner(this)

        return binding.root
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun savePost(view: View){
        val arguments = PostDetailFragmentArgs.fromBundle(requireArguments())
        val postText = binding.textPostText.text.toString()

        val app = requireActivity().applicationContext as FaithApplication
        val userId = app.userProfile?.getId()

//        val imageStream = ByteArrayOutputStream()
//                imageData?.compress(Bitmap.CompressFormat.PNG, 90, imageStream)

        val post = Post(postId = arguments.postKey, text = postText, image= imageData, userId = userId!!)//, image = imageData
        binding.createPostViewModel?.onCreatePost(post, links)

        view.findNavController().navigate(R.id.action_createPostFragment_to_postFragment)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)//, MediaStore.Images.Media.INTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            data.let{

                binding.image.setImageURI(data?.data)
                var imageBitMap = (binding.image.drawable as BitmapDrawable).bitmap

                val imageStream = ByteArrayOutputStream()
                imageBitMap?.compress(Bitmap.CompressFormat.PNG, 90, imageStream)
                imageData = imageStream.toByteArray()
            }
        }
    }
}