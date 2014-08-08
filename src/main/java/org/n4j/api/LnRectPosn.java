package org.n4j.api;

/*! \struct ln_rect_posn
 * \brief Rectangular coordinates
 *
 * Rectangular Coordinates of a body. These coordinates can be either
 * geocentric or heliocentric.
 *
 * A heliocentric position is an objects position relative to the
 * centre of the Sun. 
 * A geocentric position is an objects position relative to the centre
 * of the Earth.
 *
 * Position is in units of AU for planets and in units of km
 * for the Moon.
 */
public class LnRectPosn {

	public double X; /* !< Rectangular X coordinate */
	public double Y; /* !< Rectangular Y coordinate */
	public double Z; /* !< Rectangular Z coordinate */
}
