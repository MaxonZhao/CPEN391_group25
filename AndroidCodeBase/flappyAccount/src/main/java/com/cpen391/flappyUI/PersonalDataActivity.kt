package com.cpen391.flappyUI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBar
import androidx.core.content.FileProvider
import com.cpen391.appbase.network.SimpleObserver
import com.cpen391.appbase.ui.mvvm.MvvmActivity
import com.cpen391.flappyUI.util.LoggedInUserUtil
import com.cpen391.flappyaccount.Injection
import com.cpen391.flappyaccount.R
import com.cpen391.flappyaccount.databinding.ActivityPersonalDataBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
/**
 *  PersonalDataActivity
 *
 *  @note: Change bird's color based on user's choice on UI
 *  Display game data including current score, top 3 score, userName, email on UI
 *  Share user's game data via social media
 *
 *  @author Robin Lai
 */
class PersonalDataActivity : MvvmActivity<ActivityPersonalDataBinding>() {
    private val context: Context = this
    private val currentActivity: PersonalDataActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar = supportActionBar!!
        actionBar.hide()


        val user = LoggedInUserUtil.instance?.getUser()

        val birdImage: ImageView = findViewById(R.id.bird_image)
        when (intent.getStringExtra("birdImage")) {
            "bird_red" -> {
                birdImage.setImageResource(R.drawable.bird_red)
            }
            "bird_black" -> {
                birdImage.setImageResource(R.drawable.bird_black)
            }
            "bird_orange" -> {
                birdImage.setImageResource(R.drawable.bird_orange)
            }
            "bird_green" -> {
                birdImage.setImageResource(R.drawable.bird_green)
            }
            "bird_yellow" -> {
                birdImage.setImageResource(R.drawable.bird_yellow)
            }
            "bird_blue" -> {
                birdImage.setImageResource(R.drawable.bird_blue)
            }
        }

        binding.apply {
            if (LoggedInUserUtil.instance?.isLogin() == true) {
                val user_Name = LoggedInUserUtil.instance?.getUser()?.fullName
                val user_Email = LoggedInUserUtil.instance?.getUser()?.email
                val currentScore = LoggedInUserUtil.instance?.getUser()?.current_score
                val topThreeScore = LoggedInUserUtil.instance?.getUser()?.top_three_scores
                val historyScore = mutableListOf<Long>()
                if (topThreeScore != null) {
                    for (i in topThreeScore.indices) {
                        historyScore.add(topThreeScore[i])
                    }
                }
                if (currentScore != null) {
                    historyScore.add(currentScore)
                }
                historyScore.sorted()

                user?.let {
                    Injection.provideUserRepository()
                        .updateTopThreeScore(it, historyScore.subList(0, 3))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SimpleObserver<Boolean>() {
                            override fun whenSuccess(t: Boolean) {
                                Timber.d("top 3 scores are updated!")
                            }

                            override fun onError(e: Throwable) {
                                Timber.d("please connect to network")
                            }

                            override fun onSubscribe(d: Disposable) {
                            }
                        })
                }

                userName.text = user_Name
                userEmail.text = user_Email
                top1Score.text = historyScore[0].toString()
                top2Score.text = historyScore[1].toString()
                top3Score.text = historyScore[2].toString()
            } else {
                userName.text = "Guest"
                userEmail.text = ""
                top1Score.text = ""
                top2Score.text = ""
                top3Score.text = ""
                Toast.makeText(
                    context,
                    "Please log in to save your history data!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun createBackupFile(directory: File, exportFileName: String): File {
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, exportFileName)
        if (file.exists()) {
            file.delete()
        }
        return file
    }

    override fun initView() {
        super.initView()
        binding.apply {
            val dstString = File(context.filesDir.toString() + "/score/")
            val file = createBackupFile(dstString, "userScoreData.csv")
            val bw = BufferedWriter(FileWriter(file, true))
            val userName = LoggedInUserUtil.instance?.getUser()?.userName
            val userEmail = LoggedInUserUtil.instance?.getUser()?.email
            val currentScore = LoggedInUserUtil.instance?.getUser()?.current_score
            val top3Score = LoggedInUserUtil.instance?.getUser()?.top_three_scores
            if (userName != null && userEmail != null && currentScore != null && top3Score != null) {
                writeBw(bw, userName, userEmail, currentScore, top3Score)
            } else {
                if (currentScore != null) {
                    writeBw(bw, "Guest", "", currentScore, listOf())
                }
            }
            btnShare.setOnClickListener {
                val dst = File(dstString, "userScoreData.csv")
                val emailAddress = LoggedInUserUtil.instance?.getUser()?.email.toString()
                sendEmail(dst, currentActivity, emailAddress, "userScoreData")
            }
        }
    }

    private fun writeBw(
        bw: BufferedWriter,
        userName: String,
        userEmail: String,
        currentScore: Long,
        top3Score: List<Long>
    ) {
        bw.write("userName")
        bw.write(",")
        bw.write(userName)
        bw.write(",")
        bw.newLine()

        bw.write("userEmail")
        bw.write(",")
        bw.write(userEmail)
        bw.write(",")
        bw.newLine()

        bw.write("currentScore")
        bw.write(",")
        bw.write(currentScore.toString())
        bw.write(",")
        bw.newLine()

        bw.write("top3Score")
        bw.write(",")
        bw.write(top3Score[0].toString())
        bw.write(",")
        bw.write(top3Score[1].toString())
        bw.write(",")
        bw.write(top3Score[2].toString())
        bw.write(",")
        bw.newLine()
        bw.close()
    }

    override fun initObserver() {
    }

    override fun bind(): ActivityPersonalDataBinding {
        return ActivityPersonalDataBinding.inflate(layoutInflater)
    }

    private fun sendEmail(
        dst: File,
        c: Activity?,
        email: String,
        @Nullable titleSuffix: String?,
    ) {
        if (c == null) {
            return
        }
        val emailIntent = Intent(Intent.ACTION_SEND)
        val message = "Your Game Score"
        val fileToSend = File(dst.path)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, titleSuffix)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        emailIntent.putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                this, "com.cpen391.flappyUI" + ".provider",
                fileToSend
            )
        )
        startActivity(Intent.createChooser(emailIntent, ""))
    }
}