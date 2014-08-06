package org.n4j;

import static org.n4j.Nutation.ln_get_nutation;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_range_degrees;

import org.n4j.api.LnNutation;

public class SiderealTime {

	/*
	 * ! \fn double ln_get_mean_sidereal_time(double JD) \param JD Julian Day
	 * \return Mean sidereal time.
	 * 
	 * Calculate the mean sidereal time at the meridian of Greenwich of a given
	 * date.
	 */
	/*
	 * Formula 11.1, 11.4 pg 83
	 */

	public static double ln_get_mean_sidereal_time(double JD) {
		// TODO long double
		double sidereal;
		double T;

		T = (JD - 2451545.0) / 36525.0;

		/* calc mean angle */
		sidereal = 280.46061837 + (360.98564736629 * (JD - 2451545.0))
				+ (0.000387933 * T * T) - (T * T * T / 38710000.0);

		/* add a convenient multiple of 360 degrees */
		sidereal = ln_range_degrees(sidereal);

		/* change to hours */
		sidereal *= 24.0 / 360.0;

		return sidereal;
	}

	/*
	 * ! \fn double ln_get_apparent_sidereal_time(double JD) \param JD Julian
	 * Day /return Apparent sidereal time (hours).
	 * 
	 * Calculate the apparent sidereal time at the meridian of Greenwich of a
	 * given date.
	 */
	/*
	 * Formula 11.1, 11.4 pg 83
	 */

	public static double ln_get_apparent_sidereal_time(double JD) {
		double correction, sidereal;
		LnNutation nutation = new LnNutation();

		/* get the mean sidereal time */
		sidereal = ln_get_mean_sidereal_time(JD);

		/*
		 * add corrections for nutation in longitude and for the true obliquity
		 * of the ecliptic
		 */
		ln_get_nutation(JD, nutation);

		correction = (nutation.longitude / 15.0 * Math
				.cos(ln_deg_to_rad(nutation.obliquity)));

		sidereal += correction;

		return sidereal;
	}

}
