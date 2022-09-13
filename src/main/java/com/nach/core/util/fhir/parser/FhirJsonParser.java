package com.nach.core.util.fhir.parser;

import java.io.File;

import org.hl7.fhir.instance.model.api.IBaseResource;

import com.nach.core.util.file.FileUtil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FhirJsonParser {

	private static final FhirContext CTX;
	
	private static final IParser PARSER;

	static {
		File fhirContextFile = FileUtil.getFile("/fhir-context.txt");
		if (fhirContextFile != null && fhirContextFile.exists()) {
			String fhirContext = FileUtil.getAsString(fhirContextFile).trim();
			if ("Dstu3".equals(fhirContext)) {
				log.info("USING DSTU3 FOR FHIR PARSING");
				CTX = FhirContext.forDstu3();
			} else if ("R4".equals(fhirContext)) {
				log.info("USING R4 FOR FHIR PARSING");
				CTX = FhirContext.forR4();
			} else {
				CTX = FhirContext.forDstu3();
			}
		} else {
			CTX = FhirContext.forDstu3();
		}
		PARSER = CTX.newJsonParser();
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

	/**
	 * Generate a json string from a class.
	 */
	public static String serialize(IBaseResource resource) {
		String rtn = PARSER.encodeResourceToString(resource);
		return rtn;
	}
	
}
