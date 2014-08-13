package net.sourceforge.novaforjava;

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
import java.util.Date;
import java.util.TimeZone;

import net.sourceforge.novaforjava.api.LnDms;
import net.sourceforge.novaforjava.api.LnEquPosn;
import net.sourceforge.novaforjava.api.LnHms;
import net.sourceforge.novaforjava.api.LnHrzPosn;
import net.sourceforge.novaforjava.api.LnLnlatPosn;
import net.sourceforge.novaforjava.api.LnRectPosn;
import net.sourceforge.novaforjava.api.LnhEquPosn;
import net.sourceforge.novaforjava.api.LnhHrzPosn;
import net.sourceforge.novaforjava.api.LnhLnlatPosn;
import net.sourceforge.novaforjava.api.TimeVal;
import net.sourceforge.novaforjava.api.Timezone;
import net.sourceforge.novaforjava.util.Tokens;

public class Utility {

	private static enum Type {
		HOURS, DEGREES, LAT, LONG
	};

	/**
	 * deg.radian
	 */
	private static final BigDecimal D2R = new BigDecimal(
			"0.017453292519943295769");
	/**
	 * radian.deg
	 */
	private static final double R2D = (5.7295779513082320877e1d);
	/**
	 * arc seconds per radian
	 */
	private static final double R2S = (2.0626480624709635516e5d);
	/**
	 * radians per arc second
	 */
	private static final double S2R = (4.8481368110953599359e-6d);
	private static final double DM_PI = (2 * Math.PI);
	private static final double RADIAN = (180.0 / Math.PI);

	/**
	 * String ln_get_version (void) \return Null terminated version string.
	 * 
	 * Return the libnova library version number string e.g. "0.4.0"
	 */
	public static String ln_get_version() {
		return "1.0";// TODO
	}

	/** convert radians to degrees */
	public static double ln_rad_to_deg(double radians) {
		return (radians * R2D);
	}

	/** convert degrees to radians */
	public static double ln_deg_to_rad(double degrees) {
		return (degrees * D2R.doubleValue());
	}

	/**
	 * BigDecimal variant
	 * 
	 * @param degrees
	 * @return
	 */
	public static BigDecimal ln_deg_to_rad(BigDecimal degrees) {
		return (degrees.multiply(D2R));
	}

	/** convert hours:mins:secs to degrees */
	public static double ln_hms_to_deg(LnHms hms) {
		double degrees;

		degrees = ((double) hms.hours / 24.0d) * 360.0d;
		degrees += ((double) hms.minutes / 60.0d) * 15.0d;
		degrees += ((double) hms.seconds / 60.0d) * 0.25d;

		return degrees;
	}

	/** convert hours:mins:secs to radians */
	public static double ln_hms_to_rad(LnHms hms) {
		double radians;

		radians = ((double) hms.hours / 24.0d) * 2.0d * Math.PI;
		radians += ((double) hms.minutes / 60.0d) * 2.0d * Math.PI / 24.0d;
		radians += ((double) hms.seconds / 60.0d) * 2.0d * Math.PI / 1440.0d;

		return radians;
	}

	/** convert degrees to hh:mm:ss */
	public static void ln_deg_to_hms(double degrees, LnHms hms) {
		double dtemp;

		degrees = ln_range_degrees(degrees);

		/** divide degrees by 15 to get the hours */
		dtemp = degrees / 15.0;
		hms.hours = (int) dtemp;

		/** multiply remainder by 60 to get minutes */
		dtemp = 60.0 * (dtemp - hms.hours);
		hms.minutes = (int) dtemp;

		/** multiply remainder by 60 to get seconds */
		hms.seconds = 60.0 * (dtemp - hms.minutes);

		/** catch any overflows */
		if (hms.seconds > 59) {
			hms.seconds = 0.0;
			hms.minutes++;
		}
		if (hms.minutes > 59) {
			hms.minutes = 0;
			hms.hours++;
		}
	}

	/** convert radians to hh:mm:ss */
	public static void ln_rad_to_hms(double radians, LnHms hms) {
		double degrees;

		radians = ln_range_radians(radians);
		degrees = ln_rad_to_deg(radians);

		ln_deg_to_hms(degrees, hms);
	}

	/** convert dms to degrees */
	public static double ln_dms_to_deg(LnDms dms) {
		double degrees;

		degrees = Math.abs((double) dms.degrees);
		degrees += Math.abs((double) dms.minutes / 60.0d);
		degrees += Math.abs((double) dms.seconds / 3600.0d);

		// negative ?
		if (dms.neg != 0)
			degrees *= -1.0;

		return degrees;
	}

	/** convert dms to radians */
	public static double ln_dms_to_rad(LnDms dms) {
		double radians;

		radians = Math.abs((double) dms.degrees / 360.0d * 2.0d * Math.PI);
		radians += Math.abs((double) dms.minutes / 21600.0d * 2.0d * Math.PI);
		radians += Math.abs((double) dms.seconds / 1296000.0d * 2.0d * Math.PI);

		// negative ?
		if (dms.neg != 0)
			radians *= -1.0;

		return radians;
	}

	/** convert degrees to dms */
	public static void ln_deg_to_dms(double degrees, LnDms dms) {
		double dtemp;

		if (degrees >= 0.0)
			dms.neg = 0;
		else
			dms.neg = 1;

		degrees = Math.abs(degrees);
		dms.degrees = (int) degrees;

		/** multiply remainder by 60 to get minutes */
		dtemp = 60.0 * (degrees - dms.degrees);
		dms.minutes = (int) dtemp;

		/** multiply remainder by 60 to get seconds */
		dms.seconds = 60.0 * (dtemp - dms.minutes);

		/** catch any overflows */
		if (dms.seconds > 59) {
			dms.seconds = 0.0;
			dms.minutes++;
		}
		if (dms.minutes > 59) {
			dms.minutes = 0;
			dms.degrees++;
		}
	}

	/** convert radians to dms */
	public static void ln_rad_to_dms(double radians, LnDms dms) {
		double degrees = ln_rad_to_deg(radians);

		ln_deg_to_dms(degrees, dms);
	}

	/** puts a large angle in the correct range 0 - 360 degrees */
	public static double ln_range_degrees(double angle) {
		double temp;

		if (angle >= 0.0 && angle < 360.0)
			return angle;

		temp = (int) (angle / 360);
		if (angle < 0.0)
			temp--;
		temp *= 360.0;
		return angle - temp;
	}

	/** puts a large angle in the correct range 0 - 2PI radians */
	public static double ln_range_radians(double angle) {
		double temp;

		if (angle >= 0.0d && angle < (2.0d * Math.PI))
			return angle;

		temp = (int) (angle / (Math.PI * 2.0));

		if (angle < 0.0)
			temp--;
		temp *= (Math.PI * 2.0);
		return angle - temp;
	}

	/** puts a large angle in the correct range -2PI - 2PI radians */
	/** preserve sign */
	public static double ln_range_radians2(double angle) {
		double temp;

		if (angle > (-2.0 * Math.PI) && angle < (2.0 * Math.PI))
			return angle;

		temp = (int) (angle / (Math.PI * 2.0));
		temp *= (Math.PI * 2.0);
		return angle - temp;
	}

	/** add seconds to hms */
	public static void ln_add_secs_hms(LnHms hms, double seconds) {
		LnHms source_hms = new LnHms();

		/** breaks double seconds int hms */
		source_hms.hours = (int) (seconds / 3600d);
		seconds -= source_hms.hours * 3600;
		source_hms.minutes = (int) (seconds / 60d);
		seconds -= source_hms.minutes * 60;
		source_hms.seconds = seconds;

		/** add hms to hms */
		ln_add_hms(source_hms, hms);
	}

	/** add hms to hms */
	public static void ln_add_hms(LnHms source, LnHms dest) {
		dest.seconds += source.seconds;
		if (dest.seconds >= 60.0) {
			/** carry */
			source.minutes++;
			dest.seconds -= 60.0;
		} else {
			if (dest.seconds < 0.0) {
				/** carry */
				source.minutes--;
				dest.seconds += 60.0;
			}
		}

		dest.minutes += source.minutes;
		if (dest.minutes >= 60) {
			/** carry */
			source.hours++;
			dest.minutes -= 60;
		} else {
			if (dest.seconds < 0.0) {
				/** carry */
				source.hours--;
				dest.minutes += 60;
			}
		}

		dest.hours += source.hours;
	}

	/**
	 * void ln_hequ_to_equ(LnhEquPosn hpos, LnEquPosn pos) \brief human readable
	 * equatorial position to double equatorial position \ingroup conversion
	 */
	public static void ln_hequ_to_equ(LnhEquPosn hpos, LnEquPosn pos) {
		pos.ra = ln_hms_to_deg(hpos.ra);
		pos.dec = ln_dms_to_deg(hpos.dec);
	}

	/**
	 * void ln_equ_to_hequ(LnEquPosn pos, LnhEquPosn hpos) \brief human double
	 * equatorial position to human readable equatorial position \ingroup
	 * conversion
	 */
	public static void ln_equ_to_hequ(LnEquPosn pos, LnhEquPosn hpos) {
		ln_deg_to_hms(pos.ra, hpos.ra);
		ln_deg_to_dms(pos.dec, hpos.dec);
	}

	/**
	 * void ln_hhrz_to_hrz(LnhHrzPosn hpos, LnHrzPosn pos) \brief human readable
	 * horizontal position to double horizontal position \ingroup conversion
	 */
	public static void ln_hhrz_to_hrz(LnhHrzPosn hpos, LnHrzPosn pos) {
		pos.alt = ln_dms_to_deg(hpos.alt);
		pos.az = ln_dms_to_deg(hpos.az);
	}

	/**
	 * void ln_hrz_to_hhrz(LnHrzPosn pos, LnhHrzPosn hpos) \brief double
	 * horizontal position to human readable horizontal position \ingroup
	 * conversion
	 */
	public static void ln_hrz_to_hhrz(LnHrzPosn pos, LnhHrzPosn hpos) {
		ln_deg_to_dms(pos.alt, hpos.alt);
		ln_deg_to_dms(pos.az, hpos.az);
	}

	/**
	 * String ln_hrz_to_nswe(LnHrzPosn pos); \brief returns direction of given
	 * azimuth - like N,S,W,E,NSW,... \ingroup conversion
	 */
	public static String ln_hrz_to_nswe(LnHrzPosn pos) {
		String directions[] = { "S", "SSW", "SW", "SWW", "W", "NWW", "NW",
				"NNW", "N", "NNE", "NE", "NEE", "E", "SEE", "SE", "SSE" };

		return directions[(int) (pos.az / 22.5)];
	}

	/**
	 * void ln_hlnlat_to_lnlat(LnhLnlatPosn hpos, LnLnlatPosn pos) \brief human
	 * readable long/lat position to double long/lat position \ingroup
	 * conversion
	 */
	public static void ln_hlnlat_to_lnlat(LnhLnlatPosn hpos, LnLnlatPosn pos) {
		pos.lng = ln_dms_to_deg(hpos.lng);
		pos.lat = ln_dms_to_deg(hpos.lat);
	}

	/**
	 * void ln_lnlat_to_hlnlat(LnLnlatPosn pos, LnhLnlatPosn hpos) \brief double
	 * long/lat position to human readable long/lat position \ingroup conversion
	 */
	public static void ln_lnlat_to_hlnlat(LnLnlatPosn pos, LnhLnlatPosn hpos) {
		ln_deg_to_dms(pos.lng, hpos.lng);
		ln_deg_to_dms(pos.lat, hpos.lat);
	}

	/**
	 * \fn double ln_get_rect_distance(LnRectPosn a, LnRectPosn b) \param a
	 * First rectangular coordinate \param b Second rectangular coordinate
	 * \return Distance between a and b.
	 * 
	 * Calculate the distance between rectangular points a and b.
	 */
	public static double ln_get_rect_distance(LnRectPosn a, LnRectPosn b) {
		double x, y, z;

		x = a.X - b.X;
		y = a.Y - b.Y;
		z = a.Z - b.Z;

		x *= x;
		y *= y;
		z *= z;

		return Math.sqrt(x + y + z);
	}

	/**
	 * \fn double ln_get_light_time (double dist) \param dist Distance in AU
	 * \return Distance in light days.
	 * 
	 * Convert units of AU into light days.
	 */
	public static double ln_get_light_time(double dist) {
		return dist * 0.005775183;
	}

	/**
	 * []----------------------------------------------------------------------
	 * -- [] | trim() & strip() | | | | strips trailing whitespaces from buf. |
	 * | | [
	 * ]------------------------------------------------------------------------
	 * []
	 */
	public static String trim(String x) {
		int index = x.length() - 1;
		while (Character.isWhitespace(x.charAt(index))) {
			index--;
		}
		return x.substring(0, index + 1);
	}

	/**
	 * double ln_get_dec_location(String s) \param s Location string \return
	 * angle in degrees
	 * 
	 * Obtains Latitude, Longitude, RA or Declination from a string.
	 * 
	 * If the last char is N/S doesn't accept more than 90 degrees. If it is E/W
	 * doesn't accept more than 180 degrees. If they are hours don't accept more
	 * than 24:00
	 * 
	 * Any position can be expressed as follows: (please use a 8 bits charset if
	 * you want to view the degrees separator char '0xba')
	 * 
	 * 42.30.35,53 90º0'0,01 W 42º30'35.53 N 42º30'35.53S 42º30'N - 42.30.35.53
	 * 42:30:35.53 S + 42.30.35.53 +42º30 35,53 23h36'45,0
	 * 
	 * 
	 * 42:30:35.53 S = -42º30'35.53" + 42 30.35.53 S the same previous position,
	 * the plus (+) sign is considered like an error, the last 'S' has
	 * precedence over the sign
	 * 
	 * 90º0'0,01 N ERROR: +- 90º0'00.00" latitude limit
	 */
	public static double ln_get_dec_location(String s) {
		String ptr, dec;
		boolean negative = false;
		char delim1[] = " :.,;DdHhMm'\n\t".toCharArray();
		char delim2[] = " NSEWnsew\"\n\t".toCharArray();
		int dghh = 0, minutes = 0;
		double seconds = 0.0, pos;
		short count;
		int comma;

		if (s == null || s.isEmpty())
			return -0.0;

		int ptrIndex = 0;
		ptr = s.trim();
		if (ptr.charAt(ptrIndex) == '+' || ptr.charAt(ptrIndex) == '-')
			negative = (ptr.charAt(ptrIndex++) == '-' ? true : negative);

		/** the last letter has precedence over the sign */
		if (indexOneOf(ptr, 'S', 's', 'W', 'w') >= 0)
			negative = true;
		Type type;
		int ame;
		ptr = ptr.substring(ptrIndex).trim();
		int hh = indexOneOf(ptr, 'H', 'h');
		if (hh >= 0 && hh < 3) {
			type = Type.HOURS;
			if (negative)
				/** if RA no negative numbers */
				negative = false;
		} else if ((ame = indexOneOf(ptr, 'S', 's', 'N', 'n')) >= 0) {
			type = Type.LAT;
			if (ame == 0)
				/** the North/South found before data */
				ptr = ptr.substring(1);
		} else
			type = Type.DEGREES;
		/** unspecified, the caller must control it */

		Tokens tokens = new Tokens(ptr, delim1);
		if ((ptr = tokens.nextToken()) != null)
			dghh = Integer.parseInt(ptr);
		else
			return (-0.0);
		if ((ptr = tokens.nextToken()) != null) {
			minutes = Integer.parseInt(ptr);
			if (minutes > 59)
				return -0.0;
		} else
			return -0.0;

		if ((ptr = tokens.nextToken(delim2)) != null) {
			ptr.replaceFirst("\\.", ",");
			seconds = Double.parseDouble(ptr);
			if (seconds >= 60.0)
				return -0.0;
		}

		if ((ptr = tokens.nextToken(" \n\t".toCharArray())) != null) {
			while (Character.isWhitespace(ptr.charAt(0))) {
				ptr = ptr.substring(1);
			}
			if (ptr.charAt(0) == 'S' || ptr.charAt(0) == 'W'
					|| ptr.charAt(0) == 's' || ptr.charAt(0) == 'w')
				negative = true;
		}

		pos = dghh + minutes / 60.0 + seconds / 3600.0;
		if (type == Type.HOURS && pos > 24.0)
			return -0.0;
		if (type == Type.LAT && pos > 90.0)
			return -0.0;
		if (negative)
			pos = 0.0 - pos;

		return pos;
	}

	/**
	 * String ln_get_humanr_location(double location) \param location Location
	 * angle in degress \return Angle string
	 * 
	 * Obtains a human readable location in the form: ddºmm'ss.ss"
	 */
	public static String ln_get_humanr_location(double location) {
		double deg = 0.0;
		double min = 0.0;
		double sec = 0.0;

		BigDecimal[] split = new BigDecimal(location)
				.divideAndRemainder(BigDecimal.ONE);
		deg = split[0].doubleValue();
		sec = 60.0 * (split[1].doubleValue());
		if (sec < 0.0)
			sec *= -1.0;
		split = new BigDecimal(sec).divideAndRemainder(BigDecimal.ONE);
		min = split[0].doubleValue();
		sec = 60.0 * (split[1].doubleValue());

		return String.format("%+dº%d'%.2f\"", (int) deg, (int) min, sec);
	}

	/**
	 * double ln_interpolate3 (double n, double y1, double y2, double y3)
	 * \return interpolation value \param n Interpolation factor \param y1
	 * Argument 1 \param y2 Argument 2 \param y3 Argument 3
	 * 
	 * Calculate an intermediate value of the 3 arguments for the given
	 * interpolation factor.
	 */
	public static double ln_interpolate3(double n, double y1, double y2,
			double y3) {
		double y, a, b, c;

		/** equ 3.2 */
		a = y2 - y1;
		b = y3 - y2;
		c = b - a;

		/** equ 3.3 */
		y = y2 + n / 2.0 * (a + b + n * c);

		return y;
	}

	/**
	 * double ln_interpolate5 (double n, double y1, double y2, double y3, double
	 * y4, double y5) \return interpolation value \param n Interpolation factor
	 * \param y1 Argument 1 \param y2 Argument 2 \param y3 Argument 3 \param y4
	 * Argument 4 \param y5 Argument 5
	 * 
	 * Calculate an intermediate value of the 5 arguments for the given
	 * interpolation factor.
	 */
	public static double ln_interpolate5(double n, double y1, double y2,
			double y3, double y4, double y5) {
		double y, A, B, C, D, E, F, G, H, J, K;
		double n2, n3, n4;

		/** equ 3.8 */
		A = y2 - y1;
		B = y3 - y2;
		C = y4 - y3;
		D = y5 - y4;
		E = B - A;
		F = C - B;
		G = D - C;
		H = F - E;
		J = G - F;
		K = J - H;

		y = 0.0;
		n2 = n * n;
		n3 = n2 * n;
		n4 = n3 * n;

		y += y3;
		y += n * ((B + C) / 2.0 - (H + J) / 12.0);
		y += n2 * (F / 2.0 - K / 24.0);
		y += n3 * ((H + J) / 12.0);
		y += n4 * (K / 24.0);

		return y;
	}

	/**
	 * Catches calls to the POSIX gettimeofday and converts them to a related
	 * WIN32 version.
	 */
	public static int gettimeofday(TimeVal tv, Timezone tz) {

		long now = System.currentTimeMillis();
		tv.tv_sec = tv.tv_usec / 100L;
		tv.tv_usec = now - tv.tv_sec;

		tz.tz_dsttime = TimeZone.getDefault().inDaylightTime(new Date(now)) ? 1
				: 0;
		tz.tz_minuteswest = TimeZone.getDefault().getOffset(now) / 60000;

		return 0;
	}

	/** Simple cube root */
	public static double cbrt(double x) {
		return Math.pow(x, 1.0d / 3.0d);
	}

	/** Not a Number function generator */
	public static double nan(String code) {
		return Double.NaN;
	}

	private static int indexOneOf(String string, char... chars) {
		int index = Integer.MAX_VALUE;
		for (int charIndex = 0; charIndex < chars.length; charIndex++) {
			int currentIndex = string.indexOf(chars[index]);
			if (currentIndex >= 0) {
				index = Math.min(currentIndex, index);
			}
		}
		if (index == Integer.MAX_VALUE) {
			return -1;
		}
		return index;
	}
}
