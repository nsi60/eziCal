package com.nsi.ezcalender

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsi.ezcalender.impl.ICSReader
import com.nsi.ezcalender.impl.ICSReaderResult
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.model.SortOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import javax.inject.Inject


data class State(
    val isLoading: Boolean = false,
    val error: ICSReaderResult.Error? = null,
    val eventsList: List<Event> = emptyList<Event>(),
    var createdEventsList: SnapshotStateList<Event> = mutableStateListOf(),
    val selectedFileInputStream: InputStream? = null,
    val icsFileUrl: String? = "https://timetable.canterbury.ac.nz/even/rest/calendar/ical/24208ac0-d4dc-488d-8919-f000e870154d",
    val sortOptions: SortOptions = SortOptions.DATE_START
)

@HiltViewModel
class MainViewModel @Inject constructor(private val icsReader: ICSReader) : ViewModel() {

    private val _state = mutableStateOf(State())
    val state: MutableState<State> get() = _state

    suspend fun fetchSomeData() {
        delay(3000L)
    }

    fun readSelectedFile() {
        val result = icsReader.readInputStream(state.value.selectedFileInputStream)
        processResult(result)


    }

    fun setSelectedFileInputStream(inputStream: InputStream?) {
        _state.value = state.value.copy(selectedFileInputStream = inputStream)
    }

    private fun sortEvents() {
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
            val result = icsReader.openFromUrl2(state.value.icsFileUrl)
            processResult(result)

        }
    }

    fun saveCreatedEvent(event: Event) {
        viewModelScope.launch {
            val uid = icsReader.generateUid()
            event.uid = uid
            _state.value.createdEventsList.add(event)
//            _state.value.createdEventsList.sortBy { it.dtStart } //Todo if we want to sort
        }
    }


    fun deleteEvent(event: Event) {
        _state.value.createdEventsList.remove(event)
    }

    fun exportCreatedEvents(filesDir: File) {
        val result = icsReader.createCalender(state.value.createdEventsList.toList(), filesDir)
        processResult(result)

    }

    private fun processResult(result: ICSReaderResult) {

        if (result is ICSReaderResult.Success) {
            val events = result.data.toMutableList()
            _state.value = state.value.copy(eventsList = events, error = null, isLoading = false)
            sort(SortOptions.DATE_START)
        } else if (result is ICSReaderResult.Error) {
            _state.value = state.value.copy(error = result, isLoading = false)
        }
    }

    fun clearCreatedEvents() {
        _state.value.createdEventsList.clear()
    }

}

