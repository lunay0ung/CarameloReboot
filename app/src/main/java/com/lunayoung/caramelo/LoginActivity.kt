package com.lunayoung.caramelo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*

/*
* 2019 09 22
* */
class LoginActivity : AppCompatActivity() {

    private val TAG:String = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        Logger.d("테스트테스트테스트테스트")

    }

    private fun performLogin(){
        val email = email_edittext_login.toString()
        val password = password_edittext_login.toString()
        
    }
}
