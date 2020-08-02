package com.nach.core.util.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlUtil {

	public static String prettyPrint(String input) {
		return prettyPrint(input, 4);
	}

	public static String prettyPrint(String input, int indent) {
		try {
			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser parser = parserFactory.newSAXParser();
			parser.getXMLReader().setErrorHandler(getNullErrroListener());
			SAXSource xmlInput = new SAXSource(parser.getXMLReader(), new InputSource(new StringReader(input)));
			StringWriter stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 4);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setErrorListener(getNullErrroListener());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (Exception e) {
			return input;
		}
	}

	// ------------------------------------------------------------------------
	//
	// internal implementation (all private past here)
	//
	// ------------------------------------------------------------------------

	private static NullErrorListener getNullErrroListener() {
		return new XmlUtil().new NullErrorListener();
	}

	private class NullErrorListener implements ErrorListener, ErrorHandler {
		public void error(SAXParseException arg0) throws SAXException {
		}

		public void fatalError(SAXParseException arg0) throws SAXException {
		}

		public void warning(SAXParseException arg0) throws SAXException {
		}

		public void error(TransformerException arg0) throws TransformerException {
		}

		public void fatalError(TransformerException arg0) throws TransformerException {
		}

		public void warning(TransformerException arg0) throws TransformerException {
		}

	}

}
