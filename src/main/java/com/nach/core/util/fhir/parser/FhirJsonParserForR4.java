package com.nach.core.util.fhir.parser;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class FhirJsonParserForR4 {

	private static final FhirContext CTX;

	static {
		CTX = FhirContext.forR4();
	}

	/**
	 * Generate a class from a json string.
	 */
	public static <T extends IBaseResource> T parse(String jsonString, Class<T> cls) {
		try {
			IParser parser = CTX.newJsonParser();
			parser.setStripVersionsFromReferences(false);
			CTX.getParserOptions().setStripVersionsFromReferences(false);
			IParser jsonParser = CTX.newJsonParser();
			T rtn = jsonParser.parseResource(cls, jsonString);
			return rtn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
