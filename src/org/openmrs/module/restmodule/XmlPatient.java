package org.openmrs.module.restmodule;

import java.text.SimpleDateFormat;

import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonName;

/**
 * Facilitates the encoding and decoding of
 * <code>org.openmrs.Patient</org> objects to/from
 * XML.
 * 
 * @author Burke Mamlin
 * @version 1.0
 */
public class XmlPatient {

	/**
	 * All dates are reported in YYYY-MM-DD format
	 */
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * Convert a patient object into XML
	 * 
	 * @param patient
	 *            object to marshal into XML
	 * @return XML version of patient
	 */
	public static String encode(Patient patient) {
		StringBuffer xml = new StringBuffer();

		xml.append("<patient birthdate=\"");
		xml.append((patient.getBirthdate() != null ? dateFormatter
				.format(patient.getBirthdate()) : ""));
		xml.append("\" birthdateEstimated=\"");
		xml.append(patient.getBirthdateEstimated());
		xml.append("\" gender=\"");
		xml.append(patient.getGender());
		xml.append("\">");

		xml.append("<identifierList>");
		for (PatientIdentifier pid : patient.getIdentifiers()) {
			xml.append("<identifier");
			if (pid.isPreferred())
				xml.append(" preferred=\"1\"");
			xml.append(" type=\"");
			xml.append(pid.getIdentifierType().getName());
			// TODO: should encode invalid chars in name
			xml.append("\">");
			xml.append(pid.getIdentifier());
			xml.append("</identifier>");
		}
		xml.append("</identifierList>");

		xml.append("<name>");
		PersonName name = patient.getPersonName();
		addOptionalElement(xml, "prefix", name.getPrefix());
		addOptionalElement(xml, "givenName", name.getGivenName());
		addOptionalElement(xml, "middleName", name.getMiddleName());
		addOptionalElement(xml, "familyName", name.getFamilyName());
		addOptionalElement(xml, "familyName2", name.getFamilyName2());
		addOptionalElement(xml, "degree", name.getDegree());
		xml.append("</name>");

		xml.append("<addressList>");
		for (PersonAddress address : patient.getAddresses()) {
			xml.append("<address");
			if (address.getPreferred())
				xml.append(" preferred=\"1\">");
			else
				xml.append(">");
			addOptionalElement(xml, "address1", address.getAddress1());
			addOptionalElement(xml, "address2", address.getAddress2());
			addOptionalElement(xml, "cityVillage", address.getCityVillage());
			addOptionalElement(xml, "countyDistrict", address
					.getCountyDistrict());
			addOptionalElement(xml, "stateProvince", address.getStateProvince());
			addOptionalElement(xml, "country", address.getCountry());
			xml.append("</address>");
		}
		xml.append("</addressList>");

		xml.append("</patient>");

		return xml.toString();
	}

	/**
	 * Convenience method for rendering XML elements
	 * 
	 * @param xml
	 *            buffer for XML output
	 * @param tag
	 *            name of tag for element
	 * @param value
	 *            the value for the element. if null, then nothing is added to
	 *            the output buffer
	 */
	private static void addOptionalElement(StringBuffer xml, String tag,
			String value) {
		if (value == null)
			return;
		xml.append("<");
		xml.append(tag);
		xml.append(">");
		xml.append(value);
		xml.append("</");
		xml.append(tag);
		xml.append(">");
	}

	/**
	 * Create patient from XML representation
	 * 
	 * @param xml
	 *            XML-encoded patient
	 * @return Patient objected created from details in XML
	 */
	public static Patient decode(String xml) {
		throw new RuntimeException("Patient decoding not yet implemented");
	}

}
