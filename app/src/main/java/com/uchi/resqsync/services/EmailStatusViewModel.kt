package com.uchi.resqsync.services

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.Serializable

class EmailStatusViewModel: ViewModel(), Serializable {
    private val data = MutableStateFlow<Boolean>(false)

    fun getData(): StateFlow<Boolean> {
        return data
    }

    fun updateData(newData: Boolean) {
        data.value = newData
    }
}

