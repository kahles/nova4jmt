package org.n4j;

import static org.n4j.Aberration.ln_get_equ_aber;
import static org.n4j.Precession.ln_get_equ_prec;
import static org.n4j.ProperMotion.ln_get_equ_pm;

import org.n4j.api.LnEquPosn;

public class ApparentPosition {
	/*
	 * * Apparent place of an Object
	 */

	/*
	 * ! \fn void ln_get_apparent_posn(struct ln_equ_posn *mean_position, struct
	 * ln_equ_posn *proper_motion, double JD, struct ln_equ_posn *position)
	 * \param mean_position Mean position of object \param proper_motion Proper
	 * motion of object \param JD Julian Day \param position Pointer to store
	 * new object position
	 * 
	 * Calculate the apparent equatorial position of a star from its mean
	 * equatorial position. This function takes into account the effects of
	 * proper motion, precession, nutation, annual aberration when calculating
	 * the stars apparent position. The effects of annual parallax and the
	 * gravitational deflection of light (Einstein effect) are NOT used in this
	 * calculation.
	 */
	public static void ln_get_apparent_posn(LnEquPosn mean_position,
			LnEquPosn proper_motion, double JD, LnEquPosn position) {
		LnEquPosn proper_position = new LnEquPosn();
		LnEquPosn aberration_position = new LnEquPosn();

		ln_get_equ_pm(mean_position, proper_motion, JD, proper_position);
		ln_get_equ_aber(proper_position, JD, aberration_position);
		ln_get_equ_prec(aberration_position, JD, position);
	}
}
