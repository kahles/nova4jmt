package net.sourceforge.novaforjava.solarsystem;

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

import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.log10;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static net.sourceforge.novaforjava.RiseSet.LN_STAR_STANDART_HORIZON;
import static net.sourceforge.novaforjava.RiseSet.ln_get_body_rst_horizon;
import static net.sourceforge.novaforjava.Transform.ln_get_rect_from_helio;
import static net.sourceforge.novaforjava.Utility.ln_deg_to_rad;
import static net.sourceforge.novaforjava.Utility.ln_rad_to_deg;
import static net.sourceforge.novaforjava.Utility.ln_range_degrees;
import static net.sourceforge.novaforjava.solarsystem.Earth.ln_get_earth_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Earth.ln_get_earth_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Solar.ln_get_solar_geom_coords;
import static net.sourceforge.novaforjava.util.Reflect.getMethod;

import net.sourceforge.novaforjava.api.LnEquPosn;
import net.sourceforge.novaforjava.api.LnHelioPosn;
import net.sourceforge.novaforjava.api.LnLnlatPosn;
import net.sourceforge.novaforjava.api.LnRectPosn;
import net.sourceforge.novaforjava.api.LnRstTime;

public class Pluto {

	private static class pluto_argument {
		double J, S, P;

		public pluto_argument(double j, double s, double p) {
			super();
			J = j;
			S = s;
			P = p;
		}
	};

	private static class pluto_longitude {
		public pluto_longitude(double a, double b) {
			super();
			A = a;
			B = b;
		}

		double A, B;
	};

	private static class pluto_latitude {
		public pluto_latitude(double a, double b) {
			super();
			A = a;
			B = b;
		}

		double A, B;
	};

	private static class pluto_radius {
		public pluto_radius(double a, double b) {
			super();
			A = a;
			B = b;
		}

		double A, B;
	};

	/** cache variables */
	static double cJD = 0.0, cL = 0.0, cB = 0.0, cR = 0.0;

	static pluto_argument[] argument = { new pluto_argument(0, 0, 1),
			new pluto_argument(0, 0, 2), new pluto_argument(0, 0, 3),
			new pluto_argument(0, 0, 4), new pluto_argument(0, 0, 5),
			new pluto_argument(0, 0, 6), new pluto_argument(0, 1, -1),
			new pluto_argument(0, 1, 0), new pluto_argument(0, 1, 1),
			new pluto_argument(0, 1, 2), new pluto_argument(0, 1, 3),
			new pluto_argument(0, 2, -2), new pluto_argument(0, 2, -1),
			new pluto_argument(0, 2, 0), new pluto_argument(1, -1, 0),
			new pluto_argument(1, -1, 1), new pluto_argument(1, 0, -3),
			new pluto_argument(1, 0, -2), new pluto_argument(1, 0, -1),
			new pluto_argument(1, 0, 0), new pluto_argument(1, 0, 1),
			new pluto_argument(1, 0, 2), new pluto_argument(1, 0, 3),
			new pluto_argument(1, 0, 4), new pluto_argument(1, 1, -3),
			new pluto_argument(1, 1, -2), new pluto_argument(1, 1, -1),
			new pluto_argument(1, 1, 0), new pluto_argument(1, 1, 1),
			new pluto_argument(1, 1, 3), new pluto_argument(2, 0, -6),
			new pluto_argument(2, 0, -5), new pluto_argument(2, 0, -4),
			new pluto_argument(2, 0, -3), new pluto_argument(2, 0, -2),
			new pluto_argument(2, 0, -1), new pluto_argument(2, 0, 0),
			new pluto_argument(2, 0, 1), new pluto_argument(2, 0, 2),
			new pluto_argument(2, 0, 3), new pluto_argument(3, 0, -2),
			new pluto_argument(3, 0, -1), new pluto_argument(3, 0, 0) };

	static pluto_longitude[] longitude = {
			new pluto_longitude(-19799805, 19850055),
			new pluto_longitude(897144, -4954829),
			new pluto_longitude(611149, 1211027),
			new pluto_longitude(-341243, -189585),
			new pluto_longitude(129287, -34992),
			new pluto_longitude(-38164, 30893),
			new pluto_longitude(20442, -9987),
			new pluto_longitude(-4063, -5071),
			new pluto_longitude(-6016, -3336),
			new pluto_longitude(-3956, 3039), new pluto_longitude(-667, 3572),
			new pluto_longitude(1276, 501), new pluto_longitude(1152, -917),
			new pluto_longitude(630, -1277), new pluto_longitude(2571, -459),
			new pluto_longitude(899, -1449), new pluto_longitude(-1016, 1043),
			new pluto_longitude(-2343, -1012), new pluto_longitude(7042, 788),
			new pluto_longitude(1199, -338), new pluto_longitude(418, -67),
			new pluto_longitude(120, -274), new pluto_longitude(-60, -159),
			new pluto_longitude(-82, -29), new pluto_longitude(-36, -20),
			new pluto_longitude(-40, 7), new pluto_longitude(-14, 22),
			new pluto_longitude(4, 13), new pluto_longitude(5, 2),
			new pluto_longitude(-1, 0), new pluto_longitude(2, 0),
			new pluto_longitude(-4, 5), new pluto_longitude(4, -7),
			new pluto_longitude(14, 24), new pluto_longitude(-49, -34),
			new pluto_longitude(163, -48), new pluto_longitude(9, 24),
			new pluto_longitude(-4, 1), new pluto_longitude(-3, 1),
			new pluto_longitude(1, 3), new pluto_longitude(-3, -1),
			new pluto_longitude(5, -3), new pluto_longitude(0, 0) };

	static pluto_latitude[] latitude = {
			new pluto_latitude(-5452852, -14974862),
			new pluto_latitude(3527812, 1672790),
			new pluto_latitude(-1050748, 327647),
			new pluto_latitude(178690, -292153),
			new pluto_latitude(18650, 100340),
			new pluto_latitude(-30697, -25823),
			new pluto_latitude(4878, 11248), new pluto_latitude(226, -64),
			new pluto_latitude(2030, -836), new pluto_latitude(69, -604),
			new pluto_latitude(-247, -567), new pluto_latitude(-57, 1),
			new pluto_latitude(-122, 175), new pluto_latitude(-49, -164),
			new pluto_latitude(-197, 199), new pluto_latitude(-25, 217),
			new pluto_latitude(589, -248), new pluto_latitude(-269, 711),
			new pluto_latitude(185, 193), new pluto_latitude(315, 807),
			new pluto_latitude(-130, -43), new pluto_latitude(5, 3),
			new pluto_latitude(2, 17), new pluto_latitude(2, 5),
			new pluto_latitude(2, 3), new pluto_latitude(3, 1),
			new pluto_latitude(2, -1), new pluto_latitude(1, -1),
			new pluto_latitude(0, -1), new pluto_latitude(0, 0),
			new pluto_latitude(0, -2), new pluto_latitude(2, 2),
			new pluto_latitude(-7, 0), new pluto_latitude(10, -8),
			new pluto_latitude(-3, 20), new pluto_latitude(6, 5),
			new pluto_latitude(14, 17), new pluto_latitude(-2, 0),
			new pluto_latitude(0, 0), new pluto_latitude(0, 0),
			new pluto_latitude(0, 1), new pluto_latitude(0, 0),
			new pluto_latitude(1, 0) };

	static pluto_radius[] radius = { new pluto_radius(66865439, 68951812),
			new pluto_radius(-11827535, -332538),
			new pluto_radius(1593179, -1438890),
			new pluto_radius(-18444, 483220), new pluto_radius(-65977, -85431),
			new pluto_radius(31174, -6032), new pluto_radius(-5794, 22161),
			new pluto_radius(4601, 4032), new pluto_radius(-1729, 234),
			new pluto_radius(-415, 702), new pluto_radius(239, 723),
			new pluto_radius(67, -67), new pluto_radius(1034, -451),
			new pluto_radius(-129, 504), new pluto_radius(480, -231),
			new pluto_radius(2, -441), new pluto_radius(-3359, 265),
			new pluto_radius(7856, -7832), new pluto_radius(36, 45763),
			new pluto_radius(8663, 8547), new pluto_radius(-809, -769),
			new pluto_radius(263, -144), new pluto_radius(-126, 32),
			new pluto_radius(-35, -16), new pluto_radius(-19, -4),
			new pluto_radius(-15, 8), new pluto_radius(-4, 12),
			new pluto_radius(5, 6), new pluto_radius(3, 1),
			new pluto_radius(6, -2), new pluto_radius(2, 2),
			new pluto_radius(-2, -2), new pluto_radius(14, 13),
			new pluto_radius(-63, 13), new pluto_radius(136, -236),
			new pluto_radius(273, 1065), new pluto_radius(251, 149),
			new pluto_radius(-25, -9), new pluto_radius(9, -2),
			new pluto_radius(-8, 7), new pluto_radius(2, -10),
			new pluto_radius(19, 35), new pluto_radius(10, 2) };

	/**
	 * void ln_get_pluto_equ_coords(double JD, LnEquPosn position); \param JD
	 * julian Day \param position Pointer to store position
	 * 
	 * Calculates Pluto's equatorial position for the given julian day.
	 */
	public static void ln_get_pluto_equ_coords(double JD, LnEquPosn position) {
		LnHelioPosn h_sol = new LnHelioPosn(), h_pluto = new LnHelioPosn();
		LnRectPosn g_sol = new LnRectPosn(), g_pluto = new LnRectPosn();
		double a, b, c;
		double ra, dec, delta, diff, last, t = 0;

		/** need typdef for solar heliocentric coords */
		ln_get_solar_geom_coords(JD, h_sol);
		ln_get_rect_from_helio(h_sol, g_sol);

		do {
			last = t;
			ln_get_pluto_helio_coords(JD - t, h_pluto);
			ln_get_rect_from_helio(h_pluto, g_pluto);

			/** equ 33.10 pg 229 */
			a = g_sol.X + g_pluto.X;
			b = g_sol.Y + g_pluto.Y;
			c = g_sol.Z + g_pluto.Z;

			delta = a * a + b * b + c * c;
			delta = sqrt(delta);
			t = delta * 0.0057755183;
			diff = t - last;
		} while (diff > 0.0001 || diff < -0.0001);

		ra = atan2(b, a);
		dec = c / delta;
		dec = asin(dec);

		/** back to hours, degrees */
		position.ra = ln_range_degrees(ln_rad_to_deg(ra));
		position.dec = ln_rad_to_deg(dec);
	}

	/**
	 * void ln_get_pluto_helio_coords(double JD, LnHelioPosn position) \param JD
	 * Julian Day \param position Pointer to store new heliocentric position
	 * 
	 * Calculate Pluto's heliocentric coordinates for the given julian day. This
	 * function is accurate to within 0.07" in longitude, 0.02" in latitude and
	 * 0.000006 AU in radius vector.
	 * 
	 * Note: This function is not valid outside the period of 1885-2099.
	 */
	/**
	 * Chap 37. Equ 37.1
	 */

	public static void ln_get_pluto_helio_coords(double JD, LnHelioPosn position) {
		double sum_longitude = 0, sum_latitude = 0, sum_radius = 0;
		double J, S, P;
		double t, a, sin_a, cos_a;
		int i;

		/** check cache first */
		if (JD == cJD) {
			/** cache hit */
			position.L = cL;
			position.B = cB;
			position.R = cR;
			return;
		}

		/** get julian centuries since J2000 */
		t = (JD - 2451545.0) / 36525.0;

		/** calculate mean longitudes for jupiter, saturn and pluto */
		J = 34.35 + 3034.9057 * t;
		S = 50.08 + 1222.1138 * t;
		P = 238.96 + 144.9600 * t;

		/** calc periodic terms in table 37.A */
		for (i = 0; i < argument.length; i++) {
			a = argument[i].J * J + argument[i].S * S + argument[i].P * P;
			sin_a = sin(ln_deg_to_rad(a));
			cos_a = cos(ln_deg_to_rad(a));

			/** longitude */
			sum_longitude += longitude[i].A * sin_a + longitude[i].B * cos_a;

			/** latitude */
			sum_latitude += latitude[i].A * sin_a + latitude[i].B * cos_a;

			/** radius */
			sum_radius += radius[i].A * sin_a + radius[i].B * cos_a;
		}

		/** calc L, B, R */
		position.L = 238.958116 + 144.96 * t + sum_longitude * 0.000001;
		position.B = -3.908239 + sum_latitude * 0.000001;
		position.R = 40.7241346 + sum_radius * 0.0000001;

		/** save cache */
		cJD = JD;
		cL = position.L;
		cB = position.B;
		cR = position.R;
	}

	/**
	 * double ln_get_pluto_earth_dist(double JD); \param JD Julian day \return
	 * Distance in AU
	 * 
	 * Calculates the distance in AU between the Earth and Pluto for the given
	 * julian day.
	 */
	public static double ln_get_pluto_earth_dist(double JD) {
		LnHelioPosn h_pluto = new LnHelioPosn(), h_earth = new LnHelioPosn();
		LnRectPosn g_pluto = new LnRectPosn(), g_earth = new LnRectPosn();
		double x, y, z;

		/** get heliocentric positions */
		ln_get_pluto_helio_coords(JD, h_pluto);
		ln_get_earth_helio_coords(JD, h_earth);

		/** get geocentric coords */
		ln_get_rect_from_helio(h_pluto, g_pluto);
		ln_get_rect_from_helio(h_earth, g_earth);

		/** use pythag */
		x = g_pluto.X - g_earth.X;
		y = g_pluto.Y - g_earth.Y;
		z = g_pluto.Z - g_earth.Z;
		;
		x = x * x;
		y = y * y;
		z = z * z;

		return sqrt(x + y + z);
	}

	/**
	 * double ln_get_pluto_solar_dist(double JD); \param JD Julian day \return
	 * Distance in AU
	 * 
	 * Calculates the distance in AU between the Sun and Pluto for the given
	 * julian day.
	 */
	public static double ln_get_pluto_solar_dist(double JD) {
		LnHelioPosn h_pluto = new LnHelioPosn();

		/** get heliocentric position */
		ln_get_pluto_helio_coords(JD, h_pluto);
		return h_pluto.R;
	}

	/**
	 * double ln_get_pluto_magnitude(double JD); \param JD Julian day \return
	 * Visible magnitude of Pluto
	 * 
	 * Calculate the visible magnitude of Pluto for the given julian day.
	 */
	public static double ln_get_pluto_magnitude(double JD) {
		double delta, r;

		/** get distances */
		r = ln_get_pluto_solar_dist(JD);
		delta = ln_get_pluto_earth_dist(JD);

		return -1.0 + 5.0 * log10(r * delta);
	}

	/**
	 * double ln_get_pluto_disk(double JD); \param JD Julian day \return
	 * Illuminated fraction of Plutos disk
	 * 
	 * Calculate the illuminated fraction of Pluto's disk for the given julian
	 * day.
	 */
	/** Chapter 41 */
	public static double ln_get_pluto_disk(double JD) {
		double r, delta, R;

		/** get distances */
		R = ln_get_earth_solar_dist(JD);
		r = ln_get_pluto_solar_dist(JD);
		delta = ln_get_pluto_earth_dist(JD);

		/** calc fraction angle */
		return (((r + delta) * (r + delta)) - R * R) / (4.0 * r * delta);
	}

	/**
	 * double ln_get_pluto_phase(double JD); \param JD Julian day \return Phase
	 * angle of Pluto (degrees)
	 * 
	 * Calculate the phase angle of Pluto (Sun - Pluto - Earth) for the given
	 * julian day.
	 */
	/** Chapter 41 */
	public static double ln_get_pluto_phase(double JD) {
		double i, r, delta, R;

		/** get distances */
		R = ln_get_earth_solar_dist(JD);
		r = ln_get_pluto_solar_dist(JD);
		delta = ln_get_pluto_earth_dist(JD);

		/** calc phase */
		i = (r * r + delta * delta - R * R) / (2.0 * r * delta);
		i = acos(i);
		return ln_rad_to_deg(i);
	}

	/**
	 * double ln_get_pluto_rst(double JD, LnLnlatPosn observer, LnRstTime rst);
	 * \param JD Julian day \param observer Observers position \param rst
	 * Pointer to store Rise, Set and Transit time in JD \return 0 for success,
	 * else 1 for circumpolar.
	 * 
	 * Calculate the time the rise, set and transit (crosses the local meridian
	 * at upper culmination) time of Pluto for the given Julian day.
	 * 
	 * Note: this functions returns 1 if Pluto is circumpolar, that is it
	 * remains the whole day either above or below the horizon.
	 */
	public static int ln_get_pluto_rst(double JD, LnLnlatPosn observer,
			LnRstTime rst) {
		return ln_get_body_rst_horizon(JD, observer,
				getMethod(Pluto.class, "ln_get_pluto_equ_coords"),
				LN_STAR_STANDART_HORIZON.doubleValue(), rst);
	}

	/**
	 * double ln_get_pluto_sdiam(double JD) \param JD Julian day \return
	 * Semidiameter in arc seconds
	 * 
	 * Calculate the semidiameter of Pluto in arc seconds for the given julian
	 * day.
	 */
	public static double ln_get_pluto_sdiam(double JD) {
		double So = 2.07;
		/** at 1 AU */
		double dist;

		dist = ln_get_pluto_earth_dist(JD);
		return So / dist;
	}

	/**
	 * void ln_get_pluto_rect_helio(double JD, LnRectPosn position) \param JD
	 * Julian day. \param position pointer to return position
	 * 
	 * Calculate Plutos rectangular heliocentric coordinates for the given
	 * Julian day. Coordinates are in AU.
	 */
	public static void ln_get_pluto_rect_helio(double JD, LnRectPosn position) {
		LnHelioPosn pluto = new LnHelioPosn();

		ln_get_pluto_helio_coords(JD, pluto);
		ln_get_rect_from_helio(pluto, position);
	}

}
