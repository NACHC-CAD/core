package com.nach.core.util.string.escape.impl;

import org.apache.commons.lang3.StringEscapeUtils;

import com.nach.core.util.string.escape.Escape;

public class HtmlEscape implements Escape {

	@Override
	public String escape(String src) {
		String rtn = StringEscapeUtils.escapeHtml4(src);
		rtn = rtn.replace("\n", "&NewLine;");
		return rtn;
	}

}
