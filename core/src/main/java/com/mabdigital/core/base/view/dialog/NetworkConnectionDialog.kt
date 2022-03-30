package com.mabdigital.core.base.view.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.TextView
import com.mabdigital.core.R


fun Activity.showNetworkConnectionAlertDialog() {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.no_internet_dialog_layout)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val btnClose = dialog.findViewById<TextView>(R.id.btn_close)
    val btnSetting = dialog.findViewById<TextView>(R.id.btn_network_setting)
    val btnWifiSetting = dialog.findViewById<TextView>(R.id.btn_wifi_setting)
    val btnDataSetting = dialog.findViewById<TextView>(R.id.btn_data_setting)

    btnSetting.visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) View.VISIBLE else View.GONE
    btnWifiSetting.visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) View.GONE else View.VISIBLE
    btnDataSetting.visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) View.GONE else View.VISIBLE

    btnClose.setOnClickListener {
        dialog.dismiss()
        onBackPressed()
    }

    btnSetting.setOnClickListener {
        startActivity(Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
        dialog.dismiss()
    }

    btnWifiSetting.setOnClickListener {
        startActivity( Intent(android.provider.Settings.ACTION_WIFI_SETTINGS))
        dialog.dismiss()
    }

    btnDataSetting.setOnClickListener {
        startActivity( Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS))
        dialog.dismiss()
    }



    val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
    val height = (resources.displayMetrics.heightPixels * 0.90).toInt()
    dialog.window?.setLayout(width, height)
    dialog.show()
}