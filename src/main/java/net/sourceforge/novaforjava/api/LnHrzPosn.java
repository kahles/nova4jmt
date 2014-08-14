package net.sourceforge.novaforjava.api;

/*
 * #%L
 * libnova for Java
 * %%
 * Copyright (C) 2014 novaforjave
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

/**
 * \struct ln_hrz_posn \brief Horizontal Coordinates.
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
