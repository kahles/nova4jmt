package org.n4j.api;

/*!
 * \struct ln_ell_orbit
 * \brief Elliptic Orbital elements
 *
 *  TODO.
 * Angles are expressed in degrees.
 */
public class LnEllOrbit {

	public double a; /* !< Semi major axis, in AU */
	public double e; /* !< Eccentricity */
	public double i; /* !< Inclination in degrees */
	public double w; /* !< Argument of perihelion in degrees */
	public double omega; /* !< Longitude of ascending node in degrees */
	public double n; /* !< Mean motion, in degrees/day */
	public double JD; /* !< Time of last passage in Perihelion, in julian day */
}
