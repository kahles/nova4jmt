package org.n4j.api;


/*!
* \struct ln_nutation
* \brief Nutation in longitude, ecliptic and obliquity. 
*
* Contains Nutation in longitude, obliquity and ecliptic obliquity. 
*
* Angles are expressed in degrees.
*/
public class LnNutation {

    double longitude;   /*!< Nutation in longitude, in degrees */
    double obliquity;   /*!< Nutation in obliquity, in degrees */
    double ecliptic;    /*!< Mean obliquity of the ecliptic, in degrees */
}
