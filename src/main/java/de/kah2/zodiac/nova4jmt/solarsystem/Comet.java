package de.kah2.zodiac.nova4jmt.solarsystem;

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

import static java.lang.Math.log10;
import static de.kah2.zodiac.nova4jmt.EllipticMotion.ln_get_ell_body_solar_dist;
import static de.kah2.zodiac.nova4jmt.EllipticMotion.ln_get_ell_mean_anomaly;
import static de.kah2.zodiac.nova4jmt.EllipticMotion.ln_get_ell_mean_motion;
import static de.kah2.zodiac.nova4jmt.EllipticMotion.ln_get_ell_radius_vector;
import static de.kah2.zodiac.nova4jmt.EllipticMotion.ln_solve_kepler;
import static de.kah2.zodiac.nova4jmt.ParabolicMotion.ln_get_par_body_solar_dist;
import static de.kah2.zodiac.nova4jmt.ParabolicMotion.ln_get_par_radius_vector;
import de.kah2.zodiac.nova4jmt.api.LnEllOrbit;
import de.kah2.zodiac.nova4jmt.api.LnParOrbit;

public class Comet {

	/**
	 * double ln_get_ell_comet_mag(double JD, LnEllOrbit orbit, double g, double
	 * k) \param JD Julian day. \param orbit Orbital parameters \param g
	 * Absolute magnitude \param k Comet constant \return The visual magnitude.
	 * 
	 * Calculate the visual magnitude of a comet in an elliptical orbit.
	 */
	public static double ln_get_ell_comet_mag(double JD, LnEllOrbit orbit,
			double g, double k) {
		double d, r;
		double E, M;

		/** get mean anomaly */
		if (orbit.n == 0)
			orbit.n = ln_get_ell_mean_motion(orbit.a);
		M = ln_get_ell_mean_anomaly(orbit.n, JD - orbit.JD);

		/** get eccentric anomaly */
		E = ln_solve_kepler(orbit.e, M);

		/** get radius vector */
		r = ln_get_ell_radius_vector(orbit.a, orbit.e, E);
		d = ln_get_ell_body_solar_dist(JD, orbit);

		return g + 5.0 * log10(d) + k * log10(r);
	}

	/**
	 * double ln_get_par_comet_mag(double JD, LnParOrbit orbit, double g, double
	 * k) \param JD Julian day. \param orbit Orbital parameters \param g
	 * Absolute magnitude \param k Comet constant \return The visual magnitude.
	 * 
	 * Calculate the visual magnitude of a comet in a parabolic orbit.
	 */
	public static double ln_get_par_comet_mag(double JD, LnParOrbit orbit,
			double g, double k) {
		double d, r, t;

		/** time since perihelion */
		t = JD - orbit.JD;

		/** get radius vector */
		r = ln_get_par_radius_vector(orbit.q, t);
		d = ln_get_par_body_solar_dist(JD, orbit);

		return g + 5.0 * log10(d) + k * log10(r);
	}

}
