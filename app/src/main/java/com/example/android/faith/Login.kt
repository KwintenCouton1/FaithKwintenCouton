package com.example.android.faith

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.CustomTabsOptions
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.android.faith.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_create_post.*
import timber.log.Timber
import java.io.ByteArrayOutputStream

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    private var REQUEST_CODE = 200
    private var imageData : Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener { login() }
        binding.buttonLogout.setOnClickListener { logout() }
        binding.buttonGet.setOnClickListener { getUserMetadata() }
        binding.buttonSet.setOnClickListener { setUserMetadata() }
        binding.buttonToMenu.setOnClickListener {
            switchToMainActivity()
        }

        binding.profilePicture.setOnClickListener {
            chooseProfilePicture()
        }


        val app = applicationContext as FaithApplication
        cachedCredentials = app.userCredentials
        cachedUserProfile = app.userProfile

        updateUI()
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
                imageData = (binding.profilePicture.drawable as BitmapDrawable).bitmap
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
                    var app : FaithApplication = getApplicationContext() as FaithApplication
                    app.onLogin(credentials)
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

    private fun getUserMetadata() {
        // Guard against getting the metadata when no user is logged in
        if (cachedCredentials == null) {
            return
        }

        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)

        usersClient
            .getProfile(cachedUserProfile!!.getId()!!)
            .start(object : Callback<UserProfile, ManagementException> {

                override fun onFailure(exception: ManagementException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(userProfile: UserProfile) {
                    cachedUserProfile = userProfile
                    updateUI()

                    val country = userProfile.getUserMetadata()["country"] as String?
                    val profilePicture = userProfile.getUserMetadata()["profilePicture"] as ByteArray
                    binding.edittextCountry.setText(country)


                    profilePicture.let{
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size )
                        binding.profilePicture.setImageBitmap(bitmap)
                    }

                }

            })
    }

    private fun setUserMetadata() {
        // Guard against getting the metadata when no user is logged in
        if (cachedCredentials == null) {
            return
        }

        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)
        val imageStream = ByteArrayOutputStream()
        imageData?.compress(Bitmap.CompressFormat.PNG, 90, imageStream)
        val metadata = mapOf(
            "country" to binding.edittextCountry.text.toString(),
            "profilePicture" to imageStream.toByteArray()
        )

        usersClient
            .updateMetadata(cachedUserProfile!!.getId()!!, metadata)
            .start(object : Callback<UserProfile, ManagementException> {

                override fun onFailure(exception: ManagementException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                    Timber.i(exception.getCode())
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    updateUI()

                    showSnackBar(getString(R.string.general_success_message))
                }

            })
    }

    private fun updateUI() {
        val isLoggedIn = cachedCredentials != null

        binding.textviewTitle.text = if (isLoggedIn) {
            getString(R.string.logged_in_title)
        } else {
            getString(R.string.logged_out_title)
        }
        binding.buttonLogin.isEnabled = !isLoggedIn
        binding.buttonLogout.isEnabled = isLoggedIn
        binding.linearlayoutMetadata.isVisible = isLoggedIn
        binding.buttonToMenu.isVisible = isLoggedIn

        binding.textviewUserProfile.isVisible = isLoggedIn

        val userName = cachedUserProfile?.name ?: ""
        val userEmail = cachedUserProfile?.email ?: ""
        binding.textviewUserProfile.text = getString(R.string.user_profile, userName, userEmail)

        cachedUserProfile?.let{
            if (!it.pictureURL.isNullOrEmpty()){
                binding.profilePicture.setImageURI(Uri.parse(cachedUserProfile?.pictureURL))
            }

        }


        if (!isLoggedIn) {
            binding.edittextCountry.setText("")
        }
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