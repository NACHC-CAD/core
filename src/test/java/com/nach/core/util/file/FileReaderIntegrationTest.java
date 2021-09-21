package com.nach.core.util.file;

import java.io.File;
import java.util.Base64;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileReaderIntegrationTest {

	public static final String FILE_NAME = "C:\\_WORKSPACES\\nachc\\_PROJECT\\cosmos\\omop-concepts\\vocabulary_download_v5_{9259d46c-23e7-4760-b8d6-cddd5d86be7d}_1631306538104\\CONCEPT_RELATIONSHIP.csv";

	@Test
	public void shouldReadFile() {
		File file = new File(FILE_NAME);
		FileReader reader = new FileReader(file);
		int segmentCount = 0;
		while(reader.getIsDone() == false) {
			if(segmentCount % 100 == 0) {
				log.info("Current Line: " + reader.getCurrentLine());
				log.info("Total Lines:  " + reader.getTotalLineCount());
				log.info("Segment:      " + segmentCount);
			}
			byte[] segment = reader.getSegment().getBytes();
			String msgString = Base64.getEncoder().encodeToString(segment.toString().getBytes());
			msgString = "{\"handle\":\"" + "this-is-a-dummy-value-for-testing" + "\",\"data\":\"" + msgString + "\"}";
			segmentCount++;
		}
		log.info("Current Line: " + reader.getCurrentLine());
		log.info("Total Lines:  " + reader.getTotalLineCount());
		log.info("Segment:      " + segmentCount);
		log.info("Done.");
	}

}
