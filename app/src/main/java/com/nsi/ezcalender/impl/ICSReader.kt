package com.nsi.ezcalender.impl

//import biweekly.Biweekly
//import biweekly.ICalendar
//import biweekly.component.VEvent
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import com.nsi.ezcalender.model.Event
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.data.UnfoldingReader
import net.fortuna.ical4j.model.Component.VEVENT
import net.fortuna.ical4j.model.Property.URL
import net.fortuna.ical4j.model.component.VEvent
import java.io.*
import java.net.URL
import java.util.*
import javax.inject.Inject


//https://www.webdavsystem.com/server/creating_caldav_carddav/calendar_ics_file_structure/


const val PICK_ICS_FILE = 2

class ICSReader @Inject constructor() {

    private val events = mutableListOf<Event>()
    private val initialFileOpenUri =
        Uri.parse(Environment.getExternalStorageDirectory().absolutePath + "/Download/")

    var string = """
        BEGIN:VCALENDAR
        VERSION:2.0
        PRODID:-//Microsoft Corporation//Outlook 14.0 MIMEDIR//EN
        
        BEGIN:VEVENT
        UID:0123
        DTSTAMP:20130601T080000Z
        SUMMARY;LANGUAGE=en-us:Team Meeting
        DTSTART:20130610T120000Z
        DURATION:PT1H
        RRULE:FREQ=WEEKLY;INTERVAL=2
        END:VEVENT
        
        BEGIN:VEVENT
        UID:0123
        DTSTAMP:20130601T080000Z
        SUMMARY;LANGUAGE=en-us:Team Meeting
        DTSTART:20130610T120000Z
        DURATION:PT1H
        RRULE:FREQ=WEEKLY;INTERVAL=2
        END:VEVENT
        
        BEGIN:VEVENT
        UID:0123
        DTSTAMP:20130601T080000Z
        SUMMARY;LANGUAGE=en-us:Team Meeting
        DTSTART:20130610T120000Z
        DURATION:PT1H
        RRULE:FREQ=WEEKLY;INTERVAL=2
        END:VEVENT
        
       
        
        END:VCALENDAR
        
        """.trimIndent()


    fun readIcs3() {
        val directory =
            Environment.getExternalStorageDirectory().absolutePath + "/Download/Ics_file.ics"
        val dir = File(directory)
        println(dir.name)
        println(dir.exists())

        if (dir.name.lowercase(Locale.getDefault()).endsWith("ics")) {

            val fin = UnfoldingReader(
                FileReader(dir),
                true
            )

            val builder = CalendarBuilder()
            val calendar = builder.build(fin)
            val events = calendar.components.getComponents(VEVENT)
            for (event in events) {
                val nE: VEvent = event as VEvent

                val eventSummary = nE.summary.toString()
                val eventLocation = nE.location.toString()
                this.events.add(Event(summary = eventSummary, location = eventLocation))
            }


        }


    }


    fun getEvents(): MutableList<Event> {
        println(events)
        return events
    }

    fun readSelectedFile(inputStream: InputStream?,selectedFile: File? = null) {
//        val fin = UnfoldingReader(
//            FileReader(selectedFile),
//            true
//        )

//        val inputStream = URL("http://airbnb.com/ical/").openStream() //TODO from URL
//        try {
//            val cal = CalendarBuilder().build(inputStream)
//        } finally {
//            inputStream.close()
//        }

        val builder = CalendarBuilder()


        val calendar = builder.build(inputStream)
        val events = calendar.components.getComponents(VEVENT)
        for (event in events) {
            val nE: VEvent = event as VEvent

            val eventSummary = nE.summary.value
            val eventLocation = nE.location.value
            this.events.add(Event(summary = eventSummary, location = eventLocation))
        }
    }


}


