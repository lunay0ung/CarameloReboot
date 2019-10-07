package com.lunayoung.caramelo

import android.view.View
import com.google.android.material.snackbar.Snackbar

class utils {

    companion object{
        @JvmStatic
        fun makeSnackbarWithMessage(view: View, message:String){
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }


}