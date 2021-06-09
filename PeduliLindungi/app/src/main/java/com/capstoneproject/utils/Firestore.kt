package com.capstoneproject

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.capstoneproject.model.User
import com.capstoneproject.ui.LoginActivity
import com.capstoneproject.ui.RegisterActivity
import com.capstoneproject.ui.ResultActivity
import com.capstoneproject.ui.UploadActivity
import com.capstoneproject.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Firestore {
    private val fstore = FirebaseFirestore.getInstance()

    fun getCurrentUser(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""

        if (currentUser != null){
            currentUserId = currentUser.uid
        }
        return currentUserId
    }

    fun getUserDetail(activity:Activity){
        fstore.collection(Constants.USERS)
                .document(getCurrentUser())
                .get()
                .addOnSuccessListener {
                    Log.i(activity.javaClass.simpleName, it.toString())

                    val user = it.toObject(User::class.java)!!
                    val sharedPreferences = activity.getSharedPreferences(
                            Constants.NAME_PREFERENCES,
                            Context.MODE_PRIVATE
                    )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(
                            Constants.LOGGED_USERNAME,
                        user.fullName
                    )
                    editor.apply()

                    val email_data: SharedPreferences.Editor = sharedPreferences.edit()
                    email_data.putString(
                        Constants.EMAIL_DATA,
                        user.email
                    )
                    email_data.apply()

                    when (activity){
                        is LoginActivity ->{
                            activity.userLoginSucsess(user)
                        }
                        is RegisterActivity-> {
                            activity.userRegisterSucsess(user)
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e(activity.javaClass.simpleName,
                    "Error while register user")
                }
    }

    fun uploadImagetoCloudStorage(activity: Activity, imageFileURI: Uri?){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.IMAGE + System.currentTimeMillis()+"."
        +Constants.getFileEXtension(
                activity,
                imageFileURI
        )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener {
            Log.e(
                "Firebase Image URL",
                it.metadata!!.reference!!.downloadUrl.toString()
            )
            it.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener {
                    Log.e("Downloadable image URL", it.toString())
                    when (activity){
                        is UploadActivity ->{
                            activity.imageUploadSuccess(it.toString())
                        }
                    }
                }
        }
            .addOnFailureListener {
                when(activity){
                    is UploadActivity->{
                        Log.e(
                            activity.javaClass.simpleName,
                            it.message,
                            it
                        )
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    it.message,
                    it
                )
            }
    }

    fun addDiagnosisHistory(activity: Activity, model: String, imageURL: String) {
        val newHistoryData = HashMap<String, Any>()
        newHistoryData["image"] = imageURL
        newHistoryData["predictions"] = listOf(model)
        newHistoryData["status"] = "doing"
        newHistoryData["user_id"] = getCurrentUser()

        fstore.collection(Constants.HISTORY)
            .add(newHistoryData)
            .addOnSuccessListener {
                when(activity){
                    is UploadActivity ->{
                        activity.historyCreationSuccess(it.id)
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is UploadActivity->{
                        Log.e(
                            activity.javaClass.simpleName,
                            it.message,
                            it
                        )
                    }
                }
            }

    }

    fun getScreeningHistoryDetail(activity: Activity, historyId: String) {
        fstore.collection(Constants.HISTORY)
            .document(historyId)
            .get()
            .addOnSuccessListener {
                val data = it.data ?: mapOf()
                when(activity) {
                    is ResultActivity -> activity.onHistoryFetchSuccess(data)
                }
            }
            .addOnFailureListener {

            }
    }

}