package com.nsi.ezcalender.impl

//import biweekly.Biweekly
//import biweekly.ICalendar
//import biweekly.component.VEvent

import android.net.Uri
import android.os.Environment
import com.nsi.ezcalender.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.data.CalendarOutputter
import net.fortuna.ical4j.data.UnfoldingReader
import net.fortuna.ical4j.model.Calendar
import net.fortuna.ical4j.model.Component.VEVENT
import net.fortuna.ical4j.model.DateTime
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.ValidationException
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.property.*
import net.fortuna.ical4j.util.UidGenerator
import java.io.*
import java.net.URI
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


//https://www.webdavsystem.com/server/creating_caldav_carddav/calendar_ics_file_structure/
//http://ical4j.sourceforge.net/introduction.html

class ICSReader @Inject constructor() {

    private lateinit var events: MutableList<Event>
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
        return events
    }

    fun readSelectedFile(inputStream: InputStream?) {
//        val fin = UnfoldingReader(  //TODO to open using file path
//            FileReader(selectedFile),
//            true
//        )

        val builder = CalendarBuilder()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
        events = mutableListOf<Event>()

        val calendar = builder.build(inputStream)
        val events = calendar.components.getComponents(VEVENT)
        for (event in events) {
            val nE: VEvent = event as VEvent

            val eDateStartString = nE.startDate.value
            val localDateTimeDtStart = LocalDateTime.parse(eDateStartString, dateFormatter)

            val eDateEndString = nE.endDate.value
            val localDateTimeDtEnd = LocalDateTime.parse(eDateEndString, dateFormatter)

            val event = Event(
                uid = nE.uid,
                summary = nE.summary.value,
                dtStart = localDateTimeDtStart,
                dtEnd = localDateTimeDtEnd,
                location = nE.location.value,
                geo = nE.geographicPos?.value ?: "0;0", //TODO is this ok?
            )
            this.events.add(event)

        }
    }


    suspend fun openFromUrl2(inputUrl: String?) =
        withContext(Dispatchers.IO) {
            val url = URL(inputUrl)
            val inputStream = url.openStream()
            readSelectedFile(inputStream)
        }

    fun createCalender(createdEventsList: List<Event>, filesDir: File) {
        val icsCalendar = Calendar()
        icsCalendar.properties.add(ProdId("-//Ben Fortuna//iCal4j 1.0//EN"))
        icsCalendar.properties.add(Version.VERSION_2_0)
        icsCalendar.properties.add(CalScale.GREGORIAN)


        // Create a TimeZone
        val registry = TimeZoneRegistryFactory.getInstance().createRegistry()
        val timezone = registry.getTimeZone(TimeZone.getDefault().id)
        val tz = timezone.vTimeZone

        val startDate = GregorianCalendar()
        startDate.timeZone = timezone
        val endDate = GregorianCalendar()
        endDate.timeZone = timezone


        // Add events, etc..
        for (event in createdEventsList) {

            // Start Date
            startDate[java.util.Calendar.MONTH] = event.dtStart?.monthValue!!
            startDate[java.util.Calendar.DAY_OF_MONTH] = event.dtStart.dayOfMonth
            startDate[java.util.Calendar.YEAR] = event.dtStart.year
            startDate[java.util.Calendar.HOUR_OF_DAY] = event.dtStart.hour
            startDate[java.util.Calendar.MINUTE] = event.dtStart.minute
            startDate[java.util.Calendar.SECOND] = 0

            // END Date
            endDate[java.util.Calendar.MONTH] = event.dtEnd?.monthValue!!
            endDate[java.util.Calendar.DAY_OF_MONTH] = event.dtEnd.dayOfMonth
            endDate[java.util.Calendar.YEAR] = event.dtEnd.year
            endDate[java.util.Calendar.HOUR_OF_DAY] = event.dtEnd.hour
            endDate[java.util.Calendar.MINUTE] = event.dtEnd.minute
            endDate[java.util.Calendar.SECOND] = 0

            // Create the event
            val vEvent = VEvent(DateTime(startDate.time), DateTime(endDate.time), event.summary)

            //add timezone
            vEvent.properties.add(tz.timeZoneId)


            // add unique identifier
//            val ug = UidGenerator("uidGen")
//            val uid = ug.generateUid()
            vEvent.properties.add(event.uid)

//            // add unique identifier..
//            vEvent.properties.getProperty(Property.LOCATION).value = "event.location"

            //add location to event
            vEvent.properties.add(Location(event.location))

            //add geo to event
            vEvent.properties.add(Geo(event.geo))

            //add url to event
            vEvent.properties.add(Url(URI(event.url)))

            // Add the event and print
            icsCalendar.components.add(vEvent)
        }
        saveIcsfile(filesDir = filesDir, icsCalendar = icsCalendar)

    }

    private fun saveIcsfile(filesDir: File, filename: String = "myIcs.ics", icsCalendar: Calendar) {
        val file = File(filesDir, filename) //context.getFilesDir()  //TODO is this ok?

        var fout: FileOutputStream? = null
        try {
            fout = FileOutputStream(file)
            val outputter = CalendarOutputter()
            outputter.output(icsCalendar, fout)  //TODO doesnt show up in files.
            readSelectedFile(FileInputStream(file))  //TODO if it shows up in files, this can be done manually.

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: ValidationException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fout != null) {
                try {
                    fout.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    suspend fun generateUid(): Uid? =
        withContext(Dispatchers.Default) {
            val uidGenerator = UidGenerator("1")
            uidGenerator.generateUid()
        }


}


