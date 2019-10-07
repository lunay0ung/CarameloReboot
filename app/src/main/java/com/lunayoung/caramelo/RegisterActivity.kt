package com.lunayoung.caramelo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.lunayoung.caramelo.models.User
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_register.*
import timber.log.Timber
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private val SELECT_PHOTO = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener{
            registerUser()
        }


        already_have_an_account_text_view.setOnClickListener {
            Timber.d("Try to show Login Activity")
            Timber.d("나와?")
            startActivity(Intent(this, LoginActivity::class.java))
        }//already_have_an_account

        selectphoto_button_register.setOnClickListener {
            Timber.d("사진 추가")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent, SELECT_PHOTO)

        }//select photo

    }


    private fun askPermission(){
       //todo 코틀린 코루틴 사용하여 권한 요청/ 얻기
    }


    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {

            Timber.d("사진 선택됨")
            selectedPhotoUri = data.data
            if(Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    selectedPhotoUri
                )
                selectphoto_imageview_register.setImageBitmap(bitmap)
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                selectphoto_imageview_register.setImageBitmap(bitmap)
            }

            selectphoto_button_register.alpha = 0f
            //val bitmapDrawable = BitmapDrawable(bitmap)
            //selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)

        }//if

    }//onActivityResult


    private fun registerUser(){
        val email = email_edittext_register.text.toString()
        val username = username_edittext_register.toString()
        val password = password_edittext_register.text.toString()
        Logger.d("이메일: $email, 비밀번호: $password")

        if(email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            utils.makeSnackbarWithMessage(main_layout_register, getString(R.string.register004))
            return
        }

        //todo 일단 firebase auth로 회원등록/로그인 시키고
        //메인액티비티로 이동
        //프래그먼트 4개 만들기

        //Firebase Authentification to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) {
                    Timber.d("result : ${it.result}")
                    return@addOnCompleteListener
                }

                //else if successful
                Timber.d("유저 생성 with uid: ${it.result!!.user?.uid}")
                uploadImageToFirebaseStorage()
            }

            .addOnFailureListener{
                utils.makeSnackbarWithMessage(main_layout_register, getString(R.string.register005)+" ${it.message}")
                Timber.d("Failed to creat  user: {${it.message}")
            }

    }

    //유저가 업로드한 프로필 사진을 스토리지에 업로드한다
    private fun uploadImageToFirebaseStorage(){

        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Timber.d("사진 업로드 잘 됨: {${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    it.toString()
                    Timber.d("File Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{
                Timber.d("사진 업로드 실패: ${it.message}")
            }
    }//uploadImageToFirebaseStorage


    //가입한 유저 정보를 데이터베이스에 저장한다
    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {

        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(
            uid,
            username_edittext_register.text.toString(),
            profileImageUrl
        )
        ref.setValue(user)
            .addOnSuccessListener {
                Timber.d("유저정보를 데이터베이스에 저장함: $it")
                utils.makeSnackbarWithMessage(main_layout_register, getString(R.string.register006))

                //회원가입에 성공하면 메시지 확인 화면으로 이동  --이동하면서 기존에 쌓인 액티비티 스택을 없애거나 새로운 태스크를 생성한다
                //startActivity(Intent(this, LatestMessagesActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)))

            }

            .addOnFailureListener {
                Timber.d("유저정보 데이터베이스 저장 실패: ${it.message}")
            }
    }//saveUserToFirebaseDatabase


}
