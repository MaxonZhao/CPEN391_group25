package com.cpen391.flappyaccount.model.api

import com.cpen391.businessbase.network.remoteEntity.UserEntity
import com.cpen391.flappyaccount.consts.*
import com.cpen391.flappyaccount.model.bean.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import timber.log.Timber

/**
    Backend API class to interact with cloud database in Firebase

    @note: object in kotlin is the same as having as many static methods in a class in java
    @author Yuefeng Zhao
 */
object UserAPI {

    /**
     *  @params:
     *      String username: name of the user, it should be unique and contains no white space
     *      String password: password of the user, contains a special character
     *  @return:
     *      an Observable object wrapped with LoginStatus(see Const.kt). This is a standard
     *      usage of RxJava to perform asynchronous task
     */
    fun login(username: String, password: String): Observable<LoginStatus> {
        return Observable.create { emitter: ObservableEmitter<LoginStatus> ->

            val checkUser: Query = UserEntity.mUserRef.orderByChild("userName").equalTo(username)

            // firebase api listening to remote data resource
            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val passwordFromDB = snapshot.child(username).child("password").value

                        if (passwordFromDB!! == password) {
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

    /**
     *  @params:
     *      User user: an user object to register to the remote database
     *  @return:
     *      an Observable object wrapped with Boolean(see Const.kt) to signify if registration is successful
     */
    fun registerUser(user: User): Observable<Boolean> {
        return Observable.create<Boolean> { emitter: ObservableEmitter<Boolean> ->
            UserEntity.mUserRef.child(user.userName).setValue(user).apply {
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


    /**
     *  @params:
     *      User user: an user object to register to the remote database
     *  @return:
     *      an Observable object wrapped with Boolean(see Const.kt) to signify if registration is successful
     */
    fun findUser(username: String): Observable<User> {
        var user: User?
        return Observable.create { emitter: ObservableEmitter<User> ->
            val checkUser: Query = UserEntity.mUserRef.orderByChild("userName").equalTo(username)
            checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        snapshot.child(username).apply {
                            val email = child("email").value as String
                            val fullName = child("fullName").value as String
                            val password = child("password").value as String
                            val phoneNo = child("phoneNo").value as String
                            val currentScore: Long = child("current_score").value as Long
                            val topThreeScores: List<Long> =
                                child("top_three_scores").value as List<Long>
                            user = User(
                                fullName,
                                username,
                                email,
                                phoneNo,
                                password,
                                currentScore,
                                topThreeScores
                            )
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

    fun updateTopThreeScore(user: User, topThreeScore: List<Long>): Observable<Boolean> {
        user.top_three_scores = topThreeScore
        return registerUser(user);
    }
}