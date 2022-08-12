package com.example.android.faith.post

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.android.faith.R
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.database.Post
import com.example.android.faith.databinding.FragmentCreatePostBinding
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [CreatePostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreatePostFragment : Fragment() {
    private lateinit var viewModel : PostViewModel
    private lateinit var binding : FragmentCreatePostBinding


    private val REQUEST_CODE = 100
    private var imageUri: Uri? = null

    private val links : MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Timber.i("ViewmodelProviders called")
        val application = requireNotNull(this.activity).application

        val dataSource = FaithDatabase.getInstance(application).postDatabaseDao

        val viewModelFactory = PostViewModelFactory(dataSource, application)

        val postViewModel = ViewModelProviders.of(this, viewModelFactory).get(PostViewModel::class.java)
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)

        binding =  DataBindingUtil.inflate<FragmentCreatePostBinding>(inflater,
            R.layout.fragment_create_post, container, false)
        binding.postViewModel = viewModel

        binding.buttonSave.setOnClickListener{view: View ->
            savePost(view)
        }

        binding.buttonAddLink.setOnClickListener{view: View ->
        links.add(binding.textUrlText.text.toString())
            binding.textUrlText.setText("")

        }
        binding.buttonAddImage.setOnClickListener{view: View ->
            openGalleryForImage()

        }

        binding.setLifecycleOwner(this)

//        binding.buttonAddLink.setOnClickListener{view: View ->
//            viewModel.addLink(binding.textUrlText.text.toString())
//
//        }


        return binding.root
    }

    private fun savePost(view: View){
        val postText = binding.textPostText.text.toString()
        val post = Post(text = postText, links = links)
        binding.postViewModel?.onCreatePost(post)

        view.findNavController().navigate(R.id.action_createPostFragment_to_postFragment)
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI )
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            imageUri = data?.data
            binding.image.setImageURI(imageUri)
        }
    }
}