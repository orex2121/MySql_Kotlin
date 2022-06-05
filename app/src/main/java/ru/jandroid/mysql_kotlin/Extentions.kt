package ru.jandroid.mysql_kotlin

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


    fun AppCompatActivity.showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
