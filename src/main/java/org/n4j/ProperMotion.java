package org.n4j;

import static org.n4j.Utility.ln_range_degrees;
import static org.n4j.api.Constants.JD2000;

import org.n4j.api.LnEquPosn;

public class ProperMotion {

	/*
	 * * Proper Motion.
	 */

	/*
	 * ! \fn void ln_get_equ_pm(LnEquPosn mean_position, LnEquPosn
	 * proper_motion, double JD, LnEquPosn position) \param mean_position Mean
	 * position of object. \param proper_motion Annual Proper motion of object.
	 * \param JD Julian Day. \param position Pointer to store new object
	 * position.
	 * 
	 * Calculate a stars equatorial coordinates from it's mean coordinates
	 * (J2000.0) with the effects of proper motion for a given Julian Day.
	 */
	/*
	 * Example 20.b pg 126
	 */
	public static void ln_get_equ_pm(LnEquPosn mean_position,
			LnEquPosn proper_motion, double JD, LnEquPosn position) {
		ln_get_equ_pm_epoch(mean_position, proper_motion, JD, JD2000, position);
	}

	/*
	 * ! \fn void ln_get_equ_pm_epoch(LnEquPosn mean_position, LnEquPosn
	 * proper_motion, double JD, double epoch_JD, LnEquPosn position) \param
	 * mean_position Mean position of object. \param proper_motion Annual Proper
	 * motion of object. \param JD Julian Day. \param JD_epoch Mean position
	 * epoch in JD \param position Pointer to store new object position.
	 * 
	 * Calculate a stars equatorial coordinates from it's mean coordinates and
	 * epoch with the effects of proper motion for a given Julian Day.
	 */
	/*
	 * Example 20.b, pg 126
	 */
	public static void ln_get_equ_pm_epoch(LnEquPosn mean_position,
			LnEquPosn proper_motion, double JD, double epoch_JD,
			LnEquPosn position) {
		/* long */double T;

		T = (JD - epoch_JD) / 365.25;

		/* calc proper motion */
		position.ra = mean_position.ra + T * proper_motion.ra;
		position.dec = mean_position.dec + T * proper_motion.dec;

		/* change to degrees */
		position.ra = ln_range_degrees(position.ra);
	}
}
