package org.n4j.api;

/*!
* \struct ln_par_orbit
* \brief Parabolic Orbital elements
*
*  TODO.
* Angles are expressed in degrees.
*/
public class LnParOrbit {

    double q;   /*!< Perihelion distance in AU */
    double i;   /*!< Inclination in degrees */
    double w;   /*!< Argument of perihelion in degrees */
    double omega;   /*!< Longitude of ascending node in degrees*/
    double JD;  /*!< Time of last passage in Perihelion, in julian day */
}
