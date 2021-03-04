package com.cpen391.flappyaccount.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.cpen391.appbase.network.SimpleObserver
import com.cpen391.appbase.ui.mvvm.BaseViewModel
import com.cpen391.flappyaccount.Injection
import com.cpen391.flappyaccount.consts.*
import com.cpen391.flappyaccount.model.bean.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SignUpViewModel : BaseViewModel() {
    val registerResult: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val fullNameHasError: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val usernameHasError: MutableLiveData<String> = MutableLiveData<String>()
    val emailHasError: MutableLiveData<String> = MutableLiveData<String>()
    val phoneNoHasError: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val passwordHasError: MutableLiveData<String> = MutableLiveData<String>()

    fun registerUser(fullName: String,
                     username: String,
                     email: String,
                     phoneNo: String,
                     password: String,
                     countryCode: String) {

        val user: User = User(
            fullName,
            username,
            email,
            "+$countryCode$phoneNo",
            password
        )

        val isValidated: Boolean =
            validateFullName(fullName)    &&
                    validateUserName(username)    &&
                    validateEmail(email)       &&
                    validatePhoneNumber(phoneNo) &&
                    validatePassword(password)

        // registering request
        if (isValidated) sendRegisterRequest(user)
        else registerResult.value = false

    }

    private fun sendRegisterRequest(user: User) {
        Injection.provideUserRepository().registerUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SimpleObserver<Boolean>() {
                override fun whenSuccess(t: Boolean) {
                    registerResult.value = t
                }

                override fun onError(e: Throwable) {

                }

                override fun onSubscribe(d: Disposable) {
                }
            })
    }


    private fun validateFullName(fullName: String): Boolean {

        return if (fullName == null || fullName.isEmpty()) {
            fullNameHasError.value = true
            false
        } else {
            fullNameHasError.value = false
            true
        }
    }


    private fun validateUserName(username: String): Boolean {
        val noWhitSpace: String = "^([a-zA-Z0-9!@#\$%^&*()-_=+;:'\"|~`<>?/{}]{1,16})\$"
        val regex: Regex = Regex(noWhitSpace)

        return if (username == null || username.isEmpty()) {
            usernameHasError.value = USERNAME_EMPTY
            false
        } else if (username.length >= 15) {
            usernameHasError.value = USERNAME_TOO_LONG
            false
        } else if (!username.matches(regex)) {
            usernameHasError.value = USERNAME_HAS_WHITE_SPACE
            false
        } else {
            usernameHasError.value = USERNAME_VALID
            true
        }
    }

    private fun validateEmail(emailAddr: String): Boolean {
        val emailPattern: String = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val regex: Regex = Regex(emailPattern)

        return if (emailAddr == null || emailAddr.isEmpty()) {
            emailHasError.value = EMAIL_EMPTY
            false
        } else if (!emailAddr.matches(regex)) {
            emailHasError.value = EMAIL_INVALID
            false
        } else {
            emailHasError.value = EMAIL_VALID
            true
        }
    }

    private fun validatePhoneNumber(phoneNo: String): Boolean {
        // todo: validate phone number according to country code
        return if (phoneNo == null || phoneNo.isEmpty()) {
            phoneNoHasError.value = true
            false
        } else {
            phoneNoHasError.value = false
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        val passwordVal: String = REGEX_START +
//                REGEX_AT_LEAST_1_DIGIT +                    // at least 1 digit
//                REGEX_AT_LEAST_1_LOWERCASE +                // at least 1 lower case letter
//                REGEX_AT_LEAST_1_UPPERCASE +                // at least 1 upper case letter
                REGEX_ANY_FOUR_OR_MORE_LETTERS +          // any 4 or more letter
                REGEX_AT_LEAST_ONE_SPECIAL_CHARACTER +    // at least 1 special character
                REGEX_NO_WHITE_SPACE   +                  // no white space
                REGEX_END

        val regex: Regex = Regex(passwordVal)


        return if (password == null || password.isEmpty()) {
            passwordHasError.value = PASSWORD_EMPTY
            false
        } else if (!password.matches(regex)) {
            passwordHasError.value = PASSWORD_TOO_WEAK
            false
        } else {
            passwordHasError.value = PASSWORD_VALID
            true
        }
    }

}