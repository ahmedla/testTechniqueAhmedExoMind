package com.exomind.base

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    /**
     * Show a long toast in the bottom of the screen with provided message
     **/
    protected fun Context.toast(message: String) = Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()


}