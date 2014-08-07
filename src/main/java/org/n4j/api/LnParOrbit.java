package org.n4j.api;

/*!
* \struct ln_par_orbit
* \brief Parabolic Orbital elements
*
*  TODO.
* Angles are expressed in degrees.
*/
public class LnParOrbit {

	public double q;   /*!< Perihelion distance in AU */
	public double i;   /*!< Inclination in degrees */
	public double w;   /*!< Argument of perihelion in degrees */
	public double omega;   /*!< Longitude of ascending node in degrees*/
	public double JD;  /*!< Time of last passage in Perihelion, in julian day */
}
