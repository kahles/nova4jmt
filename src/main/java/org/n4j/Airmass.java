package org.n4j;

import static java.lang.Math.asin;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_rad_to_deg;

public class Airmass {

	/*
	 * * Airmass
	 */

	/*
	 * ! \fn double ln_get_airmass (double alt, double airmass_scale) \param alt
	 * Altitude in degrees \param airmass_scale Airmass scale - usually 750.
	 * \return Airmass for give altitude.
	 */
	public static double ln_get_airmass(double alt, double airmass_scale) {
		double a;

		a = airmass_scale * sin(ln_deg_to_rad(alt));
		return sqrt(a * a + 2 * airmass_scale + 1) - a;
	}

	/*
	 * ! \fn double ln_get_alt_from_airmass (double X, double airmass_scale)
	 * \param X Airmass \param airmass_scale Airmass scale - usually 750.
	 * \return Altitude for give airmass.
	 */
	public static double ln_get_alt_from_airmass(double X, double airmass_scale) {
		return ln_rad_to_deg(asin((2 * airmass_scale + 1 - X * X)
				/ (2 * X * airmass_scale)));
	}

}
