package org.n4j;

import static org.n4j.Utility.ln_deg_to_rad;
import static java.math.MathContext.DECIMAL128;

import java.math.BigDecimal;
import java.math.MathContext;

import org.n4j.api.LnNutation;

public class Nutation {
	static class NutationArguments {
		BigDecimal D;
		BigDecimal M;
		BigDecimal MM;
		BigDecimal F;
		BigDecimal O;

		public NutationArguments(double d, double m, double mM, double f,
				double o) {
			D = d(d);
			M = d(m);
			MM = d(mM);
			F = d(f);
			O = d(o);
		}
	};

	static class NutationCoefficients {
		BigDecimal longitude1;
		BigDecimal longitude2;
		BigDecimal obliquity1;
		BigDecimal obliquity2;

		public NutationCoefficients(double longitude1, double longitude2,
				double obliquity1, double obliquity2) {
			this.longitude1 = d(longitude1);
			this.longitude2 = d(longitude2);
			this.obliquity1 = d(obliquity1);
			this.obliquity2 = d(obliquity2);
		}
	};

	/* arguments and coefficients taken from table 21A on page 133 */

	private static final NutationArguments[] arguments = {
			new NutationArguments(0.0, 0.0, 0.0, 0.0, 1.0),
			new NutationArguments(-2.0, 0.0, 0.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, 0.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, 0.0, 0.0, 2.0),
			new NutationArguments(0.0, 1.0, 0.0, 0.0, 0.0),
			new NutationArguments(0.0, 0.0, 1.0, 0.0, 0.0),
			new NutationArguments(-2.0, 1.0, 0.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, 0.0, 2.0, 1.0),
			new NutationArguments(0.0, 0.0, 1.0, 2.0, 2.0),
			new NutationArguments(-2.0, -1.0, 0.0, 2.0, 2.0),
			new NutationArguments(-2.0, 0.0, 1.0, 0.0, 0.0),
			new NutationArguments(-2.0, 0.0, 0.0, 2.0, 1.0),
			new NutationArguments(0.0, 0.0, -1.0, 2.0, 2.0),
			new NutationArguments(2.0, 0.0, 0.0, 0.0, 0.0),
			new NutationArguments(0.0, 0.0, 1.0, 0.0, 1.0),
			new NutationArguments(2.0, 0.0, -1.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, -1.0, 0.0, 1.0),
			new NutationArguments(0.0, 0.0, 1.0, 2.0, 1.0),
			new NutationArguments(-2.0, 0.0, 2.0, 0.0, 0.0),
			new NutationArguments(0.0, 0.0, -2.0, 2.0, 1.0),
			new NutationArguments(2.0, 0.0, 0.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, 2.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, 2.0, 0.0, 0.0),
			new NutationArguments(-2.0, 0.0, 1.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, 0.0, 2.0, 0.0),
			new NutationArguments(-2.0, 0.0, 0.0, 2.0, 0.0),
			new NutationArguments(0.0, 0.0, -1.0, 2.0, 1.0),
			new NutationArguments(0.0, 2.0, 0.0, 0.0, 0.0),
			new NutationArguments(2.0, 0.0, -1.0, 0.0, 1.0),
			new NutationArguments(-2.0, 2.0, 0.0, 2.0, 2.0),
			new NutationArguments(0.0, 1.0, 0.0, 0.0, 1.0),
			new NutationArguments(-2.0, 0.0, 1.0, 0.0, 1.0),
			new NutationArguments(0.0, -1.0, 0.0, 0.0, 1.0),
			new NutationArguments(0.0, 0.0, 2.0, -2.0, 0.0),
			new NutationArguments(2.0, 0.0, -1.0, 2.0, 1.0),
			new NutationArguments(2.0, 0.0, 1.0, 2.0, 2.0),
			new NutationArguments(0.0, 1.0, 0.0, 2.0, 2.0),
			new NutationArguments(-2.0, 1.0, 1.0, 0.0, 0.0),
			new NutationArguments(0.0, -1.0, 0.0, 2.0, 2.0),
			new NutationArguments(2.0, 0.0, 0.0, 2.0, 1.0),
			new NutationArguments(2.0, 0.0, 1.0, 0.0, 0.0),
			new NutationArguments(-2.0, 0.0, 2.0, 2.0, 2.0),
			new NutationArguments(-2.0, 0.0, 1.0, 2.0, 1.0),
			new NutationArguments(2.0, 0.0, -2.0, 0.0, 1.0),
			new NutationArguments(2.0, 0.0, 0.0, 0.0, 1.0),
			new NutationArguments(0.0, -1.0, 1.0, 0.0, 0.0),
			new NutationArguments(-2.0, -1.0, 0.0, 2.0, 1.0),
			new NutationArguments(-2.0, 0.0, 0.0, 0.0, 1.0),
			new NutationArguments(0.0, 0.0, 2.0, 2.0, 1.0),
			new NutationArguments(-2.0, 0.0, 2.0, 0.0, 1.0),
			new NutationArguments(-2.0, 1.0, 0.0, 2.0, 1.0),
			new NutationArguments(0.0, 0.0, 1.0, -2.0, 0.0),
			new NutationArguments(-1.0, 0.0, 1.0, 0.0, 0.0),
			new NutationArguments(-2.0, 1.0, 0.0, 0.0, 0.0),
			new NutationArguments(1.0, 0.0, 0.0, 0.0, 0.0),
			new NutationArguments(0.0, 0.0, 1.0, 2.0, 0.0),
			new NutationArguments(0.0, 0.0, -2.0, 2.0, 2.0),
			new NutationArguments(-1.0, -1.0, 1.0, 0.0, 0.0),
			new NutationArguments(0.0, 1.0, 1.0, 0.0, 0.0),
			new NutationArguments(0.0, -1.0, 1.0, 2.0, 2.0),
			new NutationArguments(2.0, -1.0, -1.0, 2.0, 2.0),
			new NutationArguments(0.0, 0.0, 3.0, 2.0, 2.0),
			new NutationArguments(2.0, -1.0, 0.0, 2.0, 2.0) };

	private static final NutationCoefficients[] coefficients = {
			new NutationCoefficients(-171996.0, -174.2, 92025.0, 8.9),
			new NutationCoefficients(-13187.0, -1.6, 5736.0, -3.1),
			new NutationCoefficients(-2274.0, -0.2, 977.0, -0.5),
			new NutationCoefficients(2062.0, 0.2, -895.0, 0.5),
			new NutationCoefficients(1426.0, -3.4, 54.0, -0.1),
			new NutationCoefficients(712.0, 0.1, -7.0, 0.0),
			new NutationCoefficients(-517.0, 1.2, 224.0, -0.6),
			new NutationCoefficients(-386.0, -0.4, 200.0, 0.0),
			new NutationCoefficients(-301.0, 0.0, 129.0, -0.1),
			new NutationCoefficients(217.0, -0.5, -95.0, 0.3),
			new NutationCoefficients(-158.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(129.0, 0.1, -70.0, 0.0),
			new NutationCoefficients(123.0, 0.0, -53.0, 0.0),
			new NutationCoefficients(63.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(63.0, 0.1, -33.0, 0.0),
			new NutationCoefficients(-59.0, 0.0, 26.0, 0.0),
			new NutationCoefficients(-58.0, -0.1, 32.0, 0.0),
			new NutationCoefficients(-51.0, 0.0, 27.0, 0.0),
			new NutationCoefficients(48.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(46.0, 0.0, -24.0, 0.0),
			new NutationCoefficients(-38.0, 0.0, 16.0, 0.0),
			new NutationCoefficients(-31.0, 0.0, 13.0, 0.0),
			new NutationCoefficients(29.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(29.0, 0.0, -12.0, 0.0),
			new NutationCoefficients(26.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-22.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(21.0, 0.0, -10.0, 0.0),
			new NutationCoefficients(17.0, -0.1, 0.0, 0.0),
			new NutationCoefficients(16.0, 0.0, -8.0, 0.0),
			new NutationCoefficients(-16.0, 0.1, 7.0, 0.0),
			new NutationCoefficients(-15.0, 0.0, 9.0, 0.0),
			new NutationCoefficients(-13.0, 0.0, 7.0, 0.0),
			new NutationCoefficients(-12.0, 0.0, 6.0, 0.0),
			new NutationCoefficients(11.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-10.0, 0.0, 5.0, 0.0),
			new NutationCoefficients(-8.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(7.0, 0.0, -3.0, 0.0),
			new NutationCoefficients(-7.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-7.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(-7.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(6.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(6.0, 0.0, -3.0, 0.0),
			new NutationCoefficients(6.0, 0.0, -3.0, 0.0),
			new NutationCoefficients(-6.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(-6.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(5.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-5.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(-5.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(-5.0, 0.0, 3.0, 0.0),
			new NutationCoefficients(4.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(4.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(4.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-4.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-4.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-4.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(3.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-3.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-3.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-3.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-3.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-3.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-3.0, 0.0, 0.0, 0.0),
			new NutationCoefficients(-3.0, 0.0, 0.0, 0.0) };

	private static final BigDecimal LN_NUTATION_EPOCH_THRESHOLD = new BigDecimal(
			0.1);
	/* cache values */
	private static BigDecimal c_JD = BigDecimal.ZERO,
			c_longitude = BigDecimal.ZERO, c_obliquity = BigDecimal.ZERO,
			c_ecliptic = BigDecimal.ZERO;

	/*
	 * ! \fn void ln_get_nutation(double JD, struct ln_nutation *nutation)
	 * \param JD Julian Day. \param nutation Pointer to store nutation
	 * 
	 * Calculate nutation of longitude and obliquity in degrees from Julian
	 * Ephemeris Day
	 */
	/*
	 * Chapter 21 pg 131-134 Using Table 21A
	 */
	/* TODO: add argument to specify this */
	/* TODO: use JD or JDE. confirm */
	public static void ln_get_nutation(double JDdoule, LnNutation nutation) {
		BigDecimal JD = d(JDdoule);
		BigDecimal D, M, MM, F, O, T, T2, T3, JDE;
		BigDecimal coeff_sine, coeff_cos;
		BigDecimal argument;
		int i;

		/* should we bother recalculating nutation */
		if (JD.subtract(c_JD).abs().compareTo(LN_NUTATION_EPOCH_THRESHOLD) > 0) {
			/* set the new epoch */
			c_JD = JD;

			/* get julian ephemeris day */
			JDE = d(DynamicalTime.ln_get_jde(JD.doubleValue()));

			/* calc T */
			T = (JDE.subtract(d(2451545L))).divide(d(36525L),DECIMAL128);
			T2 = T.multiply(T);
			T3 = T2.multiply(T);

			/* calculate D,M,M',F and Omega */
			D = d(297.85036).add(d(445267.111480).multiply(T))
					.subtract(d(0.0019142).multiply(T2))
					.add(T3.divide(d(189474L),DECIMAL128));
			M = d(357.52772).add(d(35999.050340).multiply(T))
					.subtract(d(0.0001603).multiply(T2))
					.subtract(T3.divide(d(300000L),DECIMAL128));
			MM = d(134.96298).add(d(477198.867398).multiply(T))
					.add(d(0.0086972).multiply(T2)).add(T3.divide(d(56250L),DECIMAL128));
			F = d(93.2719100).add(d(483202.017538).multiply(T))
					.subtract(d(0.0036825).multiply(T2))
					.add(T3.divide(d(327270L),DECIMAL128));
			O = d(125.04452).subtract(d(1934.136261).multiply(T))
					.add(d(0.0020708).multiply(T2)).add(T3.divide(d(450000L),DECIMAL128));

			/* convert to radians */
			D = ln_deg_to_rad(D);
			M = ln_deg_to_rad(M);
			MM = ln_deg_to_rad(MM);
			F = ln_deg_to_rad(F);
			O = ln_deg_to_rad(O);

			/* calc sum of terms in table 21A */
			for (i = 0; i < arguments.length; i++) {
				/* calc coefficients of sine and cosine */
				coeff_sine = (coefficients[i].longitude1
						.add(coefficients[i].longitude2.multiply(T)));
				coeff_cos = (coefficients[i].obliquity1
						.add(coefficients[i].obliquity2.multiply(T)));

				argument = arguments[i].D.multiply(D)//
						.add(arguments[i].M.multiply(M)) //
						.add(arguments[i].MM.multiply(MM)) //
						.add(arguments[i].F.multiply(F))//
						.add(arguments[i].O.multiply(O));

				c_longitude = c_longitude.add(coeff_sine.multiply(d(Math
						.sin(argument.doubleValue()))));
				c_obliquity = c_obliquity.add(coeff_cos.multiply(d(Math
						.cos(argument.doubleValue()))));
			}

			/* change to arcsecs */
			c_longitude = c_longitude.divide(d(10000L),DECIMAL128);
			c_obliquity = c_obliquity.divide(d(10000L),DECIMAL128);

			/* change to degrees */
			c_longitude = c_longitude.divide(d(60L * 60L),DECIMAL128);
			c_obliquity = c_obliquity.divide(d(60L * 60L),DECIMAL128);

			/* calculate mean ecliptic - Meeus 2nd edition, eq. 22.2 */
			c_ecliptic = d(23L).add(d(26L).divide(d(60L),DECIMAL128))
					.add(d(21.448).divide(d(3600L),DECIMAL128)) //
					.subtract(d(46.8150).divide(d(3600L),DECIMAL128).multiply(T)) //
					.subtract(d(0.00059).divide(d(3600L),DECIMAL128).multiply(T2)) //
					.add(d(0.001813).divide(d(3600L),DECIMAL128).multiply(T3));

			/*
			 * c_ecliptic += c_obliquity; * Uncomment this if function should
			 * return true obliquity rather than mean obliquity
			 */
		}

		/* return results */
		nutation.longitude = c_longitude.doubleValue();
		nutation.obliquity = c_obliquity.doubleValue();
		nutation.ecliptic = c_ecliptic.doubleValue();

	}

	private static BigDecimal d(double value) {
		return BigDecimal.valueOf(value);
	}
}