package com.nach.core.util.databricks.file.exception;

import com.nach.core.util.databricks.file.response.DatabricksFileUtilResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabricksFileException extends RuntimeException {

	private DatabricksFileUtilResponse resp;

	public DatabricksFileException(DatabricksFileUtilResponse resp) {
		this.resp = resp;
	}

	public DatabricksFileException(DatabricksFileUtilResponse resp, String msg) {
		super(msg);
		this.resp = resp;
	}
	
}
