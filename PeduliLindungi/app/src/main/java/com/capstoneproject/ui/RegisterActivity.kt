package com.capstoneproject.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.capstoneproject.Firestore
import com.capstoneproject.R
import com.capstoneproject.model.User
import com.capstoneproject.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private  val auth = FirebaseAuth.getInstance()
    private  val fstore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val tv_login: TextView = findViewById(R.id.tv_login)
        val btn_signup: Button = findViewById(R.id.btn_signup)
        val email_regis: EditText = findViewById(R.id.email_regis)
        val password_regis: EditText = findViewById(R.id.password_regis)
        val progressBar:ProgressBar = findViewById(R.id.progresbar)
        val fullname_register: EditText = findViewById(R.id.fullname_register)

        tv_login.setOnClickListener(this)

        btn_signup.setOnClickListener {
            val email = email_regis.text.trim().toString()
            val password = password_regis.text.trim().toString()
            val fullname = fullname_register.text.trim().toString()

            if (TextUtils.isEmpty(fullname)){
                fullname_register.setError("Fullname is required")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(email)){
                email_regis.setError("Email is required")
                return@setOnClickListener

            }else if(TextUtils.isEmpty(password)){
                password_regis.setError("Password is required")
                return@setOnClickListener

            } else if (password.length < 6){
                password_regis.setError("Password must > 6 character")
            }

            progressBar.visibility = View.VISIBLE

            //create user
            createUser(email,password)


        }
    }


    fun createUser(email: String, password: String){
        val progressBar:ProgressBar = findViewById(R.id.progresbar)
        val fullname_register: EditText = findViewById(R.id.fullname_register)
        val email_regis: EditText = findViewById(R.id.email_regis)


        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){task->
                    val fireBaseUser: FirebaseUser = task.result!!.user!!
                    if (task.isSuccessful){

                        val user = User(
                                fireBaseUser.uid,
                                fullname_register.text.toString().trim(){it <= ' '},
                                email_regis.text.toString().trim(){it <= ' '},

                                )

                        saveUserinFireStore(user)
                        Toast.makeText(this,"User created.", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"Failed create user."+task.exception, Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                }
    }

    fun userRegisterSucsess(user: User) {

        Log.i("email: ", user.email)
        Log.i("fullName", user.fullName)

        val intent = Intent(this@RegisterActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()

    }

    fun saveUserinFireStore(user: User) {

        fstore.collection(Constants.USERS)
                .document(user.id)
                .set(user, SetOptions.merge())
                .addOnSuccessListener {
                    Firestore().getUserDetail(this@RegisterActivity)
                }
                .addOnFailureListener {

                }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_login -> {
                val moveIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }
}