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
 * \struct ln_rst_time \brief Rise, Set and Transit times.
 *
 * Contains the Rise, Set and transit times for a body.
 * 
 * Angles are expressed in degrees.
 */
public class LnRstTime {

	public double rise;
	/** !< Rise time in JD */
	public double set;
	/** !< Set time in JD */
	public double transit;
	/** !< Transit time in JD */
}
