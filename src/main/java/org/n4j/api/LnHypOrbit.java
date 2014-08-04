package org.n4j.api;

/*!
* \struct ln_hyp_orbit
* \brief Hyperbolic Orbital elements
*
*  TODO.
* Angles are expressed in degrees.
*/
public class LnHypOrbit {

    double q;   /*!< Perihelion distance in AU */
    double e;   /*!< Eccentricity */
    double i;   /*!< Inclination in degrees */
    double w;   /*!< Argument of perihelion in degrees */
    double omega;   /*!< Longitude of ascending node in degrees*/
    double JD;  /*!< Time of last passage in Perihelion, in julian day*/
}
