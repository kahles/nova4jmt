package org.n4j;

import org.n4j.api.LnHelioPosn;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;
import static org.n4j.Utility.ln_deg_to_rad;

public class Vsop87 {

	public static class LnVsop {
		public double A;
		public double B;
		public double C;
	};

	double ln_calc_series(LnVsop[] data, double t) {
		double value = 0.0;
		int i;

		for (i = 0; i < data.length; i++) {
			value += data[i].A * cos(data[i].B + data[i].C * t);

		}

		return value;
	}

	/*
	 * ! \fn void ln_vsop87_to_fk5(struct ln_helio_posn *position, double JD)
	 * \param position Position to transform. \param JD Julian day
	 * 
	 * Transform from VSOP87 to FK5 reference frame.
	 */
	/*
	 * Equation 31.3 Pg 207.
	 */
	public static void ln_vsop87_to_fk5(LnHelioPosn position, double JD) {
		double LL, cos_LL, sin_LL, T, delta_L, delta_B, B;

		/* get julian centuries from 2000 */
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
