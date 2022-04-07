package com.mabdigital.core.tools.extentions

import androidx.appcompat.app.AppCompatActivity
import com.mabdigital.core.base.view.BaseActivity

fun AppCompatActivity.showSnackBar(message:String) {
    if(this is BaseActivity) {
        (this).showSnackBarMessage(message)
    }
}