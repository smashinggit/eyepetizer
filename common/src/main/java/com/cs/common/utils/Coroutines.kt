package com.cs.common.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit = {}
) {
    viewModelScope.launch(context = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable)
    }) {
        try {
            block()
        } finally {
            onComplete()
        }
    }
}