package org.n4j;

import org.n4j.api.LnEquPosn;
import org.n4j.api.LnHelioPosn;
import org.n4j.api.LnNutation;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static org.n4j.Nutation.ln_get_nutation;
import static org.n4j.Utility.ln_range_degrees;
import static org.n4j.Utility.ln_deg_to_rad;

public class HeliocentricTime {

	/*
	 * ! \fn double ln_get_heliocentric_time_diff(double JD, struct ln_equ_posn
	 * *object) \param JD Julian day \param object Pointer to object (RA, DEC)
	 * for which heliocentric correction will be caculated
	 * 
	 * \return Heliocentric correction in fraction of day
	 * 
	 * Calculate heliocentric corection for object at given coordinates and on
	 * given date.
	 */
	public static double ln_get_heliocentric_time_diff(double JD,
			LnEquPosn object) {
		double theta, ra, dec, c_dec, obliq;
		LnNutation nutation;
		LnHelioPosn earth;

		ln_get_nutation(JD, nutation);
		ln_get_earth_helio_coords(JD, earth);

		theta = ln_deg_to_rad(ln_range_degrees(earth.L + 180));
		ra = ln_deg_to_rad(object.ra);
		dec = ln_deg_to_rad(object.dec);
		c_dec = cos(dec);
		obliq = ln_deg_to_rad(nutation.ecliptic);

		/*
		 * L.Binnendijk Properties of Double Stars, Philadelphia, University of
		 * Pennselvania Press, pp. 228-232, 1960
		 */
		return -0.0057755
				* earth.R
				* (cos(theta) * cos(ra) * c_dec + sin(theta)
						* (sin(obliq) * sin(dec) + cos(obliq) * c_dec * sin(ra)));
	}
}
