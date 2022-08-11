package com.example.android.faith

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Timber.i("ViewmodelProviders called")
        viewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)

        binding =  DataBindingUtil.inflate<FragmentCreatePostBinding>(inflater, R.layout.fragment_create_post, container, false)
        binding.postViewModel = viewModel

        binding.buttonSave.setOnClickListener{view: View ->
            val postText = binding.textPostText.toString()
            var links : List<String> = listOf()
            binding.recyclerView.forEach{
                
            }
        }

        binding.buttonAddImage.setOnClickListener{view: View ->
            openGalleryForImage()

        }


//        binding.buttonAddLink.setOnClickListener{view: View ->
//            viewModel.addLink(binding.textUrlText.text.toString())
//
//        }


        return binding.root
    }

//    private fun addLink(link : String){
//        viewModel.addLink(link)
//    }

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