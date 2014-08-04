package org.n4j.api;

/*! \struct ln_hrz_posn
 ** \brief Horizontal Coordinates.
 *
 * The Azimuth and Altitude of an object.
 *
 * Angles are expressed in degrees.
 */

public class LnHrzPosn {
	double az; /*
				 * !< AZ. Object azimuth. <p> 0 deg = South, 90 deg = West, 180
				 * deg = Nord, 270 deg = East
				 */
	double alt; /*
				 * !< ALT. Object altitude. <p> 0 deg = horizon, 90 deg = zenit,
				 * -90 deg = nadir
				 */

}
