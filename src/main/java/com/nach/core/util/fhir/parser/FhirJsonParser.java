package com.nach.core.util.fhir.parser;

import java.io.File;

import org.hl7.fhir.instance.model.api.IBaseResource;

import com.nach.core.util.file.FileUtil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FhirJsonParser {

	private static FhirContext CTX;

	private static IParser PARSER;

	static {
		String msg;
		msg = "\n";
		msg += "***************************\n";
		msg += "*\n";
		msg += "* SETTING FHIR CONTEXT\n";
		msg += "*\n";
		msg += "***************************\n";
		log.info(msg);
		log.info("Setting FHIR Context...");
		File fhirContextFile = FileUtil.getFile("/fhir-context.txt");
		log.info("FHIR CTX Config file found: " + fhirContextFile.exists());
		if (fhirContextFile.exists() == true) {
			log.info("Got context file...");
			log.info("FHIR CTX FILE Location: " + FileUtil.getCanonicalPath(fhirContextFile));
		}
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
			log.info("No file found at /fhir-context.txt, using R4");
			CTX = FhirContext.forR4();
		}
		PARSER = CTX.newJsonParser();
		msg = "\n";
		msg += "***************************\n";
		msg += "*\n";
		msg += "* DONE SETTING FHIR CONTEXT\n";
		msg += "*\n";
		msg += "***************************\n";
		log.info(msg);
	}

	public static void setFhirContext(FhirContext context) {
		CTX = context;
		PARSER = CTX.newJsonParser();
		String msg = "";
		msg = "\n";
		msg += "***************************\n";
		msg += "*\n";
		msg += "* MANUALLY SET FHIR CONTEXT TO: " + CTX + "\n";
		msg += "*\n";
		msg += "***************************\n";
		log.info(msg);
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
