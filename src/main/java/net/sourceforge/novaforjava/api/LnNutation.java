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
 * \struct ln_nutation
 * \brief Nutation in longitude, ecliptic and obliquity. 
 *
 * Contains Nutation in longitude, obliquity and ecliptic obliquity. 
 *
 * Angles are expressed in degrees.
 */
public class LnNutation {

	public double longitude;
	/** !< Nutation in longitude, in degrees */
	public double obliquity;
	/** !< Nutation in obliquity, in degrees */
	public double ecliptic;
	/** !< Mean obliquity of the ecliptic, in degrees */
}
