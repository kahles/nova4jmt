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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static net.sourceforge.novaforjava.Utility.ln_deg_to_rad;
import net.sourceforge.novaforjava.api.LnHelioPosn;

public class Vsop87 {

	public static class LnVsop {
		public double A;
		public double B;
		public double C;

		public LnVsop(double a, double b, double c) {
			A = a;
			B = b;
			C = c;
		}

	};

	public static double ln_calc_series(LnVsop[] data, double t) {
		double value = 0.0;
		int i;

		for (i = 0; i < data.length; i++) {
			value += data[i].A * cos(data[i].B + data[i].C * t);

		}

		return value;
	}

	/**
	 * void ln_vsop87_to_fk5(struct ln_helio_posn *position, double JD) \param
	 * position Position to transform. \param JD Julian day
	 * 
	 * Transform from VSOP87 to FK5 reference frame.
	 */
	/**
	 * Equation 31.3 Pg 207.
	 */
	public static void ln_vsop87_to_fk5(LnHelioPosn position, double JD) {
		double LL, cos_LL, sin_LL, T, delta_L, delta_B, B;

		/** get julian centuries from 2000 */
		T = (JD - 2451545.0) / 36525.0;

		LL = position.L + (-1.397 - 0.00031 * T) * T;
		LL = ln_deg_to_rad(LL);
		cos_LL = cos(LL);
		sin_LL = sin(LL);
		B = ln_deg_to_rad(position.B);

		delta_L = (-0.09033 / 3600.0) + (0.03916 / 3600.0) * (cos_LL + sin_LL)
				* tan(B);
		delta_B = (0.03916 / 3600.0) * (cos_LL - sin_LL);

		position.L += delta_L;
		position.B += delta_B;
	}
}
