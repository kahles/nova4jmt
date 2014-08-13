package net.sourceforge.novaforjava;

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

import static net.sourceforge.novaforjava.Nutation.ln_get_nutation;
import static net.sourceforge.novaforjava.Utility.ln_deg_to_rad;
import static net.sourceforge.novaforjava.Utility.ln_range_degrees;

import net.sourceforge.novaforjava.api.LnNutation;

public class SiderealTime {

	/**
	 * double ln_get_mean_sidereal_time(double JD) \param JD Julian Day \return
	 * Mean sidereal time.
	 * 
	 * Calculate the mean sidereal time at the meridian of Greenwich of a given
	 * date.
	 *
	 * Formula 11.1, 11.4 pg 83
	 */
	public static double ln_get_mean_sidereal_time(double JD) {
		// TODO long double
		double sidereal;
		double T;

		T = (JD - 2451545.0) / 36525.0;

		/** calc mean angle */
		sidereal = 280.46061837 + (360.98564736629 * (JD - 2451545.0))
				+ (0.000387933 * T * T) - (T * T * T / 38710000.0);

		/** add a convenient multiple of 360 degrees */
		sidereal = ln_range_degrees(sidereal);

		/** change to hours */
		sidereal *= 24.0 / 360.0;

		return sidereal;
	}

	/**
	 * double ln_get_apparent_sidereal_time(double JD) \param JD Julian Day
	 * /return Apparent sidereal time (hours).
	 * 
	 * Calculate the apparent sidereal time at the meridian of Greenwich of a
	 * given date.
	 *
	 * Formula 11.1, 11.4 pg 83
	 */
	public static double ln_get_apparent_sidereal_time(double JD) {
		double correction, sidereal;
		LnNutation nutation = new LnNutation();

		/** get the mean sidereal time */
		sidereal = ln_get_mean_sidereal_time(JD);

		/**
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
