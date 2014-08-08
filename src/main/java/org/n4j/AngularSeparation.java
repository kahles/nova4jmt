package org.n4j;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_rad_to_deg;

import org.n4j.api.LnEquPosn;

public class AngularSeparation {

	/*
	 * ! \fn double ln_get_angular_separation(LnEquPosn posn1, LnEquPosn posn2);
	 * \param posn1 Equatorial position of body 1 \param posn2 Equatorial
	 * position of body 2 \return Angular separation in degrees
	 * 
	 * Calculates the angular separation of 2 bodies. This method was devised by
	 * Mr Thierry Pauwels of the Royal Observatory Belgium.
	 */
	/* Chap 17 page 115 */
	public static double ln_get_angular_separation(LnEquPosn posn1,
			LnEquPosn posn2) {
		double d;
		double x, y, z;
		double a1, a2, d1, d2;

		/* covert to radians */
		a1 = ln_deg_to_rad(posn1.ra);
		d1 = ln_deg_to_rad(posn1.dec);
		a2 = ln_deg_to_rad(posn2.ra);
		d2 = ln_deg_to_rad(posn2.dec);

		x = (cos(d1) * sin(d2)) - (sin(d1) * cos(d2) * cos(a2 - a1));
		y = cos(d2) * sin(a2 - a1);
		z = (sin(d1) * sin(d2)) + (cos(d1) * cos(d2) * cos(a2 - a1));

		x = x * x;
		y = y * y;
		d = atan2(sqrt(x + y), z);

		return ln_rad_to_deg(d);
	}

	/*
	 * ! \fn double ln_get_rel_posn_angle(LnEquPosn posn1, LnEquPosn posn2);
	 * \param posn1 Equatorial position of body 1 \param posn2 Equatorial
	 * position of body 2 \return Position angle in degrees
	 * 
	 * Calculates the position angle of a body with respect to another body.
	 */
	/* Chapt 17, page 116 */
	public static double ln_get_rel_posn_angle(LnEquPosn posn1, LnEquPosn posn2) {
		double P;
		double a1, a2, d1, d2;
		double x, y;

		/* covert to radians */
		a1 = ln_deg_to_rad(posn1.ra);
		d1 = ln_deg_to_rad(posn1.dec);
		a2 = ln_deg_to_rad(posn2.ra);
		d2 = ln_deg_to_rad(posn2.dec);

		y = sin(a1 - a2);
		x = (cos(d2) * tan(d1)) - (sin(d2) * cos(a1 - a2));

		P = atan2(y, x);
		return ln_rad_to_deg(P);
	}

}
