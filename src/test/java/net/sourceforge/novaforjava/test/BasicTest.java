package net.sourceforge.novaforjava.test;

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

import static net.sourceforge.novaforjava.Aberration.ln_get_equ_aber;
import static net.sourceforge.novaforjava.Airmass.ln_get_airmass;
import static net.sourceforge.novaforjava.Airmass.ln_get_alt_from_airmass;
import static net.sourceforge.novaforjava.AngularSeparation.ln_get_angular_separation;
import static net.sourceforge.novaforjava.AngularSeparation.ln_get_rel_posn_angle;
import static net.sourceforge.novaforjava.ApparentPosition.ln_get_apparent_posn;
import static net.sourceforge.novaforjava.DynamicalTime.ln_get_jde;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_body_earth_dist;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_body_equ_coords;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_body_next_rst;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_body_rst;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_body_solar_dist;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_geo_rect_posn;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_helio_rect_posn;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_orbit_avel;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_orbit_len;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_orbit_pvel;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_orbit_vel;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_radius_vector;
import static net.sourceforge.novaforjava.EllipticMotion.ln_get_ell_true_anomaly;
import static net.sourceforge.novaforjava.EllipticMotion.ln_solve_kepler;
import static net.sourceforge.novaforjava.HeliocentricTime.ln_get_heliocentric_time_diff;
import static net.sourceforge.novaforjava.HyperbolicMotion.ln_get_hyp_body_equ_coords;
import static net.sourceforge.novaforjava.HyperbolicMotion.ln_get_hyp_body_next_rst_horizon;
import static net.sourceforge.novaforjava.HyperbolicMotion.ln_get_hyp_body_solar_dist;
import static net.sourceforge.novaforjava.HyperbolicMotion.ln_get_hyp_radius_vector;
import static net.sourceforge.novaforjava.HyperbolicMotion.ln_get_hyp_true_anomaly;
import static net.sourceforge.novaforjava.JulianDay.ln_date_to_zonedate;
import static net.sourceforge.novaforjava.JulianDay.ln_get_date;
import static net.sourceforge.novaforjava.JulianDay.ln_get_day_of_week;
import static net.sourceforge.novaforjava.JulianDay.ln_get_julian_day;
import static net.sourceforge.novaforjava.JulianDay.ln_get_julian_from_sys;
import static net.sourceforge.novaforjava.JulianDay.ln_zonedate_to_date;
import static net.sourceforge.novaforjava.ParabolicMotion.ln_get_par_body_earth_dist;
import static net.sourceforge.novaforjava.ParabolicMotion.ln_get_par_body_equ_coords;
import static net.sourceforge.novaforjava.ParabolicMotion.ln_get_par_body_solar_dist;
import static net.sourceforge.novaforjava.ParabolicMotion.ln_get_par_geo_rect_posn;
import static net.sourceforge.novaforjava.ParabolicMotion.ln_get_par_helio_rect_posn;
import static net.sourceforge.novaforjava.ParabolicMotion.ln_get_par_radius_vector;
import static net.sourceforge.novaforjava.ParabolicMotion.ln_get_par_true_anomaly;
import static net.sourceforge.novaforjava.Parallax.ln_get_parallax;
import static net.sourceforge.novaforjava.Precession.ln_get_equ_prec;
import static net.sourceforge.novaforjava.Precession.ln_get_equ_prec2;
import static net.sourceforge.novaforjava.ProperMotion.ln_get_equ_pm;
import static net.sourceforge.novaforjava.RiseSet.ln_get_body_next_rst_horizon_future;
import static net.sourceforge.novaforjava.RiseSet.ln_get_object_next_rst;
import static net.sourceforge.novaforjava.RiseSet.ln_get_object_next_rst_horizon;
import static net.sourceforge.novaforjava.RiseSet.ln_get_object_rst;
import static net.sourceforge.novaforjava.RiseSet.ln_get_object_rst_horizon;
import static net.sourceforge.novaforjava.SiderealTime.ln_get_apparent_sidereal_time;
import static net.sourceforge.novaforjava.SiderealTime.ln_get_mean_sidereal_time;
import static net.sourceforge.novaforjava.Transform.ln_get_ecl_from_equ;
import static net.sourceforge.novaforjava.Transform.ln_get_equ_from_ecl;
import static net.sourceforge.novaforjava.Transform.ln_get_equ_from_gal;
import static net.sourceforge.novaforjava.Transform.ln_get_gal_from_equ;
import static net.sourceforge.novaforjava.Transform.ln_get_gal_from_equ2000;
import static net.sourceforge.novaforjava.Transform.ln_get_hrz_from_equ;
import static net.sourceforge.novaforjava.Utility.ln_deg_to_dms;
import static net.sourceforge.novaforjava.Utility.ln_dms_to_deg;
import static net.sourceforge.novaforjava.Utility.ln_equ_to_hequ;
import static net.sourceforge.novaforjava.Utility.ln_hequ_to_equ;
import static net.sourceforge.novaforjava.Utility.ln_hlnlat_to_lnlat;
import static net.sourceforge.novaforjava.Utility.ln_hms_to_deg;
import static net.sourceforge.novaforjava.Utility.ln_lnlat_to_hlnlat;
import static net.sourceforge.novaforjava.api.Constants.B1900;
import static net.sourceforge.novaforjava.api.Constants.JD2000;
import static net.sourceforge.novaforjava.api.Constants.JD2050;
import static net.sourceforge.novaforjava.solarsystem.Earth.ln_get_earth_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Earth.ln_get_earth_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_disk;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_equ_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_phase;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_pol_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Jupiter.ln_get_jupiter_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Lunar.ln_get_lunar_bright_limb;
import static net.sourceforge.novaforjava.solarsystem.Lunar.ln_get_lunar_disk;
import static net.sourceforge.novaforjava.solarsystem.Lunar.ln_get_lunar_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Lunar.ln_get_lunar_ecl_coords;
import static net.sourceforge.novaforjava.solarsystem.Lunar.ln_get_lunar_equ_coords_prec;
import static net.sourceforge.novaforjava.solarsystem.Lunar.ln_get_lunar_geo_posn;
import static net.sourceforge.novaforjava.solarsystem.Lunar.ln_get_lunar_phase;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_disk;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_phase;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Mars.ln_get_mars_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_disk;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_phase;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Mercury.ln_get_mercury_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_disk;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_phase;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Neptune.ln_get_neptune_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_disk;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_phase;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Pluto.ln_get_pluto_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_disk;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_equ_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_phase;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_pol_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Saturn.ln_get_saturn_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Solar.LN_SOLAR_STANDART_HORIZON;
import static net.sourceforge.novaforjava.solarsystem.Solar.ln_get_solar_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Solar.ln_get_solar_geom_coords;
import static net.sourceforge.novaforjava.solarsystem.Solar.ln_get_solar_rst;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_disk;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_phase;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Uranus.ln_get_uranus_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_disk;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_earth_dist;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_equ_coords;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_helio_coords;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_magnitude;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_phase;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_rst;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_sdiam;
import static net.sourceforge.novaforjava.solarsystem.Venus.ln_get_venus_solar_dist;

import java.math.BigDecimal;
import java.util.Locale;

import net.sourceforge.novaforjava.Nutation;
import net.sourceforge.novaforjava.api.LnDate;
import net.sourceforge.novaforjava.api.LnDms;
import net.sourceforge.novaforjava.api.LnEllOrbit;
import net.sourceforge.novaforjava.api.LnEquPosn;
import net.sourceforge.novaforjava.api.LnGalPosn;
import net.sourceforge.novaforjava.api.LnHelioPosn;
import net.sourceforge.novaforjava.api.LnHms;
import net.sourceforge.novaforjava.api.LnHrzPosn;
import net.sourceforge.novaforjava.api.LnHypOrbit;
import net.sourceforge.novaforjava.api.LnLnlatPosn;
import net.sourceforge.novaforjava.api.LnNutation;
import net.sourceforge.novaforjava.api.LnParOrbit;
import net.sourceforge.novaforjava.api.LnRectPosn;
import net.sourceforge.novaforjava.api.LnRstTime;
import net.sourceforge.novaforjava.api.LnZoneDate;
import net.sourceforge.novaforjava.api.LnhEquPosn;
import net.sourceforge.novaforjava.api.LnhLnlatPosn;
import net.sourceforge.novaforjava.util.IGetEquBodyCoords;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicTest {

	@Before
	public void setup() {
		Locale.setDefault(Locale.US);

	}

	void usleep(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			// ignore
		}
	}

	/**
	 * Define DATE or SYS_DATE for testing VSOP87 theory with other JD
	 */
	// #define DATE
	// #define SYS_DATE

	// holds number of tests
	int test_number = 0;

	double compare_results(double calc, double expect, double tolerance) {
		if (calc - expect > tolerance || calc - expect < (tolerance * -1.0))
			return calc - expect;
		else
			return 0;
	}

	int test_result(String test, boolean calc, double expect, double tolerance) {
		return test_result(test, calc ? 1 : 0, expect, tolerance);
	}

	int test_result(String test, double calc, double expect, double tolerance) {
		double diff;

		System.out.format("TEST %s....", test);

		test_number++;

		diff = compare_results(calc, expect, tolerance);
		if (diff != 0) {
			System.out.format("[FAILED]\n");
			System.out.format(
					"   Expected %8.8f but calculated %8.8f %8.12f error.\n\n",
					expect, calc, diff);
			Assert.fail(String.format(
					"   Expected %8.8f but calculated %8.8f %8.12f error.\n\n",
					expect, calc, diff));
			return 1;
		} else {
			System.out.format("[PASSED]\n");
			System.out.format("   Expected and calculated %8.8f.\n\n", calc);
			return 0;
		}
	}

	/** test julian day calculations */

	@Test
	public void julian_test() {
		double JD, JD2;
		int wday, failed = 0;
		LnDate date = new LnDate(), pdate = new LnDate();
		LnZoneDate zonedate = new LnZoneDate();

		/** Get julian day for 04/10/1957 19:00:00 */
		date.years = 1957;
		date.months = 10;
		date.days = 4;
		date.hours = 19;
		date.minutes = 0;
		date.seconds = 0;
		JD = ln_get_julian_day(date);
		failed += test_result("(Julian Day) JD for 4/10/1957 19:00:00", JD,
				2436116.29166667, 0.00001);

		/** Get julian day for 27/01/333 12:00:00 */
		date.years = 333;
		date.months = 1;
		date.days = 27;
		date.hours = 12;
		JD = ln_get_julian_day(date);
		failed += test_result("(Julian Day) JD for 27/01/333 12:00:00", JD,
				1842713.0, 0.1);

		/** Get julian day for 30/06/1954 00:00:00 */
		date.years = 1954;
		date.months = 6;
		date.days = 30;
		date.hours = 0;
		JD = ln_get_julian_day(date);
		failed += test_result("(Julian Day) JD for 30/06/1954 00:00:00", JD,
				2434923.5, 0.1);

		wday = ln_get_day_of_week(date);
		failed += test_result("(Julian Day) Weekday No", wday, 3, 0.1);

		/** Test ln_date_to_zonedate and back */

		ln_date_to_zonedate(date, zonedate, 7200);
		ln_zonedate_to_date(zonedate, date);

		JD = ln_get_julian_day(date);

		failed += test_result("(Julian Day) ln_date_to_zonedate and "
				+ "ln_zonedate_to_date check - JD for 30/06/1954 00:00:00", JD,
				2434923.5, 0.1);

		ln_get_date(JD, pdate);
		failed += test_result(
				"(Julian Day) Day from JD for 30/06/1954 00:00:00", pdate.days,
				30, 0.1);

		failed += test_result(
				"(Julian Day) Month from JD for 30/06/1954 00:00:00",
				pdate.months, 6, 0.1);

		failed += test_result(
				"(Julian Day) Year from JD for 30/06/1954 00:00:00",
				pdate.years, 1954, 0.1);

		failed += test_result(
				"(Julian Day) Hour from JD for 30/06/1954 00:00:00",
				pdate.hours, 0, 0.1);

		failed += test_result(
				"(Julian Day) Minute from JD for 30/06/1954 00:00:00",
				pdate.minutes, 0, 0.1);

		failed += test_result(
				"(Julian Day) Second from JD for 30/06/1954 00:00:00",
				pdate.seconds, 0, 0.001);

		JD = ln_get_julian_from_sys();

		usleep(10000);

		JD2 = ln_get_julian_from_sys();

		failed += test_result("(Julian Day) Diferrence between two successive "
				+ "ln_get_julian_from_sys() calls (it shall never be zero)",
				JD2 - JD, 1e-2 / 86400.0, .99e-1);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void dynamical_test() {
		LnDate date = new LnDate();
		double TD, JD;
		int failed = 0;

		/** Dynamical Time test for 01/01/2000 00:00:00 */
		date.years = 2000;
		date.months = 1;
		date.days = 1;
		date.hours = 0;
		date.minutes = 0;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);
		TD = ln_get_jde(JD);
		failed += test_result("(Dynamical Time) TD for 01/01/2000 00:00:00",
				TD, 2451544.50073877, 0.000001);
		Assert.assertEquals(0, failed);
	}

	@Test
	public void heliocentric_test() {
		LnEquPosn object = new LnEquPosn();
		LnDate date = new LnDate();
		double JD;
		double diff;
		int failed = 0;

		object.ra = 0.0;
		object.dec = 60.0;

		date.years = 2000;
		date.months = 1;
		date.days = 1;
		date.hours = 0;
		date.minutes = 0;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);

		diff = ln_get_heliocentric_time_diff(JD, object);

		failed += test_result(
				"(Heliocentric time) TD for 01/01, object on 0h +60", diff,
				15.0 * 0.0001, 0.0001);

		object.ra = 270.0;
		object.dec = 50.0;

		diff = ln_get_heliocentric_time_diff(JD, object);

		failed += test_result(
				"(Heliocentric time) TD for 01/01, object on 18h +50", diff,
				-16.0 * 0.0001, 0.0001);

		date.months = 8;
		date.days = 8;

		JD = ln_get_julian_day(date);

		diff = ln_get_heliocentric_time_diff(JD, object);

		failed += test_result(
				"(Heliocentric time) TD for 08/08, object on 18h +50", diff,
				12.0 * 0.0001, 0.0001);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void nutation_test() {
		double JD;
		LnNutation nutation = new LnNutation();
		int failed = 0;

		JD = 2446895.5;

		new Nutation().ln_get_nutation(JD, nutation);
		failed += test_result("(Nutation) longitude (deg) for JD 2446895.5",
				nutation.longitude, -0.00105222, 0.00000001);

		failed += test_result("(Nutation) obliquity (deg) for JD 2446895.5",
				nutation.obliquity, 0.00262293, 0.00000001);

		failed += test_result("(Nutation) ecliptic (deg) for JD 2446895.5",
				nutation.ecliptic, 23.44094649, 0.00000001);
		Assert.assertEquals(0, failed);
	}

	@Test
	public void transform_test() {
		LnhEquPosn hobject = new LnhEquPosn(), hpollux = new LnhEquPosn();
		LnhLnlatPosn hobserver = new LnhLnlatPosn(), hecl = new LnhLnlatPosn();
		LnEquPosn object = new LnEquPosn(), pollux = new LnEquPosn(), equ = new LnEquPosn();
		LnHrzPosn hrz = new LnHrzPosn();
		LnLnlatPosn observer = new LnLnlatPosn(), ecl = new LnLnlatPosn();
		LnGalPosn gal = new LnGalPosn();
		double JD;
		LnDate date = new LnDate();
		int failed = 0;

		/** observers position */
		hobserver.lng.neg = 0;
		hobserver.lng.degrees = 282;
		hobserver.lng.minutes = 56;
		hobserver.lng.seconds = 4.0;
		hobserver.lat.neg = 0;
		hobserver.lat.degrees = 38;
		hobserver.lat.minutes = 55;
		hobserver.lat.seconds = 17.0;

		/** object position */
		hobject.ra.hours = 23;
		hobject.ra.minutes = 9;
		hobject.ra.seconds = 16.641;
		hobject.dec.neg = 1;
		hobject.dec.degrees = 6;
		hobject.dec.minutes = 43;
		hobject.dec.seconds = 11.61;

		/** date and time */
		date.years = 1987;
		date.months = 4;
		date.days = 10;
		date.hours = 19;
		date.minutes = 21;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);
		ln_hequ_to_equ(hobject, object);
		ln_hlnlat_to_lnlat(hobserver, observer);

		ln_get_hrz_from_equ(object, observer, JD, hrz);
		failed += test_result("(Transforms) Equ to Horiz ALT ", hrz.alt,
				15.12426274, 0.00000001);
		failed += test_result("(Transforms) Equ to Horiz AZ ", hrz.az,
				68.03429264, 0.00000001);

		/** try something close to the pole */
		object.dec = 90.0;

		ln_get_hrz_from_equ(object, observer, JD, hrz);
		failed += test_result("(Transforms) Equ to Horiz ALT ", hrz.alt,
				38.9213888888, 0.00000001);
		failed += test_result("(Transforms) Equ to Horiz AZ ", hrz.az, 180.0,
				0.00000001);

		object.dec = -90.0;

		ln_get_hrz_from_equ(object, observer, JD, hrz);
		failed += test_result("(Transforms) Equ to Horiz ALT ", hrz.alt,
				-38.9213888888, 0.00000001);
		failed += test_result("(Transforms) Equ to Horiz AZ ", hrz.az, 0.0,
				0.00000001);

		observer.lat *= -1.0;

		ln_get_hrz_from_equ(object, observer, JD, hrz);
		failed += test_result("(Transforms) Equ to Horiz ALT ", hrz.alt,
				38.9213888888, 0.00000001);
		failed += test_result("(Transforms) Equ to Horiz AZ ", hrz.az, 0.0,
				0.00000001);

		object.dec = 90.0;

		ln_get_hrz_from_equ(object, observer, JD, hrz);
		failed += test_result("(Transforms) Equ to Horiz ALT ", hrz.alt,
				-38.9213888888, 0.00000001);
		failed += test_result("(Transforms) Equ to Horiz AZ ", hrz.az, 180.0,
				0.00000001);

		/** Equ position of Pollux */
		hpollux.ra.hours = 7;
		hpollux.ra.minutes = 45;
		hpollux.ra.seconds = 18.946;
		hpollux.dec.neg = 0;
		hpollux.dec.degrees = 28;
		hpollux.dec.minutes = 1;
		hpollux.dec.seconds = 34.26;

		ln_hequ_to_equ(hpollux, pollux);
		ln_get_ecl_from_equ(pollux, JD, ecl);

		ln_lnlat_to_hlnlat(ecl, hecl);
		failed += test_result("(Transforms) Equ to Ecl longitude ", ecl.lng,
				113.21555278, 0.00000001);
		failed += test_result("(Transforms) Equ to Ecl latitude", ecl.lat,
				6.68264899, 0.00000001);

		ln_get_equ_from_ecl(ecl, JD, equ);
		failed += test_result("(Transforms) Ecl to Equ RA ", equ.ra,
				116.32894167, 0.00000001);
		failed += test_result("(Transforms) Ecl to Equ DEC", equ.dec,
				28.02618333, 0.00000001);

		/** Gal pole */
		gal.l = 0.0;
		gal.b = 90.0;

		ln_get_equ_from_gal(gal, equ);
		failed += test_result("(Transforms) Gal to Equ RA", equ.ra, 192.25,
				0.00000001);
		failed += test_result("(Transforms) Gal to Equ DEC", equ.dec, 27.4,
				0.00000001);

		ln_get_gal_from_equ(equ, gal);
		failed += test_result("(Transforms) Equ to Gal b", gal.b, 90,
				0.00000001);

		// Swift triger 174738

		equ.ra = 125.2401;
		equ.dec = +31.9260;

		ln_get_gal_from_equ2000(equ, gal);
		failed += test_result("(Transforms) Equ J2000 to Gal l", gal.l, 190.54,
				0.005);
		failed += test_result("(Transforms) Equ J2000 to Gal b", gal.b, 31.92,
				0.005);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void sidereal_test() {
		LnDate date = new LnDate();
		double sd;
		double JD;
		int failed = 0;

		/** 10/04/1987 19:21:00 */
		date.years = 1987;
		date.months = 4;
		date.days = 10;
		date.hours = 19;
		date.minutes = 21;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);
		sd = ln_get_mean_sidereal_time(JD);

		failed += test_result("(Sidereal) mean hours on 10/04/1987 19:21:00 ",
				sd, 8.58252488, 0.000001);
		sd = ln_get_apparent_sidereal_time(JD);
		failed += test_result(
				"(Sidereal) apparent hours on 10/04/1987 19:21:00 ", sd,
				8.58245327, 0.000001);
		Assert.assertEquals(0, failed);
	}

	@Test
	public void solar_coord_test() {
		LnHelioPosn pos = new LnHelioPosn();
		int failed = 0;

		ln_get_solar_geom_coords(2448908.5, pos);
		failed += test_result(
				"(Solar Coords) longitude (deg) on JD 2448908.5  ", pos.L,
				200.00810889, 0.00000001);
		failed += test_result(
				"(Solar Coords) latitude (deg) on JD 2448908.5  ", pos.B,
				0.00018690, 0.00000001);
		failed += test_result(
				"(Solar Coords) radius vector (AU) on JD 2448908.5  ", pos.R,
				0.99760852, 0.00000001);
		Assert.assertEquals(0, failed);
	}

	@Test
	public void aberration_test() {
		LnhEquPosn hobject = new LnhEquPosn();
		LnEquPosn object = new LnEquPosn(), pos = new LnEquPosn();
		LnDate date = new LnDate();
		double JD;
		int failed = 0;

		/** object position */
		hobject.ra.hours = 2;
		hobject.ra.minutes = 44;
		hobject.ra.seconds = 12.9747;
		hobject.dec.neg = 0;
		hobject.dec.degrees = 49;
		hobject.dec.minutes = 13;
		hobject.dec.seconds = 39.896;

		/** date */
		date.years = 2028;
		date.months = 11;
		date.days = 13;
		date.hours = 4;
		date.minutes = 31;
		date.seconds = 0;

		JD = ln_get_julian_day(date);

		ln_hequ_to_equ(hobject, object);
		ln_get_equ_aber(object, JD, pos);
		failed += test_result("(Aberration) RA  ", pos.ra, 41.06238352,
				0.00000001);
		failed += test_result("(Aberration) DEC  ", pos.dec, 49.22962359,
				0.00000001);
		Assert.assertEquals(0, failed);
	}

	@Test
	public void precession_test() {
		double JD;
		LnEquPosn object = new LnEquPosn(), pos = new LnEquPosn(), pos2 = new LnEquPosn(), pm = new LnEquPosn();
		LnhEquPosn hobject = new LnhEquPosn();
		LnDate grb_date = new LnDate();
		int failed = 0;

		/** object position */
		hobject.ra.hours = 2;
		hobject.ra.minutes = 44;
		hobject.ra.seconds = 11.986;
		hobject.dec.neg = 0;
		hobject.dec.degrees = 49;
		hobject.dec.minutes = 13;
		hobject.dec.seconds = 42.48;

		JD = 2462088.69;
		ln_hequ_to_equ(hobject, object);

		pm.ra = 0.03425 * (15.0 / 3600.0);
		pm.dec = -0.0895 / 3600.0;

		ln_get_equ_pm(object, pm, JD, object);

		failed += test_result("(Proper motion) RA on JD 2462088.69  ",
				object.ra, 41.054063, 0.00001);
		failed += test_result("(Proper motion) DEC on JD 2462088.69  ",
				object.dec, 49.227750, 0.00001);

		ln_get_equ_prec(object, JD, pos);
		failed += test_result("(Precession) RA on JD 2462088.69  ", pos.ra,
				41.547214, 0.00003);
		failed += test_result("(Precession) DEC on JD 2462088.69  ", pos.dec,
				49.348483, 0.00001);

		ln_get_equ_prec2(object, JD2000, JD, pos);

		failed += test_result("(Precession 2) RA on JD 2462088.69  ", pos.ra,
				41.547214, 0.00003);
		failed += test_result("(Precession 2) DEC on JD 2462088.69  ", pos.dec,
				49.348483, 0.00001);

		ln_get_equ_prec2(pos, JD, JD2000, pos2);

		failed += test_result("(Precession 2) RA on JD 2451545.0  ", pos2.ra,
				object.ra, 0.00001);
		failed += test_result("(Precession 2) DEC on JD 2451545.0  ", pos2.dec,
				object.dec, 0.00001);

		// INTEGRAL GRB050922A coordinates lead to RA not in <0-360> range
		pos.ra = 271.2473;
		pos.dec = -32.0227;

		grb_date.years = 2005;
		grb_date.months = 9;
		grb_date.days = 22;
		grb_date.hours = 13;
		grb_date.minutes = 43;
		grb_date.seconds = 18.0;

		JD = ln_get_julian_day(grb_date);

		ln_get_equ_prec2(pos, JD, JD2000, pos2);

		failed += test_result("(Precession 2) RA on JD 2451545.0  ", pos2.ra,
				271.1541, 0.0002);
		failed += test_result("(Precession 2) DEC on JD 2451545.0  ", pos2.dec,
				-32.0235, 0.0002);

		// second test from AA, p. 128
		hobject.ra.hours = 2;
		hobject.ra.minutes = 31;
		hobject.ra.seconds = 48.704;
		hobject.dec.neg = 0;
		hobject.dec.degrees = 89;
		hobject.dec.minutes = 15;
		hobject.dec.seconds = 50.72;

		ln_hequ_to_equ(hobject, object);

		// TODO long double
		// proper motions
		pm.ra = (0.19877) * (15.0 / 3600.0);
		pm.dec = (-0.0152) / 3600.0;

		ln_get_equ_pm(object, pm, B1900, pos);

		ln_get_equ_prec2(pos, JD2000, B1900, pos2);

		// the position is so close to pole, that it depends a lot on how
		// precise
		// functions we will use. So we get such big errors compared to Meeus.
		// I checked results agains SLAlib on-line calculator and SLAlib
		// performs
		// even worse then we

		failed += test_result("(Precision 2) RA on B1900  ", pos2.ra,
				20.6412499980, 0.002);
		failed += test_result("(Precision 2) DEC on B1900  ", pos2.dec,
				88.7739388888, 0.0001);

		ln_get_equ_pm(object, pm, JD2050, pos);

		ln_get_equ_prec2(pos, JD2000, JD2050, pos2);

		failed += test_result("(Precision 2) RA on J2050  ", pos2.ra,
				57.0684583320, 0.003);
		failed += test_result("(Precision 2) DEC on J2050  ", pos2.dec,
				89.4542722222, 0.0001);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void apparent_position_test() {
		double JD;
		LnhEquPosn hobject = new LnhEquPosn(), hpm = new LnhEquPosn();
		LnEquPosn object = new LnEquPosn(), pm = new LnEquPosn(), pos = new LnEquPosn();
		int failed = 0;

		/** objects position */
		hobject.ra.hours = 2;
		hobject.ra.minutes = 44;
		hobject.ra.seconds = 12.9747;
		hobject.dec.neg = 0;
		hobject.dec.degrees = 49;
		hobject.dec.minutes = 13;
		hobject.dec.seconds = 39.896;

		/** proper motion of object */
		hpm.ra.hours = 0;
		hpm.ra.minutes = 0;
		hpm.ra.seconds = 0.03425;
		hpm.dec.neg = 1;
		hpm.dec.degrees = 0;
		hpm.dec.minutes = 0;
		hpm.dec.seconds = 0.0895;

		JD = 2462088.69;
		ln_hequ_to_equ(hobject, object);
		ln_hequ_to_equ(hpm, pm);
		ln_get_apparent_posn(object, pm, JD, pos);

		failed += test_result("(Apparent Position) RA on JD 2462088.69  ",
				pos.ra, 41.55966517, 0.00000001);
		failed += test_result("(Apparent Position) DEC on JD 2462088.69  ",
				pos.dec, 49.34962340, 0.00000001);
		Assert.assertEquals(0, failed);
	}

	@Test
	public void vsop87_test() {
		LnHelioPosn pos = new LnHelioPosn();
		LnhEquPosn hequ = new LnhEquPosn();
		LnEquPosn equ = new LnEquPosn();
		;
		double JD = 2448976.5;
		double au;
		int failed = 0;

		ln_get_solar_equ_coords(JD, equ);
		failed += test_result("(Solar Position) RA on JD 2448976.5  ", equ.ra,
				268.32141013, 0.00000001);
		failed += test_result("(Solar Position) DEC on JD 2448976.5  ",
				equ.dec, -23.43013835, 0.00000001);

		ln_get_mercury_helio_coords(JD, pos);
		System.out.format("Mercury L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_mercury_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Mercury RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_mercury_earth_dist(JD);
		System.out.format("mercury -> Earth dist (AU) %f\n", au);
		au = ln_get_mercury_solar_dist(JD);
		System.out.format("mercury -> Sun dist (AU) %f\n", au);
		au = ln_get_mercury_disk(JD);
		System.out.format("mercury -> illuminated disk %f\n", au);
		au = ln_get_mercury_magnitude(JD);
		System.out.format("mercury -> magnitude %f\n", au);
		au = ln_get_mercury_phase(JD);
		System.out.format("mercury -> phase %f\n", au);
		au = ln_get_mercury_sdiam(JD);
		System.out.format("mercury -> sdiam %f\n", au);

		ln_get_venus_helio_coords(JD, pos);
		System.out.format("Venus L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_venus_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Venus RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_venus_earth_dist(JD);
		System.out.format("venus -> Earth dist (AU) %f\n", au);
		au = ln_get_venus_solar_dist(JD);
		System.out.format("venus -> Sun dist (AU) %f\n", au);
		au = ln_get_venus_disk(JD);
		System.out.format("venus -> illuminated disk %f\n", au);
		au = ln_get_venus_magnitude(JD);
		System.out.format("venus -> magnitude %f\n", au);
		au = ln_get_venus_phase(JD);
		System.out.format("venus -> phase %f\n", au);
		au = ln_get_venus_sdiam(JD);
		System.out.format("venus -> sdiam %f\n", au);

		ln_get_earth_helio_coords(JD, pos);
		System.out.format("Earth L %f B %f R %f\n", pos.L, pos.B, pos.R);
		au = ln_get_earth_solar_dist(JD);
		System.out.format("earth -> Sun dist (AU) %f\n", au);

		ln_get_mars_helio_coords(JD, pos);
		System.out.format("Mars L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_mars_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Mars RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_mars_earth_dist(JD);
		System.out.format("mars -> Earth dist (AU) %f\n", au);
		au = ln_get_mars_solar_dist(JD);
		System.out.format("mars -> Sun dist (AU) %f\n", au);
		au = ln_get_mars_disk(JD);
		System.out.format("mars -> illuminated disk %f\n", au);
		au = ln_get_mars_magnitude(JD);
		System.out.format("mars -> magnitude %f\n", au);
		au = ln_get_mars_phase(JD);
		System.out.format("mars -> phase %f\n", au);
		au = ln_get_mars_sdiam(JD);
		System.out.format("mars -> sdiam %f\n", au);

		ln_get_jupiter_helio_coords(JD, pos);
		System.out.format("Jupiter L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_jupiter_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Jupiter RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_jupiter_earth_dist(JD);
		System.out.format("jupiter -> Earth dist (AU) %f\n", au);
		au = ln_get_jupiter_solar_dist(JD);
		System.out.format("jupiter -> Sun dist (AU) %f\n", au);
		au = ln_get_jupiter_disk(JD);
		System.out.format("jupiter -> illuminated disk %f\n", au);
		au = ln_get_jupiter_magnitude(JD);
		System.out.format("jupiter -> magnitude %f\n", au);
		au = ln_get_jupiter_phase(JD);
		System.out.format("jupiter -> phase %f\n", au);
		au = ln_get_jupiter_pol_sdiam(JD);
		System.out.format("jupiter -> polar sdiam %f\n", au);
		au = ln_get_jupiter_equ_sdiam(JD);
		System.out.format("jupiter -> equ sdiam %f\n", au);

		ln_get_saturn_helio_coords(JD, pos);
		System.out.format("Saturn L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_saturn_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Saturn RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_saturn_earth_dist(JD);
		System.out.format("saturn -> Earth dist (AU) %f\n", au);
		au = ln_get_saturn_solar_dist(JD);
		System.out.format("saturn -> Sun dist (AU) %f\n", au);
		au = ln_get_saturn_disk(JD);
		System.out.format("saturn -> illuminated disk %f\n", au);
		au = ln_get_saturn_magnitude(JD);
		System.out.format("saturn -> magnitude %f\n", au);
		au = ln_get_saturn_phase(JD);
		System.out.format("saturn -> phase %f\n", au);
		au = ln_get_saturn_pol_sdiam(JD);
		System.out.format("saturn -> polar sdiam %f\n", au);
		au = ln_get_saturn_equ_sdiam(JD);
		System.out.format("saturn -> equ sdiam %f\n", au);

		ln_get_uranus_helio_coords(JD, pos);
		System.out.format("Uranus L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_uranus_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Uranus RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_uranus_earth_dist(JD);
		System.out.format("uranus -> Earth dist (AU) %f\n", au);
		au = ln_get_uranus_solar_dist(JD);
		System.out.format("uranus -> Sun dist (AU) %f\n", au);
		au = ln_get_uranus_disk(JD);
		System.out.format("uranus -> illuminated disk %f\n", au);
		au = ln_get_uranus_magnitude(JD);
		System.out.format("uranus -> magnitude %f\n", au);
		au = ln_get_uranus_phase(JD);
		System.out.format("uranus -> phase %f\n", au);
		au = ln_get_uranus_sdiam(JD);
		System.out.format("uranus -> sdiam %f\n", au);

		ln_get_neptune_helio_coords(JD, pos);
		System.out.format("Neptune L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_neptune_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Neptune RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_neptune_earth_dist(JD);
		System.out.format("neptune -> Earth dist (AU) %f\n", au);
		au = ln_get_neptune_solar_dist(JD);
		System.out.format("neptune -> Sun dist (AU) %f\n", au);
		au = ln_get_neptune_disk(JD);
		System.out.format("neptune -> illuminated disk %f\n", au);
		au = ln_get_neptune_magnitude(JD);
		System.out.format("neptune -> magnitude %f\n", au);
		au = ln_get_neptune_phase(JD);
		System.out.format("neptune -> phase %f\n", au);
		au = ln_get_neptune_sdiam(JD);
		System.out.format("neptune -> sdiam %f\n", au);

		ln_get_pluto_helio_coords(JD, pos);
		System.out.format("Pluto L %f B %f R %f\n", pos.L, pos.B, pos.R);
		ln_get_pluto_equ_coords(JD, equ);
		ln_equ_to_hequ(equ, hequ);
		System.out.format("Pluto RA %d:%d:%f Dec %d:%d:%f\n", hequ.ra.hours,
				hequ.ra.minutes, hequ.ra.seconds, hequ.dec.degrees,
				hequ.dec.minutes, hequ.dec.seconds);
		au = ln_get_pluto_earth_dist(JD);
		System.out.format("pluto -> Earth dist (AU) %f\n", au);
		au = ln_get_pluto_solar_dist(JD);
		System.out.format("pluto -> Sun dist (AU) %f\n", au);
		au = ln_get_pluto_disk(JD);
		System.out.format("pluto -> illuminated disk %f\n", au);
		au = ln_get_pluto_magnitude(JD);
		System.out.format("pluto -> magnitude %f\n", au);
		au = ln_get_pluto_phase(JD);
		System.out.format("pluto -> phase %f\n", au);
		au = ln_get_pluto_sdiam(JD);
		System.out.format("pluto -> sdiam %f\n", au);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void lunar_test() {
		double JD = 2448724.5;

		LnRectPosn moon = new LnRectPosn();
		LnEquPosn equ = new LnEquPosn();
		LnLnlatPosn ecl = new LnLnlatPosn();
		int failed = 0;

		/** JD = get_julian_from_sys(); */
		/** JD=2448724.5; */
		ln_get_lunar_geo_posn(JD, moon, 0);

		Assert.assertEquals(
				"lunar x -252118.863940  y 267821.304702  z -20748.127775",
				String.format("lunar x %f  y %f  z %f", moon.X, moon.Y, moon.Z));
		ln_get_lunar_ecl_coords(JD, ecl, 0);

		Assert.assertEquals("lunar long 133.270166  lat -3.228533",
				String.format("lunar long %f  lat %f", ecl.lng, ecl.lat));

		ln_get_lunar_equ_coords_prec(JD, equ, 0);

		Assert.assertEquals("lunar RA 134.790108  Dec 13.739882",
				String.format("lunar RA %f  Dec %f", equ.ra, equ.dec));

		Assert.assertEquals("lunar distance km 368406.458002", String.format(
				"lunar distance km %f", ln_get_lunar_earth_dist(JD)));

		Assert.assertEquals("lunar disk 0.678514",
				String.format("lunar disk %f", ln_get_lunar_disk(JD)));
		Assert.assertEquals("lunar phase 69.082257",
				String.format("lunar phase %f", ln_get_lunar_phase(JD)));
		Assert.assertEquals("lunar bright limb 285.076674", String.format(
				"lunar bright limb %f", ln_get_lunar_bright_limb(JD)));

		Assert.assertEquals(0, failed);
	}

	@Test
	public void elliptic_motion_test() {
		double r, v, l, V, dist;
		double E, e_JD, o_JD;
		LnEllOrbit orbit = new LnEllOrbit();
		LnRectPosn posn = new LnRectPosn();
		LnDate epoch_date = new LnDate(), obs_date = new LnDate();
		LnEquPosn equ_posn = new LnEquPosn();
		int failed = 0;

		obs_date.years = 1990;
		obs_date.months = 10;
		obs_date.days = 6;
		obs_date.hours = 0;
		obs_date.minutes = 0;
		obs_date.seconds = 0;

		epoch_date.years = 1990;
		epoch_date.months = 10;
		epoch_date.days = 28;
		epoch_date.hours = 12;
		epoch_date.minutes = 30;
		epoch_date.seconds = 0;

		e_JD = ln_get_julian_day(epoch_date);
		o_JD = ln_get_julian_day(obs_date);

		orbit.JD = e_JD;
		orbit.a = 2.2091404;
		orbit.e = 0.8502196;
		orbit.i = 11.94525;
		orbit.omega = 334.75006;
		orbit.w = 186.23352;
		orbit.n = 0;

		E = ln_solve_kepler(0.1, 5.0);
		failed += test_result(
				"(Equation of kepler) E when e is 0.1 and M is 5.0   ", E,
				5.554589253872320, 0.000000000001);

		v = ln_get_ell_true_anomaly(0.1, E);
		failed += test_result(
				"(True Anomaly) v when e is 0.1 and E is 5.5545   ", v,
				6.13976152, 0.00000001);

		r = ln_get_ell_radius_vector(0.5, 0.1, E);
		failed += test_result(
				"(Radius Vector) r when v is , e is 0.1 and E is 5.5545   ", r,
				0.45023478, 0.00000001);

		ln_get_ell_geo_rect_posn(orbit, o_JD, posn);
		failed += test_result("(Geocentric Rect Coords X) for comet Enckle   ",
				posn.X, 0.72549850, 0.00000001);
		failed += test_result("(Geocentric Rect Coords Y) for comet Enckle   ",
				posn.Y, -0.28443537, 0.00000001);
		failed += test_result("(Geocentric Rect Coords Z) for comet Enckle   ",
				posn.Z, -0.27031656, 0.00000001);

		ln_get_ell_helio_rect_posn(orbit, o_JD, posn);
		failed += test_result(
				"(Heliocentric Rect Coords X) for comet Enckle   ", posn.X,
				0.25017473, 0.00000001);
		failed += test_result(
				"(Heliocentric Rect Coords Y) for comet Enckle   ", posn.Y,
				0.48476422, 0.00000001);
		failed += test_result(
				"(Heliocentric Rect Coords Z) for comet Enckle   ", posn.Z,
				0.35716517, 0.00000001);

		ln_get_ell_body_equ_coords(o_JD, orbit, equ_posn);
		failed += test_result("(RA) for comet Enckle   ", equ_posn.ra,
				158.58242653, 0.00000001);
		failed += test_result("(Dec) for comet Enckle   ", equ_posn.dec,
				19.13924815, 0.00000001);

		l = ln_get_ell_orbit_len(orbit);
		failed += test_result("(Orbit Length) for comet Enckle in AU   ", l,
				10.85028112, 0.00000001);

		V = ln_get_ell_orbit_pvel(orbit);
		failed += test_result(
				"(Orbit Perihelion Vel) for comet Enckle in kms   ", V,
				70.43130198, 0.00000001);

		V = ln_get_ell_orbit_avel(orbit);
		failed += test_result(
				"(Orbit Aphelion Vel) for comet Enckle in kms   ", V,
				5.70160892, 0.00000001);

		V = ln_get_ell_orbit_vel(o_JD, orbit);
		failed += test_result("(Orbit Vel JD) for comet Enckle in kms   ", V,
				48.16148331, 0.00000001);

		dist = ln_get_ell_body_solar_dist(o_JD, orbit);
		failed += test_result("(Body Solar Dist) for comet Enckle in AU   ",
				dist, 0.65203581, 0.00000001);

		dist = ln_get_ell_body_earth_dist(o_JD, orbit);
		failed += test_result("(Body Earth Dist) for comet Enckle in AU   ",
				dist, 0.82481670, 0.00000001);

		// TNO http://www.cfa.harvard.edu/mpec/K05/K05O42.html

		obs_date.years = 2006;
		obs_date.months = 5;
		obs_date.days = 5;
		obs_date.hours = 0;
		obs_date.minutes = 0;
		obs_date.seconds = 0;

		epoch_date.years = 2006;
		epoch_date.months = 3;
		epoch_date.days = 6;
		epoch_date.hours = 0;
		epoch_date.minutes = 0;
		epoch_date.seconds = 0;

		e_JD = ln_get_julian_day(epoch_date);
		o_JD = ln_get_julian_day(obs_date);

		orbit.JD = e_JD;
		orbit.a = 45.7082927;
		orbit.e = 0.1550125;
		orbit.i = 28.99870;
		orbit.omega = 79.55499;
		orbit.w = 296.40937;
		orbit.n = 0.00318942;

		// MPO refers to Mean anomaly & epoch, we hence need to convert epoch
		// to perihelion pass

		orbit.JD -= 147.09926 / orbit.n;

		ln_get_ell_body_equ_coords(o_JD, orbit, equ_posn);
		failed += test_result("(RA) for TNO K05F09Y   ", equ_posn.ra,
				184.3699999995, 0.001);
		failed += test_result("(Dec) for TNO K05F09Y  ", equ_posn.dec,
				30.3316666666, 0.001);

		Assert.assertEquals(0, failed);
	}

	/** need a proper parabolic orbit to properly test */

	@Test
	public void parabolic_motion_test() {
		double r, v, dist;
		double e_JD, o_JD;
		LnParOrbit orbit = new LnParOrbit();
		LnRectPosn posn = new LnRectPosn();
		;
		LnDate epoch_date = new LnDate(), obs_date = new LnDate();
		LnEquPosn equ_posn = new LnEquPosn();
		int failed = 0;

		obs_date.years = 2003;
		obs_date.months = 1;
		obs_date.days = 11;
		obs_date.hours = 0;
		obs_date.minutes = 0;
		obs_date.seconds = 0;

		epoch_date.years = 2003;
		epoch_date.months = 1;
		epoch_date.days = 29;
		epoch_date.hours = 0;
		epoch_date.minutes = 6;
		epoch_date.seconds = 37.44;

		e_JD = ln_get_julian_day(epoch_date);
		o_JD = ln_get_julian_day(obs_date);

		orbit.q = 0.190082;
		orbit.i = 94.1511;
		orbit.w = 187.5613;
		orbit.omega = 119.0676;
		orbit.JD = e_JD;

		v = ln_get_par_true_anomaly(orbit.q, o_JD - e_JD);
		failed += test_result(
				"(True Anomaly) v when e is 0.1 and E is 5.5545   ", v,
				247.18968605, 0.00000001);

		r = ln_get_par_radius_vector(orbit.q, o_JD - e_JD);
		failed += test_result(
				"(Radius Vector) r when v is , e is 0.1 and E is 5.5545   ", r,
				0.62085992, 0.00000001);

		ln_get_par_geo_rect_posn(orbit, o_JD, posn);
		failed += test_result(
				"(Geocentric Rect Coords X) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				posn.X, 0.29972461, 0.00000001);
		failed += test_result(
				"(Geocentric Rect Coords Y) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				posn.Y, -0.93359772, 0.00000001);
		failed += test_result(
				"(Geocentric Rect Coords Z) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				posn.Z, 0.24639194, 0.00000001);

		ln_get_par_helio_rect_posn(orbit, o_JD, posn);
		failed += test_result(
				"(Heliocentric Rect Coords X) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				posn.X, -0.04143700, 0.00000001);
		failed += test_result(
				"(Heliocentric Rect Coords Y) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				posn.Y, -0.08736588, 0.00000001);
		failed += test_result(
				"(Heliocentric Rect Coords Z) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				posn.Z, 0.61328397, 0.00000001);

		ln_get_par_body_equ_coords(o_JD, orbit, equ_posn);
		failed += test_result("(RA) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				equ_posn.ra, 287.79617309, 0.00000001);
		failed += test_result("(Dec) for comet C/2002 X5 (Kudo-Fujikawa)   ",
				equ_posn.dec, 14.11800859, 0.00000001);

		dist = ln_get_par_body_solar_dist(o_JD, orbit);
		failed += test_result(
				"(Body Solar Dist) for comet C/2002 X5 (Kudo-Fujikawa) in AU   ",
				dist, 0.62085992, 0.00001);

		dist = ln_get_par_body_earth_dist(o_JD, orbit);
		failed += test_result(
				"(Body Earth Dist) for comet C/2002 X5 (Kudo-Fujikawa) in AU   ",
				dist, 1.01101362, 0.00001);
		Assert.assertEquals(0, failed);
	}

	/** data from Meeus, chapter 35 */

	@Test
	public void hyperbolic_motion_test() {
		double r, v, dist;
		double e_JD, o_JD;
		LnHypOrbit orbit = new LnHypOrbit();
		LnDate epoch_date = new LnDate(), obs_date = new LnDate();
		LnEquPosn equ_posn = new LnEquPosn();
		int failed = 0;

		orbit.q = 3.363943;
		orbit.e = 1.05731;

		// the one from Meeus..
		v = ln_get_hyp_true_anomaly(orbit.q, orbit.e, 1237.1);
		failed += test_result(
				"(True Anomaly) v when q is 3.363943 and e is 1.05731   ", v,
				109.40598, 0.00001);

		r = ln_get_hyp_radius_vector(orbit.q, orbit.e, 1237.1);
		failed += test_result(
				"(Radius Vector) r when q is 3.363943 and e is 1.05731  ", r,
				10.668551, 0.00001);

		// and now something real.. C/2001 Q4 (NEAT)
		obs_date.years = 2004;
		obs_date.months = 5;
		obs_date.days = 15;
		obs_date.hours = 0;
		obs_date.minutes = 0;
		obs_date.seconds = 0;

		epoch_date.years = 2004;
		epoch_date.months = 5;
		epoch_date.days = 15;
		epoch_date.hours = 23;
		epoch_date.minutes = 12;
		epoch_date.seconds = 37.44;

		e_JD = ln_get_julian_day(epoch_date);
		o_JD = ln_get_julian_day(obs_date);

		orbit.q = 0.961957;
		orbit.e = 1.000744;
		orbit.i = 99.6426;
		orbit.w = 1.2065;
		orbit.omega = 210.2785;
		orbit.JD = e_JD;

		r = ln_get_hyp_radius_vector(orbit.q, orbit.e, o_JD - e_JD);
		failed += test_result("(Radius Vector) r for C/2001 Q4 (NEAT)   ", r,
				0.962, 0.001);

		ln_get_hyp_body_equ_coords(o_JD, orbit, equ_posn);
		failed += test_result("(RA) for comet C/2001 Q4 (NEAT)   ",
				equ_posn.ra, 128.01, 0.01);
		failed += test_result("(Dec) for comet C/2001 Q4 (NEAT)   ",
				equ_posn.dec, 18.3266666666, 0.03);

		dist = ln_get_hyp_body_solar_dist(o_JD, orbit);
		failed += test_result(
				"(Body Solar Dist) for comet C/2001 Q4 (NEAT) in AU   ", dist,
				0.962, 0.001);

		obs_date.years = 2005;
		obs_date.months = 1;

		o_JD = ln_get_julian_day(obs_date);

		r = ln_get_hyp_radius_vector(orbit.q, orbit.e, o_JD - e_JD);
		failed += test_result("(Radius Vector) r for C/2001 Q4 (NEAT)   ", r,
				3.581, 0.001);

		ln_get_hyp_body_equ_coords(o_JD, orbit, equ_posn);
		failed += test_result("(RA) for comet C/2001 Q4 (NEAT)   ",
				equ_posn.ra, 332.9025, 0.01);
		failed += test_result("(Dec) for comet C/2001 Q4 (NEAT)   ",
				equ_posn.dec, 58.6116666666, 0.001);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void rst_test() {
		LnLnlatPosn observer = new LnLnlatPosn();
		LnRstTime rst = new LnRstTime();
		LnHms hms = new LnHms();
		LnDms dms = new LnDms();
		LnDate date = new LnDate();
		LnEquPosn object = new LnEquPosn();
		double JD, JD_next;
		int ret;
		int failed = 0;

		// Arcturus
		hms.hours = 14;
		hms.minutes = 15;
		hms.seconds = 39.67;

		dms.neg = 0;
		dms.degrees = 19;
		dms.minutes = 10;
		dms.seconds = 56.7;

		object.ra = ln_hms_to_deg(hms);
		object.dec = ln_dms_to_deg(dms);

		date.years = 2006;
		date.months = 1;
		date.days = 17;

		date.hours = 0;
		date.minutes = 0;
		date.seconds = 0;

		JD = ln_get_julian_day(date);

		observer.lng = 15;
		observer.lat = 51;

		ret = ln_get_object_rst(JD, observer, object, rst);
		failed += test_result("Arcturus sometimes rise at 15 E, 51 N", ret, 0,
				0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus rise hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 21, 0);
			failed += test_result(
					"Arcturus rise minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 40, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus transit hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus transit minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 29, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus set hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 13, 0);
			failed += test_result(
					"Arcturus set minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 14, 0);
		}

		JD_next = rst.transit - 0.001;
		ret = ln_get_object_next_rst(JD_next, observer, object, rst);
		failed += test_result("Arcturus sometimes rise at 15 E, 51 N", ret, 0,
				0);

		if (ret == 0) {
			failed += test_result(
					"Arcturus next date is less then transit time",
					(JD_next < rst.transit), 1, 0);
			failed += test_result(
					"Arcturus next transit time is less then set time",
					(rst.transit < rst.set), 1, 0);
			failed += test_result(
					"Arcturus next set time is less then rise time",
					(rst.set < rst.rise), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 21, 0);
			failed += test_result(
					"Arcturus next rise minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 40, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 29, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 13, 0);
			failed += test_result(
					"Arcturus next set minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 14, 0);
		}

		JD_next = rst.set - 0.001;
		ret = ln_get_object_next_rst(JD_next, observer, object, rst);
		failed += test_result("Arcturus sometimes rise at 15 E, 51 N", ret, 0,
				0);

		if (ret == 0) {
			failed += test_result("Arcturus next date is less then set time",
					(JD_next < rst.set), 1, 0);
			failed += test_result(
					"Arcturus next set time is less then rise time",
					(rst.set < rst.rise), 1, 0);
			failed += test_result(
					"Arcturus next rise time is less then transit time",
					(rst.rise < rst.transit), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 21, 0);
			failed += test_result(
					"Arcturus next rise minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 40, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 25, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 13, 0);
			failed += test_result(
					"Arcturus next set minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 14, 0);
		}

		JD_next = rst.rise - 0.001;
		ret = ln_get_object_next_rst(JD_next, observer, object, rst);
		failed += test_result("Arcturus sometimes rise at 15 E, 51 N", ret, 0,
				0);

		if (ret == 0) {
			failed += test_result("Arcturus next date is less then rise time",
					(JD_next < rst.rise), 1, 0);
			failed += test_result(
					"Arcturus next rise time is less then transit time",
					(rst.rise < rst.transit), 1, 0);
			failed += test_result(
					"Arcturus next transit time is less then set time",
					(rst.transit < rst.set), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 21, 0);
			failed += test_result(
					"Arcturus next rise minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 40, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 25, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 13, 0);
			failed += test_result(
					"Arcturus next set minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 10, 0);
		}

		JD_next = rst.rise + 0.001;
		ret = ln_get_object_next_rst(JD_next, observer, object, rst);
		failed += test_result("Arcturus sometimes rise at 15 E, 51 N", ret, 0,
				0);

		if (ret == 0) {
			failed += test_result(
					"Arcturus next date is less then transit time",
					(JD_next < rst.transit), 1, 0);
			failed += test_result(
					"Arcturus next transit time is less then set time",
					(rst.transit < rst.set), 1, 0);
			failed += test_result(
					"Arcturus next set time is less then rise time",
					(rst.set < rst.rise), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 21, 0);
			failed += test_result(
					"Arcturus next rise minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 37, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 25, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 13, 0);
			failed += test_result(
					"Arcturus next set minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 10, 0);
		}

		ret = ln_get_object_rst_horizon(JD, observer, object,
				BigDecimal.valueOf(20), rst);
		failed += test_result(
				"Arcturus sometimes rise above 20 deg at 15 E, 51 N", ret, 0, 0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus rise above 20 deg hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 0, 0);
			failed += test_result(
					"Arcturus rise above 20 deg minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 6, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus transit hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus transit minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 29, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus set bellow 20 deg hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 10, 0);
			failed += test_result(
					"Arcturus set bellow 20 deg minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 52, 0);
		}

		JD_next = rst.rise - 0.002;
		ret = ln_get_object_next_rst_horizon(JD_next, observer, object,
				BigDecimal.valueOf(20), rst);
		failed += test_result(
				"Arcturus sometimes rise above 20 deg at 15 E, 51 N", ret, 0, 0);

		if (ret == 0) {
			failed += test_result("Arcturus next date is less then rise time",
					(JD_next < rst.rise), 1, 0);
			failed += test_result(
					"Arcturus next rise time is less then transit time",
					(rst.rise < rst.transit), 1, 0);
			failed += test_result(
					"Arcturus next transit time is less then set time",
					(rst.transit < rst.set), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise above 20 deg hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 0, 0);
			failed += test_result(
					"Arcturus next rise above 20 deg minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 6, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 29, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set bellow 20 deg hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 10, 0);
			failed += test_result(
					"Arcturus next set bellow 20 deg minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 52, 0);
		}

		JD_next = rst.transit - 0.001;
		ret = ln_get_object_next_rst_horizon(JD_next, observer, object,
				BigDecimal.valueOf(20), rst);
		failed += test_result(
				"Arcturus sometimes rise above 20 deg at 15 E, 51 N", ret, 0, 0);

		if (ret == 0) {
			failed += test_result(
					"Arcturus next date is less then transit time",
					(JD_next < rst.transit), 1, 0);
			failed += test_result(
					"Arcturus next transit time is less then set time",
					(rst.transit < rst.set), 1, 0);
			failed += test_result(
					"Arcturus next set time is less then rise time",
					(rst.set < rst.rise), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise above 20 deg hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 0, 0);
			failed += test_result(
					"Arcturus next rise above 20 deg minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 2, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 29, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set bellow 20 deg hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 10, 0);
			failed += test_result(
					"Arcturus next set bellow 20 deg minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 52, 0);
		}

		JD_next = rst.set - 0.001;
		ret = ln_get_object_next_rst_horizon(JD_next, observer, object,
				BigDecimal.valueOf(20), rst);
		failed += test_result(
				"Arcturus sometimes rise above 20 deg at 15 E, 51 N", ret, 0, 0);

		if (ret == 0) {
			failed += test_result("Arcturus next date is less then set time",
					(JD_next < rst.set), 1, 0);
			failed += test_result(
					"Arcturus next set time is less then rise time",
					(rst.set < rst.rise), 1, 0);
			failed += test_result(
					"Arcturus next rise time is less then transit time",
					(rst.rise < rst.transit), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise above 20 deg hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 0, 0);
			failed += test_result(
					"Arcturus next rise above 20 deg minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 2, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 25, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set bellow 20 deg hour on 2006/01/17 at (15 E,51 N)",
					date.hours, 10, 0);
			failed += test_result(
					"Arcturus next set bellow 20 deg minute on 2006/01/17 at (15 E,51 N)",
					date.minutes, 52, 0);
		}

		JD_next = rst.set + 0.001;
		ret = ln_get_object_next_rst_horizon(JD_next, observer, object,
				BigDecimal.valueOf(20), rst);
		failed += test_result(
				"Arcturus sometimes rise above 20 deg at 15 E, 51 N", ret, 0, 0);

		if (ret == 0) {
			failed += test_result("Arcturus next date is less then rise time",
					(JD_next < rst.rise), 1, 0);
			failed += test_result(
					"Arcturus next rise time is less then transit time",
					(rst.rise < rst.transit), 1, 0);
			failed += test_result(
					"Arcturus next transit time is less then set time",
					(rst.transit < rst.set), 1, 0);

			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus next rise above 20 deg hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 0, 0);
			failed += test_result(
					"Arcturus next rise above 20 deg minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 2, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus next transit hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus next transit minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 25, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus next set bellow 20 deg hour on 2006/01/18 at (15 E,51 N)",
					date.hours, 10, 0);
			failed += test_result(
					"Arcturus next set bellow 20 deg minute on 2006/01/18 at (15 E,51 N)",
					date.minutes, 48, 0);
		}

		observer.lat = -51;

		ret = ln_get_object_rst(JD, observer, object, rst);
		failed += test_result("Arcturus sometimes rise at 15 E, 51 S", ret, 0,
				0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus rise hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 1, 0);
			failed += test_result(
					"Arcturus rise minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 7, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus transit hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus transit minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 29, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus set hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 9, 0);
			failed += test_result(
					"Arcturus set minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 51, 0);
		}

		ret = ln_get_object_rst_horizon(JD, observer, object,
				BigDecimal.valueOf(-20), rst);
		failed += test_result(
				"Arcturus sometimes rise above -20 deg at 15 E, 51 S", ret, 0,
				0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"Arcturus rise above -20 deg hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 22, 0);
			failed += test_result(
					"Arcturus rise above -20 deg minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 50, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Arcturus transit hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 5, 0);
			failed += test_result(
					"Arcturus transit minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 29, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Arcturus set bellow -20 deg hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 12, 0);
			failed += test_result(
					"Arcturus set bellow -20 deg minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 4, 0);
		}

		ret = ln_get_solar_rst(JD, observer, rst);
		failed += test_result("Sun sometimes rise on 2006/01/17 at 15 E, 51 S",
				ret, 0, 0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result("Sun rise hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 3, 0);
			failed += test_result(
					"Sun rise minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 11, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Sun transit hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 11, 0);
			failed += test_result(
					"Sun transit minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 9, 0);

			ln_get_date(rst.set, date);
			failed += test_result("Sun set hour on 2006/01/17 at (15 E,51 S)",
					date.hours, 19, 0);
			failed += test_result(
					"Sun set minute on 2006/01/17 at (15 E,51 S)",
					date.minutes, 7, 0);
		}

		observer.lat = 37;

		object.dec = -54;
		failed += test_result("Object at dec -54 never rise at 37 N",
				ln_get_object_rst(JD, observer, object, rst), -1, 0);

		object.dec = -52;
		failed += test_result("Object at dec -52 rise at 37 N",
				ln_get_object_rst(JD, observer, object, rst), 0, 0);

		object.dec = 54;
		failed += test_result(
				"Object at dec 54 is always above the horizon at 37 N",
				ln_get_object_rst(JD, observer, object, rst), 1, 0);

		object.dec = 52;
		failed += test_result("Object at dec 52 rise at 37 N",
				ln_get_object_rst(JD, observer, object, rst), 0, 0);

		observer.lat = -37;

		object.dec = 54;
		failed += test_result("Object at dec 54 never rise at 37 S",
				ln_get_object_rst(JD, observer, object, rst), -1, 0);

		object.dec = 52;
		failed += test_result("Object at dec 52 rise at 37 S",
				ln_get_object_rst(JD, observer, object, rst), 0, 0);

		object.dec = -54;
		failed += test_result(
				"Object at dec -54 is always above the horizon at 37 S",
				ln_get_object_rst(JD, observer, object, rst), 1, 0);

		object.dec = -52;
		failed += test_result("Object at dec -52 rise at 37 S",
				ln_get_object_rst(JD, observer, object, rst), 0, 0);

		/** Venus on 1988 March 20 at Boston */
		date.years = 1988;
		date.months = 3;
		date.days = 20;

		date.hours = 0;
		date.minutes = 0;
		date.seconds = 0;

		JD = ln_get_julian_day(date);

		observer.lng = -71.0833;
		observer.lat = 42.3333;

		ret = ln_get_venus_rst(JD, observer, rst);
		failed += test_result("Venus sometime rise on 1988/03/20 at Boston",
				ret, 0, 0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result("Venus rise hour on 1988/03/20 at Boston",
					date.hours, 12, 0);
			failed += test_result("Venus rise minute on 1988/03/20 at Boston",
					date.minutes, 25, 0);

			ln_get_date(rst.transit, date);
			failed += test_result("Venus transit hour on 1988/03/20 at Boston",
					date.hours, 19, 0);
			failed += test_result(
					"Venus transit minute on 1988/03/20 at Boston",
					date.minutes, 41, 0);

			ln_get_date(rst.set, date);
			failed += test_result("Venus set hour on 1988/03/20 at Boston",
					date.hours, 2, 0);
			failed += test_result("Venus set minute on 1988/03/20 at Boston",
					date.minutes, 55, 0);
		}

		Assert.assertEquals(0, failed);
	}

	@Test
	public void ell_rst_test() {
		LnLnlatPosn observer = new LnLnlatPosn();
		LnEllOrbit orbit = new LnEllOrbit();
		LnDate date = new LnDate();
		LnRstTime rst = new LnRstTime();
		LnEquPosn pos = new LnEquPosn();
		double JD;
		int ret;
		int failed = 0;

		/** Comment C/1996 B2 (Hyakutake) somewhere at Japan */

		observer.lng = 135.0;
		observer.lat = 35.0;

		date.years = 1996;
		date.months = 5;
		date.days = 1;

		date.hours = 0;
		date.minutes = 0;
		date.seconds = 0.0;

		orbit.JD = ln_get_julian_day(date);
		orbit.JD += 0.39481;
		orbit.a = 1014.2022026431;
		orbit.e = 0.9997730;
		orbit.i = 124.92379;
		orbit.omega = 188.04546;
		orbit.w = 130.17654;
		orbit.n = 0.0;

		date.years = 1996;
		date.months = 3;
		date.days = 24;

		date.hours = date.minutes = 0;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);

		ln_get_ell_body_equ_coords(JD, orbit, pos);
		failed += test_result("(RA) for Hyakutake 1996/03/28 00:00", pos.ra,
				220.8554, 0.001);
		failed += test_result("(Dec) for Hyakutake 1996/03/28 00:00", pos.dec,
				36.5341, 0.001);

		date.days = 28;

		date.hours = 10;
		date.minutes = 42;

		JD = ln_get_julian_day(date);

		ln_get_ell_body_equ_coords(JD, orbit, pos);
		failed += test_result("(RA) for Hyakutake 1996/03/28 10:42", pos.ra,
				56.2140, 0.001);
		failed += test_result("(Dec) for Hyakutake 1996/03/28 10:42", pos.dec,
				74.4302, 0.001);

		date.days = 23;

		date.hours = 17;
		date.minutes = 38;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);

		ln_get_ell_body_equ_coords(JD, orbit, pos);
		failed += test_result("(RA) for Hyakutake 1996/03/23 17:38", pos.ra,
				221.2153, 0.001);
		failed += test_result("(Dec) for Hyakutake 1996/03/23 17:38", pos.dec,
				32.4803, 0.001);

		JD = ln_get_julian_day(date);

		ret = ln_get_ell_body_rst(JD, observer, orbit, rst);
		failed += test_result(
				"Hyakutake sometime rise on 1996/03/23 at 135 E, 35 N", ret, 0,
				0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"Hyakutake rise hour on 1996/03/23 at 135 E, 35 N",
					date.hours, 9, 0);
			failed += test_result(
					"Hyakutake rise minute on 1996/03/23 at 135 E, 35 N",
					date.minutes, 22, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Hyakutake transit hour on 1996/03/23 at 135 E, 35 N",
					date.hours, 17, 0);
			failed += test_result(
					"Hyakutake transit minute on 1996/03/23 at 135 E, 35 N",
					date.minutes, 27, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Hyakutake set hour on 1996/03/23 at 135 E, 35 N",
					date.hours, 1, 0);
			failed += test_result(
					"Hyakutake set minute on 1996/03/23 at 135 E, 35 N",
					date.minutes, 49, 0);
		}

		ret = ln_get_ell_body_next_rst(JD, observer, orbit, rst);
		failed += test_result(
				"Hyakutake sometime rise on 1996/03/23 at 135 E, 35 N", ret, 0,
				0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"Hyakutake next rise hour on 1996/03/23 at 135 E, 35 N",
					date.hours, 9, 0);
			failed += test_result(
					"Hyakutake next rise minute on 1996/03/23 at 135 E, 35 N",
					date.minutes, 44, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Hyakutake next transit hour on 1996/03/24 at 135 E, 35 N",
					date.hours, 17, 0);
			failed += test_result(
					"Hyakutake next transit minute on 1996/03/24 at 135 E, 35 N",
					date.minutes, 38, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Hyakutake next set hour on 1996/03/23 at 135 E, 35 N",
					date.hours, 1, 0);
			failed += test_result(
					"Hyakutake next set minute on 1996/03/23 at 135 E, 35 N",
					date.minutes, 32, 0);
		}

		Assert.assertEquals(0, failed);
	}

	@Test
	public void hyp_future_rst_test() {
		LnLnlatPosn observer = new LnLnlatPosn();
		LnHypOrbit orbit = new LnHypOrbit();
		LnDate date = new LnDate();
		LnRstTime rst = new LnRstTime();
		double JD;
		int ret;
		int failed = 0;

		observer.lng = 15.0;
		observer.lat = 50.0;

		/** C/2006 P1 (McNaught) */

		orbit.q = 0.170742005109787;
		orbit.e = 1.00001895427704;
		orbit.i = 77.8348999023438;
		orbit.w = 155.977096557617;
		orbit.omega = 267.414398193359;
		orbit.JD = 2454113.251;

		date.years = 2007;
		date.months = 1;
		date.days = 17;

		date.hours = 12;
		date.minutes = 0;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);

		ret = ln_get_hyp_body_next_rst_horizon(JD, observer, orbit, 0, rst);
		failed += test_result("McNaught rise on 2997/01/18 at 15 E, 50 N", ret,
				0, 0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"McNaught rise hour on 2007/01/18 at 15 E, 50 N",
					date.hours, 8, 0);
			failed += test_result(
					"McNaught rise minute on 2007/01/18 at 15 E, 50 N",
					date.minutes, 52, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"McNaught transit hour on 2007/01/18 at 15 E, 50 N",
					date.hours, 11, 0);
			failed += test_result(
					"McNaught transit minute on 2007/01/18 at 15 E, 50 N",
					date.minutes, 38, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"McNaught set hour on 2007/01/17 at 15 E, 50 N",
					date.hours, 14, 0);
			failed += test_result(
					"McNaught set minute on 2007/01/17 at 15 E, 50 N",
					date.minutes, 23, 0);
		}

		ret = ln_get_hyp_body_next_rst_horizon(JD, observer, orbit, 15, rst);
		failed += test_result("McNaught does not rise above 15 degrees on"
				+ "2007/01/17 at 15 E, 50 N", ret, -1, 0);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void body_future_rst_test() {
		LnLnlatPosn observer = new LnLnlatPosn();
		LnDate date = new LnDate();
		LnRstTime rst = new LnRstTime();
		double JD;
		int ret;
		int failed = 0;

		observer.lng = 0;
		observer.lat = 85;

		date.years = 2006;
		date.months = 1;
		date.days = 1;

		date.hours = date.minutes = 0;
		date.seconds = 0.0;

		JD = ln_get_julian_day(date);

		ret = ln_get_body_next_rst_horizon_future(JD, observer,
				new IGetEquBodyCoords() {

					@Override
					public void get_equ_body_coords(double JD,
							LnEquPosn position) {
						ln_get_solar_equ_coords(JD, position);
					}
				}, LN_SOLAR_STANDART_HORIZON, 300, rst);

		failed += test_result("Sun is above horizon sometimes at 0, 85 N", ret,
				0, 0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result("Solar next rise years at 0, 85 N",
					date.years, 2006, 0);
			failed += test_result("Solar next rise months at 0, 85 N",
					date.months, 3, 0);
			failed += test_result("Solar next rise days at 0, 85 N", date.days,
					7, 0);
			failed += test_result("Solar next rise hour at 0, 85 N",
					date.hours, 10, 0);
			failed += test_result("Solar next rise minute at 0, 85 N",
					date.minutes, 19, 0);

			ln_get_date(rst.transit, date);
			failed += test_result("Solar next transit years at 0, 85 N",
					date.years, 2006, 0);
			failed += test_result("Solar next transit months at 0, 85 N",
					date.months, 3, 0);
			failed += test_result("Solar next transit days at 0, 85 N",
					date.days, 7, 0);
			failed += test_result("Solar next transit hour at 0 E, 85 N",
					date.hours, 12, 0);
			failed += test_result("Solar next transit minute at 0 E, 85 N",
					date.minutes, 10, 0);

			ln_get_date(rst.set, date);
			failed += test_result("Solar next set years at 0, 85 N",
					date.years, 2006, 0);
			failed += test_result("Solar next set months at 0, 85 N",
					date.months, 3, 0);
			failed += test_result("Solar next set days at 0, 85 N", date.days,
					7, 0);
			failed += test_result("Solar next set hour at 0 E, 85 N",
					date.hours, 14, 0);
			failed += test_result("Solar next set minute at 0, 85 N",
					date.minutes, 8, 0);
		}

		ret = ln_get_body_next_rst_horizon_future(JD, observer,
				new IGetEquBodyCoords() {

					@Override
					public void get_equ_body_coords(double JD,
							LnEquPosn position) {
						ln_get_solar_equ_coords(JD, position);
					}
				}, 0, 300, rst);
		failed += test_result("Sun is above 0 horizon sometimes at 0, 85 N",
				ret, 0, 0);

		if (ret == 0) {
			ln_get_date(rst.rise, date);
			failed += test_result(
					"Solar next rise years at 0, 85 N with 0 horizon",
					date.years, 2006, 0);
			failed += test_result(
					"Solar next rise months at 0, 85 N with 0 horizon",
					date.months, 3, 0);
			failed += test_result(
					"Solar next rise days at 0, 85 N with 0 horizon",
					date.days, 9, 0);
			failed += test_result(
					"Solar next rise hour at 0, 85 N with 0 horizon",
					date.hours, 10, 0);
			failed += test_result(
					"Solar next rise minute at 0, 85 N with 0 horizon",
					date.minutes, 23, 0);

			ln_get_date(rst.transit, date);
			failed += test_result(
					"Solar next transit years at 0, 85 N with 0 horizon",
					date.years, 2006, 0);
			failed += test_result(
					"Solar next transit months at 0, 85 N with 0 horizon",
					date.months, 3, 0);
			failed += test_result(
					"Solar next transit days at 0, 85 N with 0 horizon",
					date.days, 9, 0);
			failed += test_result(
					"Solar next transit hour at 0 E, 85 N with 0 horizon",
					date.hours, 12, 0);
			failed += test_result(
					"Solar next transit minute at 0 E, 85 N with 0 horizon",
					date.minutes, 10, 0);

			ln_get_date(rst.set, date);
			failed += test_result(
					"Solar next set years at 0, 85 N with 0 horizon",
					date.years, 2006, 0);
			failed += test_result(
					"Solar next set months at 0, 85 N with 0 horizon",
					date.months, 3, 0);
			failed += test_result(
					"Solar next set days at 0, 85 N with 0 horizon", date.days,
					9, 0);
			failed += test_result(
					"Solar next set hour at 0 E, 85 N with 0 horizon",
					date.hours, 14, 0);
			failed += test_result(
					"Solar next set minute at 0, 85 N with 0 horizon",
					date.minutes, 2, 0);
		}

		Assert.assertEquals(0, failed);
	}

	@Test
	public void parallax_test() {
		LnEquPosn mars = new LnEquPosn(), parallax = new LnEquPosn();
		LnLnlatPosn observer = new LnLnlatPosn();
		LnDms dms = new LnDms();
		LnDate date = new LnDate();
		double jd;
		int failed = 0;

		dms.neg = 0;
		dms.degrees = 33;
		dms.minutes = 21;
		dms.seconds = 22.0;

		observer.lat = ln_dms_to_deg(dms);

		dms.neg = 1;
		dms.degrees = 116;
		dms.minutes = 51;
		dms.seconds = 47.0;

		observer.lng = ln_dms_to_deg(dms);

		date.years = 2003;
		date.months = 8;
		date.days = 28;

		date.hours = 3;
		date.minutes = 17;
		date.seconds = 0.0;

		jd = ln_get_julian_day(date);

		ln_get_mars_equ_coords(jd, mars);

		ln_get_parallax(mars, ln_get_mars_earth_dist(jd), observer, 1706, jd,
				parallax);

		/** parallax is hard to calculate, so we allow relatively big error */
		failed += test_result("Mars RA parallax for Palomar observatory at"
				+ "2003/08/28 3:17 UT  ", parallax.ra, 0.0053917, 0.00001);
		failed += test_result("Mars DEC parallax for Palomar observatory at"
				+ "2003/08/28 3:17 UT  ", parallax.dec, -14.1 / 3600.0, 0.00002);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void angular_test() {
		int failed = 0;
		double d;
		LnEquPosn posn1 = new LnEquPosn(), posn2 = new LnEquPosn();

		/** alpha Bootes (Arcturus) */
		posn1.ra = 213.9154;
		posn1.dec = 19.1825;

		/** alpha Virgo (Spica) */
		posn2.ra = 201.2983;
		posn2.dec = -11.1614;

		d = ln_get_angular_separation(posn1, posn2);
		failed += test_result("(Angular) Separation of Arcturus and Spica   ",
				d, 32.79302684, 0.00001);

		d = ln_get_rel_posn_angle(posn1, posn2);
		failed += test_result(
				"(Angular) Position Angle of Arcturus and Spica   ", d,
				22.39042787, 0.00001);

		Assert.assertEquals(0, failed);
	}

	@Test
	public void utility_test() {
		LnDms dms = new LnDms();
		double deg = -1.23, deg2 = 1.23, deg3 = -0.5;

		ln_deg_to_dms(deg, dms);
		System.out
				.format("TEST deg %f ==> deg %c%d min %d sec %f\n", deg,
						dms.neg != 0 ? '-' : '+', dms.degrees, dms.minutes,
						dms.seconds);

		Assert.assertEquals(
				"TEST deg -1.230000 ==> deg -1 min 13 sec 48.000000", String
						.format("TEST deg %f ==> deg %c%d min %d sec %f", deg,
								dms.neg != 0 ? '-' : '+', dms.degrees,
								dms.minutes, dms.seconds));

		ln_deg_to_dms(deg2, dms);
		System.out
				.format("TEST deg %f ==> deg %c%d min %d sec %f\n", deg2,
						dms.neg != 0 ? '-' : '+', dms.degrees, dms.minutes,
						dms.seconds);
		Assert.assertEquals(
				"TEST deg 1.230000 ==> deg +1 min 13 sec 48.000000", String
						.format("TEST deg %f ==> deg %c%d min %d sec %f", deg2,
								dms.neg != 0 ? '-' : '+', dms.degrees,
								dms.minutes, dms.seconds));
		ln_deg_to_dms(deg3, dms);
		System.out
				.format("TEST deg %f ==> deg %c%d min %d sec %f\n", deg3,
						dms.neg != 0 ? '-' : '+', dms.degrees, dms.minutes,
						dms.seconds);
		Assert.assertEquals(
				"TEST deg -0.500000 ==> deg -0 min 30 sec 0.000000", String
						.format("TEST deg %f ==> deg %c%d min %d sec %f", deg3,
								dms.neg != 0 ? '-' : '+', dms.degrees,
								dms.minutes, dms.seconds));
	}

	@Test
	public void airmass_test() {
		int failed = 0;
		double x;

		double X = ln_get_airmass(90, 750.0);
		failed += test_result("(Airmass) Airmass at Zenith", X, 1, 0);

		X = ln_get_airmass(10, 750.0);
		failed += test_result("(Airmass) Airmass at 10 degrees altitude", X,
				5.64, 0.1);

		X = ln_get_alt_from_airmass(1, 750.0);
		failed += test_result("(Airmass) Altitude at airmass 1", X, 90, 0);

		for (x = -10; x < 90; x += 10.54546456) {
			X = ln_get_alt_from_airmass(ln_get_airmass(x, 750.0), 750.0);
			failed += test_result("(Airmass) Altitude->Airmass->Altitude at"
					+ "10 degrees", X, x, 0.000000001);
		}

		Assert.assertEquals(0, failed);
	}
	
}
