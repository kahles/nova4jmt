package org.n4j.util;

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

public class Tokens {

	private char[] string;
	private char[] delimiters;

	private int position;

	public Tokens(String string, char[] delimiters) {
		this.string = string.toCharArray();
		this.delimiters = delimiters;
		this.position = 0;
	}

	public String nextToken() {
		return nextToken(delimiters);
	}

	public String nextToken(char[] delimiters) {
		StringBuffer token = new StringBuffer();
		while (position < string.length
				&& isDelimiter(string[position], delimiters)) {
			position++;
		}
		while (position < string.length
				&& !isDelimiter(string[position], delimiters)) {
			token.append(string[position]);
			position++;
		}
		if (token.length() == 0) {
			return null;
		}
		return token.toString();
	}

	private static boolean isDelimiter(char c, char[] delimiters) {
		for (int index = 0; index < delimiters.length; index++) {
			if (c == delimiters[index]) {
				return true;
			}
		}
		return false;
	}
}
