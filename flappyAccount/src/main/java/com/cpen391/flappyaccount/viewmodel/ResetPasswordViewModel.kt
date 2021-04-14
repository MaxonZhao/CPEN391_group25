package com.cpen391.flappyaccount.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cpen391.appbase.network.SimpleObserver
import com.cpen391.appbase.ui.mvvm.BaseViewModel
import com.cpen391.flappyaccount.Injection
import com.cpen391.flappyaccount.consts.*
import com.cpen391.flappyaccount.model.bean.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ResetPasswordViewModel : BaseViewModel() {
    val newPasswordHasError: MutableLiveData<String> = MutableLiveData()
    val confirmPasswordHasError: MutableLiveData<String> = MutableLiveData()
    val resetPasswordResult: MutableLiveData<Boolean> = MutableLiveData()


    fun resetPassword(user: User, newPassword: String, confirmPassword: String) {
        if (validateNewPassword(newPassword) && validateConfirmPassword(
                newPassword,
                confirmPassword
            )
        ) {
            Injection.provideUserRepository().resetPassword(user, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SimpleObserver<Boolean>() {
                    override fun onError(e: Throwable) {
                        Timber.d("please connect to network")
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun whenSuccess(t: Boolean) {
                        resetPasswordResult.value = true
                    }
                })
        }
    }


    private fun validateNewPassword(password: String): Boolean {

        val passwordVal: String = REGEX_START +
//                REGEX_AT_LEAST_1_DIGIT +                    // at least 1 digit
//                REGEX_AT_LEAST_1_LOWERCASE +                // at least 1 lower case letter
//                REGEX_AT_LEAST_1_UPPERCASE +                // at least 1 upper case letter
                REGEX_ANY_FOUR_OR_MORE_LETTERS +          // any 4 or more letter
                REGEX_AT_LEAST_ONE_SPECIAL_CHARACTER +    // at least 1 special character
                REGEX_NO_WHITE_SPACE +                  // no white space
                REGEX_END

        val regex: Regex = Regex(passwordVal)


        return if (password == null || password.isEmpty()) {
            newPasswordHasError.value = PASSWORD_EMPTY
            false
        } else if (!password.matches(regex)) {
            newPasswordHasError.value = PASSWORD_TOO_WEAK
            false
        } else {
            newPasswordHasError.value = PASSWORD_VALID
            true
        }
    }

    private fun validateConfirmPassword(password: String, newPassword: String): Boolean {
        return if (password == null || password.isEmpty()) {
            confirmPasswordHasError.value = PASSWORD_EMPTY
            false
        } else if (password != newPassword) {
            confirmPasswordHasError.value = PASSWORD_NOT_MATCH
            false
        } else {
            confirmPasswordHasError.value = PASSWORD_VALID
            true
        }
    }
}