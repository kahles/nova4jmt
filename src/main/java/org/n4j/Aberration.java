package org.n4j;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_rad_to_deg;
import static org.n4j.solarsystem.Solar.ln_get_solar_geom_coords;

import org.n4j.api.LnEquPosn;
import org.n4j.api.LnHelioPosn;
import org.n4j.api.LnLnlatPosn;

public class Aberration {

	/* data structures to hold arguments and coefficients of Ron-Vondrak theory */
	private static class Arg {
		double a_L2;
		double a_L3;
		double a_L4;
		double a_L5;
		double a_L6;
		double a_L7;
		double a_L8;
		double a_LL;
		double a_D;
		double a_MM;
		double a_F;

		public Arg(double a_L2, double a_L3, double a_L4, double a_L5,
				double a_L6, double a_L7, double a_L8, double a_LL, double a_D,
				double a_MM, double a_F) {
			super();
			this.a_L2 = a_L2;
			this.a_L3 = a_L3;
			this.a_L4 = a_L4;
			this.a_L5 = a_L5;
			this.a_L6 = a_L6;
			this.a_L7 = a_L7;
			this.a_L8 = a_L8;
			this.a_LL = a_LL;
			this.a_D = a_D;
			this.a_MM = a_MM;
			this.a_F = a_F;
		}
	};

	private static class XYZ {
		double sin1;
		double sin2;
		double cos1;
		double cos2;

		public XYZ(double sin1, double sin2, double cos1, double cos2) {
			super();
			this.sin1 = sin1;
			this.sin2 = sin2;
			this.cos1 = cos1;
			this.cos2 = cos2;
		}
	};

	static final Arg[] arguments = {
	/* L2 3 4 5 6 7 8 LL D MM F */
	new Arg(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0),
			new Arg(0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1),
			new Arg(0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0),
			new Arg(0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 2, 0, -1, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 3, -8, 3, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 5, -8, 3, 0, 0, 0, 0, 0, 0, 0),
			new Arg(2, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0),
			new Arg(0, 1, 0, -2, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0),
			new Arg(0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0),
			new Arg(2, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 1, 0, -1, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 3, 0, -2, 0, 0, 0, 0, 0, 0, 0),
			new Arg(1, -2, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(2, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0),
			new Arg(2, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 3, -2, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 0, 0, 0, 1, 2, -1, 0),
			new Arg(8, 12, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(8, 14, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 2, 0, -2, 0, 0, 0, 0, 0, 0, 0),
			new Arg(3, -3, 0, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 2, -2, 0, 0, 0, 0, 0, 0, 0, 0),
			new Arg(0, 0, 0, 0, 0, 0, 0, 1, -2, 0, 0) };

	static final XYZ[] x_coefficients = { new XYZ(-1719914, -2, -25, 0),
			new XYZ(6434, 141, 28007, -107), new XYZ(715, 0, 0, 0),
			new XYZ(715, 0, 0, 0), new XYZ(486, -5, -236, -4),
			new XYZ(159, 0, 0, 0), new XYZ(0, 0, 0, 0), new XYZ(39, 0, 0, 0),
			new XYZ(33, 0, -10, 0), new XYZ(31, 0, 1, 0),
			new XYZ(8, 0, -28, 0), new XYZ(8, 0, -28, 0), new XYZ(21, 0, 0, 0),
			new XYZ(-19, 0, 0, 0), new XYZ(17, 0, 0, 0), new XYZ(16, 0, 0, 0),
			new XYZ(16, 0, 0, 0), new XYZ(11, 0, -1, 0), new XYZ(0, 0, -11, 0),
			new XYZ(-11, 0, -2, 0), new XYZ(-7, 0, -8, 0),
			new XYZ(-10, 0, 0, 0), new XYZ(-9, 0, 0, 0), new XYZ(-9, 0, 0, 0),
			new XYZ(0, 0, -9, 0), new XYZ(0, 0, -9, 0), new XYZ(8, 0, 0, 0),
			new XYZ(8, 0, 0, 0), new XYZ(-4, 0, -7, 0), new XYZ(-4, 0, -7, 0),
			new XYZ(-6, 0, -5, 0), new XYZ(-1, 0, -1, 0), new XYZ(4, 0, -6, 0),
			new XYZ(0, 0, -7, 0), new XYZ(5, 0, -5, 0), new XYZ(5, 0, 0, 0) };

	static final XYZ[] y_coefficients = { new XYZ(25, -13, 1578089, 156),
			new XYZ(25697, -95, -5904, -130), new XYZ(6, 0, -657, 0),
			new XYZ(0, 0, -656, 0), new XYZ(-216, -4, -446, 5),
			new XYZ(2, 0, -147, 0), new XYZ(0, 0, 26, 0),
			new XYZ(0, 0, -36, 0), new XYZ(-9, 0, -30, 0),
			new XYZ(1, 0, -28, 0), new XYZ(25, 0, 8, 0),
			new XYZ(-25, 0, -8, 0), new XYZ(0, 0, -19, 0),
			new XYZ(0, 0, 17, 0), new XYZ(0, 0, -16, 0), new XYZ(0, 0, 15, 0),
			new XYZ(1, 0, -15, 0), new XYZ(-1, 0, -10, 0),
			new XYZ(-10, 0, 0, 0), new XYZ(-2, 0, 9, 0), new XYZ(-8, 0, 6, 0),
			new XYZ(0, 0, 9, 0), new XYZ(0, 0, -9, 0), new XYZ(0, 0, -8, 0),
			new XYZ(-8, 0, 0, 0), new XYZ(8, 0, 0, 0), new XYZ(0, 0, -8, 0),
			new XYZ(0, 0, -7, 0), new XYZ(-6, 0, -4, 0), new XYZ(6, 0, -4, 0),
			new XYZ(-4, 0, 5, 0), new XYZ(-2, 0, -7, 0), new XYZ(-5, 0, -4, 0),
			new XYZ(-6, 0, 0, 0), new XYZ(-4, 0, -5, 0), new XYZ(0, 0, -5, 0) };

	static final XYZ[] z_coefficients = { new XYZ(10, 32, 684185, -358),
			new XYZ(11141, -48, -2559, -55), new XYZ(-15, 0, -282, 0),
			new XYZ(0, 0, -285, 0), new XYZ(-94, 0, -193, 0),
			new XYZ(-6, 0, -61, 0), new XYZ(0, 0, 59, 0), new XYZ(0, 0, 16, 0),
			new XYZ(-5, 0, -13, 0), new XYZ(0, 0, -12, 0),
			new XYZ(11, 0, 3, 0), new XYZ(-11, 0, -3, 0), new XYZ(0, 0, -8, 0),
			new XYZ(0, 0, 8, 0), new XYZ(0, 0, -7, 0), new XYZ(1, 0, 7, 0),
			new XYZ(-3, 0, -6, 0), new XYZ(-1, 0, 5, 0), new XYZ(-4, 0, 0, 0),
			new XYZ(-1, 0, 4, 0), new XYZ(-3, 0, 3, 0), new XYZ(0, 0, 4, 0),
			new XYZ(0, 0, -4, 0), new XYZ(0, 0, -4, 0), new XYZ(-3, 0, 0, 0),
			new XYZ(3, 0, 0, 0), new XYZ(0, 0, -3, 0), new XYZ(0, 0, -3, 0),
			new XYZ(-3, 0, 2, 0), new XYZ(3, 0, -2, 0), new XYZ(-2, 0, 2, 0),
			new XYZ(1, 0, -4, 0), new XYZ(-2, 0, -2, 0), new XYZ(-3, 0, 0, 0),
			new XYZ(-2, 0, -2, 0), new XYZ(0, 0, -2, 0) };

	/*
	 * ! \fn void ln_get_equ_aber(struct ln_equ_posn *mean_position, double JD,
	 * struct ln_equ_posn *position) \param mean_position Mean position of
	 * object \param JD Julian Day \param position Pointer to store new object
	 * position.
	 * 
	 * Calculate a stars equatorial coordinates from it's mean equatorial
	 * coordinates with the effects of aberration and nutation for a given
	 * Julian Day.
	 */
	/*
	 * Equ 22.1, 22.1, 22.3, 22.4
	 */
	public static void ln_get_equ_aber(LnEquPosn mean_position, double JD,
			LnEquPosn position) {
		/* long */double mean_ra, mean_dec, delta_ra, delta_dec;
		/* long */double L2, L3, L4, L5, L6, L7, L8, LL, D, MM, F, T, X, Y, Z, A;
		/* long */double c;
		int i;

		/* speed of light in 10-8 au per day */
		c = 17314463350.0;

		/* calc T */
		T = (JD - 2451545.0) / 36525.0;

		/* calc planetary perturbutions */
		L2 = 3.1761467 + 1021.3285546 * T;
		L3 = 1.7534703 + 628.3075849 * T;
		L4 = 6.2034809 + 334.0612431 * T;
		L5 = 0.5995464 + 52.9690965 * T;
		L6 = 0.8740168 + 21.329909095 * T;
		L7 = 5.4812939 + 7.4781599 * T;
		L8 = 5.3118863 + 3.8133036 * T;
		LL = 3.8103444 + 8399.6847337 * T;
		D = 5.1984667 + 7771.3771486 * T;
		MM = 2.3555559 + 8328.6914289 * T;
		F = 1.6279052 + 8433.4661601 * T;

		X = 0;
		Y = 0;
		Z = 0;

		/* sum the terms */
		for (i = 0; i < arguments.length; i++) {
			A = arguments[i].a_L2 * L2 + arguments[i].a_L3 * L3
					+ arguments[i].a_L4 * L4 + arguments[i].a_L5 * L5
					+ arguments[i].a_L6 * L6 + arguments[i].a_L7 * L7
					+ arguments[i].a_L8 * L8 + arguments[i].a_LL * LL
					+ arguments[i].a_D * D + arguments[i].a_MM * MM
					+ arguments[i].a_F * F;

			X += (x_coefficients[i].sin1 + x_coefficients[i].sin2 * T) * sin(A)
					+ (x_coefficients[i].cos1 + x_coefficients[i].cos2 * T)
					* cos(A);
			Y += (y_coefficients[i].sin1 + y_coefficients[i].sin2 * T) * sin(A)
					+ (y_coefficients[i].cos1 + y_coefficients[i].cos2 * T)
					* cos(A);
			Z += (z_coefficients[i].sin1 + z_coefficients[i].sin2 * T) * sin(A)
					+ (z_coefficients[i].cos1 + z_coefficients[i].cos2 * T)
					* cos(A);
		}

		/* Equ 22.4 */
		mean_ra = ln_deg_to_rad(mean_position.ra);
		mean_dec = ln_deg_to_rad(mean_position.dec);

		delta_ra = (Y * cos(mean_ra) - X * sin(mean_ra)) / (c * cos(mean_dec));
		delta_dec = (X * cos(mean_ra) + Y * sin(mean_ra)) * sin(mean_dec) - Z
				* cos(mean_dec);
		delta_dec /= -c;

		position.ra = ln_rad_to_deg(mean_ra + delta_ra);
		position.dec = ln_rad_to_deg(mean_dec + delta_dec);
	}

	/*
	 * ! \fn void ln_get_ecl_aber(struct ln_lnlat_posn *mean_position, double
	 * JD, struct ln_lnlat_posn *position) \param mean_position Mean position of
	 * object \param JD Julian Day \param position Pointer to store new object
	 * position.
	 * 
	 * Calculate a stars ecliptical coordinates from it's mean ecliptical
	 * coordinates with the effects of aberration and nutation for a given
	 * Julian Day.
	 */
	/*
	 * Equ 22.2 pg 139
	 */
	public static void ln_get_ecl_aber(LnLnlatPosn mean_position, double JD,
			LnLnlatPosn position)

	{
		double delta_lng, delta_lat, mean_lng, mean_lat, e, t, k;
		double true_longitude, T, T2;
		LnHelioPosn sol_position = new LnHelioPosn();

		/* constant of aberration */
		k = ln_deg_to_rad(20.49552 * (1.0 / 3600.0));

		/* Equ 21.1 */
		T = (JD - 2451545) / 36525;
		T2 = T * T;

		/* suns longitude in radians */
		ln_get_solar_geom_coords(JD, sol_position);
		true_longitude = ln_deg_to_rad(sol_position.B);

		/* Earth orbit ecentricity */
		e = 0.016708617 - 0.000042037 * T - 0.0000001236 * T2;
		e = ln_deg_to_rad(e);

		/* longitude of perihelion Earths orbit */
		t = 102.93735 + 1.71953 * T + 0.000046 * T2;
		t = ln_deg_to_rad(t);

		/* change object long/lat to radians */
		mean_lng = ln_deg_to_rad(mean_position.lng);
		mean_lat = ln_deg_to_rad(mean_position.lat);

		/* equ 22.2 */
		delta_lng = (-k * cos(true_longitude - mean_lng) + e * k
				* cos(t - mean_lng))
				/ cos(mean_lat);
		delta_lat = -k * sin(mean_lat)
				* (sin(true_longitude - mean_lng) - e * sin(t - mean_lng));

		mean_lng += delta_lng;
		mean_lat += delta_lat;

		position.lng = ln_rad_to_deg(mean_lng);
		position.lat = ln_rad_to_deg(mean_lat);
	}

}
