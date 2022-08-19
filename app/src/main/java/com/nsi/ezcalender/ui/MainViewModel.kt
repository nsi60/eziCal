package com.nsi.ezcalender.ui

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi.ezcalender.impl.ICSReader
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.model.SortOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.fortuna.ical4j.model.ComponentList
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.inject.Inject


data class State(
    val isLoading: Boolean = false,
    val eventsList: List<Event> = emptyList<Event>(),
    var createdEventsList: MutableList<Event> = mutableListOf(),
    val selectedFileInputStream: InputStream? = null,
    val icsFileUrl: String? = "https://timetable.canterbury.ac.nz/even/rest/calendar/ical/24208ac0-d4dc-488d-8919-f000e870154d",
    val sortOptions: SortOptions = SortOptions.DATE_START
)

@HiltViewModel
class MainViewModel @Inject constructor(private val icsReader: ICSReader) : ViewModel() {

    private val _state = mutableStateOf(State())
    val state: MutableState<State> get() = _state


    fun readSelectedFile() {
        icsReader.readSelectedFile(state.value.selectedFileInputStream)
        val events = icsReader.getEvents()
        events.sortBy { it.dtStart }
        _state.value = state.value.copy(eventsList = events)
    }

    fun setSelectedFileInputStream(inputStream: InputStream?) {
        _state.value = state.value.copy(selectedFileInputStream = inputStream)
    }

    fun sortEvents() {
        val events = state.value.eventsList.toMutableList()
        when (state.value.sortOptions) {
            SortOptions.DATE_START -> {
                events.sortBy { it.dtStart }
            }
            SortOptions.SUMMARY_NAME -> {
                events.sortBy { it.summary }
            }
            SortOptions.LOCATION_NAME -> {
                events.sortBy { it.location }
            }
        }
        _state.value = state.value.copy(eventsList = events)
    }

    fun sort(sortOption: SortOptions) {
        _state.value = state.value.copy(sortOptions = sortOption)
        sortEvents()
    }

    fun setInputUrl(url: String) {
        _state.value = state.value.copy(icsFileUrl = url)
    }

    fun openFromUrl() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            icsReader.openFromUrl2(state.value.icsFileUrl)
            val events = icsReader.getEvents()
            events.sortBy { it.dtStart }
            _state.value = state.value.copy(eventsList = events)
            _state.value = state.value.copy(isLoading = false)

        }
    }

    fun saveCreatedEvent(event: Event) {
        _state.value.createdEventsList.add(event)
//        _state.value = state.value.copy(createdEventsList = events)

    }

}

