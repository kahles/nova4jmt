package org.n4j.api;

import java.math.BigDecimal;

public class Constants {

	public static final BigDecimal M_PI_2 = new BigDecimal(
			"1.5707963267948966192313216916398");

	public static final BigDecimal M_PI_4 = new BigDecimal(
			"0.78539816339744830961566084581988");

	public static final BigDecimal M_PI = new BigDecimal(
			"3.1415926535897932384626433832795");

	/* sidereal day length in seconds and days (for JD) */
	public static final double LN_SIDEREAL_DAY_SEC = 86164.09d;

	public static final double LN_SIDEREAL_DAY_DAY = (LN_SIDEREAL_DAY_SEC / 86400.0d);

	/* 1.1.2000 Julian Day & others */
	public static final double JD2000 = 2451545.0d;

	public static final double JD2050 = 2469807.50d;

	public static final double B1900 = 2415020.3135d;

	public static final double B1950 = 2433282.4235d;
}
