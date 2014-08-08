package org.n4j.api;

/*!
 * \struct ln_hyp_orbit
 * \brief Hyperbolic Orbital elements
 *
 *  TODO.
 * Angles are expressed in degrees.
 */
public class LnHypOrbit {

	public double q; /* !< Perihelion distance in AU */
	public double e; /* !< Eccentricity */
	public double i; /* !< Inclination in degrees */
	public double w; /* !< Argument of perihelion in degrees */
	public double omega; /* !< Longitude of ascending node in degrees */
	public double JD; /* !< Time of last passage in Perihelion, in julian day */
}
