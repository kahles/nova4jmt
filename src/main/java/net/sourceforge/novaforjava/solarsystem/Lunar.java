package net.sourceforge.novaforjava.solarsystem;

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

import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static net.sourceforge.novaforjava.RiseSet.ln_get_body_rst_horizon;
import static net.sourceforge.novaforjava.Transform.ln_get_equ_from_ecl;
import static net.sourceforge.novaforjava.Utility.ln_deg_to_rad;
import static net.sourceforge.novaforjava.Utility.ln_rad_to_deg;
import static net.sourceforge.novaforjava.Utility.ln_range_degrees;
import static net.sourceforge.novaforjava.Utility.ln_range_radians;
import static net.sourceforge.novaforjava.Utility.ln_range_radians2;
import static net.sourceforge.novaforjava.solarsystem.Earth.ln_get_earth_solar_dist;
import static net.sourceforge.novaforjava.solarsystem.Solar.ln_get_solar_ecl_coords;
import static net.sourceforge.novaforjava.solarsystem.Solar.ln_get_solar_equ_coords;
import static net.sourceforge.novaforjava.util.DataReader.format;
import static net.sourceforge.novaforjava.util.DataReader.readDoubleArray;
import static net.sourceforge.novaforjava.util.DataReader.readIntArray;
import static net.sourceforge.novaforjava.util.DataReader.skipBlanks;
import static net.sourceforge.novaforjava.util.DataReader.skipOverChar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.novaforjava.api.LnEquPosn;
import net.sourceforge.novaforjava.api.LnLnlatPosn;
import net.sourceforge.novaforjava.api.LnRectPosn;
import net.sourceforge.novaforjava.api.LnRstTime;
import net.sourceforge.novaforjava.util.IGetEquBodyCoords;

public class Lunar {

	public static double LN_LUNAR_STANDART_HORIZON = 0.125;
	/** AU in KM */
	public static long AU = 149597870L;

	/** Chapront theory lunar constants */
	static final double RAD = (648000.0 / Math.PI);
	static final double DEG = (Math.PI / 180.0);
	static final double M_PI_2 = (2.0 * Math.PI);
	static final double PIS2 = (Math.PI / 2.0);
	static final double ATH = 384747.9806743165;
	static final double A0 = 384747.9806448954;
	static final double AM = 0.074801329518;
	static final double ALPHA = 0.002571881335;
	static final double DTASM = (2.0 * ALPHA / (3.0 * AM));
	static final double W12 = (1732559343.73604 / RAD);
	static final double PRECES = (5029.0966 / RAD);
	static final double C1 = 60.0;
	static final double C2 = 3600.0;

	/** Corrections of the constants for DE200/LE200 */
	static final double DELNU = ((0.55604 / RAD) / W12);
	static final double DELE = (0.01789 / RAD);
	static final double DELG = (-0.08066 / RAD);
	static final double DELNP = ((-0.06424 / RAD) / W12);
	static final double DELEP = (-0.12879 / RAD);

	/** Precession matrix */
	static final double P1 = 0.10180391e-4d;
	static final double P2 = 0.47020439e-6d;
	static final double P3 = -0.5417367e-9d;
	static final double P4 = -0.2507948e-11d;
	static final double P5 = 0.463486e-14d;
	static final double Q1 = -0.113469002e-3d;
	static final double Q2 = 0.12372674e-6d;
	static final double Q3 = 0.1265417e-8d;
	static final double Q4 = -0.1371808e-11d;
	static final double Q5 = -0.320334e-14d;

	/** used for elp1 - 3 */
	public static class MainProblem {
		int ilu[];// 4
		double A;
		double B[];// 5

		public MainProblem(int[] ilu, double a, double[] b) {
			super();
			this.ilu = ilu;
			A = a;
			B = b;
		}

		public MainProblem(String line) {
			ParsePosition pos = new ParsePosition(1);
			ilu = readIntArray(line, pos, 4);
			skipOverChar(line, pos, ',');
			skipBlanks(line, pos);
			A = format.parse(line, pos).doubleValue();
			skipOverChar(line, pos, ',');
			B = readDoubleArray(line, pos, 5);
		}

		@Override
		public String toString() {
			return "{" + Arrays.toString(ilu) + "," + A + ","
					+ Arrays.toString(B) + "}";
		}

	};

	/** used for elp 4 - 9 */
	public static class EarthPert {
		int iz;
		int ilu[];// 4
		double O;
		double A;
		double P;

		public EarthPert(int iz, int[] ilu, double o, double a, double p) {
			super();
			this.iz = iz;
			this.ilu = ilu;
			O = o;
			A = a;
			P = p;
		}

		public EarthPert(String line) {
			ParsePosition pos = new ParsePosition(1);
			skipBlanks(line, pos);
			iz = format.parse(line, pos).intValue();
			skipOverChar(line, pos, ',');
			ilu = readIntArray(line, pos, 4);
			skipOverChar(line, pos, ',');
			skipBlanks(line, pos);
			O = format.parse(line, pos).doubleValue();
			skipOverChar(line, pos, ',');
			skipBlanks(line, pos);
			A = format.parse(line, pos).doubleValue();
			skipOverChar(line, pos, ',');
			skipBlanks(line, pos);
			P = format.parse(line, pos).doubleValue();
		}
	};

	/** used for elp 10 - 21 */
	public static class PlanetPert {
		int ipla[];// 11
		double theta;
		double O;
		double P;

		public PlanetPert(int[] ipla, double theta, double o, double p) {
			super();
			this.ipla = ipla;
			this.theta = theta;
			O = o;
			P = p;
		}

		public PlanetPert(String line) {
			ParsePosition pos = new ParsePosition(1);
			ipla = readIntArray(line, pos, 11);
			skipOverChar(line, pos, ',');
			skipBlanks(line, pos);
			theta = format.parse(line, pos).doubleValue();
			skipOverChar(line, pos, ',');
			skipBlanks(line, pos);
			O = format.parse(line, pos).doubleValue();
			skipOverChar(line, pos, ',');
			skipBlanks(line, pos);
			P = format.parse(line, pos).doubleValue();
		}

	};

	public static class TidalEffects extends EarthPert {

		public TidalEffects(int iz, int[] ilu, double o, double a, double p) {
			super(iz, ilu, o, a, p);
		}

		public TidalEffects(String line) {
			super(line);
		}
	}

	public static class MoonPert extends EarthPert {
		public MoonPert(int iz, int[] ilu, double o, double a, double p) {
			super(iz, ilu, o, a, p);
		}

		public MoonPert(String line) {
			super(line);
		}

	}

	public static class RelPert extends EarthPert {
		public RelPert(int iz, int[] ilu, double o, double a, double p) {
			super(iz, ilu, o, a, p);
		}

		public RelPert(String line) {
			super(line);
		}

	}

	public static class PlanSolPert extends EarthPert {
		public PlanSolPert(int iz, int[] ilu, double o, double a, double p) {
			super(iz, ilu, o, a, p);
		}

		public PlanSolPert(String line) {
			super(line);
		}

	}

	/** cache values */
	static double c_JD = 0;
	static double c_X = 0;
	static double c_Y = 0;
	static double c_Z = 0;
	static double c_precision = 1.0;

	/** constants with corrections for DE200 / LE200 */
	static double W1[] = {
			((218.0 + (18.0 / 60.0) + (59.95571 / 3600.0))) * DEG,
			1732559343.73604 / RAD, -5.8883 / RAD, 0.006604 / RAD,
			-0.00003169 / RAD };

	static double W2[] = {
			((83.0 + (21.0 / 60.0) + (11.67475 / 3600.0))) * DEG,
			14643420.2632 / RAD, -38.2776 / RAD, -0.045047 / RAD,
			0.00021301 / RAD };

	static double W3[] = { (125.0 + (2.0 / 60.0) + (40.39816 / 3600.0)) * DEG,
			-6967919.3622 / RAD, 6.3622 / RAD, 0.007625 / RAD,
			-0.00003586 / RAD };

	static double earth[] = {
			(100.0 + (27.0 / 60.0) + (59.22059 / 3600.0)) * DEG,
			129597742.2758 / RAD, -0.0202 / RAD, 0.000009 / RAD,
			0.00000015 / RAD };

	static double peri[] = {
			(102.0 + (56.0 / 60.0) + (14.42753 / 3600.0)) * DEG,
			1161.2283 / RAD, 0.5327 / RAD, -0.000138 / RAD, 0 };

	/** Delaunay's arguments. */
	static double del[][] = {
			{ 5.198466741027443, 7771.377146811758394, -0.000028449351621,
					0.000000031973462, -0.000000000154365 },
			{ -0.043125180208125, 628.301955168488007, -0.000002680534843,
					0.000000000712676, 0.000000000000727 },
			{ 2.355555898265799, 8328.691426955554562, 0.000157027757616,
					0.000000250411114, -0.000000001186339 },
			{ 1.627905233371468, 8433.466158130539043, -0.000059392100004,
					-0.000000004949948, 0.000000000020217 } };

	static double zeta[] = {
			(218.0 + (18.0 / 60.0) + (59.95571 / 3600.0)) * DEG,
			((1732559343.73604 / RAD) + PRECES) };

	/** Planetary arguments */
	static double p[][] = {
			{ (252 + 15 / C1 + 3.25986 / C2) * DEG, 538101628.68898 / RAD },
			{ (181 + 58 / C1 + 47.28305 / C2) * DEG, 210664136.43355 / RAD },
			{ (100.0 + (27.0 / 60.0) + (59.22059 / 3600.0)) * DEG,
					129597742.2758 / RAD },
			{ (355 + 25 / C1 + 59.78866 / C2) * DEG, 68905077.59284 / RAD },
			{ (34 + 21 / C1 + 5.34212 / C2) * DEG, 10925660.42861 / RAD },
			{ (50 + 4 / C1 + 38.89694 / C2) * DEG, 4399609.65932 / RAD },
			{ (314 + 3 / C1 + 18.01841 / C2) * DEG, 1542481.19393 / RAD },
			{ (304 + 20 / C1 + 55.19575 / C2) * DEG, 786550.32074 / RAD } };

	/** precision */
	static double pre[] = new double[3];

	private static final MainProblem[] main_elp1;
	private static final MainProblem[] main_elp2;
	private static final MainProblem[] main_elp3;
	private static final EarthPert[] earth_pert_elp4;
	private static final EarthPert[] earth_pert_elp5;
	private static final EarthPert[] earth_pert_elp6;
	private static final EarthPert[] earth_pert_elp7;
	private static final EarthPert[] earth_pert_elp8;
	private static final EarthPert[] earth_pert_elp9;
	private static final PlanetPert[] plan_pert_elp10;
	private static final PlanetPert[] plan_pert_elp11;
	private static final PlanetPert[] plan_pert_elp12;
	private static final PlanetPert[] plan_pert_elp13;
	private static final PlanetPert[] plan_pert_elp14;
	private static final PlanetPert[] plan_pert_elp15;
	private static final PlanetPert[] plan_pert_elp16;
	private static final PlanetPert[] plan_pert_elp17;
	private static final PlanetPert[] plan_pert_elp18;
	private static final PlanetPert[] plan_pert_elp19;
	private static final PlanetPert[] plan_pert_elp20;
	private static final PlanetPert[] plan_pert_elp21;
	private static final TidalEffects[] tidal_effects_elp22;
	private static final TidalEffects[] tidal_effects_elp23;
	private static final TidalEffects[] tidal_effects_elp24;
	private static final TidalEffects[] tidal_effects_elp25;
	private static final TidalEffects[] tidal_effects_elp26;
	private static final TidalEffects[] tidal_effects_elp27;
	private static final MoonPert[] moon_pert_elp28;
	private static final MoonPert[] moon_pert_elp29;
	private static final MoonPert[] moon_pert_elp30;
	private static final RelPert[] rel_pert_elp31;
	private static final RelPert[] rel_pert_elp32;
	private static final RelPert[] rel_pert_elp33;
	private static final PlanSolPert[] plan_sol_pert_elp34;
	private static final PlanSolPert[] plan_sol_pert_elp35;
	private static final PlanSolPert[] plan_sol_pert_elp36;

	static {
		try {
			InputStream in = Lunar.class.getResourceAsStream("lunar.data");
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;
			String varName = null;
			boolean readingMainProblem = false;
			boolean readingEarthPert = false;
			boolean readingPlanpert = false;
			boolean readingTidalEffects = false;
			boolean readingMoonPert = false;
			boolean readingRelPert = false;
			boolean readingPlanSolPert = false;
			List<MainProblem> mainProblems = new ArrayList<MainProblem>();
			List<EarthPert> earthPerts = new ArrayList<EarthPert>();
			List<PlanetPert> planPerts = new ArrayList<PlanetPert>();
			List<TidalEffects> tidalEffects = new ArrayList<TidalEffects>();
			List<MoonPert> moonPerts = new ArrayList<MoonPert>();
			List<RelPert> relPerts = new ArrayList<RelPert>();
			List<PlanSolPert> planSolPerts = new ArrayList<PlanSolPert>();
			Map<String, Object> objectLists = new HashMap<String, Object>();
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0) {
					if (line.charAt(0) == '{') {
						if (readingMainProblem) {
							mainProblems.add(new MainProblem(line));
						} else if (readingEarthPert) {
							earthPerts.add(new EarthPert(line));
						} else if (readingPlanpert) {
							planPerts.add(new PlanetPert(line));
						} else if (readingTidalEffects) {
							tidalEffects.add(new TidalEffects(line));
						} else if (readingMoonPert) {
							moonPerts.add(new MoonPert(line));
						} else if (readingRelPert) {
							relPerts.add(new RelPert(line));
						} else if (readingPlanSolPert) {
							planSolPerts.add(new PlanSolPert(line));
						}
					} else {
						if (readingMainProblem) {
							objectLists.put(varName, mainProblems
									.toArray(new MainProblem[mainProblems
											.size()]));
						} else if (readingEarthPert) {
							objectLists.put(varName, earthPerts
									.toArray(new EarthPert[earthPerts.size()]));
						} else if (readingPlanpert) {
							objectLists.put(varName, planPerts
									.toArray(new PlanetPert[planPerts.size()]));
						} else if (readingTidalEffects) {
							objectLists.put(varName, tidalEffects
									.toArray(new TidalEffects[tidalEffects
											.size()]));
						} else if (readingMoonPert) {
							objectLists.put(varName, moonPerts
									.toArray(new MoonPert[moonPerts.size()]));
						} else if (readingRelPert) {
							objectLists.put(varName, relPerts
									.toArray(new RelPert[relPerts.size()]));
						} else if (readingPlanSolPert) {
							objectLists.put(varName, planSolPerts
									.toArray(new PlanSolPert[planSolPerts
											.size()]));
						}

						readingMainProblem = false;
						readingEarthPert = false;
						readingPlanpert = false;
						readingTidalEffects = false;
						readingMoonPert = false;
						readingRelPert = false;
						readingPlanSolPert = false;
						varName = line.substring(line.lastIndexOf(' ') + 1);
						if (line.startsWith("main_problem")) {
							readingMainProblem = true;
							mainProblems.clear();
						} else if (line.startsWith("earth_pert")) {
							readingEarthPert = true;
							earthPerts.clear();
						} else if (line.startsWith("planet_pert")) {
							readingPlanpert = true;
							planPerts.clear();
						} else if (line.startsWith("tidal_effects")) {
							readingTidalEffects = true;
							tidalEffects.clear();
						} else if (line.startsWith("moon_pert")) {
							readingMoonPert = true;
							moonPerts.clear();
						} else if (line.startsWith("rel_pert")) {
							readingRelPert = true;
							relPerts.clear();
						} else if (line.startsWith("plan_sol_pert")) {
							readingPlanSolPert = true;
							planSolPerts.clear();
						} else {
							throw new RuntimeException("unknown data");
						}
					}
				}
			}
			objectLists.put(varName,
					planSolPerts.toArray(new PlanSolPert[planSolPerts.size()]));
			// read lunar.data
			main_elp1 = (MainProblem[]) objectLists.get("main_elp1");
			main_elp2 = (MainProblem[]) objectLists.get("main_elp2");
			main_elp3 = (MainProblem[]) objectLists.get("main_elp3");
			earth_pert_elp4 = (EarthPert[]) objectLists.get("earth_pert_elp4");
			earth_pert_elp5 = (EarthPert[]) objectLists.get("earth_pert_elp5");
			earth_pert_elp6 = (EarthPert[]) objectLists.get("earth_pert_elp6");
			earth_pert_elp7 = (EarthPert[]) objectLists.get("earth_pert_elp7");
			earth_pert_elp8 = (EarthPert[]) objectLists.get("earth_pert_elp8");
			earth_pert_elp9 = (EarthPert[]) objectLists.get("earth_pert_elp9");
			plan_pert_elp10 = (PlanetPert[]) objectLists.get("plan_pert_elp10");
			plan_pert_elp11 = (PlanetPert[]) objectLists.get("plan_pert_elp11");
			plan_pert_elp12 = (PlanetPert[]) objectLists.get("plan_pert_elp12");
			plan_pert_elp13 = (PlanetPert[]) objectLists.get("plan_pert_elp13");
			plan_pert_elp14 = (PlanetPert[]) objectLists.get("plan_pert_elp14");
			plan_pert_elp15 = (PlanetPert[]) objectLists.get("plan_pert_elp15");
			plan_pert_elp16 = (PlanetPert[]) objectLists.get("plan_pert_elp16");
			plan_pert_elp17 = (PlanetPert[]) objectLists.get("plan_pert_elp17");
			plan_pert_elp18 = (PlanetPert[]) objectLists.get("plan_pert_elp18");
			plan_pert_elp19 = (PlanetPert[]) objectLists.get("plan_pert_elp19");
			plan_pert_elp20 = (PlanetPert[]) objectLists.get("plan_pert_elp20");
			plan_pert_elp21 = (PlanetPert[]) objectLists.get("plan_pert_elp21");
			tidal_effects_elp22 = (TidalEffects[]) objectLists
					.get("tidal_effects_elp22");
			tidal_effects_elp23 = (TidalEffects[]) objectLists
					.get("tidal_effects_elp23");
			tidal_effects_elp24 = (TidalEffects[]) objectLists
					.get("tidal_effects_elp24");
			tidal_effects_elp25 = (TidalEffects[]) objectLists
					.get("tidal_effects_elp25");
			tidal_effects_elp26 = (TidalEffects[]) objectLists
					.get("tidal_effects_elp26");
			tidal_effects_elp27 = (TidalEffects[]) objectLists
					.get("tidal_effects_elp27");
			moon_pert_elp28 = (MoonPert[]) objectLists.get("moon_pert_elp28");
			moon_pert_elp29 = (MoonPert[]) objectLists.get("moon_pert_elp29");
			moon_pert_elp30 = (MoonPert[]) objectLists.get("moon_pert_elp30");
			rel_pert_elp31 = (RelPert[]) objectLists.get("rel_pert_elp31");
			rel_pert_elp32 = (RelPert[]) objectLists.get("rel_pert_elp32");
			rel_pert_elp33 = (RelPert[]) objectLists.get("rel_pert_elp33");
			plan_sol_pert_elp34 = (PlanSolPert[]) objectLists
					.get("plan_sol_pert_elp34");
			plan_sol_pert_elp35 = (PlanSolPert[]) objectLists
					.get("plan_sol_pert_elp35");
			plan_sol_pert_elp36 = (PlanSolPert[]) objectLists
					.get("plan_sol_pert_elp36");
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	/** sum lunar elp1 series */
	public static double sum_series_elp1(double[] t) {
		double result = 0;
		double x, y;
		double tgv;
		int i, j, k;

		for (j = 0; j < main_elp1.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(main_elp1[j].A) > pre[0]) {
				/** derivatives of A */
				tgv = main_elp1[j].B[0] + DTASM * main_elp1[j].B[4];
				x = main_elp1[j].A + tgv * (DELNP - AM * DELNU)
						+ main_elp1[j].B[1] * DELG + main_elp1[j].B[2] * DELE
						+ main_elp1[j].B[3] * DELEP;

				y = 0;
				for (k = 0; k < 5; k++) {
					for (i = 0; i < 4; i++)
						y += main_elp1[j].ilu[i] * del[i][k] * t[k];
				}

				/** y in correct quad */
				y = ln_range_radians2(y);
				result += x * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp2 series */
	public static double sum_series_elp2(double[] t) {
		double result = 0;
		double x, y;
		double tgv;
		int i, j, k;

		for (j = 0; j < main_elp2.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(main_elp2[j].A) > pre[1]) {
				/** derivatives of A */
				tgv = main_elp2[j].B[0] + DTASM * main_elp2[j].B[4];
				x = main_elp2[j].A + tgv * (DELNP - AM * DELNU)
						+ main_elp2[j].B[1] * DELG + main_elp2[j].B[2] * DELE
						+ main_elp2[j].B[3] * DELEP;

				y = 0;
				for (k = 0; k < 5; k++) {
					for (i = 0; i < 4; i++)
						y += main_elp2[j].ilu[i] * del[i][k] * t[k];
				}
				/** y in correct quad */
				y = ln_range_radians2(y);
				result += x * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp3 series */
	public static double sum_series_elp3(double[] t) {
		double result = 0;
		double x, y;
		double tgv;
		int i, j, k;

		for (j = 0; j < main_elp3.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(main_elp3[j].A) > pre[2]) {
				/** derivatives of A */
				tgv = main_elp3[j].B[0] + DTASM * main_elp3[j].B[4];
				x = main_elp3[j].A + tgv * (DELNP - AM * DELNU)
						+ main_elp3[j].B[1] * DELG + main_elp3[j].B[2] * DELE
						+ main_elp3[j].B[3] * DELEP;

				y = 0;
				for (k = 0; k < 5; k++) {
					for (i = 0; i < 4; i++)
						y += main_elp3[j].ilu[i] * del[i][k] * t[k];
				}
				y += (M_PI_2);
				/** y in correct quad */
				y = ln_range_radians2(y);
				result += x * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp4 series */
	public static double sum_series_elp4(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < earth_pert_elp4.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(earth_pert_elp4[j].A) > pre[0]) {
				y = earth_pert_elp4[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += earth_pert_elp4[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += earth_pert_elp4[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += earth_pert_elp4[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp5 series */
	public static double sum_series_elp5(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < earth_pert_elp5.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(earth_pert_elp5[j].A) > pre[1]) {
				y = earth_pert_elp5[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += earth_pert_elp5[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += earth_pert_elp5[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += earth_pert_elp5[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp6 series */
	public static double sum_series_elp6(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < earth_pert_elp6.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(earth_pert_elp6[j].A) > pre[2]) {
				y = earth_pert_elp6[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += earth_pert_elp6[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += earth_pert_elp6[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += earth_pert_elp6[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp7 series */
	public static double sum_series_elp7(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < earth_pert_elp7.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(earth_pert_elp7[j].A) > pre[0]) {
				A = earth_pert_elp7[j].A * t[1];
				y = earth_pert_elp7[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += earth_pert_elp7[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += earth_pert_elp7[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp8 series */
	public static double sum_series_elp8(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < earth_pert_elp8.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(earth_pert_elp8[j].A) > pre[1]) {
				y = earth_pert_elp8[j].O * DEG;
				A = earth_pert_elp8[j].A * t[1];
				for (k = 0; k < 2; k++) {
					y += earth_pert_elp8[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += earth_pert_elp8[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp9 series */
	public static double sum_series_elp9(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < earth_pert_elp9.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(earth_pert_elp9[j].A) > pre[2]) {
				A = earth_pert_elp9[j].A * t[1];
				y = earth_pert_elp9[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += earth_pert_elp9[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += earth_pert_elp9[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp10 series */
	public static double sum_series_elp10(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < plan_pert_elp10.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp10[j].O) > pre[0]) {
				y = plan_pert_elp10[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					y += (plan_pert_elp10[j].ipla[8] * del[0][k]
							+ plan_pert_elp10[j].ipla[9] * del[2][k] + plan_pert_elp10[j].ipla[10]
							* del[3][k])
							* t[k];
					for (i = 0; i < 8; i++)
						y += plan_pert_elp10[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += plan_pert_elp10[j].O * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp11 series */
	public static double sum_series_elp11(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < plan_pert_elp11.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp11[j].O) > pre[1]) {
				y = plan_pert_elp11[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					y += (plan_pert_elp11[j].ipla[8] * del[0][k]
							+ plan_pert_elp11[j].ipla[9] * del[2][k] + plan_pert_elp11[j].ipla[10]
							* del[3][k])
							* t[k];
					for (i = 0; i < 8; i++)
						y += plan_pert_elp11[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += plan_pert_elp11[j].O * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp12 series */
	public static double sum_series_elp12(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < plan_pert_elp12.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp12[j].O) > pre[2]) {
				y = plan_pert_elp12[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					y += (plan_pert_elp12[j].ipla[8] * del[0][k]
							+ plan_pert_elp12[j].ipla[9] * del[2][k] + plan_pert_elp12[j].ipla[10]
							* del[3][k])
							* t[k];
					for (i = 0; i < 8; i++)
						y += plan_pert_elp12[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += plan_pert_elp12[j].O * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp13 series */
	public static double sum_series_elp13(double[] t) {
		double result = 0;
		int i, j, k;
		double y, x;

		for (j = 0; j < plan_pert_elp13.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp13[j].O) > pre[0]) {
				y = plan_pert_elp13[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					y += (plan_pert_elp13[j].ipla[8] * del[0][k]
							+ plan_pert_elp13[j].ipla[9] * del[2][k] + plan_pert_elp13[j].ipla[10]
							* del[3][k])
							* t[k];
					for (i = 0; i < 8; i++)
						y += plan_pert_elp13[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				x = plan_pert_elp13[j].O * t[1];
				result += x * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp14 series */
	public static double sum_series_elp14(double[] t) {
		double result = 0;
		int i, j, k;
		double y, x;

		for (j = 0; j < plan_pert_elp14.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp14[j].O) > pre[1]) {
				y = plan_pert_elp14[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					y += (plan_pert_elp14[j].ipla[8] * del[0][k]
							+ plan_pert_elp14[j].ipla[9] * del[2][k] + plan_pert_elp14[j].ipla[10]
							* del[3][k])
							* t[k];
					for (i = 0; i < 8; i++)
						y += plan_pert_elp14[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				x = plan_pert_elp14[j].O * t[1];
				result += x * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp15 series */
	public static double sum_series_elp15(double[] t) {
		double result = 0;
		int i, j, k;
		double y, x;

		for (j = 0; j < plan_pert_elp15.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp15[j].O) > pre[2]) {
				y = plan_pert_elp15[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					y += (plan_pert_elp15[j].ipla[8] * del[0][k]
							+ plan_pert_elp15[j].ipla[9] * del[2][k] + plan_pert_elp15[j].ipla[10]
							* del[3][k])
							* t[k];
					for (i = 0; i < 8; i++)
						y += plan_pert_elp15[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				x = plan_pert_elp15[j].O * t[1];
				result += x * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp16 series */
	public static double sum_series_elp16(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < plan_pert_elp16.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp16[j].O) > pre[0]) {
				y = plan_pert_elp16[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					for (i = 0; i < 4; i++)
						y += plan_pert_elp16[j].ipla[i + 7] * del[i][k] * t[k];
					for (i = 0; i < 7; i++)
						y += plan_pert_elp16[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += plan_pert_elp16[j].O * sin(y);
			}
		}
		return result;
	}

	public static double sum_series_elp17(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < plan_pert_elp17.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp17[j].O) > pre[1]) {
				y = plan_pert_elp17[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					for (i = 0; i < 4; i++)
						y += plan_pert_elp17[j].ipla[i + 7] * del[i][k] * t[k];
					for (i = 0; i < 7; i++)
						y += plan_pert_elp17[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += plan_pert_elp17[j].O * sin(y);
			}
		}
		return result;
	}

	public static double sum_series_elp18(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < plan_pert_elp18.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp18[j].O) > pre[2]) {
				y = plan_pert_elp18[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					for (i = 0; i < 4; i++)
						y += plan_pert_elp18[j].ipla[i + 7] * del[i][k] * t[k];
					for (i = 0; i < 7; i++)
						y += plan_pert_elp18[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += plan_pert_elp18[j].O * sin(y);
			}
		}
		return result;
	}

	public static double sum_series_elp19(double[] t) {
		double result = 0;
		int i, j, k;
		double y, x;

		for (j = 0; j < plan_pert_elp19.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp19[j].O) > pre[0]) {
				y = plan_pert_elp19[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					for (i = 0; i < 4; i++)
						y += plan_pert_elp19[j].ipla[i + 7] * del[i][k] * t[k];
					for (i = 0; i < 7; i++)
						y += plan_pert_elp19[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				x = plan_pert_elp19[j].O * t[1];
				result += x * sin(y);
			}
		}
		return result;
	}

	public static double sum_series_elp20(double[] t) {
		double result = 0;
		int i, j, k;
		double y, x;

		for (j = 0; j < plan_pert_elp20.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp20[j].O) > pre[1]) {
				y = plan_pert_elp20[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					for (i = 0; i < 4; i++)
						y += plan_pert_elp20[j].ipla[i + 7] * del[i][k] * t[k];
					for (i = 0; i < 7; i++)
						y += plan_pert_elp20[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				x = plan_pert_elp20[j].O * t[1];
				result += x * sin(y);
			}
		}
		return result;
	}

	public static double sum_series_elp21(double[] t) {
		double result = 0;
		int i, j, k;
		double y, x;

		for (j = 0; j < plan_pert_elp21.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_pert_elp21[j].O) > pre[2]) {
				y = plan_pert_elp21[j].theta * DEG;
				for (k = 0; k < 2; k++) {
					for (i = 0; i < 4; i++)
						y += plan_pert_elp21[j].ipla[i + 7] * del[i][k] * t[k];
					for (i = 0; i < 7; i++)
						y += plan_pert_elp21[j].ipla[i] * p[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				x = plan_pert_elp21[j].O * t[1];
				result += x * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp22 series */
	public static double sum_series_elp22(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < tidal_effects_elp22.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(tidal_effects_elp22[j].A) > pre[0]) {
				y = tidal_effects_elp22[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += tidal_effects_elp22[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += tidal_effects_elp22[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += tidal_effects_elp22[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp23 series */
	public static double sum_series_elp23(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < tidal_effects_elp23.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(tidal_effects_elp23[j].A) > pre[1]) {
				y = tidal_effects_elp23[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += tidal_effects_elp23[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += tidal_effects_elp23[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += tidal_effects_elp23[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp24 series */
	public static double sum_series_elp24(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < tidal_effects_elp24.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(tidal_effects_elp24[j].A) > pre[2]) {
				y = tidal_effects_elp24[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += tidal_effects_elp24[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += tidal_effects_elp24[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += tidal_effects_elp24[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp25 series */
	public static double sum_series_elp25(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < tidal_effects_elp25.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(tidal_effects_elp25[j].A) > pre[0]) {
				A = tidal_effects_elp25[j].A * t[1];
				y = tidal_effects_elp25[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += tidal_effects_elp25[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += tidal_effects_elp25[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp26 series */
	public static double sum_series_elp26(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < tidal_effects_elp26.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(tidal_effects_elp26[j].A) > pre[1]) {
				A = tidal_effects_elp26[j].A * t[1];
				y = tidal_effects_elp26[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += tidal_effects_elp26[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += tidal_effects_elp26[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp27 series */
	public static double sum_series_elp27(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < tidal_effects_elp27.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(tidal_effects_elp27[j].A) > pre[2]) {
				A = tidal_effects_elp27[j].A * t[1];
				y = tidal_effects_elp27[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += tidal_effects_elp27[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += tidal_effects_elp27[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp28 series */
	public static double sum_series_elp28(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < moon_pert_elp28.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(moon_pert_elp28[j].A) > pre[0]) {
				y = moon_pert_elp28[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += moon_pert_elp28[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += moon_pert_elp28[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += moon_pert_elp28[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp29 series */
	public static double sum_series_elp29(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < moon_pert_elp29.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(moon_pert_elp29[j].A) > pre[1]) {
				y = moon_pert_elp29[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += moon_pert_elp29[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += moon_pert_elp29[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += moon_pert_elp29[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp30 series */
	public static double sum_series_elp30(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < moon_pert_elp30.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(moon_pert_elp30[j].A) > pre[2]) {
				y = moon_pert_elp30[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += moon_pert_elp30[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += moon_pert_elp30[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += moon_pert_elp30[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp31 series */
	public static double sum_series_elp31(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < rel_pert_elp31.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(rel_pert_elp31[j].A) > pre[0]) {
				y = rel_pert_elp31[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += rel_pert_elp31[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += rel_pert_elp31[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += rel_pert_elp31[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp32 series */
	public static double sum_series_elp32(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < rel_pert_elp32.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(rel_pert_elp32[j].A) > pre[1]) {
				y = rel_pert_elp32[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += rel_pert_elp32[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += rel_pert_elp32[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += rel_pert_elp32[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp33 series */
	public static double sum_series_elp33(double[] t) {
		double result = 0;
		int i, j, k;
		double y;

		for (j = 0; j < rel_pert_elp33.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(rel_pert_elp33[j].A) > pre[2]) {
				y = rel_pert_elp33[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += rel_pert_elp33[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += rel_pert_elp33[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += rel_pert_elp33[j].A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp34 series */
	public static double sum_series_elp34(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < plan_sol_pert_elp34.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_sol_pert_elp34[j].A) > pre[0]) {
				A = plan_sol_pert_elp34[j].A * t[2];
				y = plan_sol_pert_elp34[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += plan_sol_pert_elp34[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += plan_sol_pert_elp34[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp35 series */
	public static double sum_series_elp35(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < plan_sol_pert_elp35.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_sol_pert_elp35[j].A) > pre[1]) {
				A = plan_sol_pert_elp35[j].A * t[2];
				y = plan_sol_pert_elp35[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += plan_sol_pert_elp35[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += plan_sol_pert_elp35[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/** sum lunar elp36 series */
	public static double sum_series_elp36(double[] t) {
		double result = 0;
		int i, j, k;
		double y, A;

		for (j = 0; j < plan_sol_pert_elp36.length; j++) {
			/** do we need to calc this value */
			if (Math.abs(plan_sol_pert_elp36[j].A) > pre[2]) {
				A = plan_sol_pert_elp36[j].A * t[2];
				y = plan_sol_pert_elp36[j].O * DEG;
				for (k = 0; k < 2; k++) {
					y += plan_sol_pert_elp36[j].iz * zeta[k] * t[k];
					for (i = 0; i < 4; i++)
						y += plan_sol_pert_elp36[j].ilu[i] * del[i][k] * t[k];
				}
				/** put y in correct quad */
				y = ln_range_radians2(y);
				result += A * sin(y);
			}
		}
		return result;
	}

	/**
	 * void ln_get_lunar_geo_posn(double JD, LnRectPosn pos, double precision);
	 * \param JD Julian day. \param pos Pointer to a geocentric position
	 * structure to held result. \param precision The truncation level of the
	 * series in radians for longitude and latitude and in km for distance.
	 * (Valid range 0 - 0.01, 0 being highest accuracy) \ingroup lunar
	 * 
	 * Calculate the rectangular geocentric lunar coordinates to the inertial
	 * mean ecliptic and equinox of J2000. The geocentric coordinates returned
	 * are in units of km.
	 * 
	 * This function is based upon the Lunar Solution ELP2000-82B by Michelle
	 * Chapront-Touze and Jean Chapront of the Bureau des Longitudes, Paris.
	 */
	/** ELP 2000-82B theory */
	public static void ln_get_lunar_geo_posn(double JD, LnRectPosn moon,
			double precision) {
		double t[] = new double[5];
		double elp[] = new double[36];
		double a, b, c;
		double x, y, z;
		double pw, qw, pwqw, pw2, qw2, ra;

		/** is precision too low ? */
		if (precision > 0.01)
			precision = 0.01;

		/** is value in cache ? */
		if (JD == c_JD && precision >= c_precision) {
			moon.X = c_X;
			moon.Y = c_Y;
			moon.Z = c_Z;
			return;
		}

		/** calc julian centuries */
		t[0] = 1.0;
		t[1] = (JD - 2451545.0) / 36525.0;
		t[2] = t[1] * t[1];
		t[3] = t[2] * t[1];
		t[4] = t[3] * t[1];

		/** calc precision */
		pre[0] = precision * RAD;
		pre[1] = precision * RAD;
		pre[2] = precision * ATH;

		/** sum elp series */
		elp[0] = sum_series_elp1(t);
		elp[1] = sum_series_elp2(t);
		elp[2] = sum_series_elp3(t);
		elp[3] = sum_series_elp4(t);
		elp[4] = sum_series_elp5(t);
		elp[5] = sum_series_elp6(t);
		elp[6] = sum_series_elp7(t);
		elp[7] = sum_series_elp8(t);
		elp[8] = sum_series_elp9(t);
		elp[9] = sum_series_elp10(t);
		elp[10] = sum_series_elp11(t);
		elp[11] = sum_series_elp12(t);
		elp[12] = sum_series_elp13(t);
		elp[13] = sum_series_elp14(t);
		elp[14] = sum_series_elp15(t);
		elp[15] = sum_series_elp16(t);
		elp[16] = sum_series_elp17(t);
		elp[17] = sum_series_elp18(t);
		elp[18] = sum_series_elp19(t);
		elp[19] = sum_series_elp20(t);
		elp[20] = sum_series_elp21(t);
		elp[21] = sum_series_elp22(t);
		elp[22] = sum_series_elp23(t);
		elp[23] = sum_series_elp24(t);
		elp[24] = sum_series_elp25(t);
		elp[25] = sum_series_elp26(t);
		elp[26] = sum_series_elp27(t);
		elp[27] = sum_series_elp28(t);
		elp[28] = sum_series_elp29(t);
		elp[29] = sum_series_elp30(t);
		elp[30] = sum_series_elp31(t);
		elp[31] = sum_series_elp32(t);
		elp[32] = sum_series_elp33(t);
		elp[33] = sum_series_elp34(t);
		elp[34] = sum_series_elp35(t);
		elp[35] = sum_series_elp36(t);

		a = elp[0] + elp[3] + elp[6] + elp[9] + elp[12] + elp[15] + elp[18]
				+ elp[21] + elp[24] + elp[27] + elp[30] + elp[33];
		b = elp[1] + elp[4] + elp[7] + elp[10] + elp[13] + elp[16] + elp[19]
				+ elp[22] + elp[25] + elp[28] + elp[31] + elp[34];
		c = elp[2] + elp[5] + elp[8] + elp[11] + elp[14] + elp[17] + elp[20]
				+ elp[23] + elp[26] + elp[29] + elp[32] + elp[35];

		/** calculate geocentric coords */
		a = a / RAD + W1[0] + W1[1] * t[1] + W1[2] * t[2] + W1[3] * t[3]
				+ W1[4] * t[4];
		b = b / RAD;
		c = c * A0 / ATH;

		x = c * cos(b);
		y = x * sin(a);
		x = x * cos(a);
		z = c * sin(b);

		/** Laskars series */
		pw = (P1 + P2 * t[1] + P3 * t[2] + P4 * t[3] + P5 * t[4]) * t[1];
		qw = (Q1 + Q2 * t[1] + Q3 * t[2] + Q4 * t[3] + Q5 * t[4]) * t[1];
		ra = 2.0 * sqrt(1 - pw * pw - qw * qw);
		pwqw = 2.0 * pw * qw;
		pw2 = 1.0 - 2.0 * pw * pw;
		qw2 = 1.0 - 2.0 * qw * qw;
		pw = pw * ra;
		qw = qw * ra;
		a = pw2 * x + pwqw * y + pw * z;
		b = pwqw * x + qw2 * y - qw * z;
		c = -pw * x + qw * y + (pw2 + qw2 - 1) * z;

		/** save cache and result */
		c_JD = JD;
		c_X = moon.X = a;
		c_Y = moon.Y = b;
		c_Z = moon.Z = c;
	}

	/**
	 * void ln_get_lunar_equ_coords_prec(double JD, LnEquPosn position, double
	 * precision); \param JD Julian Day \param position Pointer to a LnLnlatPosn
	 * to store result. \param precision The truncation level of the series in
	 * radians for longitude and latitude and in km for distance. (Valid range 0
	 * - 0.01, 0 being highest accuracy) \ingroup lunar
	 * 
	 * Calculate the lunar RA and DEC for Julian day JD. Accuracy is better than
	 * 10 arcsecs in right ascension and 4 arcsecs in declination.
	 */
	public static void ln_get_lunar_equ_coords_prec(double JD,
			LnEquPosn position, double precision) {
		LnLnlatPosn ecl = new LnLnlatPosn();

		ln_get_lunar_ecl_coords(JD, ecl, precision);
		ln_get_equ_from_ecl(ecl, JD, position);
	}

	/**
	 * void ln_get_lunar_equ_coords(double JD, LnEquPosn position); \param JD
	 * Julian Day \param position Pointer to a LnLnlatPosn to store result.
	 * \ingroup lunar
	 * 
	 * Calculate the lunar RA and DEC for Julian day JD. Accuracy is better than
	 * 10 arcsecs in right ascension and 4 arcsecs in declination.
	 */
	public static void ln_get_lunar_equ_coords(double JD, LnEquPosn position) {
		ln_get_lunar_equ_coords_prec(JD, position, 0);
	}

	/**
	 * void ln_get_lunar_ecl_coords(double JD, LnLnlatPosn position, double
	 * precision); \param JD Julian Day \param position Pointer to a LnLnlatPosn
	 * to store result. \param precision The truncation level of the series in
	 * radians for longitude and latitude and in km for distance. (Valid range 0
	 * - 0.01, 0 being highest accuracy) \ingroup lunar
	 * 
	 * Calculate the lunar longitude and latitude for Julian day JD. Accuracy is
	 * better than 10 arcsecs in longitude and 4 arcsecs in latitude.
	 */
	public static void ln_get_lunar_ecl_coords(double JD, LnLnlatPosn position,
			double precision) {
		LnRectPosn moon = new LnRectPosn();

		/** get lunar geocentric position */
		ln_get_lunar_geo_posn(JD, moon, precision);

		/** convert to long and lat */
		position.lng = atan2(moon.Y, moon.X);
		position.lat = atan2(moon.Z, (sqrt((moon.X * moon.X)
				+ (moon.Y * moon.Y))));
		position.lng = ln_range_degrees(ln_rad_to_deg(position.lng));
		position.lat = ln_rad_to_deg(position.lat);
	}

	/**
	 * double ln_get_lunar_earth_dist(double JD); \param JD Julian Day \return
	 * The distance between the Earth and Moon in km. \ingroup lunar
	 * 
	 * Calculates the distance between the centre of the Earth and the centre of
	 * the Moon in km.
	 */
	public static double ln_get_lunar_earth_dist(double JD) {
		LnRectPosn moon = new LnRectPosn();

		ln_get_lunar_geo_posn(JD, moon, 0.00001);
		return sqrt((moon.X * moon.X) + (moon.Y * moon.Y) + (moon.Z * moon.Z));
	}

	/**
	 * double ln_get_lunar_phase(double JD); \param JD Julian Day \return Phase
	 * angle. (Value between 0 and 180) \ingroup lunar
	 * 
	 * Calculates the angle Sun - Moon - Earth.
	 */
	public static double ln_get_lunar_phase(double JD) {
		double phase = 0;
		LnLnlatPosn moon = new LnLnlatPosn(), sunlp = new LnLnlatPosn();
		double lunar_elong;
		double R, delta;

		/** get lunar and solar long + lat */
		ln_get_lunar_ecl_coords(JD, moon, 0.0001);
		ln_get_solar_ecl_coords(JD, sunlp);

		/** calc lunar geocentric elongation equ 48.2 */
		lunar_elong = acos(cos(ln_deg_to_rad(moon.lat))
				* cos(ln_deg_to_rad(sunlp.lng - moon.lng)));

		/** now calc phase Equ 48.2 */
		R = ln_get_earth_solar_dist(JD);
		delta = ln_get_lunar_earth_dist(JD);
		R = R * AU;
		/** convert R to km */
		phase = atan2((R * sin(lunar_elong)), (delta - R * cos(lunar_elong)));
		return ln_rad_to_deg(phase);
	}

	/**
	 * double ln_get_lunar_disk(double JD); \param JD Julian Day \return
	 * Illuminated fraction. (Value between 0 and 1) \brief Calculate the
	 * illuminated fraction of the moons disk \ingroup lunar
	 * 
	 * Calculates the illuminated fraction of the Moon's disk.
	 */
	public static double ln_get_lunar_disk(double JD) {
		double i;

		/** Equ 48.1 */
		i = ln_deg_to_rad(ln_get_lunar_phase(JD));
		return (1.0 + cos(i)) / 2.0;
	}

	/**
	 * double ln_get_lunar_bright_limb(double JD); \param JD Julian Day \return
	 * The position angle in degrees. \brief Calculate the position angle of the
	 * Moon's bright limb. \ingroup lunar
	 * 
	 * Calculates the position angle of the midpoint of the illuminated limb of
	 * the moon, reckoned eastward from the north point of the disk.
	 * 
	 * The angle is near 270 deg for first quarter and near 90 deg after a full
	 * moon. The position angle of the cusps are +90 deg and -90 deg.
	 */
	public static double ln_get_lunar_bright_limb(double JD) {
		double angle;
		double x, y;

		LnEquPosn moon = new LnEquPosn(), sunlp = new LnEquPosn();

		/** get lunar and solar long + lat */
		ln_get_lunar_equ_coords(JD, moon);
		ln_get_solar_equ_coords(JD, sunlp);

		/** Equ 48.5 */
		x = cos(ln_deg_to_rad(sunlp.dec))
				* sin(ln_deg_to_rad(sunlp.ra - moon.ra));
		y = sin((ln_deg_to_rad(sunlp.dec)) * cos(ln_deg_to_rad(moon.dec)))
				- (cos(ln_deg_to_rad(sunlp.dec)) * sin(ln_deg_to_rad(moon.dec)) * cos(ln_deg_to_rad(sunlp.ra
						- moon.ra)));
		angle = atan2(x, y);

		angle = ln_range_radians(angle);
		return ln_rad_to_deg(angle);
	}

	/**
	 * double ln_get_lunar_rst(double JD, LnLnlatPosn observer, LnRstTime rst);
	 * \param JD Julian day \param observer Observers position \param rst
	 * Pointer to store Rise, Set and Transit time in JD \return 0 for success,
	 * else 1 for circumpolar. \todo Improve lunar standard altitude for rst
	 * 
	 * Calculate the time the rise, set and transit (crosses the local meridian
	 * at upper culmination) time of the Moon for the given Julian day.
	 * 
	 * Note: this functions returns 1 if the Moon is circumpolar, that is it
	 * remains the whole day either above or below the horizon.
	 */
	public static int ln_get_lunar_rst(double JD, LnLnlatPosn observer,
			LnRstTime rst) {
		return ln_get_body_rst_horizon(JD, observer, new IGetEquBodyCoords() {

			@Override
			public void get_equ_body_coords(double JD, LnEquPosn position) {
				ln_get_lunar_equ_coords(JD, position);

			}
		}, LN_LUNAR_STANDART_HORIZON, rst);
	}

	/**
	 * double ln_get_lunar_sdiam(double JD) \param JD Julian day \return
	 * Semidiameter in arc seconds \todo Use Topocentric distance.
	 * 
	 * Calculate the semidiameter of the Moon in arc seconds for the given
	 * julian day.
	 */
	public static double ln_get_lunar_sdiam(double JD) {
		double So = 358473400;
		double dist;

		dist = ln_get_lunar_earth_dist(JD);
		return So / dist;
	}

	/**
	 * double ln_get_lunar_long_asc_node(double JD); \param JD Julian Day.
	 * \return Longitude of ascending node in degrees.
	 * 
	 * Calculate the mean longitude of the Moons ascening node for the given
	 * Julian day.
	 */
	public static double ln_get_lunar_long_asc_node(double JD) {
		/** calc julian centuries */
		double T = (JD - 2451545.0) / 36525.0;
		double omega = 125.0445479;
		double T2 = T * T;
		double T3 = T2 * T;
		double T4 = T3 * T;

		/** equ 47.7 */
		omega -= 1934.1362891 * T + 0.0020754 * T2 + T3 / 467441.0 - T4
				/ 60616000.0;
		return omega;
	}

	/**
	 * double ln_get_lunar_long_perigee(double JD); \param JD Julian Day \return
	 * Longitude of Moons mean perigee in degrees.
	 * 
	 * Calculate the longitude of the Moon's mean perigee.
	 */
	public static double ln_get_lunar_long_perigee(double JD) {
		/** calc julian centuries */
		double T = (JD - 2451545.0) / 36525.0;
		double per = 83.3532465;
		double T2 = T * T;
		double T3 = T2 * T;
		double T4 = T3 * T;

		/** equ 47.7 */
		per += 4069.0137287 * T - 0.0103200 * T2 - T3 / 80053.0 + T4
				/ 18999000.0;
		return per;
	}

}
