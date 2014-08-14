package net.sourceforge.novaforjava;

/*
 * #%L
 * libnova for Java
 * %%
 * Copyright (C) 2014 novaforjave
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

public class Misc {

	/**
	 * double interpolate3 (double n, double y1, double y2, double y3) \return
	 * interpolation value \param n Interpolation factor \param y1 Argument 1
	 * \param y2 Argument 2 \param y3 Argument 3
	 * 
	 * Calculate an intermediate value of the 3 arguments for the given
	 * interpolation factor.
	 */
	public static double interpolate3(double n, double y1, double y2, double y3) {
		double y, a, b, c;

		/** equ 3.2 */
		a = y2 - y1;
		b = y3 - y2;
		c = a - b;

		/** equ 3.3 */
		y = y2 + n / 2.0 * (a + b + n * c);

		return y;
	}

	/**
	 * double interpolate5 (double n, double y1, double y2, double y3, double
	 * y4, double y5) \return interpolation value \param n Interpolation factor
	 * \param y1 Argument 1 \param y2 Argument 2 \param y3 Argument 3 \param y4
	 * Argument 4 \param y5 Argument 5
	 * 
	 * Calculate an intermediate value of the 5 arguments for the given
	 * interpolation factor.
	 */
	public static double interpolate5(double n, double y1, double y2, double y3, double y4,
			double y5) {
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

}
