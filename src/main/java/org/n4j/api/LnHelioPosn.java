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

	public double L; /* !< Heliocentric longitude */
	public double B; /* !< Heliocentric latitude */
	public double R; /* !< Heliocentric radius vector */
}
