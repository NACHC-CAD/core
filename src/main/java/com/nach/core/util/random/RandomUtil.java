package com.nach.core.util.random;

import java.util.List;

public class RandomUtil {

	public static int getRandom(int max) {
		int rtn = (int)(Math.random() * max + 1);
		rtn = rtn - 1;
		return rtn;
	}
	
	public static <T extends Object> T getRandom(List<T> list) {
		int max = list.size();
		int index = getRandom(max);
		return list.get(index);
	}
	
	public static <T extends Object> T getRandom(T[] list) {
		int max = list.length;
		int index = getRandom(max);
		return list[index];
	}
	
}
