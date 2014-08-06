package org.n4j;

import org.n4j.api.Constants;
import org.n4j.api.LnEquPosn;
import org.n4j.api.LnGalPosn;
import org.n4j.api.LnHelioPosn;
import org.n4j.api.LnHrzPosn;
import org.n4j.api.LnLnlatPosn;
import org.n4j.api.LnNutation;
import org.n4j.api.LnRectPosn;

import static org.n4j.Utility.ln_get_version;
import static org.n4j.Utility.ln_rad_to_deg;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_hms_to_deg;
import static org.n4j.Utility.ln_hms_to_rad;
import static org.n4j.Utility.ln_deg_to_hms;
import static org.n4j.Utility.ln_rad_to_hms;
import static org.n4j.Utility.ln_dms_to_deg;
import static org.n4j.Utility.ln_dms_to_rad;
import static org.n4j.Utility.ln_deg_to_dms;
import static org.n4j.Utility.ln_rad_to_dms;
import static org.n4j.Utility.ln_range_degrees;
import static org.n4j.Utility.ln_range_radians;
import static org.n4j.Utility.ln_range_radians2;
import static org.n4j.Utility.ln_add_secs_hms;
import static org.n4j.Utility.ln_add_hms;
import static org.n4j.Utility.ln_hequ_to_equ;
import static org.n4j.Utility.ln_equ_to_hequ;
import static org.n4j.Utility.ln_hhrz_to_hrz;
import static org.n4j.Utility.ln_hrz_to_hhrz;
import static org.n4j.Utility.ln_hrz_to_nswe;
import static org.n4j.Utility.ln_hlnlat_to_lnlat;
import static org.n4j.Utility.ln_lnlat_to_hlnlat;
import static org.n4j.Utility.ln_get_rect_distance;
import static org.n4j.Utility.ln_get_light_time;
import static org.n4j.Utility.trim;
import static org.n4j.Utility.ln_get_dec_location;
import static org.n4j.Utility.ln_get_humanr_location;
import static org.n4j.Utility.ln_interpolate3;
import static org.n4j.Utility.ln_interpolate5;
import static org.n4j.Utility.gettimeofday;
import static org.n4j.Utility.cbrt;
import static org.n4j.Utility.nan;
import static org.n4j.SiderealTime.ln_get_mean_sidereal_time;
import static org.n4j.SiderealTime.ln_get_apparent_sidereal_time;
import static org.n4j.Nutation.ln_get_nutation;
import static org.n4j.Precession.ln_get_equ_prec2;

public class Transform {
	/*
	 * ! \fn void ln_get_rect_from_helio( ln_helio_posn *object, LnRectPosn
	 * position); \param object Object heliocentric coordinates \param position
	 * Pointer to store new position
	 * 
	 * Transform an objects heliocentric ecliptical coordinates into
	 * heliocentric rectangular coordinates.
	 */
	/*
	 * Equ 37.1 Pg 264
	 */
	public static void ln_get_rect_from_helio(LnHelioPosn object,
			LnRectPosn position) {
		double sin_e, cos_e;
		double cos_B, sin_B, sin_L, cos_L;

		/* ecliptic J2000 */
		sin_e = 0.397777156;
		cos_e = 0.917482062;

		/* calc common values */
		cos_B = Math.cos(ln_deg_to_rad(object.B));
		cos_L = Math.cos(ln_deg_to_rad(object.L));
		sin_B = Math.sin(ln_deg_to_rad(object.B));
		sin_L = Math.sin(ln_deg_to_rad(object.L));

		/* equ 37.1 */
		position.X = object.R * cos_L * cos_B;
		position.Y = object.R * (sin_L * cos_B * cos_e - sin_B * sin_e);
		position.Z = object.R * (sin_L * cos_B * sin_e + sin_B * cos_e);
	}

	/*
	 * ! \fn void ln_get_hrz_from_equ( LnEquPosn object, LnLnlatPosnobserver,
	 * double JD, ln_hrz_posn *position) \param object Object coordinates.
	 * \param observer Observer cordinates. \param JD Julian day \param position
	 * Pointer to store new position.
	 * 
	 * Transform an objects equatorial coordinates into horizontal coordinates
	 * for the given julian day and observers position.
	 * 
	 * 0 deg azimuth = south, 90 deg = west.
	 */
	/*
	 * Equ 12.1,12.2 pg 88
	 * 
	 * TODO: Transform horizontal coordinates to galactic coordinates.
	 */

	public static void ln_get_hrz_from_equ(LnEquPosn object,
			LnLnlatPosn observer, double JD, LnHrzPosn position) {
		double sidereal;

		/* get mean sidereal time in hours */
		sidereal = ln_get_mean_sidereal_time(JD);
		ln_get_hrz_from_equ_sidereal_time(object, observer, sidereal, position);
	}

	public static void ln_get_hrz_from_equ_sidereal_time(LnEquPosn object,
			LnLnlatPosn observer, double sidereal, LnHrzPosn position) {
		// ritchie use BigDecimal here (was long double)
		double H, ra, latitude, declination, A, Ac, As, h, Z, Zs;

		/* change sidereal_time from hours to radians */
		sidereal *= 2.0 * Math.PI / 24.0;

		/* calculate hour angle of object at observers position */
		ra = ln_deg_to_rad(object.ra);
		H = sidereal + ln_deg_to_rad(observer.lng) - ra;

		/* hence formula 12.5 and 12.6 give */
		/*
		 * convert to radians - hour angle, observers latitude, object
		 * declination
		 */
		latitude = ln_deg_to_rad(observer.lat);
		declination = ln_deg_to_rad(object.dec);

		/* formula 12.6 *; missuse of A (you have been warned) */
		A = Math.sin(latitude) * Math.sin(declination) + Math.cos(latitude)
				* Math.cos(declination) * Math.cos(H);
		h = Math.asin(A);

		/* convert back to degrees */
		position.alt = ln_rad_to_deg(h);

		/* zenith distance, Telescope Control 6.8a */
		Z = Math.acos(A);

		/* is'n there better way to compute that? */
		Zs = Math.sin(Z);

		/* sane check for zenith distance; don't try to divide by 0 */
		if (Math.abs(Zs) < 1e-5) {
			if (object.dec > 0.0)
				position.az = 180.0;
			else
				position.az = 0.0;
			if ((object.dec > 0.0 && observer.lat > 0.0)
					|| (object.dec < 0.0 && observer.lat < 0.0))
				position.alt = 90.0;
			else
				position.alt = -90.0;
			return;
		}

		/* formulas TC 6.8d Taff 1991, pp. 2 and 13 - vector transformations */
		As = (Math.cos(declination) * Math.sin(H)) / Zs;
		Ac = (Math.sin(latitude) * Math.cos(declination) * Math.cos(H) - Math
				.cos(latitude) * Math.sin(declination))
				/ Zs;

		// don't blom at atan2
		if (Ac == 0.0 && As == 0.0) {
			if (object.dec > 0)
				position.az = 180.0;
			else
				position.az = 0.0;
			return;
		}
		A = Math.atan2(As, Ac);

		/* convert back to degrees */
		position.az = ln_range_degrees(ln_rad_to_deg(A));
	}

	/*
	 * ! \fn void ln_get_equ_from_hrz( ln_hrz_posn *object, LnLnlatPosnobserver,
	 * double JD, LnEquPosn position) \param object Object coordinates. \param
	 * observer Observer cordinates. \param JD Julian day \param position
	 * Pointer to store new position.
	 * 
	 * Transform an objects horizontal coordinates into equatorial coordinates
	 * for the given julian day and observers position.
	 */
	public static void ln_get_equ_from_hrz(LnHrzPosn object,
			LnLnlatPosn observer, double JD, LnEquPosn position) {
		// ritchie use BigDecimal here (was long double)
		double H, longitude, declination, latitude, A, h, sidereal;

		/* change observer/object position into radians */

		/* object alt/az */
		A = ln_deg_to_rad(object.az);
		h = ln_deg_to_rad(object.alt);

		/* observer long / lat */
		longitude = ln_deg_to_rad(observer.lng);
		latitude = ln_deg_to_rad(observer.lat);

		/* equ on pg89 */
		H = Math.atan2(
				Math.sin(A),
				(Math.cos(A) * Math.sin(latitude) + Math.tan(h)
						* Math.cos(latitude)));
		declination = Math.sin(latitude) * Math.sin(h) - Math.cos(latitude)
				* Math.cos(h) * Math.cos(A);
		declination = Math.asin(declination);

		/* get ra = sidereal - longitude + H and change sidereal to radians */
		sidereal = ln_get_apparent_sidereal_time(JD);
		sidereal *= 2.0 * Math.PI / 24.0;

		position.ra = ln_range_degrees(ln_rad_to_deg(sidereal - H + longitude));
		position.dec = ln_rad_to_deg(declination);
	}

	/*
	 * ! \fn void ln_get_equ_from_ecl( LnLnlatPosnobject, double JD, LnEquPosn
	 * position) \param object Object coordinates. \param JD Julian day \param
	 * position Pointer to store new position.
	 * 
	 * Transform an objects ecliptical coordinates into equatorial coordinates
	 * for the given julian day.
	 */
	/*
	 * Equ 12.3, 12.4 pg 89
	 */
	public static void ln_get_equ_from_ecl(LnLnlatPosn object, double JD,
			LnEquPosn position) {
		double ra, declination, longitude, latitude;
		LnNutation nutation = new LnNutation();

		/* get obliquity of ecliptic and change it to rads */
		ln_get_nutation(JD, nutation);
		nutation.ecliptic = ln_deg_to_rad(nutation.ecliptic);

		/* change object's position into radians */

		/* object */
		longitude = ln_deg_to_rad(object.lng);
		latitude = ln_deg_to_rad(object.lat);

		/* Equ 12.3, 12.4 */
		ra = Math.atan2(
				(Math.sin(longitude) * Math.cos(nutation.ecliptic) - Math
						.tan(latitude) * Math.sin(nutation.ecliptic)),
				Math.cos(longitude));
		declination = Math.sin(latitude) * Math.cos(nutation.ecliptic)
				+ Math.cos(latitude) * Math.sin(nutation.ecliptic)
				* Math.sin(longitude);
		declination = Math.asin(declination);

		/* store in position */
		position.ra = ln_range_degrees(ln_rad_to_deg(ra));
		position.dec = ln_rad_to_deg(declination);
	}

	/*
	 * ! \fn void ln_get_ecl_from_equ( LnEquPosn object, double JD,
	 * LnLnlatPosnposition) \param object Object coordinates in B1950. Use
	 * ln_get_equ_prec2 to transform from J2000. \param JD Julian day \param
	 * position Pointer to store new position.
	 * 
	 * Transform an objects equatorial cordinates into ecliptical coordinates
	 * for the given julian day.
	 */
	/*
	 * Equ 12.1, 12.2 Pg 88
	 */
	public static void ln_get_ecl_from_equ(LnEquPosn object, double JD,
			LnLnlatPosn position) {
		double ra, declination, latitude, longitude;
		LnNutation nutation = new LnNutation();

		/* object position */
		ra = ln_deg_to_rad(object.ra);
		declination = ln_deg_to_rad(object.dec);
		ln_get_nutation(JD, nutation);
		nutation.ecliptic = ln_deg_to_rad(nutation.ecliptic);

		/* Equ 12.1, 12.2 */
		longitude = Math.atan2(
				(Math.sin(ra) * Math.cos(nutation.ecliptic) + Math
						.tan(declination) * Math.sin(nutation.ecliptic)),
				Math.cos(ra));
		latitude = Math.sin(declination) * Math.cos(nutation.ecliptic)
				- Math.cos(declination) * Math.sin(nutation.ecliptic)
				* Math.sin(ra);
		latitude = Math.asin(latitude);

		/* store in position */
		position.lat = ln_rad_to_deg(latitude);
		position.lng = ln_range_degrees(ln_rad_to_deg(longitude));
	}

	/*
	 * ! \fn void ln_get_ecl_from_rect( LnRectPosn rect, LnLnlatPosnposn) \param
	 * rect Rectangular coordinates. \param posn Pointer to store new position.
	 * 
	 * Transform an objects rectangular coordinates into ecliptical coordinates.
	 */
	/*
	 * Equ 33.2
	 */
	public static void ln_get_ecl_from_rect(LnRectPosn rect, LnLnlatPosn posn) {
		double t;

		t = Math.sqrt(rect.X * rect.X + rect.Y * rect.Y);
		posn.lng = ln_range_degrees(ln_rad_to_deg(Math.atan2(rect.X, rect.Y)));
		posn.lat = ln_rad_to_deg(Math.atan2(t, rect.Z));
	}

	/*
	 * ! \fn void ln_get_equ_from_gal( LnGalPosn gal, LnEquPosn equ) \param gal
	 * Galactic coordinates. \param equ B1950 equatorial coordinates. Use
	 * ln_get_equ_prec2 to transform to J2000.
	 * 
	 * Transform an object galactic coordinates into B1950 equatorial
	 * coordinate.
	 */
	/* Pg 94 */
	public static void ln_get_equ_from_gal(LnGalPosn gal, LnEquPosn equ) {
		double RAD_27_4, SIN_27_4, COS_27_4;
		double l_123, cos_l_123;
		double sin_b, cos_b, rad_gal_b;
		double y;

		RAD_27_4 = ln_deg_to_rad(27.4);
		SIN_27_4 = Math.sin(RAD_27_4);
		COS_27_4 = Math.cos(RAD_27_4);

		l_123 = ln_deg_to_rad(gal.l - 123);
		cos_l_123 = Math.cos(l_123);

		rad_gal_b = ln_deg_to_rad(gal.b);

		sin_b = Math.sin(rad_gal_b);
		cos_b = Math.cos(rad_gal_b);

		y = Math.atan2(Math.sin(l_123), cos_l_123 * SIN_27_4 - (sin_b / cos_b)
				* COS_27_4);
		equ.ra = ln_range_degrees(ln_rad_to_deg(y) + 12.25);
		equ.dec = ln_rad_to_deg(Math.asin(sin_b * SIN_27_4 + cos_b * COS_27_4
				* cos_l_123));
	}

	/*
	 * ! \fn void ln_get_equ2000_from_gal( LnGalPosn gal, LnEquPosn equ) \param
	 * gal Galactic coordinates. \param equ J2000 equatorial coordinates.
	 * 
	 * Transform an object galactic coordinates into equatorial coordinate.
	 */
	public static void ln_get_equ2000_from_gal(LnGalPosn gal, LnEquPosn equ) {
		ln_get_equ_from_gal(gal, equ);
		ln_get_equ_prec2(equ, Constants.B1950, Constants.JD2000, equ);
	}

	/*
	 * ! \fn ln_get_gal_from_equ( LnEquPosn equ, LnGalPosn gal) \param equ B1950
	 * equatorial coordinates. \param gal Galactic coordinates.
	 * 
	 * Transform an object B1950 equatorial coordinate into galactic
	 * coordinates.
	 */
	/* Pg 94 */
	public static void ln_get_gal_from_equ(LnEquPosn equ, LnGalPosn gal) {
		double RAD_27_4, SIN_27_4, COS_27_4;
		double ra_192_25, cos_ra_192_25;
		double rad_equ_dec;
		double cos_dec, sin_dec;
		double x;

		RAD_27_4 = ln_deg_to_rad(27.4);
		SIN_27_4 = Math.sin(RAD_27_4);
		COS_27_4 = Math.cos(RAD_27_4);

		ra_192_25 = ln_deg_to_rad(192.25 - equ.ra);
		cos_ra_192_25 = Math.cos(ra_192_25);

		rad_equ_dec = ln_deg_to_rad(equ.dec);

		sin_dec = Math.sin(rad_equ_dec);
		cos_dec = Math.cos(rad_equ_dec);

		x = Math.atan2(Math.sin(ra_192_25), cos_ra_192_25 * SIN_27_4
				- (sin_dec / cos_dec) * COS_27_4);
		gal.l = ln_range_degrees(303 - ln_rad_to_deg(x));
		gal.b = ln_rad_to_deg(Math.asin(sin_dec * SIN_27_4 + cos_dec * COS_27_4
				* cos_ra_192_25));
	}

	/*
	 * ! \fn void ln_get_gal_from_equ2000( LnEquPosn equ, LnGalPosn gal) \param
	 * equ J2000 equatorial coordinates. \param gal Galactic coordinates.
	 * 
	 * Transform an object J2000 equatorial coordinate into galactic
	 * coordinates.
	 */
	public static void ln_get_gal_from_equ2000(LnEquPosn equ, LnGalPosn gal) {
		LnEquPosn equ_1950 = new LnEquPosn();
		ln_get_equ_prec2(equ, Constants.JD2000, Constants.B1950, equ_1950);
		ln_get_gal_from_equ(equ_1950, gal);
	}

	/*
	 * ! \example transforms.c
	 * 
	 * Examples of how to use transformation functions.
	 */
}
