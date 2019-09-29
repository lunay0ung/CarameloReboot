package com.lunayoung.caramelo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_login.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


    }


    private fun performLogin(){
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()
        Logger.d("이메일: $email, 비밀번호: $password")

        //todo 일단 firebase auth로 회원등록/로그인 시키고
        //메인액티비티로 이동
        //프래그먼트 4개 만들기 

    }


}
