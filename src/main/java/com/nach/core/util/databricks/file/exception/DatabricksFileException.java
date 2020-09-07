package com.nach.core.util.databricks.file.exception;

import com.nach.core.util.databricks.file.response.DatabricksFileUtilResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabricksFileException extends Exception {

	private DatabricksFileUtilResponse resp;

	public DatabricksFileException(DatabricksFileUtilResponse resp) {
		this.resp = resp;
	}

}
