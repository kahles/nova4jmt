package org.n4j.api;

/*!
** Zone date
* \struct ln_zonedate
* \brief Human readable Date and time with timezone information used
* by libnova
*
* This is the Human readable (easy printf) date with timezone format
* used by libnova.
*/

public class LnZoneDate {
    int years;      /*!< Years. All values are valid */
    int months;     /*!< Months. Valid values : 1 (January) - 12 (December) */
    int days;       /*!< Days. Valid values 1 - 28,29,30,31 Depends on month.*/
    int hours;      /*!< Hours. Valid values 0 - 23. */
    int minutes;    /*!< Minutes. Valid values 0 - 59. */
    double seconds; /*!< Seconds. Valid values 0 - 59.99999.... */
    long gmtoff;        /*!< Timezone offset. Seconds east of UTC. Valid values 0..86400 */
}
