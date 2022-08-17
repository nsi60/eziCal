package com.nsi.ezcalender.model


/**
 * year
 * date start
 * date end or duration
 * time
 * summary
 * location
 * time
 * geo
 * link
 * recurrence: frequency, interval
 *
 */

data class Event(
    var summary: String,
    var rRule: String = "RRULE:FREQ=YEARLY;INTERVAL=1;BYMONTH=2;BYMONTHDAY=12",
    var dtStart: String = "20080212",
    var dtEnd: String = "20080213",
    var location: String? = "Hodgenville, Kentucky",
    var geo: String = "37.5739497;-85.7399606",
    var description: String = "Born February 12, 1809 Sixteenth President (1861-1865) \n http://AmericanHistoryCalendar.com",
    var url: String = "http://americanhistorycalendar.com/peoplecalendar/1,328-abraham-lincol",

)