package org.n4j;

/*
 * #%L
 * libnova for Java
 * %%
 * Copyright (C) 2014 novaforjave
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
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_rad_to_deg;
import static org.n4j.Utility.ln_range_degrees;

import org.n4j.api.Constants;
import org.n4j.api.LnEquPosn;
import org.n4j.api.LnLnlatPosn;

public class Precession {

	/**
	 * void ln_get_equ_prec(struct ln_equ_posn *mean_position, double JD, struct
	 * ln_equ_posn *position) \param mean_position Mean object position \param
	 * JD Julian day \param position Pointer to store new object position.
	 * 
	 * Calculate equatorial coordinates with the effects of precession for a
	 * given Julian Day. Uses mean equatorial coordinates and is only for
	 * initial epoch J2000.0
	 * 
	 * Equ 20.3, 20.4 pg 126
	 */
	public static void ln_get_equ_prec(LnEquPosn mean_position, double JD,
			LnEquPosn position) {
		double t, t2, t3, A, B, C, zeta, eta, theta, ra, dec, mean_ra, mean_dec;
		// TODO BigDecimal
		/** change original ra and dec to radians */
		mean_ra = ln_deg_to_rad(mean_position.ra);
		mean_dec = ln_deg_to_rad(mean_position.dec);

		/** calc t, zeta, eta and theta for J2000.0 Equ 20.3 */
		t = (JD - Constants.JD2000) / 36525.0;
		t *= 1.0 / 3600.0;
		t2 = t * t;
		t3 = t2 * t;
		zeta = 2306.2181 * t + 0.30188 * t2 + 0.017998 * t3;
		eta = 2306.2181 * t + 1.09468 * t2 + 0.041833 * t3;
		theta = 2004.3109 * t - 0.42665 * t2 - 0.041833 * t3;
		zeta = ln_deg_to_rad(zeta);
		eta = ln_deg_to_rad(eta);
		theta = ln_deg_to_rad(theta);

		/** calc A,B,C equ 20.4 */
		A = cos(mean_dec) * sin(mean_ra + zeta);
		B = cos(theta) * cos(mean_dec) * cos(mean_ra + zeta) - sin(theta)
				* sin(mean_dec);
		C = sin(theta) * cos(mean_dec) * cos(mean_ra + zeta) + cos(theta)
				* sin(mean_dec);

		ra = Math.atan2(A, B) + eta;

		/** check for object near celestial pole */
		if (mean_dec > (0.4 * Math.PI) || mean_dec < (-0.4 * Math.PI)) {
			/** close to pole */
			dec = Math.acos(Math.sqrt(A * A + B * B));
			if (mean_dec < 0.)
				dec *= -1;
			/** 0 <= acos() <= PI */
		} else {
			/** not close to pole */
			dec = Math.asin(C);
		}

		/** change to degrees */
		position.ra = ln_range_degrees(ln_rad_to_deg(ra));
		position.dec = ln_rad_to_deg(dec);
	}

	/**
	 * void ln_get_equ_prec2(struct ln_equ_posn *mean_position, double fromJD,
	 * double toJD, struct ln_equ_posn *position);
	 * 
	 * \param mean_position Mean object position \param fromJD Julian day
	 * (start) \param toJD Julian day (end) \param position Pointer to store new
	 * object position.
	 * 
	 * Calculate the effects of precession on equatorial coordinates, between
	 * arbitary Jxxxx epochs. Use fromJD and toJD parameters to specify required
	 * Jxxxx epochs.
	 *
	 * Equ 20.2, 20.4 pg 126
	 */
	public static void ln_get_equ_prec2(LnEquPosn mean_position, double fromJD,
			double toJD, LnEquPosn position) {
		// TODO BigDecimal
		double t, t2, t3, A, B, C, zeta, eta, theta, ra, dec, mean_ra, mean_dec, T, T2;

		/** change original ra and dec to radians */
		mean_ra = ln_deg_to_rad(mean_position.ra);
		mean_dec = ln_deg_to_rad(mean_position.dec);

		/** calc t, T, zeta, eta and theta Equ 20.2 */
		T = ((double) (fromJD - Constants.JD2000)) / 36525.0;
		T *= 1.0 / 3600.0;
		t = ((double) (toJD - fromJD)) / 36525.0;
		t *= 1.0 / 3600.0;
		T2 = T * T;
		t2 = t * t;
		t3 = t2 * t;
		zeta = (2306.2181 + 1.39656 * T - 0.000139 * T2) * t
				+ (0.30188 - 0.000344 * T) * t2 + 0.017998 * t3;
		eta = (2306.2181 + 1.39656 * T - 0.000139 * T2) * t
				+ (1.09468 + 0.000066 * T) * t2 + 0.018203 * t3;
		theta = (2004.3109 - 0.85330 * T - 0.000217 * T2) * t
				- (0.42665 + 0.000217 * T) * t2 - 0.041833 * t3;
		zeta = ln_deg_to_rad(zeta);
		eta = ln_deg_to_rad(eta);
		theta = ln_deg_to_rad(theta);

		/** calc A,B,C equ 20.4 */
		A = cos(mean_dec) * sin(mean_ra + zeta);
		B = cos(theta) * cos(mean_dec) * cos(mean_ra + zeta) - sin(theta)
				* sin(mean_dec);
		C = sin(theta) * cos(mean_dec) * cos(mean_ra + zeta) + cos(theta)
				* sin(mean_dec);

		ra = Math.atan2(A, B) + eta;

		/** check for object near celestial pole */
		if (mean_dec > (0.4 * Math.PI) || mean_dec < (-0.4 * Math.PI)) {
			/** close to pole */
			dec = Math.acos(Math.sqrt(A * A + B * B));
			if (mean_dec < 0.)
				dec *= -1;
			/** 0 <= acos() <= PI */
		} else {
			/** not close to pole */
			dec = Math.asin(C);
		}

		/** change to degrees */
		position.ra = ln_range_degrees(ln_rad_to_deg(ra));
		position.dec = ln_rad_to_deg(dec);
	}

	/**
	 * void ln_get_ecl_prec(struct ln_lnlat_posn *mean_position, double JD,
	 * struct ln_lnlat_posn *position) \param mean_position Mean object position
	 * \param JD Julian day \param position Pointer to store new object
	 * position.
	 * 
	 * Calculate ecliptical coordinates with the effects of precession for a
	 * given Julian Day. Uses mean ecliptical coordinates and is only for
	 * initial epoch J2000.0 \todo To be implemented.
	 *
	 * Equ 20.5, 20.6 pg 128
	 */
	public static void ln_get_ecl_prec(LnLnlatPosn mean_position, double JD,
			LnLnlatPosn position) {

	}
}
