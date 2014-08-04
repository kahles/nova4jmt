package org.n4j.api;

/**
 ** Date \struct ln_date \brief Human readable Date and time used by libnova This
 * is the Human readable (easy printf) date format used by libnova. It's always
 * in UTC. For local time, please use ln_zonedate.
 */
public class LnDate {

    /** Years. All values are valid */
    int years;

    /** Months. Valid values : 1 (January) - 12 (December) */
    int months;

    /** Days. Valid values 1 - 28,29,30,31 Depends on month. */
    int days;

    /** Hours. Valid values 0 - 23. */
    int hours;

    /** Minutes. Valid values 0 - 59. */
    int minutes;

    /** Seconds. Valid values 0 - 59.99999.... */
    double seconds;
}
