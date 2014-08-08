package org.n4j.util;

import java.lang.reflect.Method;

public class Reflect {

	public static Method getMethod(Class<?> class1, String name) {
		for (Method method : class1.getMethods()) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		return null;
	}
}
