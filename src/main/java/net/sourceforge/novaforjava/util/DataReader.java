package net.sourceforge.novaforjava.util;

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
