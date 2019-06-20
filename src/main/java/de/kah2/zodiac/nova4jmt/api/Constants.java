package de.kah2.zodiac.nova4jmt.api;

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

import java.math.BigDecimal;

public class Constants {

	public static final BigDecimal M_PI_2 = new BigDecimal(
			"1.5707963267948966192313216916398");

	public static final BigDecimal M_PI_4 = new BigDecimal(
			"0.78539816339744830961566084581988");

	public static final BigDecimal M_PI = new BigDecimal(
			"3.1415926535897932384626433832795");

	/** sidereal day length in seconds and days (for JD) */
	public static final double LN_SIDEREAL_DAY_SEC = 86164.09d;

	public static final double LN_SIDEREAL_DAY_DAY = (LN_SIDEREAL_DAY_SEC / 86400.0d);

	/** 1.1.2000 Julian Day & others */
	public static final double JD2000 = 2451545.0d;

	public static final double JD2050 = 2469807.50d;

	public static final double B1900 = 2415020.3135d;

	public static final double B1950 = 2433282.4235d;
}
