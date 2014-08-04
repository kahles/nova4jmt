package org.n4j.api;


/*! \struct ln_helio_posn
* \brief Heliocentric position 
*
* A heliocentric position is an objects position relative to the
* centre of the Sun. 
*
* Angles are expressed in degrees.
* Radius vector is in AU.
*/
public class LnHelioPosn {

    double L;   /*!< Heliocentric longitude */
    double B;   /*!< Heliocentric latitude */
    double R;   /*!< Heliocentric radius vector */
}
