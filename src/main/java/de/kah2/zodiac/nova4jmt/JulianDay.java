package de.kah2.zodiac.nova4jmt;

/*
 * #%L
 * libnova for Java
 * %%
 * Copyright (C) 2014 novaforjava
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.Calendar;
import java.util.TimeZone;

import de.kah2.zodiac.nova4jmt.api.LnDate;
import de.kah2.zodiac.nova4jmt.api.LnZoneDate;

public class JulianDay {

	/**
	 * double ln_get_julian_day(LnDate *date) \param date Date required. \return
	 * Julian day
	 * 
	 * Calculate the julian day from a calendar day. Valid for positive and
	 * negative years but not for negative JD.
	 */
	/**
	 * Formula 7.1 on pg 61
	 */
	public static double ln_get_julian_day(LnDate date) {
		double JD;
		double days;
		int a, b;
		/** create local copy */
		LnDate local_date = date.copy();

		/** check for month = January or February */
		if (local_date.months < 3) {
			local_date.years--;
			local_date.months += 12;
		}

		a = local_date.years / 100;

		/** check for Julian or Gregorian calendar (starts Oct 4th 1582) */
		if (local_date.years > 1582
				|| (local_date.years == 1582 && (local_date.months > 10 || (local_date.months == 10 && local_date.days >= 4)))) {
			/** Gregorian calendar */
			b = 2 - a + (a / 4);
		} else {
			/** Julian calendar */
			b = 0;
		}

		/** add a fraction of hours, minutes and secs to days */
		days = local_date.days + local_date.hours / 24.0 + local_date.minutes
				/ 1440.0 + local_date.seconds / 86400.0;

		/** now get the JD */
		JD = (int) (365.25 * (local_date.years + 4716))
				+ (int) (30.6001 * (local_date.months + 1)) + days + b - 1524.5;

		return JD;
	}

	/**
	 * unsigned int ln_get_day_of_week(LnDate *date) \param date Date required
	 * \return Day of the week
	 * 
	 * Calculate the day of the week. Returns 0 = Sunday .. 6 = Saturday
	 */
	public static int ln_get_day_of_week(LnDate date) {
		int day;
		double JD;

		/** get julian day */
		JD = ln_get_julian_day(date);
		JD += 1.5;
		day = (int) JD % 7;

		return day;
	}

	/**
	 * void ln_get_date(double JD, LnDate *date) \param JD Julian day \param
	 * date Pointer to new calendar date.
	 * 
	 * Calculate the date from the Julian day
	 */
	public static void ln_get_date(double JD, LnDate date) {
		int A, a, B, C, D, E;
		double F, Z;

		JD += 0.5;
		Z = (int) JD;
		F = JD - Z;

		if (Z < 2299161d)
			A = (int) Z;
		else {
			a = (int) ((Z - 1867216.25) / 36524.25);
			A = (int) (Z + 1 + a - a / 4);
		}

		B = A + 1524;
		C = (int) ((B - 122.1) / 365.25);
		D = (int) (365.25 * C);
		E = (int) ((B - D) / 30.6001);

		/** get the hms */
		date.hours = (int) (F * 24);
		F -= (double) date.hours / 24;
		date.minutes = (int) (F * 1440);
		F -= (double) date.minutes / 1440;
		date.seconds = F * 86400;

		/** get the day */
		date.days = B - D - (int) (30.6001 * E);

		/** get the month */
		if (E < 14)
			date.months = E - 1;
		else
			date.months = E - 13;

		/** get the year */
		if (date.months > 2)
			date.years = C - 4716;
		else
			date.years = C - 4715;
	}

	/**
	 * void ln_get_date_from_sys(LnDate *date) \param date Pointer to store
	 * date.
	 * 
	 * Calculate local date from system date.
	 */
	public static void ln_get_date_from_sys(LnDate date) {
		long now = System.currentTimeMillis();
		ln_get_date_from_UTC_milliseconds(date, now);
	}

	/**
	 * Calculate date from a java calendar.
	 */
	public static void ln_get_date_from_cal(LnDate date, Calendar calendar) {
		ln_get_date_from_UTC_milliseconds(date, calendar.getTimeInMillis());
	}

	/**
	 * Calculate date from java UTC millisecunds in EPOCH.
	 */
	public static void ln_get_date_from_UTC_milliseconds(LnDate date, long now) {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("UTC"));
		c.setTimeInMillis(now);
		c.getTimeInMillis(); // force calculation

		/** fill in date struct */
		date.seconds = (c.get(Calendar.SECOND)) + (c.get(Calendar.MILLISECOND))
				/ 1000d;
		date.minutes = c.get(Calendar.MINUTE);
		date.hours = c.get(Calendar.HOUR_OF_DAY);
		date.days = c.get(Calendar.DAY_OF_MONTH);
		date.months = c.get(Calendar.MONTH) + 1;// 0 based in calendar
		date.years = c.get(Calendar.YEAR);
	}

	/**
	 * double ln_get_julian_from_sys() \return Julian day (UT)
	 * 
	 * Calculate the julian day (UT) from the local system time
	 */
	public static double ln_get_julian_from_sys() {
		double JD;
		LnDate date = new LnDate();

		/** get sys date */
		ln_get_date_from_sys(date);
		JD = ln_get_julian_day(date);

		return JD;
	}

	/**
	 * int ln_get_date_from_mpc(LnDate *date, char *mpc_date) \param date
	 * Pointer to new calendar date. \param mpc_date Pointer to string MPC date
	 * \return 0 for valid date
	 * 
	 * Calculate the local date from the a MPC packed date. See
	 * http://cfa-www.harvard.edu/iau/info/PackedDates.html for info.
	 */
	public static int ln_get_date_from_mpc(LnDate date, String mpc_date) {
		/** is mpc_date correct length */
		if (mpc_date.length() != 5)
			return -1;

		/** get the century */
		switch (mpc_date.charAt(0)) {
		case 'I':
			date.years = 1800;
			break;
		case 'J':
			date.years = 1900;
			break;
		case 'K':
			date.years = 2000;
			break;
		default:
			return -1;
		}

		/** get the year */
		date.years += Integer.parseInt(mpc_date.substring(1, 3));

		/** month */
		date.months = Integer.parseInt(mpc_date.substring(3, 4), 16);

		/** day */
		date.days = date.months = Integer
				.parseInt(mpc_date.substring(4, 5), 31);
		;

		/** reset hours,min,secs to 0 */
		date.hours = 0;
		date.minutes = 0;
		date.seconds = 0;
		return 0;
	}

	/**
	 * double ln_get_julian_from_mpc (char *mpc_date) \param mpc_date Pointer to
	 * string MPC date \return Julian day.
	 * 
	 * Calculate the julian day from the a MPC packed date. See
	 * http://cfa-www.harvard.edu/iau/info/PackedDates.html for info.
	 */
	public static double ln_get_julian_from_mpc(String mpc_date) {
		LnDate date = new LnDate();
		double JD;

		ln_get_date_from_mpc(date, mpc_date);
		JD = ln_get_julian_day(date);

		return JD;
	}

	/**
	 * void ln_date_to_zonedate(LnDate *date, LnZonedate *zonedate, long gmtoff)
	 * \param zonedate Ptr to zonedate \param gmtoff Offset in seconds from UT
	 * \param date Ptr to date
	 * 
	 * Converts a ln_date (UT) to a ln_zonedate (local time).
	 */
	public static void ln_date_to_zonedate(LnDate date, LnZoneDate zonedate,
			long gmtoff) {
		double jd;
		LnDate dat = new LnDate();

		jd = ln_get_julian_day(date);
		jd += gmtoff / 86400.0;
		ln_get_date(jd, dat);

		zonedate.years = dat.years;
		zonedate.months = dat.months;
		zonedate.days = dat.days;
		zonedate.hours = dat.hours;
		zonedate.minutes = dat.minutes;
		zonedate.seconds = dat.seconds;

		zonedate.gmtoff = gmtoff;
	}

	/**
	 * void ln_zonedate_to_date(LnZonedate *zonedate, LnDate *date) \param
	 * zonedate Ptr to zonedate \param date Ptr to date
	 * 
	 * Converts a ln_zonedate (local time) to a ln_date (UT).
	 */
	public static void ln_zonedate_to_date(LnZoneDate zonedate, LnDate date) {
		double jd;
		LnDate dat = new LnDate();

		dat.years = zonedate.years;
		dat.months = zonedate.months;
		dat.days = zonedate.days;
		dat.hours = zonedate.hours;
		dat.minutes = zonedate.minutes;
		dat.seconds = zonedate.seconds;

		jd = ln_get_julian_day(dat);
		jd -= zonedate.gmtoff / 86400.0;
		ln_get_date(jd, date);
	}

}
