package org.n4j.api;

/*! \struct ln_hrz_posn
 ** \brief Horizontal Coordinates.
 *
 * The Azimuth and Altitude of an object.
 *
 * Angles are expressed in degrees.
 */

public class LnHrzPosn {
	/**
	 * AZ. Object azimuth.
	 * <p>
	 * 0 deg = South, 90 deg = West, 180 deg = Nord, 270 deg = East
	 */
	public double az;
	/**
	 * ALT. Object altitude.
	 * <p>
	 * 0 deg = horizon, 90 deg = zenit, -90 deg = nadir
	 */
	public double alt;

}
