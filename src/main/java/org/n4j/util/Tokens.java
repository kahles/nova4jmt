package org.n4j.util;

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
