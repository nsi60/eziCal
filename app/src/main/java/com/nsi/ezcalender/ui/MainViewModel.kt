package com.nsi.ezcalender.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi.ezcalender.impl.ICSReader
import com.nsi.ezcalender.model.Event
import kotlinx.coroutines.launch
import net.fortuna.ical4j.model.ComponentList


data class State(
    val isLoading: Boolean = false,
    val eventsList: List<Event> = emptyList<Event>(),
    val componentsList: ComponentList? = null
)

class MainViewModel(val icsReader: ICSReader = ICSReader()): ViewModel() {

    private val _state = mutableStateOf(State())
    val state : MutableState<State> get() = _state


    fun getEvents() {
        val events = icsReader.getEvents()
        _state.value = state.value.copy(eventsList = events)

    }

    fun readIcs_3() {
        icsReader.readIcs3()
        val events = icsReader.getEvents()
        _state.value = state.value.copy(eventsList = events)
    }

    fun openIcsFile() {
        TODO("Not yet implemented")
    }

}

