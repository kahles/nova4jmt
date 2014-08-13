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

/** \struct ln_helio_posn
 * \brief Heliocentric position 
 *
 * A heliocentric position is an objects position relative to the
 * centre of the Sun. 
 *
 * Angles are expressed in degrees.
 * Radius vector is in AU.
 */
public class LnHelioPosn {

	public double L;
	/** !< Heliocentric longitude */
	public double B;
	/** !< Heliocentric latitude */
	public double R;
	/** !< Heliocentric radius vector */
}
