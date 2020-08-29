package com.nach.core.util.string;

import java.util.ArrayList;

public class StringReplacer {

	private ArrayList<String[]> replacements = new ArrayList<String[]>();
	
	public void add(String src, String replacement) {
		replacements.add(new String[] {src, replacement});
	}
	
	public String replaceAll(String sourceString) {
		for(String[] replacement : replacements) {
			sourceString = sourceString.replace(replacement[0], replacement[1]);
		}
		return sourceString;
	}
	
}
