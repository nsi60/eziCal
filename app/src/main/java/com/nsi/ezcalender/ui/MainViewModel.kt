package com.nsi.ezcalender.ui

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.nsi.ezcalender.impl.ICSReader
import com.nsi.ezcalender.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import net.fortuna.ical4j.model.ComponentList
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.inject.Inject


data class State(
    val isLoading: Boolean = false,
    val eventsList: List<Event> = emptyList<Event>(),
    val selectedFileInputStream: InputStream? = null,
    )

@HiltViewModel
class MainViewModel @Inject constructor(private val icsReader: ICSReader) : ViewModel() {

    private val _state = mutableStateOf(State())
    val state: MutableState<State> get() = _state


    fun readSelectedFile() {
        icsReader.readSelectedFile(state.value.selectedFileInputStream)
        val events = icsReader.getEvents()
        _state.value = state.value.copy(eventsList = events)
    }

    fun setSelectedFileInputStream(inputStream: InputStream?) {
        _state.value = state.value.copy(selectedFileInputStream = inputStream)
    }


}

