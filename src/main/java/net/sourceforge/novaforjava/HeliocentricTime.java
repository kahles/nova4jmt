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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static net.sourceforge.novaforjava.Nutation.ln_get_nutation;
import static net.sourceforge.novaforjava.Utility.ln_deg_to_rad;
import static net.sourceforge.novaforjava.Utility.ln_range_degrees;
import static net.sourceforge.novaforjava.solarsystem.Earth.ln_get_earth_helio_coords;
import net.sourceforge.novaforjava.api.LnEquPosn;
import net.sourceforge.novaforjava.api.LnHelioPosn;
import net.sourceforge.novaforjava.api.LnNutation;

public class HeliocentricTime {

	/**
	 * double ln_get_heliocentric_time_diff(double JD, struct ln_equ_posn
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
		LnNutation nutation = new LnNutation();
		LnHelioPosn earth = new LnHelioPosn();

		ln_get_nutation(JD, nutation);
		ln_get_earth_helio_coords(JD, earth);

		theta = ln_deg_to_rad(ln_range_degrees(earth.L + 180));
		ra = ln_deg_to_rad(object.ra);
		dec = ln_deg_to_rad(object.dec);
		c_dec = cos(dec);
		obliq = ln_deg_to_rad(nutation.ecliptic);

		/**
		 * L.Binnendijk Properties of Double Stars, Philadelphia, University of
		 * Pennselvania Press, pp. 228-232, 1960
		 */
		return -0.0057755
				* earth.R
				* (cos(theta) * cos(ra) * c_dec + sin(theta)
						* (sin(obliq) * sin(dec) + cos(obliq) * c_dec * sin(ra)));
	}
}
