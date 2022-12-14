package com.example.android.faith

import android.app.AlertDialog
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.android.faith.database.FaithDatabase
import com.example.android.faith.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_create_post.*
import java.io.ByteArrayOutputStream

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null
    private lateinit var accountViewModel: AccountViewModel

    private var REQUEST_CODE = 200
    private var imageData : ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this).application

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )



        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener { login() }
        binding.buttonLogout.setOnClickListener { logout() }
//        binding.buttonGet.setOnClickListener { getUserMetadata() }
//        binding.buttonSet.setOnClickListener { setUserMetadata() }
        binding.buttonProfilePicture.setOnClickListener {
            chooseProfilePicture()
        }
        binding.buttonToMenu.setOnClickListener {switchToMainActivity()}
        binding.profilePicture.setOnClickListener { chooseProfilePicture() }
        binding.buttonEditUserName.setOnClickListener { showEditUserNamePopup() }


        val app = applicationContext as FaithApplication
        cachedCredentials = app.userCredentials
        cachedUserProfile = app.userProfile
        initializeAccountViewModel(application)
        accountViewModel.getUser().observe(this, Observer{
            binding.userEntity = it
        })
        updateUI()
    }

    private fun showEditUserNamePopup() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(R.string.edit_user_name)
        val editText = EditText(this)
        val user = accountViewModel.getUser().value?.user!!

        editText.setText(user.userName)
        alert.setView(editText)

        alert.setPositiveButton(R.string.edit) { dialog, buttonId ->
            user.userName = editText.text.toString()
            accountViewModel.updateUser(user)
        }
        alert.show()
    }

    private fun initializeAccountViewModel(application: Application) {
        val userDao = FaithDatabase.getInstance(application).userDao
        var viewModelFactory = AccountViewModelFactory("", userDao, application)
        cachedUserProfile?.let{
             viewModelFactory = AccountViewModelFactory(it.getId()!!, userDao, application)
        }
        accountViewModel =  ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel::class.java)
    }

    private fun chooseProfilePicture() {
        val intent = Intent(Intent.ACTION_PICK)//, MediaStore.Images.Media.INTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            data.let{

                binding.profilePicture.setImageURI(data?.data)
                var imageBitMap = (binding.profilePicture.drawable as BitmapDrawable).bitmap

                val imageStream = ByteArrayOutputStream()
                imageBitMap?.compress(Bitmap.CompressFormat.PNG, 90, imageStream)
                imageData = imageStream.toByteArray()


                var user = accountViewModel.getUser().value?.user!!
                user.profilePicture = imageData
                accountViewModel.updateUser(user)

                binding.profilePicture.setImageBitmap(imageBitMap)
            }
        }
    }

    private fun login() {
        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.login_audience, getString(R.string.com_auth0_domain)))
            .start(this, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message, exception.getCode()))
                }

                override fun onSuccess(credentials: Credentials) {
                    cachedCredentials = credentials
                    showSnackBar(getString(R.string.login_success_message, credentials.accessToken))
//
                    val app : FaithApplication = getApplicationContext() as FaithApplication
                    app.onLogin(credentials)
                    initializeAccountViewModel(application)
                    updateUI()
                    showUserProfile()
                }
            })
    }

    private fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    updateUI()
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(payload: Void?) {
                    cachedCredentials = null
                    cachedUserProfile = null

                    var app : FaithApplication = getApplicationContext() as FaithApplication
                    app.onLogout()
                    updateUI()
                }

            })
    }

    private fun showUserProfile() {
        // Guard against showing the profile when no user is logged in
        if (cachedCredentials == null) {
            return
        }

        val client = AuthenticationAPIClient(account)
        client
            .userInfo(cachedCredentials!!.accessToken!!)
            .start(object : Callback<UserProfile, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    initializeAccountViewModel(application)
                    updateUI()
                    switchToMainActivity()
                }

            })
    }

    private fun switchToMainActivity(){

            val switchActivityIntent = Intent(this, MainActivity::class.java)
        switchActivityIntent.putExtra("user", cachedUserProfile)

            startActivity(switchActivityIntent)
            finish()


    }


    private fun updateUI() {
        val isLoggedIn = cachedCredentials != null

        binding.buttonLogin.isEnabled = !isLoggedIn
        binding.buttonLogout.isEnabled = isLoggedIn
        //binding.linearlayoutMetadata.isVisible = isLoggedIn
        binding.buttonToMenu.isVisible = isLoggedIn

        binding.buttonProfilePicture.isVisible = isLoggedIn
        binding.profilePicture.isVisible = isLoggedIn

        binding.buttonEditUserName.isVisible = isLoggedIn
        binding.textviewUserProfile.isVisible = isLoggedIn
        binding.buttonToMenu.isVisible = isLoggedIn

        val userName = cachedUserProfile?.name ?: ""
        val userEmail = cachedUserProfile?.email ?: ""
        binding.textviewUserProfile.text = getString(R.string.user_profile, userName, userEmail)

        binding.profilePicture.setImageBitmap(null)

        cachedUserProfile?.let{
            val userProfile = it
            accountViewModel?.let {
                it.getUser().observe(this, Observer {
                binding.userEntity = it
            })
            }

        }


//        if (!isLoggedIn) {
//            binding.edittextCountry.setText("")
//        }
    }

    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }


}