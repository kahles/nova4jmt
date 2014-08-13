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

import static org.n4j.Aberration.ln_get_equ_aber;
import static org.n4j.Precession.ln_get_equ_prec;
import static org.n4j.ProperMotion.ln_get_equ_pm;

import org.n4j.api.LnEquPosn;

public class ApparentPosition {
	/**
	 * * Apparent place of an Object
	 */

	/**
	 * void ln_get_apparent_posn(struct ln_equ_posn *mean_position, struct
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
