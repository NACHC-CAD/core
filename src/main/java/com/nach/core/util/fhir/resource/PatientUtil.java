package com.nach.core.util.fhir.resource;

import java.util.Date;
import java.util.List;

import org.hl7.fhir.dstu3.model.Address;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.yaorma.util.time.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PatientUtil {

	public static String getGivenName(Patient patient) {
		String rtn = "";
		List<HumanName> list = patient.getName();
		if (list.size() > 0) {
			HumanName humanName = list.get(0);
			rtn = humanName.getGivenAsSingleString();
		}
		return rtn;
	}

	public static String getFamilyName(Patient patient) {
		String rtn = "";
		List<HumanName> list = patient.getName();
		if (list.size() > 0) {
			HumanName humanName = list.get(0);
			rtn = humanName.getFamily();
		}
		return rtn;
	}

	public static String getDateOfBirth(Patient patient) {
		Date date = patient.getBirthDate();
		String rtn = TimeUtil.getDateAsYyyyMmDd(date, '-');
		return rtn;
	}

	public static String getSex(Patient patient) {
		AdministrativeGender gender = patient.getGender();
		if (gender != null) {
			return gender.name();
		} else {
			return "";
		}
	}

	public static String getPhoneNumber(Patient patient) {
		String rtn = "";
		List<ContactPoint> cons = patient.getTelecom();
		for (ContactPoint con : cons) {
			ContactPointSystem system = con.getSystem();
			log.info("System: " + system);
			if (ContactPointSystem.PHONE == system) {
				rtn = con.getValue();
				break;
			}
		}
		return rtn;
	}

	public static Address getAddress(Patient patient) {
		List<Address> list = patient.getAddress();
		for (Address add : list) {
			AddressUse use = add.getUse();
			if (use == AddressUse.HOME) {
				return add;
			}
		}
		if(list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static String getAddressStreet1(Patient patient) {
		String rtn = "";
		Address add = getAddress(patient);
		if(add != null) {
			List<StringType> lines = add.getLine();
			if (lines.size() > 0) {
				rtn = lines.get(0) + "";
				return rtn;
			}
		}
		return rtn;
	}

	public static String getAddressZip(Patient patient) {
		String rtn = "";
		Address add = getAddress(patient);
		if(add != null) {
			rtn = add.getPostalCode();
		}
		return rtn;
	}
	
}
