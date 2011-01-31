/**
 * 
 */
package org.openmrs.module.restmodule;

import java.util.Locale;
import java.util.Set;

import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.Role;
import org.openmrs.User;

/**
 * 
 *
 */
public class XmlUser {
	
	public static String encode(User user) {
		StringBuffer xml = new StringBuffer();
		xml.append("<user userId=\"");
		xml.append(user.getUserId());
		xml.append("\" systemId=\"");
		xml.append(user.getSystemId());
		xml.append("\" username=\"");
		xml.append(user.getUsername());
		xml.append("\">");

		xml.append("<name>");
		PersonName name = user.getPersonName();
		if (name != null) {
			addOptionalElement(xml, "prefix", name.getPrefix());
			addOptionalElement(xml, "givenName", name.getGivenName());
			addOptionalElement(xml, "middleName", name.getMiddleName());
			addOptionalElement(xml, "familyName", name.getFamilyName());
			addOptionalElement(xml, "familyName2", name.getFamilyName2());
			addOptionalElement(xml, "degree", name.getDegree());
		}
		xml.append("</name>");
		addOptionalElement(xml, "secretQuestion", user.getSecretQuestion());

		xml.append("<roles>");
		for (Role r : user.getRoles())
			addOptionalElement(xml, "role", r.getRole());
		xml.append("</roles>");
		xml.append("</user>");

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
	 * @return User objected created from details in XML
	 */
	public static User decode(String xml) {
		throw new RuntimeException("User decoding not yet implemented");
	}
}
