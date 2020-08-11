package com.nach.core.util.method;

import java.lang.reflect.Method;

public class MethodUtil {

	public static String getStringForMethod(Object obj, String methodName) {
		try {
			Method method = obj.getClass().getMethod(methodName);
			Object rtn = method.invoke(obj);
			if(rtn == null) {
				return null;
			} else {
				return rtn.toString();
			}
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
}
