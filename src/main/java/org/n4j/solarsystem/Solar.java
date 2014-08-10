package org.n4j.solarsystem;

import static org.n4j.Nutation.ln_get_nutation;
import static org.n4j.RiseSet.ln_get_body_rst_horizon;
import static org.n4j.Transform.ln_get_equ_from_ecl;
import static org.n4j.Transform.ln_get_rect_from_helio;
import static org.n4j.Utility.ln_range_degrees;
import static org.n4j.solarsystem.Earth.ln_get_earth_helio_coords;
import static org.n4j.solarsystem.Earth.ln_get_earth_solar_dist;
import static org.n4j.util.Reflect.getMethod;

import org.n4j.api.LnEquPosn;
import org.n4j.api.LnHelioPosn;
import org.n4j.api.LnLnlatPosn;
import org.n4j.api.LnNutation;
import org.n4j.api.LnRectPosn;
import org.n4j.api.LnRstTime;

public class Solar {

	public static final double LN_SOLAR_STANDART_HORIZON = -0.8333;

	/*
	 * ! \fn void ln_get_solar_geom_coords(double JD, LnHelioPosn position)
	 * \param JD Julian day \param position Pointer to store calculated solar
	 * position.
	 * 
	 * Calculate geometric coordinates and radius vector accuracy 0.01 arc
	 * second error - uses VSOP87 solution.
	 * 
	 * Latitude and Longitude returned are in degrees, whilst radius vector
	 * returned is in AU.
	 */

	public static void ln_get_solar_geom_coords(double JD, LnHelioPosn position) {
		/* get earths heliocentric position */
		ln_get_earth_helio_coords(JD, position);

		position.L += 180.0;
		position.L = ln_range_degrees(position.L);
		position.B *= -1.0;
	}

	/*
	 * ! \fn void ln_get_solar_equ_coords(double JD, LnEquPosn position) \param
	 * JD Julian day \param position Pointer to store calculated solar position.
	 * 
	 * Calculate apparent equatorial solar coordinates for given julian day.
	 * This function includes the effects of aberration and nutation.
	 */
	public static void ln_get_solar_equ_coords(double JD, LnEquPosn position) {
		LnHelioPosn sol = new LnHelioPosn();
		;
		LnLnlatPosn LB = new LnLnlatPosn();
		LnNutation nutation = new LnNutation();
		double aberration;

		/* get geometric coords */
		ln_get_solar_geom_coords(JD, sol);

		/* add nutation */
		ln_get_nutation(JD, nutation);
		sol.L += nutation.longitude;

		/* aberration */
		aberration = (20.4898 / (360.0 * 60.0 * 60.0)) / sol.R;
		sol.L -= aberration;

		/* transform to equatorial */
		LB.lat = sol.B;
		LB.lng = sol.L;
		ln_get_equ_from_ecl(LB, JD, position);
	}

	/*
	 * ! \fn void ln_get_solar_ecl_coords(double JD, LnLnlatPosn position)
	 * \param JD Julian day \param position Pointer to store calculated solar
	 * position.
	 * 
	 * Calculate apparent ecliptical solar coordinates for given julian day.
	 * This function includes the effects of aberration and nutation.
	 */
	public static void ln_get_solar_ecl_coords(double JD, LnLnlatPosn position) {
		LnHelioPosn sol = new LnHelioPosn();
		LnNutation nutation = new LnNutation();
		double aberration;

		/* get geometric coords */
		ln_get_solar_geom_coords(JD, sol);

		/* add nutation */
		ln_get_nutation(JD, nutation);
		sol.L += nutation.longitude;

		/* aberration */
		aberration = (20.4898 / (360.0 * 60.0 * 60.0)) / sol.R;
		sol.L -= aberration;

		position.lng = sol.L;
		position.lat = sol.B;
	}

	/*
	 * ! \fn void ln_get_solar_geo_coords(double JD, LnRectPosn position) \param
	 * JD Julian day \param position Pointer to store calculated solar position.
	 * 
	 * Calculate geocentric coordinates (rectangular) for given julian day.
	 * Accuracy 0.01 arc second error - uses VSOP87 solution. Position returned
	 * is in units of AU.
	 */
	public static void ln_get_solar_geo_coords(double JD, LnRectPosn position) {
		/* get earths's heliocentric position */
		LnHelioPosn sol = new LnHelioPosn();
		ln_get_earth_helio_coords(JD, sol);

		/* now get rectangular coords */
		ln_get_rect_from_helio(sol, position);
		position.X *= -1.0;
		position.Y *= -1.0;
		position.Z *= -1.0;
	}

	public static int ln_get_solar_rst_horizon(double JD, LnLnlatPosn observer,
			double horizon, LnRstTime rst) {
		return ln_get_body_rst_horizon(JD, observer,
				getMethod(Solar.class, "ln_get_solar_equ_coords"), horizon, rst);
	}

	/*
	 * ! \fn double ln_get_solar_rst(double JD, LnLnlatPosn observer, LnRstTime
	 * rst); Calls get_solar_rst_horizon with horizon set to
	 * LN_SOLAR_STANDART_HORIZON.
	 */

	public static int ln_get_solar_rst(double JD, LnLnlatPosn observer,
			LnRstTime rst) {
		return ln_get_solar_rst_horizon(JD, observer,
				LN_SOLAR_STANDART_HORIZON, rst);
	}

	/*
	 * ! \fn double ln_get_solar_sdiam(double JD) \param JD Julian day \return
	 * Semidiameter in arc seconds
	 * 
	 * Calculate the semidiameter of the Sun in arc seconds for the given julian
	 * day.
	 */
	public static double ln_get_solar_sdiam(double JD) {
		double So = 959.63; /* at 1 AU */
		double dist;

		dist = ln_get_earth_solar_dist(JD);
		return So / dist;
	}
}
