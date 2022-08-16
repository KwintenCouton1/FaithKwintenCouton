package com.example.android.faith

import android.app.Application
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import timber.log.Timber


class FaithApplication: Application() {
//    private var _userProfile : MutableLiveData<UserProfile?> = MutableLiveData(null)
////    var userProfile : UserProfile?
////            get() = _userProfile.value
////        set(value) {
////            _userProfile.value = value!!
////        }

    var userProfile : UserProfile? = null

//    private var _userCredentials : MutableLiveData<Credentials?> = MutableLiveData(null)
//    var userCredentials: Credentials?
//    get () = _userCredentials.value
//    set(value: Credentials?){
//        _userCredentials.value = value!!
//    }

    var userCredentials : Credentials? = null
    fun onLogin(credentials: Credentials){

        if (credentials == null){
            //No logged in user
            this.userCredentials = null
            userProfile = null
            return
        }

        val account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
        val client = AuthenticationAPIClient(account)
        client
            .userInfo(credentials.accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception : AuthenticationException){
                    Timber.i(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(result: UserProfile) {
                    userProfile = result
                    userCredentials = credentials
                }
            })



    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

    fun onLogout() {
        userCredentials = null
        userProfile = null
    }
}