package com.nsi.ezcalender.model

import net.fortuna.ical4j.model.property.DtStart
import net.fortuna.ical4j.model.property.Uid
import java.time.LocalDateTime


/**
 * summary
 * year
 * date start
 * time
 * location
 *
 *
 * geo
 * link
 * recurrence: frequency, interval
 * date end or duration
 */

data class Event(
    var uid: Uid? = null,
    var summary: String = "",
//    var dtStart: String = "",
    var dtStart: LocalDateTime? = null,
    var dtEnd: LocalDateTime? = null,
    var location: String = "",
    var geo: String? = null,


//    var rRule: String = "RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTH=2;BYMONTHDAY=12",
//    var dtEnd: String = "20080213",
//    var geo: String = "37.5739497;-85.7399606",
//    var description: String = "Born February 12, 1809 Sixteenth President (1861-1865) \n http://AmericanHistoryCalendar.com",
//    var url: String = "http://americanhistorycalendar.com/peoplecalendar/1,328-abraham-lincol",

)