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
 * \struct ln_rect_posn \brief Rectangular coordinates
 *
 * Rectangular Coordinates of a body. These coordinates can be either geocentric
 * or heliocentric.
 *
 * A heliocentric position is an objects position relative to the centre of the
 * Sun. A geocentric position is an objects position relative to the centre of
 * the Earth.
 *
 * Position is in units of AU for planets and in units of km for the Moon.
 */
public class LnRectPosn {

	public double X;
	/** !< Rectangular X coordinate */
	public double Y;
	/** !< Rectangular Y coordinate */
	public double Z;
	/** !< Rectangular Z coordinate */
}
