package org.n4j;

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

import static java.lang.Math.asin;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_rad_to_deg;

public class Airmass {

	/**
	 * * Airmass
	 */

	/**
	 * double ln_get_airmass (double alt, double airmass_scale) \param alt
	 * Altitude in degrees \param airmass_scale Airmass scale - usually 750.
	 * \return Airmass for give altitude.
	 */
	public static double ln_get_airmass(double alt, double airmass_scale) {
		double a;

		a = airmass_scale * sin(ln_deg_to_rad(alt));
		return sqrt(a * a + 2 * airmass_scale + 1) - a;
	}

	/**
	 * double ln_get_alt_from_airmass (double X, double airmass_scale) \param X
	 * Airmass \param airmass_scale Airmass scale - usually 750. \return
	 * Altitude for give airmass.
	 */
	public static double ln_get_alt_from_airmass(double X, double airmass_scale) {
		return ln_rad_to_deg(asin((2 * airmass_scale + 1 - X * X)
				/ (2 * X * airmass_scale)));
	}

}
