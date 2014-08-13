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

import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static org.n4j.SiderealTime.ln_get_apparent_sidereal_time;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_rad_to_deg;

import org.n4j.api.DoubleRef;
import org.n4j.api.LnEquPosn;
import org.n4j.api.LnLnlatPosn;

public class Parallax {
	/**
	 * Equ on page 77 - chapter 10, The Earth's globe
	 */
	public static void get_topocentric(LnLnlatPosn observer, double height,
			DoubleRef ro_sin, DoubleRef ro_cos) {
		double u, lat_rad;

		lat_rad = ln_deg_to_rad(observer.lat);
		u = atan(0.99664719 * tan(lat_rad));
		ro_sin.value = 0.99664719 * sin(u) + (height / 6378140) * sin(lat_rad);
		ro_cos.value = cos(u) + (height / 6378140) * cos(lat_rad);

		// the quantity ro_sin is positive in the northern hemisphere, negative
		// in the southern one
		if (observer.lat > 0)
			ro_sin.value = Math.abs(ro_sin.value);
		else
			ro_sin.value = Math.abs(ro_sin.value) * -1;
		// ro_cos is always positive
		ro_cos.value = Math.abs(ro_cos.value);
	}

	/**
	 * void ln_get_parallax(LnEquPosn object, double au_distance, LnLnlatPosn
	 * observer, double height, double JD, LnEquPosn parallax); \param object
	 * Object geocentric coordinates \param au_distance Distance of object from
	 * Earth in AU \param observer Geographics observer positions \param height
	 * Observer height in m \param JD Julian day of observation \param parallax
	 * RA and DEC parallax
	 * 
	 * Calculate body parallax, which is need to calculate topocentric position
	 * of the body.
	 */
	/**
	 * Equ 39.1, 39.2, 39.3 Pg 263 and 264
	 */
	public static void ln_get_parallax(LnEquPosn object, double au_distance,
			LnLnlatPosn observer, double height, double JD, LnEquPosn parallax) {
		double H;

		H = ln_get_apparent_sidereal_time(JD) + (observer.lng - object.ra)
				/ 15.0;
		ln_get_parallax_ha(object, au_distance, observer, height, H, parallax);
	}

	/**
	 * void ln_get_parallax_ha(LnEquPosn object, double au_distance, LnLnlatPosn
	 * observer, double height, double H, LnEquPosn parallax); \param object
	 * Object geocentric coordinates \param au_distance Distance of object from
	 * Earth in AU \param observer Geographics observer positions \param height
	 * Observer height in m \param H Hour angle of object in hours \param
	 * parallax RA and DEC parallax
	 * 
	 * Calculate body parallax, which is need to calculate topocentric position
	 * of the body. Uses hour angle as time reference (handy in case we already
	 * compute it).
	 */
	/**
	 * Equ 39.1, 39.2, 39.3 Pg 263 and 264
	 */
	public static void ln_get_parallax_ha(LnEquPosn object, double au_distance,
			LnLnlatPosn observer, double height, double H, LnEquPosn parallax) {
		DoubleRef ro_sin = new DoubleRef(), ro_cos = new DoubleRef();
		double sin_pi, sin_H, cos_H, dec_rad, cos_dec;

		get_topocentric(observer, height, ro_sin, ro_cos);
		sin_pi = sin(ln_deg_to_rad((8.794 / au_distance) / 3600.0)); // (39.1)

		/** change hour angle from hours to radians */
		H *= Math.PI / 12.0;

		sin_H = sin(H);
		cos_H = cos(H);

		dec_rad = ln_deg_to_rad(object.dec);
		cos_dec = cos(dec_rad);

		parallax.ra = atan2(-ro_cos.value * sin_pi * sin_H, cos_dec
				- ro_cos.value * sin_pi * cos_H); // (39.2)
		parallax.dec = atan2((sin(dec_rad) - ro_sin.value * sin_pi)
				* cos(parallax.ra), cos_dec - ro_cos.value * sin_pi * cos_H); // (39.3)

		parallax.ra = ln_rad_to_deg(parallax.ra);
		parallax.dec = ln_rad_to_deg(parallax.dec) - object.dec;
	}

}
