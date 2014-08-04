package org.n4j.api;

/*!
* \struct ln_ell_orbit
* \brief Elliptic Orbital elements
*
*  TODO.
* Angles are expressed in degrees.
*/
public class LnEllOrbit {

    double a;   /*!< Semi major axis, in AU */
    double e;   /*!< Eccentricity */
    double i;   /*!< Inclination in degrees */
    double w;   /*!< Argument of perihelion in degrees */
    double omega;   /*!< Longitude of ascending node in degrees*/
    double n;   /*!< Mean motion, in degrees/day */
    double JD;  /*!< Time of last passage in Perihelion, in julian day*/
}
