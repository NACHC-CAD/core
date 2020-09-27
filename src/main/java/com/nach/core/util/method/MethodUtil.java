package com.nach.core.util.method;

import java.lang.reflect.Method;

public class MethodUtil {

	public static String getStringForMethod(Object obj, String methodName) {
		try {
			Method method = obj.getClass().getMethod(methodName);
			Object rtn = method.invoke(obj);
			if (rtn == null) {
				return null;
			} else {
				return rtn.toString();
			}
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static void set(Object obj, String methodName, Object val) {
		try {
			Class[] sig = new Class[] { val.getClass() };
			Object[] args = new Object[] { val };
			Method method = obj.getClass().getMethod(methodName, sig);
			method.invoke(obj, args);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
