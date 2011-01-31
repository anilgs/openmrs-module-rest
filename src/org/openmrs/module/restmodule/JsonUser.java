/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.restmodule;

import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.web.WebUtil;

/**
 *
 */
public class JsonUser {
	/**
	 * Convert a user object into JSON
	 * 
	 * @param user
	 *            object to marshal into JSON
	 * @return JSON String version of user
	 */
	public static String encode(User user) {
		StringBuffer json = new StringBuffer();

		json.append("{");

		if (user.getUserId() != null) {
			addOptionalElement(json, "userId", user.getUserId().toString());
		}
		json.append("\"systemId\":\"").append(user.getSystemId()).append("\",");

		json.append("\"username\":\"").append(
				WebUtil.escapeQuotes(user.getUsername())).append("\",");

		json.append("\"name\":{");
		PersonName name = user.getPersonName();
		if (name != null) {
			boolean hasContent = addOptionalElement(json, "prefix", name
					.getPrefix());
			hasContent |= addOptionalElement(json, "givenName", name
					.getGivenName());
			hasContent |= addOptionalElement(json, "middleName", name
					.getMiddleName());
			hasContent |= addOptionalElement(json, "familyName", name
					.getFamilyName());
			hasContent |= addOptionalElement(json, "familyName2", name
					.getFamilyName2());
			hasContent |= addOptionalElement(json, "familyNameSuffix", name
					.getFamilyNameSuffix());
			hasContent |= addOptionalElement(json, "degree", name.getDegree());
			if (hasContent)
				json.deleteCharAt(json.length() - 1); // delete last comma if at
														// least something was
														// added
		}
		json.append("},");

		json.append("\"roles\":[");
		boolean first = true;
		for (Role r : user.getRoles()) {
			if (!first)
				json.append(",");
			json.append("\"").append(r.getName()).append("\"");
			first = false;
		}

		json.append("]");

		json.append("}");

		return json.toString();
	}

	/**
	 * Convenience method for rendering JSON elements
	 * 
	 * @param json
	 *            buffer for output
	 * @param attrName
	 *            name of element
	 * @param value
	 *            the value for the element. if null, then nothing is added to
	 *            the output buffer
	 * @return true if an element was added
	 */
	private static boolean addOptionalElement(StringBuffer json,
			String attrName, String value) {
		if (value == null || "".equals(value))
			return false;

		json.append("\"");
		json.append(attrName);
		json.append("\":\"");
		json.append(WebUtil.escapeQuotes(value));
		json.append("\",");

		return true;
	}

	/**
	 * Create patient from JSON representation
	 * 
	 * @param xml
	 *            JSON-encoded patient
	 * @return Patient objected created from details in JSON
	 */
	public static Patient decode(String xml) {
		throw new RuntimeException("Patient decoding not yet implemented");
	}

}