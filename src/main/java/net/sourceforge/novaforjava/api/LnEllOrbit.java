package net.sourceforge.novaforjava.api;

/*
 * #%L
 * libnova for Java
 * %%
 * Copyright (C) 2014 novaforjava
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
 * \struct ln_ell_orbit \brief Elliptic Orbital elements
 *
 * TODO. Angles are expressed in degrees.
 */
public class LnEllOrbit {

	public double a;
	/** !< Semi major axis, in AU */
	public double e;
	/** !< Eccentricity */
	public double i;
	/** !< Inclination in degrees */
	public double w;
	/** !< Argument of perihelion in degrees */
	public double omega;
	/** !< Longitude of ascending node in degrees */
	public double n;
	/** !< Mean motion, in degrees/day */
	public double JD;
	/** !< Time of last passage in Perihelion, in julian day */
}
