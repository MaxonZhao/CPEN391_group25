package com.cpen391.flappyaccount.model.api

import android.content.Intent
import android.widget.Toast
import com.cpen391.businessbase.network.remoteEntity.UserEntity
import com.cpen391.flappyaccount.activity.StartActivity
import com.cpen391.flappyaccount.consts.*
import com.cpen391.flappyaccount.model.bean.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import timber.log.Timber
import java.util.*

object UserAPI {
    fun login(username: String, password: String) : Observable<LoginStatus> {
        return Observable.create<LoginStatus> { emitter: ObservableEmitter<LoginStatus> ->

            val checkUser: Query = UserEntity.mUserRef.orderByChild("userName").equalTo(username)

            checkUser.addListenerForSingleValueEvent (object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val passwordFromDB = snapshot.child(username).child("password").value

                        Timber.d("fetched password is: " + passwordFromDB)
                        if (passwordFromDB!!.equals(password)) {
                            Timber.d("login success")
                            emitter.onNext(LoginStatus(LOGIN_SUCCEED))
                        } else {
                            emitter.onNext(LoginStatus(LOGIN_INCORRECT_PASSWORD))
                        }
                    } else {
                        emitter.onNext(LoginStatus(LOGIN_USERNOTFOUND))
                    }
                    emitter.onComplete()
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.w(error.toException())
                    emitter.onNext(LoginStatus(LOGIN_EXCEPTION))
                    emitter.onComplete()
                }

            })

        }
    }

    fun registerUser(user: User) : Observable<Boolean> {
        return Observable.create<Boolean> { emitter: ObservableEmitter<Boolean> ->
            UserEntity.mUserRef.child(user.userName).setValue(user).apply{
                addOnSuccessListener {
                    emitter.apply {
                        onNext(true)
                        onComplete()
                    }
                }

                addOnFailureListener {
                    emitter.apply {
                        onNext(false)
                        onComplete()
                    }
                }
            }
        }
    }


    fun findUser(username: String) : Observable<User> {
        var user: User? = null
        return Observable.create { emitter: ObservableEmitter<User> ->
            val checkUser: Query = UserEntity.mUserRef.orderByChild("userName").equalTo(username)
            checkUser.addListenerForSingleValueEvent (object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        snapshot.child(username).apply {
                            val email = child("email").value as String
                            val fullName = child("fullName").value as String
                            val password = child("password").value as String
                            val phoneNo = child("phoneNo").value as String
                            val currentScore: Long = child("current_score").value as Long
                            val topThreeScores: List<Long> = child("top_three_scores").value as List<Long>
                            user = User(fullName, username, email, phoneNo, password, currentScore, topThreeScores)
                            emitter.onNext(user!!)
                        }
                    } else {
                        emitter.onNext(User())
                    }
                    emitter.onComplete()
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.w(error.toException())
                    emitter.onNext(User())
                    emitter.onComplete()
                }

            })
        }
    }


    fun resetPassword(user: User, newPassword: String): Observable<Boolean> {
        user.password = newPassword
        return registerUser(user)
    }

    fun updateTopThreeScore(user: User, topThreeScore: List<Long>){ //: Observable<Boolean>
        user.top_three_scores = topThreeScore
    }
}