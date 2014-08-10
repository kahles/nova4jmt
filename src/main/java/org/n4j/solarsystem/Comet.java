package org.n4j.solarsystem;

import static java.lang.Math.log10;
import static org.n4j.EllipticMotion.ln_get_ell_body_solar_dist;
import static org.n4j.EllipticMotion.ln_get_ell_mean_anomaly;
import static org.n4j.EllipticMotion.ln_get_ell_mean_motion;
import static org.n4j.EllipticMotion.ln_get_ell_radius_vector;
import static org.n4j.EllipticMotion.ln_solve_kepler;
import static org.n4j.ParabolicMotion.ln_get_par_body_solar_dist;
import static org.n4j.ParabolicMotion.ln_get_par_radius_vector;

import org.n4j.api.LnEllOrbit;
import org.n4j.api.LnParOrbit;

public class Comet {

	/*
	 * ! \fn double ln_get_ell_comet_mag(double JD, LnEllOrbit orbit, double g,
	 * double k) \param JD Julian day. \param orbit Orbital parameters \param g
	 * Absolute magnitude \param k Comet constant \return The visual magnitude.
	 * 
	 * Calculate the visual magnitude of a comet in an elliptical orbit.
	 */
	public static double ln_get_ell_comet_mag(double JD, LnEllOrbit orbit,
			double g, double k) {
		double d, r;
		double E, M;

		/* get mean anomaly */
		if (orbit.n == 0)
			orbit.n = ln_get_ell_mean_motion(orbit.a);
		M = ln_get_ell_mean_anomaly(orbit.n, JD - orbit.JD);

		/* get eccentric anomaly */
		E = ln_solve_kepler(orbit.e, M);

		/* get radius vector */
		r = ln_get_ell_radius_vector(orbit.a, orbit.e, E);
		d = ln_get_ell_body_solar_dist(JD, orbit);

		return g + 5.0 * log10(d) + k * log10(r);
	}

	/*
	 * ! \fn double ln_get_par_comet_mag(double JD, LnParOrbit orbit, double g,
	 * double k) \param JD Julian day. \param orbit Orbital parameters \param g
	 * Absolute magnitude \param k Comet constant \return The visual magnitude.
	 * 
	 * Calculate the visual magnitude of a comet in a parabolic orbit.
	 */
	public static double ln_get_par_comet_mag(double JD, LnParOrbit orbit,
			double g, double k) {
		double d, r, t;

		/* time since perihelion */
		t = JD - orbit.JD;

		/* get radius vector */
		r = ln_get_par_radius_vector(orbit.q, t);
		d = ln_get_par_body_solar_dist(JD, orbit);

		return g + 5.0 * log10(d) + k * log10(r);
	}

}
