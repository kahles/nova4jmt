package de.kah2.zodiac.nova4jmt;

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

import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static de.kah2.zodiac.nova4jmt.RiseSet.LN_STAR_STANDART_HORIZON;
import static de.kah2.zodiac.nova4jmt.RiseSet.ln_get_motion_body_next_rst_horizon;
import static de.kah2.zodiac.nova4jmt.RiseSet.ln_get_motion_body_next_rst_horizon_future;
import static de.kah2.zodiac.nova4jmt.RiseSet.ln_get_motion_body_rst_horizon;
import static de.kah2.zodiac.nova4jmt.Transform.ln_get_rect_from_helio;
import static de.kah2.zodiac.nova4jmt.Utility.ln_deg_to_rad;
import static de.kah2.zodiac.nova4jmt.Utility.ln_get_light_time;
import static de.kah2.zodiac.nova4jmt.Utility.ln_get_rect_distance;
import static de.kah2.zodiac.nova4jmt.Utility.ln_rad_to_deg;
import static de.kah2.zodiac.nova4jmt.Utility.ln_range_degrees;
import static de.kah2.zodiac.nova4jmt.api.Constants.M_PI_2;
import static de.kah2.zodiac.nova4jmt.api.Constants.M_PI_4;
import static de.kah2.zodiac.nova4jmt.solarsystem.Earth.ln_get_earth_helio_coords;
import static de.kah2.zodiac.nova4jmt.solarsystem.Earth.ln_get_earth_solar_dist;
import static de.kah2.zodiac.nova4jmt.solarsystem.Solar.ln_get_solar_geo_coords;
import de.kah2.zodiac.nova4jmt.api.LnEllOrbit;
import de.kah2.zodiac.nova4jmt.api.LnEquPosn;
import de.kah2.zodiac.nova4jmt.api.LnHelioPosn;
import de.kah2.zodiac.nova4jmt.api.LnLnlatPosn;
import de.kah2.zodiac.nova4jmt.api.LnRectPosn;
import de.kah2.zodiac.nova4jmt.api.LnRstTime;
import de.kah2.zodiac.nova4jmt.util.IGetMotionBodyCoords;

public class EllipticMotion {

	/**
	 * number of steps in calculation, 3.32 steps for each significant digit
	 * required
	 */
	static final double KEPLER_STEPS = 53;

	/** the BASIC SGN() function for doubles */
	public static double sgn(double x) {
		if (x == 0.0)
			return (x);
		else if (x < 0.0)
			return (-1.0);
		else
			return (1.0);
	}

	/**
	 * double ln_solve_kepler (double E, double M); \param E Orbital
	 * eccentricity \param M Mean anomaly \return Eccentric anomaly
	 * 
	 * Calculate the eccentric anomaly. This method was devised by Roger
	 * Sinnott. (Sky and Telescope, Vol 70, pg 159)
	 */
	public static double ln_solve_kepler(double e, double M) {
		double Eo = M_PI_2.doubleValue();
		double F, M1;
		double D = M_PI_4.doubleValue();
		int i;

		/** covert to radians */
		M = ln_deg_to_rad(M);

		F = sgn(M);
		M = Math.abs(M) / (2.0 * Math.PI);
		M = (M - (int) M) * 2.0 * Math.PI * F;

		if (M < 0)
			M = M + 2.0 * Math.PI;
		F = 1.0;

		if (M > Math.PI)
			F = -1.0;

		if (M > Math.PI)
			M = 2.0 * Math.PI - M;

		for (i = 0; i < KEPLER_STEPS; i++) {
			M1 = Eo - e * sin(Eo);
			Eo = Eo + D * sgn(M - M1);
			D /= 2.0;
		}
		Eo *= F;

		/** back to degrees */
		Eo = ln_rad_to_deg(Eo);
		return Eo;
	}

	/**
	 * double ln_get_ell_mean_anomaly (double n, double delta_JD); \param n Mean
	 * motion (degrees/day) \param delta_JD Time since perihelion \return Mean
	 * anomaly (degrees)
	 * 
	 * Calculate the mean anomaly.
	 */
	public static double ln_get_ell_mean_anomaly(double n, double delta_JD) {
		return delta_JD * n;
	}

	/**
	 * double ln_get_ell_true_anomaly (double e, double E); \param e Orbital
	 * eccentricity \param E Eccentric anomaly \return True anomaly (degrees)
	 * 
	 * Calculate the true anomaly.
	 */
	/** equ 30.1 */
	public static double ln_get_ell_true_anomaly(double e, double E) {
		double v;

		E = ln_deg_to_rad(E);
		v = sqrt((1.0 + e) / (1.0 - e)) * tan(E / 2.0);
		v = 2.0 * atan(v);
		v = ln_range_degrees(ln_rad_to_deg(v));
		return v;
	}

	/**
	 * double ln_get_ell_radius_vector (double a, double e, double E); \param a
	 * Semi-Major axis in AU \param e Orbital eccentricity \param E Eccentric
	 * anomaly \return Radius vector AU
	 * 
	 * Calculate the radius vector.
	 */
	/** equ 30.2 */
	public static double ln_get_ell_radius_vector(double a, double e, double E) {
		return a * (1.0 - e * cos(ln_deg_to_rad(E)));
	}

	/**
	 * double ln_get_ell_smajor_diam (double e, double q); \param e Eccentricity
	 * \param q Perihelion distance in AU \return Semi-major diameter in AU
	 * 
	 * Calculate the semi major diameter.
	 */
	public static double ln_get_ell_smajor_diam(double e, double q) {
		return q / (1.0 - e);
	}

	/**
	 * double ln_get_ell_sminor_diam (double e, double a); \param e Eccentricity
	 * \param a Semi-Major diameter in AU \return Semi-minor diameter in AU
	 * 
	 * Calculate the semi minor diameter.
	 */
	public static double ln_get_ell_sminor_diam(double e, double a) {
		return a * sqrt(1 - e * e);
	}

	/**
	 * double ln_get_ell_mean_motion (double a); \param a Semi major diameter in
	 * AU \return Mean daily motion (degrees/day)
	 * 
	 * Calculate the mean daily motion (degrees/day).
	 */
	public static double ln_get_ell_mean_motion(double a) {
		double q = 0.9856076686;
		/** Gaussian gravitational constant (degrees) */
		return q / (a * sqrt(a));
	}

	/**
	 * void ln_get_ell_helio_rect_posn(LnEllOrbit orbit, double JD, LnRectPosn
	 * posn); \param orbit Orbital parameters of object. \param JD Julian day
	 * \param posn Position pointer to store objects position
	 * 
	 * Calculate the objects rectangular heliocentric position given it's
	 * orbital elements for the given julian day.
	 */
	public static void ln_get_ell_helio_rect_posn(LnEllOrbit orbit, double JD,
			LnRectPosn posn) {
		double A, B, C;
		double F, G, H;
		double P, Q, R;
		double sin_e, cos_e;
		double a, b, c;
		double sin_omega, sin_i, cos_omega, cos_i;
		double M, v, E, r;

		/** J2000 obliquity of the ecliptic */
		sin_e = 0.397777156;
		cos_e = 0.917482062;

		/** equ 33.7 */
		sin_omega = sin(ln_deg_to_rad(orbit.omega));
		cos_omega = cos(ln_deg_to_rad(orbit.omega));
		sin_i = sin(ln_deg_to_rad(orbit.i));
		cos_i = cos(ln_deg_to_rad(orbit.i));
		F = cos_omega;
		G = sin_omega * cos_e;
		H = sin_omega * sin_e;
		P = -sin_omega * cos_i;
		Q = cos_omega * cos_i * cos_e - sin_i * sin_e;
		R = cos_omega * cos_i * sin_e + sin_i * cos_e;

		/** equ 33.8 */
		A = atan2(F, P);
		B = atan2(G, Q);
		C = atan2(H, R);
		a = sqrt(F * F + P * P);
		b = sqrt(G * G + Q * Q);
		c = sqrt(H * H + R * R);

		/** get daily motion */
		if (orbit.n == 0.0)
			orbit.n = ln_get_ell_mean_motion(orbit.a);

		/** get mean anomaly */
		M = ln_get_ell_mean_anomaly(orbit.n, JD - orbit.JD);

		/** get eccentric anomaly */
		E = ln_solve_kepler(orbit.e, M);

		/** get true anomaly */
		v = ln_get_ell_true_anomaly(orbit.e, E);

		/** get radius vector */
		r = ln_get_ell_radius_vector(orbit.a, orbit.e, E);

		/** equ 33.9 */
		posn.X = r * a * sin(A + ln_deg_to_rad(orbit.w + v));
		posn.Y = r * b * sin(B + ln_deg_to_rad(orbit.w + v));
		posn.Z = r * c * sin(C + ln_deg_to_rad(orbit.w + v));
	}

	/**
	 * void ln_get_ell_geo_rect_posn(LnEllOrbit orbit, double JD, LnRectPosn
	 * posn); \param orbit Orbital parameters of object. \param JD Julian day
	 * \param posn Position pointer to store objects position
	 * 
	 * Calculate the objects rectangular geocentric position given it's orbital
	 * elements for the given julian day.
	 */
	public static void ln_get_ell_geo_rect_posn(LnEllOrbit orbit, double JD,
			LnRectPosn posn) {
		LnRectPosn p_posn = new LnRectPosn(), e_posn = new LnRectPosn();
		LnHelioPosn earth = new LnHelioPosn();

		/** elliptic helio rect coords */
		ln_get_ell_helio_rect_posn(orbit, JD, p_posn);

		/** earth rect coords */
		ln_get_earth_helio_coords(JD, earth);
		ln_get_rect_from_helio(earth, e_posn);

		posn.X = e_posn.X - p_posn.X;
		posn.Y = e_posn.Y - p_posn.Y;
		posn.Z = e_posn.Z - p_posn.Z;
	}

	/**
	 * void ln_get_ell_body_equ_coords(double JD, LnEllOrbit orbit, LnEquPosn
	 * posn) \param JD Julian Day. \param orbit Orbital parameters. \param posn
	 * Pointer to hold asteroid position.
	 * 
	 * Calculate a bodies equatorial coordinates for the given julian day.
	 */
	public static void ln_get_ell_body_equ_coords(double JD, LnEllOrbit orbit,
			LnEquPosn posn) {
		LnRectPosn body_rect_posn = new LnRectPosn(), sol_rect_posn = new LnRectPosn();
		double dist, t;
		double x, y, z;

		/** get solar and body rect coords */
		ln_get_ell_helio_rect_posn(orbit, JD, body_rect_posn);
		ln_get_solar_geo_coords(JD, sol_rect_posn);

		/** calc distance and light time */
		dist = ln_get_rect_distance(body_rect_posn, sol_rect_posn);
		t = ln_get_light_time(dist);

		/** repeat calculation with new time (i.e. JD - t) */
		ln_get_ell_helio_rect_posn(orbit, JD - t, body_rect_posn);

		/** calc equ coords equ 33.10 */
		x = sol_rect_posn.X + body_rect_posn.X;
		y = sol_rect_posn.Y + body_rect_posn.Y;
		z = sol_rect_posn.Z + body_rect_posn.Z;

		posn.ra = ln_range_degrees(ln_rad_to_deg(atan2(y, x)));
		posn.dec = ln_rad_to_deg(asin(z / sqrt(x * x + y * y + z * z)));
	}

	/**
	 * double ln_get_ell_orbit_len(LnEllOrbit orbit); \param orbit Orbital
	 * parameters \return Orbital length in AU
	 * 
	 * Calculate the orbital length in AU.
	 * 
	 * Accuracy: - 0.001% for e &lt; 0.88 - 0.01% for e &lt; 0.95 - 1% for e = 0.9997
	 * - 3% for e = 1
	 */
	public static double ln_get_ell_orbit_len(LnEllOrbit orbit) {
		double A, G, H;
		double b;

		b = ln_get_ell_sminor_diam(orbit.e, orbit.a);

		A = (orbit.a + b) / 2.0;
		G = sqrt(orbit.a * b);
		H = (2.0 * orbit.a * b) / (orbit.a + b);

		/** Meeus, page 239, 2nd edition */
		return Math.PI * ((21.0 * A - 2.0 * G - 3.0 * H) / 8.0);
	}

	/**
	 * double ln_get_ell_orbit_vel(double JD, LnEllOrbit orbit); \param JD
	 * Julian day. \param orbit Orbital parameters \return Orbital velocity in
	 * km/s.
	 * 
	 * Calculate orbital velocity in km/s for the given julian day.
	 */
	public static double ln_get_ell_orbit_vel(double JD, LnEllOrbit orbit) {
		double V;
		double r;

		r = ln_get_ell_body_solar_dist(JD, orbit);
		V = 1.0 / r - 1.0 / (2.0 * orbit.a);
		V = 42.1219 * sqrt(V);
		return V;
	}

	/**
	 * double ln_get_ell_orbit_pvel(LnEllOrbit orbit); \param orbit Orbital
	 * parameters \return Orbital velocity in km/s.
	 * 
	 * Calculate orbital velocity at perihelion in km/s.
	 */
	public static double ln_get_ell_orbit_pvel(LnEllOrbit orbit) {
		double V;

		V = 29.7847 / sqrt(orbit.a);
		V *= sqrt((1.0 + orbit.e) / (1.0 - orbit.e));
		return V;
	}

	/**
	 * double ln_get_ell_orbit_avel(LnEllOrbit orbit); \param orbit Orbital
	 * parameters \return Orbital velocity in km/s.
	 * 
	 * Calculate the orbital velocity at aphelion in km/s.
	 */
	public static double ln_get_ell_orbit_avel(LnEllOrbit orbit) {
		double V;

		V = 29.7847 / sqrt(orbit.a);
		V *= sqrt((1.0 - orbit.e) / (1.0 + orbit.e));
		return V;
	}

	/**
	 * double ln_get_ell_body_solar_dist(double JD, LnEllOrbit orbit) \param JD
	 * Julian Day. \param orbit Orbital parameters \return The distance in AU
	 * between the Sun and the body.
	 * 
	 * Calculate the distance between a body and the Sun.
	 */
	public static double ln_get_ell_body_solar_dist(double JD, LnEllOrbit orbit) {
		LnRectPosn body_rect_posn = new LnRectPosn(), sol_rect_posn = new LnRectPosn();

		/** get solar and body rect coords */
		ln_get_ell_helio_rect_posn(orbit, JD, body_rect_posn);
		sol_rect_posn.X = 0;
		sol_rect_posn.Y = 0;
		sol_rect_posn.Z = 0;

		/** calc distance */
		return ln_get_rect_distance(body_rect_posn, sol_rect_posn);
	}

	/**
	 * double ln_get_ell_body_earth_dist(double JD, LnEllOrbit orbit) \param JD
	 * Julian day. \param orbit Orbital parameters \returns Distance in AU
	 * 
	 * Calculate the distance between an body and the Earth for the given julian
	 * day.
	 */
	public static double ln_get_ell_body_earth_dist(double JD, LnEllOrbit orbit) {
		LnRectPosn body_rect_posn = new LnRectPosn(), earth_rect_posn = new LnRectPosn();

		/** get solar and body rect coords */
		ln_get_ell_geo_rect_posn(orbit, JD, body_rect_posn);
		earth_rect_posn.X = 0;
		earth_rect_posn.Y = 0;
		earth_rect_posn.Z = 0;

		/** calc distance */
		return ln_get_rect_distance(body_rect_posn, earth_rect_posn);
	}

	/**
	 * double ln_get_ell_body_phase_angle(double JD, LnEllOrbit orbit); \param
	 * JD Julian day \param orbit Orbital parameters \return Phase angle.
	 * 
	 * Calculate the phase angle of the body. The angle Sun - body - Earth.
	 */
	public static double ln_get_ell_body_phase_angle(double JD, LnEllOrbit orbit) {
		double r, R, d;
		double E, M;
		double phase;

		/** get mean anomaly */
		if (orbit.n == 0.0)
			orbit.n = ln_get_ell_mean_motion(orbit.a);
		M = ln_get_ell_mean_anomaly(orbit.n, JD - orbit.JD);

		/** get eccentric anomaly */
		E = ln_solve_kepler(orbit.e, M);

		/** get radius vector */
		r = ln_get_ell_radius_vector(orbit.a, orbit.e, E);

		/** get solar and Earth distances */
		R = ln_get_ell_body_earth_dist(JD, orbit);
		d = ln_get_ell_body_solar_dist(JD, orbit);

		phase = (r * r + d * d - R * R) / (2.0 * r * d);
		return ln_range_degrees(acos(ln_deg_to_rad(phase)));
	}

	/**
	 * double ln_get_ell_body_elong(double JD, LnEllOrbit orbit); \param JD
	 * Julian day \param orbit Orbital parameters \return Elongation to the Sun.
	 * 
	 * Calculate the bodies elongation to the Sun..
	 */
	public static double ln_get_ell_body_elong(double JD, LnEllOrbit orbit) {
		double r, R, d;
		double t;
		double elong;
		double E, M;

		/** time since perihelion */
		t = JD - orbit.JD;

		/** get mean anomaly */
		if (orbit.n == 0.0)
			orbit.n = ln_get_ell_mean_motion(orbit.a);
		M = ln_get_ell_mean_anomaly(orbit.n, t);

		/** get eccentric anomaly */
		E = ln_solve_kepler(orbit.e, M);

		/** get radius vector */
		r = ln_get_ell_radius_vector(orbit.a, orbit.e, E);

		/** get solar and Earth-Sun distances */
		R = ln_get_earth_solar_dist(JD);
		d = ln_get_ell_body_solar_dist(JD, orbit);

		elong = (R * R + d * d - r * r) / (2.0 * R * d);
		return ln_range_degrees(ln_rad_to_deg(acos(elong)));
	}

	/**
	 * double ln_get_ell_body_rst(double JD, LnLnlatPosn observer, LnEllOrbit
	 * orbit, LnRstTime rst); \param JD Julian day \param observer Observers
	 * position \param orbit Orbital parameters \param rst Pointer to store
	 * Rise, Set and Transit time in JD \return 0 for success, else 1 for
	 * circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	 * 
	 * Calculate the time the rise, set and transit (crosses the local meridian
	 * at upper culmination) time of a body with an elliptic orbit for the given
	 * Julian day.
	 * 
	 * Note: this functions returns 1 if the body is circumpolar, that is it
	 * remains the whole day above the horizon. Returns -1 when it remains the
	 * whole day below the horizon.
	 */
	public static int ln_get_ell_body_rst(double JD, LnLnlatPosn observer,
			LnEllOrbit orbit, LnRstTime rst) {
		return ln_get_ell_body_rst_horizon(JD, observer, orbit,
				LN_STAR_STANDART_HORIZON.doubleValue(), rst);
	}

	/**
	 * double ln_get_ell_body_rst_horizon(double JD, LnLnlatPosn observer,
	 * LnEllOrbit orbit, double horizon, LnRstTime rst); \param JD Julian day
	 * \param observer Observers position \param orbit Orbital parameters \param
	 * horizon Horizon height \param rst Pointer to store Rise, Set and Transit
	 * time in JD \return 0 for success, else 1 for circumpolar (above the
	 * horizon), -1 for circumpolar (bellow the horizon)
	 * 
	 * Calculate the time the rise, set and transit (crosses the local meridian
	 * at upper culmination) time of a body with an elliptic orbit for the given
	 * Julian day.
	 * 
	 * Note: this functions returns 1 if the body is circumpolar, that is it
	 * remains the whole day above the horizon. Returns -1 when it remains the
	 * whole day below the horizon.
	 */
	public static int ln_get_ell_body_rst_horizon(double JD,
			LnLnlatPosn observer, LnEllOrbit orbit, double horizon,
			LnRstTime rst) {
		return ln_get_motion_body_rst_horizon(JD, observer,
				new IGetMotionBodyCoords<LnEllOrbit>() {

					@Override
					public void get_motion_body_coords(double JD, LnEllOrbit orbit,
							LnEquPosn posn) {
						ln_get_ell_body_equ_coords(JD, orbit, posn);
					}
				}, orbit, horizon, rst);
	}

	/**
	 * double ln_get_ell_body_next_rst(double JD, LnLnlatPosn observer,
	 * LnEllOrbit orbit, LnRstTime rst); \param JD Julian day \param observer
	 * Observers position \param orbit Orbital parameters \param rst Pointer to
	 * store Rise, Set and Transit time in JD \return 0 for success, else 1 for
	 * circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	 * 
	 * Calculate the time of next rise, set and transit (crosses the local
	 * meridian at upper culmination) time of a body with an elliptic orbit for
	 * the given Julian day.
	 * 
	 * This function guarantee, that rise, set and transit will be in &lt;JD, JD+1&gt;
	 * range.
	 * 
	 * Note: this functions returns 1 if the body is circumpolar, that is it
	 * remains the whole day above the horizon. Returns -1 when it remains the
	 * whole day below the horizon.
	 */
	public static int ln_get_ell_body_next_rst(double JD, LnLnlatPosn observer,
			LnEllOrbit orbit, LnRstTime rst) {
		return ln_get_ell_body_next_rst_horizon(JD, observer, orbit,
				LN_STAR_STANDART_HORIZON.doubleValue(), rst);
	}

	/**
	 * double ln_get_ell_body_next_rst_horizon(double JD, LnLnlatPosn observer,
	 * LnEllOrbit orbit, double horizon, LnRstTime rst); \param JD Julian day
	 * \param observer Observers position \param orbit Orbital parameters \param
	 * horizon Horizon height \param rst Pointer to store Rise, Set and Transit
	 * time in JD \return 0 for success, else 1 for circumpolar (above the
	 * horizon), -1 for circumpolar (bellow the horizon)
	 * 
	 * Calculate the time of next rise, set and transit (crosses the local
	 * meridian at upper culmination) time of a body with an elliptic orbit for
	 * the given Julian day.
	 * 
	 * This function guarantee, that rise, set and transit will be in &lt;JD, JD+1&gt;
	 * range.
	 * 
	 * Note: this functions returns 1 if the body is circumpolar, that is it
	 * remains the whole day above the horizon. Returns -1 when it remains the
	 * whole day below the horizon.
	 */
	public static int ln_get_ell_body_next_rst_horizon(double JD,
			LnLnlatPosn observer, LnEllOrbit orbit, double horizon,
			LnRstTime rst) {
		return ln_get_motion_body_next_rst_horizon(JD, observer,
				new IGetMotionBodyCoords<LnEllOrbit>() {

					@Override
					public void get_motion_body_coords(double JD, LnEllOrbit orbit,
							LnEquPosn posn) {
						ln_get_ell_body_equ_coords(JD,  orbit, posn);
					}
				}, orbit, horizon, rst);
	}

	/**
	 * double ln_get_ell_body_next_rst_horizon(double JD, LnLnlatPosn observer,
	 * LnEllOrbit orbit, double horizon, LnRstTime rst); \param JD Julian day
	 * \param observer Observers position \param orbit Orbital parameters \param
	 * horizon Horizon height \param day_limit Maximal number of days that will
	 * be searched for next rise and set \param rst Pointer to store Rise, Set
	 * and Transit time in JD \return 0 for success, else 1 for circumpolar
	 * (above the horizon), -1 for circumpolar (bellow the horizon)
	 * 
	 * Calculate the time of next rise, set and transit (crosses the local
	 * meridian at upper culmination) time of a body with an elliptic orbit for
	 * the given Julian day.
	 * 
	 * This function guarantee, that rise, set and transit will be in &lt;JD, JD +
	 * day_limit&gt; range.
	 * 
	 * Note: this functions returns 1 if the body is circumpolar, that is it
	 * remains the whole day above the horizon. Returns -1 when it remains the
	 * whole day below the horizon.
	 */
	public static int ln_get_ell_body_next_rst_horizon_future(double JD,
			LnLnlatPosn observer, LnEllOrbit orbit, double horizon,
			int day_limit, LnRstTime rst) {
		return ln_get_motion_body_next_rst_horizon_future(JD, observer,
				new IGetMotionBodyCoords<LnEllOrbit>() {

					@Override
					public void get_motion_body_coords(double JD, LnEllOrbit orbit,
							LnEquPosn posn) {
						ln_get_ell_body_equ_coords(JD, orbit, posn);
					}
				}, orbit, horizon, day_limit, rst);
	}

	/**
	 * !\fn double ln_get_ell_last_perihelion (double epoch_JD, double M, double
	 * n); \param epoch_JD Julian day of epoch \param M Mean anomaly \param n
	 * daily motion in degrees
	 * 
	 * Calculate the julian day of the last perihelion.
	 */
	public static double ln_get_ell_last_perihelion(double epoch_JD, double M,
			double n) {
		return epoch_JD - (M / n);
	}

}
