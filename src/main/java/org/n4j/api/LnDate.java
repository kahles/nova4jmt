package org.n4j.api;

/**
 ** Date \struct ln_date \brief Human readable Date and time used by libnova This
 * is the Human readable (easy printf) date format used by libnova. It's always
 * in UTC. For local time, please use ln_zonedate.
 */
public class LnDate {

	/** Years. All values are valid */
	public int years;

	/** Months. Valid values : 1 (January) - 12 (December) */
	public int months;

	/** Days. Valid values 1 - 28,29,30,31 Depends on month. */
	public int days;

	/** Hours. Valid values 0 - 23. */
	public int hours;

	/** Minutes. Valid values 0 - 59. */
	public int minutes;

	/** Seconds. Valid values 0 - 59.99999.... */
	public double seconds;

	public LnDate copy() {
		LnDate result = new LnDate();
		result.years = years;
		result.months = months;
		result.days = days;
		result.hours = hours;
		result.minutes = minutes;
		result.seconds = seconds;
		return result;
	}
}
