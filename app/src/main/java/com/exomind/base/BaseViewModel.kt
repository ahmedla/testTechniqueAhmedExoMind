package com.exomind.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    /**
     * Launch coroutine in the viewmodel scope
     **/
    protected fun launch(coroutine: suspend CoroutineScope.() -> Unit): Job {
        return viewModelScope.launch {
            coroutine()
        }
    }
}