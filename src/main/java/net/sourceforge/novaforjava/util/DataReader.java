package net.sourceforge.novaforjava.util;

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

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

public class DataReader {

	public static final NumberFormat format = NumberFormat
			.getInstance(Locale.US);

	public static double[] readDoubleArray(String string, ParsePosition pos,
			int size) {
		skipOverChar(string, pos, '{');
		double[] result = new double[size];
		int index = 0;
		while (index < (size - 1)) {
			skipBlanks(string, pos);
			result[index++] = format.parse(string, pos).doubleValue();
			skipOverChar(string, pos, ',');
		}
		skipBlanks(string, pos);
		result[index++] = format.parse(string, pos).doubleValue();
		skipOverChar(string, pos, '}');
		return result;
	}

	public static void skipBlanks(String string, ParsePosition pos) {
		while (Character.isWhitespace(string.charAt(pos.getIndex()))) {
			pos.setIndex(pos.getIndex() + 1);
		}

	}

	public static int[] readIntArray(String string, ParsePosition pos, int size) {
		skipOverChar(string, pos, '{');
		int[] result = new int[size];
		int index = 0;
		while (index < (size - 1)) {
			skipBlanks(string, pos);
			result[index++] = format.parse(string, pos).intValue();
			skipOverChar(string, pos, ',');
		}
		skipBlanks(string, pos);
		result[index++] = format.parse(string, pos).intValue();
		skipOverChar(string, pos, '}');
		return result;
	}

	public static void skipOverChar(String string, ParsePosition pos,
			char bracket) {
		skipBlanks(string, pos);
		if (string.charAt(pos.getIndex()) == bracket) {
			pos.setIndex(pos.getIndex() + 1);
		} else {
			throw new IllegalArgumentException("no array here");
		}
	}
}
