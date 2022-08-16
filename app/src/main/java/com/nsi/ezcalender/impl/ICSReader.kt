package com.nsi.ezcalender.impl

import android.os.Environment
import com.nsi.ezcalender.model.Event
//import biweekly.Biweekly
//import biweekly.ICalendar
//import biweekly.component.VEvent
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.data.UnfoldingReader
import net.fortuna.ical4j.model.Component.VEVENT
import net.fortuna.ical4j.model.component.VEvent
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.StringReader
import java.util.*


//https://www.webdavsystem.com/server/creating_caldav_carddav/calendar_ics_file_structure/




class ICSReader {

    private val events = mutableListOf<Event>()

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


}