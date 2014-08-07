package org.n4j;

import java.math.BigDecimal;

import org.n4j.api.LnEquPosn;
import org.n4j.api.LnLnlatPosn;
import org.n4j.api.LnRstTime;

import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.tan;
import static java.lang.Math.asin;
import static java.lang.Math.acos;
import static java.lang.Math.atan;
import static java.lang.Math.abs;

import static org.n4j.DynamicalTime.ln_get_jde;
import static org.n4j.Nutation.ln_get_nutation;
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_range_degrees;
       
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
import static org.n4j.Utility.ln_deg_to_rad;
import static org.n4j.Utility.ln_rad_to_deg;
import static org.n4j.Utility.ln_range_degrees;
import static org.n4j.JulianDay.ln_get_julian_day;
import static org.n4j.JulianDay.ln_get_day_of_week;
import static org.n4j.JulianDay.ln_get_date;
import static org.n4j.JulianDay.ln_get_date_from_sys;
import static org.n4j.JulianDay.ln_get_julian_from_sys;
import static org.n4j.JulianDay.ln_get_julian_local_date;
import static org.n4j.JulianDay.ln_get_date_from_mpc;
import static org.n4j.JulianDay.ln_get_julian_from_mpc;
import static org.n4j.JulianDay.ln_date_to_zonedate;
import static org.n4j.JulianDay.ln_zonedate_to_date;
import static org.n4j.JulianDay.ln_get_tms_from_julian;
import static org.n4j.JulianDay.ln_get_julian_from_tms;
import static org.n4j.api.Constants.JD2000;
import static org.n4j.api.Constants.B1900;
import static org.n4j.api.Constants.B1950;
import static org.n4j.api.Constants.JD2050;

public class RiseSet {
	static final double LN_STAR_STANDART_HORIZON	=	-0.5667;
	// helper function to check if object can be visible
	public static int check_coords(LnLnlatPosn observer, double H1,
		double horizon, LnEquPosn object)
	{
		double h;

		/* check if body is circumpolar */
		if (Math.abs(H1) > 1.0) {
			/* check if maximal height < horizon */
			// h = asin(cos(ln_deg_to_rad(observer.lat - object.dec)))
			h = 90.0 + object.dec - observer.lat;
			// normalize to <-90;+90>
			if (h > 90.0)
				h = 180.0 - h;
			if (h < -90.0)
			  	h = -180.0 - h;
			if (h < horizon)
				return -1;
			// else it must be above horizon
			return 1;
		}
		return 0;
	}

	/*! \fn int ln_get_object_rst(double JD, LnLnlatPosn observer, LnEquPosn object, LnRstTime rst);
	* \param JD Julian day
	* \param observer Observers position
	* \param object Object position
	* \param rst Pointer to store Rise, Set and Transit time in JD
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time the rise, set and transit (crosses the local meridian at upper culmination)
	* time of the object for the given Julian day.
	*
	* Note: this functions returns 1 if the object is circumpolar, that is it remains the whole
	* day above the horizon. Returns -1 when it remains the whole day bellow the horizon.
	*/
	public static int ln_get_object_rst(double JD, LnLnlatPosn observer,
		LnEquPosn object, LnRstTime rst)
	{
		return ln_get_object_rst_horizon(JD, observer, object,
			LN_STAR_STANDART_HORIZON, rst);	/* standard altitude of stars */
	}

	/*! \fn int ln_get_object_rst_horizon(double JD, LnLnlatPosn observer, LnEquPosn object, long double horizon, LnRstTime rst);
	* \param JD Julian day
	* \param observer Observers position
	* \param object Object position
	* \param horizon Horizon height
	* \param rst Pointer to store Rise, Set and Transit time in JD
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time the rise, set and transit (crosses the local meridian at upper culmination)
	* time of the object for the given Julian day and horizon.
	*
	* Note: this functions returns 1 if the object is circumpolar, that is it remains the whole
	* day above the horizon. Returns -1 when it remains whole day bellow the horizon.
	*/
	public static int ln_get_object_rst_horizon(double JD, LnLnlatPosn observer,
		LnEquPosn object, BigDecimal horizon, LnRstTime rst)
	{
		return ln_get_object_rst_horizon_offset(JD, observer, object, horizon,
				rst, 0.5);
	}

	public static int ln_get_object_rst_horizon_offset(double JD, LnLnlatPosn observer,
		LnEquPosn object, BigDecimal horizon, LnRstTime rst,
		double ut_offset)
	{
		int jd;
		//TODO BigDecimal
		/*long*/double O, JD_UT, H0, H1;
		double Hat, Har, Has, altr, alts;
		double mt, mr, ms, mst, msr, mss;
		double dmt, dmr, dms;
		int ret, i;

		if (Double.isNaN(ut_offset)) {
			JD_UT = JD;
		} else {
			/* convert local sidereal time into degrees
				 for 0h of UT on day JD */
			jd = (int)JD;
			JD_UT = jd + ut_offset;
		}

		O = ln_get_apparent_sidereal_time(JD_UT);
		O *= 15.0;

		/* equ 15.1 */
		H0 = (sin(ln_deg_to_rad(horizon.doubleValue())) -
			 sin(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(object.dec)));
		H1 = (cos(ln_deg_to_rad(observer.lat)) * cos(ln_deg_to_rad(object.dec)));

		H1 = H0 / H1;

		ret = check_coords(observer, H1, horizon, object);
		if (ret)
			return ret;

		H0 = acos(H1);
		H0 = ln_rad_to_deg(H0);

		/* equ 15.2 */
		mt = (object.ra - observer.lng - O) / 360.0;
		mr = mt - H0 / 360.0;
		ms = mt + H0 / 360.0;

		for (i = 0; i < 3; i++) {
			/* put in correct range */
			if (mt > 1.0)
				mt--;
			else if (mt < 0)
				mt++;
			if (mr > 1.0)
				mr--;
			else if (mr < 0)
				mr++;
			if (ms > 1.0)
				ms--;
			else if (ms < 0)
				ms++;

			/* find sidereal time at Greenwich, in degrees, for each m */
			mst = O + 360.985647 * mt;
			msr = O + 360.985647 * mr;
			mss = O + 360.985647 * ms;

			/* find local hour angle */
			Hat = mst + observer.lng - object.ra;
			Har = msr + observer.lng - object.ra;
			Has = mss + observer.lng - object.ra;

			/* find altitude for rise and set */
			altr = sin(ln_deg_to_rad(observer.lat)) *
				sin(ln_deg_to_rad(object.dec)) +
				cos(ln_deg_to_rad(observer.lat)) *
				cos(ln_deg_to_rad(object.dec)) *
				cos(ln_deg_to_rad(Har));
			alts = sin(ln_deg_to_rad(observer.lat)) *
				sin(ln_deg_to_rad(object.dec)) +
				cos(ln_deg_to_rad(observer.lat)) *
				cos(ln_deg_to_rad(object.dec)) *
				cos(ln_deg_to_rad(Has));

			/* must be in degrees */
			altr = ln_rad_to_deg(altr);
			alts = ln_rad_to_deg(alts);

			/* corrections for m */
			ln_range_degrees(Hat);
			if (Hat > 180.0)
				Hat -= 360;

			dmt = -(Hat / 360.0);
			dmr = (altr - horizon.doubleValue()) / (360 * cos(ln_deg_to_rad(object.dec)) *
				cos(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(Har)));
			dms = (alts - horizon.doubleValue()) / (360 * cos(ln_deg_to_rad(object.dec)) *
				cos(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(Has)));

			/* add corrections and change to JD */
			mt += dmt;
			mr += dmr;
			ms += dms;

			if (mt <= 1.0 && mt >= 0.0 &&
				mr <= 1.0 && mr >= 0.0 &&
				ms <= 1.0 && ms >= 0.0)
				break;
		}

		rst.rise = JD_UT + mr;
		rst.transit = JD_UT + mt;
		rst.set = JD_UT + ms;

		/* not circumpolar */
		return 0;
	}

	/*! \fn int ln_get_object_next_rst(double JD, LnLnlatPosn observer, LnEquPosn object, LnRstTime rst);
	* \param JD Julian day
	* \param observer Observers position
	* \param object Object position
	* \param rst Pointer to store Rise, Set and Transit time in JD
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time of next rise, set and transit (crosses the local meridian at upper culmination)
	* time of the object for the given Julian day and horizon.
	*
	* This function guarantee, that rise, set and transit will be in <JD, JD+1> range.
	*
	* Note: this functions returns 1 if the object is circumpolar, that is it remains the whole
	* day above the horizon. Returns -1 when it remains whole day bellow the horizon.
	*/
	int ln_get_object_next_rst(double JD, LnLnlatPosn observer,
		LnEquPosn object, LnRstTime rst)
	{
		return ln_get_object_next_rst_horizon(JD, observer, object,
			LN_STAR_STANDART_HORIZON, rst);
	}

	//helper functions for ln_get_object_next_rst_horizon
	static void set_next_rst(LnRstTime rst, double diff,
			LnRstTime out)
	{
		out.rise = rst.rise + diff;
		out.transit = rst.transit + diff;
		out.set = rst.set + diff;
	}

	static double find_next(double JD, double jd1, double jd2, double jd3)
	{
		if (Double.isNaN(jd1) && Double.isNaN(jd2))
			return jd3;

		if(JD < jd1)
			return jd1;

		if(JD < jd2)
			return jd2;

		return jd3;
	}

	/*! \fn int ln_get_object_next_rst_horizon(double JD, LnLnlatPosn observer, LnEquPosn object, double horizon, LnRstTime rst);
	* \param JD Julian day
	* \param observer Observers position
	* \param object Object position
	* \param horizon Horizon height
	* \param rst Pointer to store Rise, Set and Transit time in JD
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time of next rise, set and transit (crosses the local meridian at upper culmination)
	* time of the object for the given Julian day and horizon.
	*
	* This function guarantee, that rise, set and transit will be in <JD, JD+1> range.
	*
	* Note: this functions returns 1 if the object is circumpolar, that is it remains the whole
	* day above the horizon. Returns -1 when it remains whole day bellow the horizon.
	*/
	int ln_get_object_next_rst_horizon(double JD, LnLnlatPosn observer,
		LnEquPosn object, double horizon, LnRstTime rst)
	{
		int ret;
		LnRstTime rst_1, rst_2;

		ret = ln_get_object_rst_horizon_offset(JD, observer, object, horizon, rst, nan("0"));
		if (ret)
			// circumpolar
			return ret;

		if (rst.rise > (JD + 0.5) || rst.transit > (JD + 0.5)
				|| rst.set > (JD + 0.5))
			ln_get_object_rst_horizon_offset(JD - 1.0, observer, object, horizon,
					rst_1, nan("0"));
		else
			set_next_rst(rst, -1.0, &rst_1);

		if (rst.rise < JD || rst.transit < JD || rst.set < JD)
			ln_get_object_rst_horizon_offset(JD + 1.0, observer, object, horizon,
					rst_2, nan("0"));
		else
			set_next_rst (rst, 1.0, rst_2);

		rst.rise = find_next(JD, rst_1.rise, rst.rise, rst_2.rise);
		rst.transit = find_next(JD, rst_1.transit, rst.transit, rst_2.transit);
		rst.set = find_next(JD, rst_1.set, rst.set, rst_2.set);

		if (Double.isNaN (rst.rise))
			return ret;

		return 0;
	}

	/*! \fn int ln_get_body_rst_horizon(double JD, LnLnlatPosn observer, void (*get_equ_body_coords) (double, LnEquPosn ), double horizon, LnRstTime rst); 
	* \param JD Julian day 
	* \param observer Observers position 
	* \param get_equ_body_coords Pointer to get_equ_body_coords() function
	* \param horizon Horizon, see LN_XXX_HORIZON constants
	* \param rst Pointer to store Rise, Set and Transit time in JD 
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time the rise, set and transit (crosses the local meridian at
	* upper culmination) time of the body for the given Julian day and given
	* horizon.
	*
	*
	* Note 1: this functions returns 1 if the object is circumpolar, that is it remains the whole
	* day above the horizon. Returns -1 when it remains whole day bellow the horizon.
	*
	* Note 2: this function will not work for body, which ra changes more
	* then 180 deg in one day (get_equ_body_coords changes so much). But
	* you should't use that function for any body which moves to fast..use
	* some special function for such things.
	*/
	int ln_get_body_rst_horizon(double JD, LnLnlatPosn observer,
		void (*get_equ_body_coords) (double,LnEquPosn ), double horizon,
		LnRstTime rst)
	{
		return ln_get_body_rst_horizon_offset(JD, observer, get_equ_body_coords,
			horizon, rst, 0.5);
	}

	int ln_get_body_rst_horizon_offset(double JD, LnLnlatPosn observer,
		void (*get_equ_body_coords) (double,LnEquPosn ), double horizon,
		LnRstTime rst, double ut_offset)
	{
		int jd;
		double T, O, JD_UT, H0, H1;
		double Hat, Har, Has, altr, alts;
		double mt, mr, ms, mst, msr, mss, nt, nr, ns;
		struct ln_equ_posn sol1, sol2, sol3, post, posr, poss;
		double dmt, dmr, dms;
		int ret, i;

		/* dynamical time diff */
		T = ln_get_dynamical_time_diff(JD);

		if (Double.isNaN(ut_offset)) {
			JD_UT = JD;
		} else {
			jd = (int)JD;
			JD_UT = jd + ut_offset;
		}
		/* convert local sidereal time into degrees
			 for 0h of UT on day JD */
		JD_UT = JD;
		O = ln_get_apparent_sidereal_time(JD_UT);
		O *= 15.0;

		/* get body coords for JD_UT -1, JD_UT and JD_UT + 1 */
		get_equ_body_coords(JD_UT - 1.0, &sol1);
		get_equ_body_coords(JD_UT, &sol2);
		get_equ_body_coords(JD_UT + 1.0, &sol3);

		/* equ 15.1 */
		H0 =
			(sin(ln_deg_to_rad(horizon)) -
			 sin(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(sol2.dec)));
		H1 = (cos(ln_deg_to_rad(observer.lat)) * cos(ln_deg_to_rad(sol2.dec)));

		H1 = H0 / H1;

		ret = check_coords (observer, H1, horizon, &sol2);
		if (ret)
			return ret;

		H0 = acos(H1);
		H0 = ln_rad_to_deg(H0);

		/* correct ra values for interpolation	- put them to the same side of circle */
		if ((sol1.ra - sol2.ra) > 180.0)
			sol2.ra += 360.0;

		if ((sol2.ra - sol3.ra) > 180.0)
			sol3.ra += 360.0;

		if ((sol3.ra - sol2.ra) > 180.0)
			sol3.ra -= 360.0;

		if ((sol2.ra - sol1.ra) > 180.0)
			sol3.ra -= 360.0;

		/* equ 15.2 */
		mt = (sol2.ra - observer.lng - O) / 360.0;
		mr = mt - H0 / 360.0;
		ms = mt + H0 / 360.0;

		for (i = 0; i < 3; i++) {
			/* put in correct range */
			if (mt > 1.0)
				mt--;
			else if (mt < 0)
				mt++;
			if (mr > 1.0)
				mr--;
			else if (mr < 0)
				mr++;
			if (ms > 1.0)
				ms--;
			else if (ms < 0)
				ms++;

			/* find sidereal time at Greenwich, in degrees, for each m */
			mst = O + 360.985647 * mt;
			msr = O + 360.985647 * mr;
			mss = O + 360.985647 * ms;
		
			nt = mt + T / 86400.0;
			nr = mr + T / 86400.0;
			ns = ms + T / 86400.0;

			/* interpolate ra and dec for each m, except for transit dec (dec2) */
			posr.ra = ln_interpolate3(nr, sol1.ra, sol2.ra, sol3.ra);
			posr.dec = ln_interpolate3(nr, sol1.dec, sol2.dec, sol3.dec);
			post.ra = ln_interpolate3(nt, sol1.ra, sol2.ra, sol3.ra);
			poss.ra = ln_interpolate3(ns, sol1.ra, sol2.ra, sol3.ra);
			poss.dec = ln_interpolate3(ns, sol1.dec, sol2.dec, sol3.dec);

			/* find local hour angle */
			Hat = mst + observer.lng - post.ra;
			Har = msr + observer.lng - posr.ra;
			Has = mss + observer.lng - poss.ra;

			/* find altitude for rise and set */
			altr = sin(ln_deg_to_rad(observer.lat)) *
					sin(ln_deg_to_rad(posr.dec)) +
					cos(ln_deg_to_rad(observer.lat)) *
					cos(ln_deg_to_rad(posr.dec)) *
					cos(ln_deg_to_rad(Har));
			alts = sin(ln_deg_to_rad(observer.lat)) *
					sin(ln_deg_to_rad(poss.dec)) +
					cos(ln_deg_to_rad(observer.lat)) *
					cos(ln_deg_to_rad(poss.dec)) *
					cos(ln_deg_to_rad(Has));

			/* must be in degrees */
			altr = ln_rad_to_deg(altr);
			alts = ln_rad_to_deg(alts);

			/* corrections for m */
			ln_range_degrees(Hat);
			if (Hat > 180.0)
				Hat -= 360;

			dmt = -(Hat / 360.0);
			dmr = (altr - horizon) / (360.0 * cos(ln_deg_to_rad(posr.dec)) *
				cos(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(Har)));
			dms = (alts - horizon) / (360.0 * cos(ln_deg_to_rad(poss.dec)) *
				cos(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(Has)));

			/* add corrections and change to JD */
			mt += dmt;
			mr += dmr;
			ms += dms;

			if (mt <= 1.0 && mt >= 0.0 &&
				mr <= 1.0 && mr >= 0.0 &&
				ms <= 1.0 && ms >= 0.0)
				break;
		}

		rst.rise = JD_UT + mr;
		rst.transit = JD_UT + mt;
		rst.set = JD_UT + ms;

		/* not circumpolar */
		return 0;
	}

	/*! \fn int ln_get_body_next_rst_horizon(double JD, LnLnlatPosn observer, LnEquPosn object, double horizon, LnRstTime rst);
	* \param JD Julian day 
	* \param observer Observers position 
	* \param get_equ_body_coords Pointer to get_equ_body_coords() function
	* \param horizon Horizon, see LN_XXX_HORIZON constants
	* \param rst Pointer to store Rise, Set and Transit time in JD 
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time of next rise, set and transit (crosses the local meridian at
	* upper culmination) time of the body for the given Julian day and given
	* horizon.
	*
	* This function guarantee, that rise, set and transit will be in <JD, JD+1> range.
	*
	* Note 1: this functions returns 1 if the body is circumpolar, that is it remains
	* the whole day either above or below the horizon.
	*
	* Note 2: This function will not work for body, which ra changes more
	* then 180 deg in one day (get_equ_body_coords changes so much). But
	* you should't use that function for any body which moves to fast..use
	* some special function for such things.
	*/
	int ln_get_body_next_rst_horizon(double JD, LnLnlatPosn observer,
		void (*get_equ_body_coords) (double,LnEquPosn ), double horizon,
		LnRstTime rst)
	{
		return ln_get_body_next_rst_horizon_future(JD, observer,
			get_equ_body_coords, horizon, 1, rst);
	}

	/*! \fn int ln_get_body_next_rst_horizon_future(double JD, LnLnlatPosn observer, void (*get_equ_body_coords) (double,LnEquPosn ), double horizon, int day_limit, LnRstTime rst);
	* \param JD Julian day 
	* \param observer Observers position 
	* \param get_equ_body_coords Pointer to get_equ_body_coords() function
	* \param horizon Horizon, see LN_XXX_HORIZON constants
	* \param day_limit Maximal number of days that will be searched for next rise and set
	* \param rst Pointer to store Rise, Set and Transit time in JD 
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time of next rise, set and transit (crosses the local meridian at
	* upper culmination) time of the body for the given Julian day and given
	* horizon.
	*
	* This function guarantee, that rise, set and transit will be in <JD, JD + day_limit> range.
	*
	* Note 1: this functions returns 1 if the body is circumpolar, that is it remains
	* the whole day either above or below the horizon.
	*
	* Note 2: This function will not work for body, which ra changes more
	* than 180 deg in one day (get_equ_body_coords changes so much). But
	* you should't use that function for any body which moves to fast..use
	* some special function for such things.
	*/
	int ln_get_body_next_rst_horizon_future(double JD,
		LnLnlatPosn observer,
		void (*get_equ_body_coords) (double,LnEquPosn ),
		double horizon, int day_limit, LnRstTime rst)
	{
		int ret;
		struct ln_rst_time rst_1, rst_2;

		ret = ln_get_body_rst_horizon_offset(JD, observer, get_equ_body_coords,
			horizon, rst, nan("0"));
		if (ret && day_limit == 1)
			// circumpolar
			return ret;

		if (!ret &&
			(rst.rise >(JD + 0.5) || rst.transit >(JD + 0.5) ||
			rst.set >(JD + 0.5))) {

			ret = ln_get_body_rst_horizon_offset(JD - 1, observer,
				get_equ_body_coords, horizon, &rst_1, nan ("0"));
			if (ret)
				set_next_rst (rst, -1, &rst_1);
		} else {
			rst.rise = nan("0");
			rst.transit = nan("0");
			rst.set = nan("0");

			set_next_rst(rst, -1, &rst_1);
		}

		if (ret || (rst.rise < JD || rst.transit < JD || rst.set < JD)) {
		  	// find next day when it will rise, up to day_limit days
			int day = 1;

			while (day <= day_limit) {
				ret = ln_get_body_rst_horizon_offset(JD + day, observer,
					get_equ_body_coords, horizon, &rst_2, nan ("0"));

				if (!ret) {
					day = day_limit + 2;
					break;
				}
				day++;
			}
			if (day == day_limit + 1)
				// it's then really circumpolar in searched period
				return ret;
		} else {
			set_next_rst(rst, +1, &rst_2);
		}

		rst.rise = find_next(JD, rst_1.rise, rst.rise, rst_2.rise);
		rst.transit = find_next(JD, rst_1.transit, rst.transit, rst_2.transit);
		rst.set = find_next(JD, rst_1.set, rst.set, rst_2.set);
		if (Double.isNaN (rst.rise))
			return ret;

		return 0;
	}

	/*! \fn int ln_get_body_rst_horizon(double JD, LnLnlatPosn observer, void (*get_equ_body_coords) (double, LnEquPosn ), double horizon, LnRstTime rst); 
	* \param JD Julian day 
	* \param observer Observers position 
	* \param get_motion_body_coords Pointer to ln_get_ell_body_equ_coords. ln_get_para_body_equ_coords or ln_get_hyp_body_equ_coords function
	* \param horizon Horizon, see LN_XXX_HORIZON constants
	* \param rst Pointer to store Rise, Set and Transit time in JD 
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time the rise, set and transit (crosses the local meridian at
	* upper culmination) time of the body for the given Julian day and given
	* horizon.
	*
	* Note 1: this functions returns 1 if the body is circumpolar, that is it remains
	* the whole day either above or below the horizon.
	*/
	int ln_get_motion_body_rst_horizon(double JD, LnLnlatPosn observer,
		get_motion_body_coords_t get_motion_body_coords,
		void * orbit, double horizon, LnRstTime rst)
	{
		return ln_get_motion_body_rst_horizon_offset(JD, observer,
			get_motion_body_coords, orbit, horizon, rst, 0.5);
	}

	int ln_get_motion_body_rst_horizon_offset(double JD,
		LnLnlatPosn observer,
		get_motion_body_coords_t get_motion_body_coords, void *orbit,
		double horizon, LnRstTime rst, double ut_offset)
	{
		int jd;
		double T, O, JD_UT, H0, H1;
		double Hat, Har, Has, altr, alts;
		double mt, mr, ms, mst, msr, mss, nt, nr, ns;
		struct ln_equ_posn sol1, sol2, sol3, post, posr, poss;
		double dmt, dmr, dms;
		int ret, i;
			
		/* dynamical time diff */
		T = ln_get_dynamical_time_diff(JD);

		if (Double.isNaN(ut_offset)) {
			JD_UT = JD;
		} else {
			jd = (int)JD;
			JD_UT = jd + ut_offset;
		}
		O = ln_get_apparent_sidereal_time(JD_UT);
		O *= 15.0;
		
		/* get body coords for JD_UT -1, JD_UT and JD_UT + 1 */
		get_motion_body_coords(JD_UT - 1.0, orbit, &sol1);
		get_motion_body_coords(JD_UT, orbit, &sol2);
		get_motion_body_coords(JD_UT + 1.0, orbit, &sol3);
		
		/* equ 15.1 */
		H0 = (sin(ln_deg_to_rad(horizon)) - sin(ln_deg_to_rad(observer.lat)) *
				sin(ln_deg_to_rad(sol2.dec)));
		H1 = (cos(ln_deg_to_rad(observer.lat)) * cos(ln_deg_to_rad(sol2.dec)));

		H1 = H0 / H1;

		ret = check_coords(observer, H1, horizon, &sol2);
		if (ret)
			return ret;

		H0 = acos(H1);
		H0 = ln_rad_to_deg(H0);

		/* correct ra values for interpolation	- put them to the same side of circle */
		if ((sol1.ra - sol2.ra) > 180.0)
			sol2.ra += 360.0;

		if ((sol2.ra - sol3.ra) > 180.0)
			sol3.ra += 360.0;

		if ((sol3.ra - sol2.ra) > 180.0)
			sol3.ra -= 360.0;

		if ((sol2.ra - sol1.ra) > 180.0)
			sol3.ra -= 360.0;

		for (i = 0; i < 3; i++) {
			/* equ 15.2 */
			mt = (sol2.ra - observer.lng - O) / 360.0;
			mr = mt - H0 / 360.0;
			ms = mt + H0 / 360.0;

			/* put in correct range */
			if (mt > 1.0 )
				mt--;
			else if (mt < 0.0)
				mt++;
			if (mr > 1.0 )
				mr--;
			else if (mr < 0.0)
				mr++;
			if (ms > 1.0 )
				ms--;
			else if (ms < 0.0)
				ms++;
		
			/* find sidereal time at Greenwich, in degrees, for each m*/
			mst = O + 360.985647 * mt;
			msr = O + 360.985647 * mr;
			mss = O + 360.985647 * ms;

			nt = mt + T / 86400.0;
			nr = mr + T / 86400.0;
			ns = ms + T / 86400.0;
		
			/* interpolate ra and dec for each m, except for transit dec (dec2) */
			posr.ra = ln_interpolate3(nr, sol1.ra, sol2.ra, sol3.ra);
			posr.dec = ln_interpolate3(nr, sol1.dec, sol2.dec, sol3.dec);
			post.ra = ln_interpolate3(nt, sol1.ra, sol2.ra, sol3.ra);
			poss.ra = ln_interpolate3(ns, sol1.ra, sol2.ra, sol3.ra);
			poss.dec = ln_interpolate3(ns, sol1.dec, sol2.dec, sol3.dec);
		
			/* find local hour angle */
			Hat = mst + observer.lng - post.ra;
			Har = msr + observer.lng - posr.ra;
			Has = mss + observer.lng - poss.ra;

			/* find altitude for rise and set */
			altr = sin(ln_deg_to_rad(observer.lat)) *
					sin(ln_deg_to_rad(posr.dec)) +
					cos(ln_deg_to_rad(observer.lat)) *
					cos(ln_deg_to_rad(posr.dec)) *
					cos(ln_deg_to_rad(Har));
			alts = sin(ln_deg_to_rad(observer.lat)) *
					sin(ln_deg_to_rad(poss.dec)) +
					cos(ln_deg_to_rad(observer.lat)) *
					cos(ln_deg_to_rad(poss.dec)) *
					cos(ln_deg_to_rad(Has));

			/* corrections for m */
			dmt = - (Hat / 360.0);
			dmr = (altr - horizon) / (360.0 * cos(ln_deg_to_rad(posr.dec)) *
				cos(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(Har)));
			dms = (alts - horizon) / (360.0 * cos(ln_deg_to_rad(poss.dec)) *
				cos(ln_deg_to_rad(observer.lat)) * sin(ln_deg_to_rad(Has)));

			/* add corrections and change to JD */
			mt += dmt;
			mr += dmr;
			ms += dms;

			if (mt <= 1.0 && mt >= 0.0 &&
				mr <= 1.0 && mr >= 0.0 &&
				ms <= 1.0 && ms >= 0.0)
				break;
		}

		rst.rise = JD_UT + mr;
		rst.transit = JD_UT + mt;
		rst.set = JD_UT + ms;
		
		/* not circumpolar */
		return 0;
	}

	/*! \fn int ln_get_body_next_rst_horizon(double JD, LnLnlatPosn observer, void (*get_equ_body_coords) (double, LnEquPosn ), double horizon, LnRstTime rst); 
	* \param JD Julian day 
	* \param observer Observers position 
	* \param get_motion_body_coords Pointer to ln_get_ell_body_equ_coords. ln_get_para_body_equ_coords or ln_get_hyp_body_equ_coords function
	* \param horizon Horizon, see LN_XXX_HORIZON constants
	* \param rst Pointer to store Rise, Set and Transit time in JD 
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time of next rise, set and transit (crosses the local meridian at
	* upper culmination) time of the body for the given Julian day and given
	* horizon.
	*
	* This function guarantee, that rise, set and transit will be in <JD, JD+1> range.
	*
	* Note 1: this functions returns 1 if the body is circumpolar, that is it remains
	* the whole day either above or below the horizon.
	*/
	int ln_get_motion_body_next_rst_horizon(double JD,
		LnLnlatPosn observer,
		get_motion_body_coords_t get_motion_body_coords, void *orbit,
		double horizon, LnRstTime rst)
	{
		return ln_get_motion_body_next_rst_horizon_future(JD, observer,
			get_motion_body_coords, orbit, horizon, 1, rst);
	}

	/*! \fn int ln_get_motion_body_next_rst_horizon_future(double JD, LnLnlatPosn observer, void (*get_equ_body_coords) (double, LnEquPosn ), double horizon, int day_limit, LnRstTime rst); 
	* \param JD Julian day 
	* \param observer Observers position 
	* \param get_motion_body_coords Pointer to ln_get_ell_body_equ_coords. ln_get_para_body_equ_coords or ln_get_hyp_body_equ_coords function
	* \param horizon Horizon, see LN_XXX_HORIZON constants
	* \param day_limit Maximal number of days that will be searched for next rise and set
	* \param rst Pointer to store Rise, Set and Transit time in JD 
	* \return 0 for success, 1 for circumpolar (above the horizon), -1 for circumpolar (bellow the horizon)
	*
	* Calculate the time of next rise, set and transit (crosses the local meridian at
	* upper culmination) time of the body for the given Julian day and given
	* horizon.
	*
	* This function guarantee, that rise, set and transit will be in <JD, JD + day_limit> range.
	*
	* Note 1: this functions returns 1 if the body is circumpolar, that is it remains
	* the whole day either above or below the horizon.
	*/
	int ln_get_motion_body_next_rst_horizon_future(double JD,
		LnLnlatPosn observer,
		get_motion_body_coords_t get_motion_body_coords, void *orbit,
		double horizon, int day_limit, LnRstTime rst)
	{
		int ret;
		struct ln_rst_time rst_1, rst_2;

		ret = ln_get_motion_body_rst_horizon_offset(JD, observer,
			get_motion_body_coords, orbit, horizon, rst, nan("0"));
		if (ret && day_limit == 1)
			// circumpolar
			return ret;

		if (!ret &&
			(rst.rise >(JD + 0.5) || rst.transit >(JD + 0.5) ||
			rst.set >(JD + 0.5))) {

			ret = ln_get_motion_body_rst_horizon_offset(JD - 1.0, observer,
				get_motion_body_coords, orbit, horizon, &rst_1, nan ("0"));
			if (ret)
				set_next_rst(rst, -1.0, &rst_1);
		} else {
			rst.rise = nan("0");
			rst.transit = nan("0");
			rst.set = nan("0");

			set_next_rst(rst, -1.0, &rst_1);
		}

		if (ret || (rst.rise < JD || rst.transit < JD || rst.set < JD)) {
		  	// find next day when it will rise, up to day_limit days
			int day = 1;

			while (day <= day_limit) {

				ret = ln_get_motion_body_rst_horizon_offset(JD + day, observer,
					get_motion_body_coords, orbit, horizon, &rst_2, nan ("0"));

				if (!ret) {
					day = day_limit + 2;
					break;
				}
				day++;
			}

			if (day == day_limit + 1)

				// it's then really circumpolar in searched period
				return ret;
		} else {
			set_next_rst(rst, +1.0, &rst_2);
		}

		rst.rise = find_next(JD, rst_1.rise, rst.rise, rst_2.rise);
		rst.transit = find_next(JD, rst_1.transit, rst.transit, rst_2.transit);
		rst.set = find_next(JD, rst_1.set, rst.set, rst_2.set);

		if (Double.isNaN (rst.rise))
			return ret;

		return 0;
	}

}
