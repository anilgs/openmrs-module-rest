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

import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonName;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.web.WebUtil;

/**
 * Facilitates the encoding of <code>org.openmrs.User</org> objects to/from JSON
 */
public class JsonUser {
	
	/**
	 * All dates are reported in YYYY-MM-DD format
	 */
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Convert a user object into JSON
	 * 
	 * @param user object to marshal into JSON
	 * @return JSON String version of user
	 */
	public static String encode(User user) {
		if (user == null)
			return "";
		
		JSONObject json = new JSONObject();
		JSONObject jsonTemp;

		json.put("uuid", user.getUuid());
		json.put("userId", user.getUserId());
		json.put("systemId", user.getSystemId());
		json.put("username", WebUtil.escapeQuotes(user.getUsername()));
		
		JSONArray jsonTempArray = new JSONArray();
		for (Role role : user.getRoles()) {
			jsonTempArray.put(role.getRole());
		}
		json.put("roleList", jsonTempArray);
		
		jsonTemp = new JSONObject();
		PersonName name = user.getPersonName();
		jsonTemp.put("prefix", name.getPrefix());
		jsonTemp.put("givenName", name.getGivenName());
		jsonTemp.put("middleName", name.getMiddleName());
		jsonTemp.put("familyName", name.getFamilyName());
		jsonTemp.put("familyName2", name.getFamilyName2());
		jsonTemp.put("familyNameSuffix", name.getFamilyNameSuffix());
		jsonTemp.put("degree", name.getDegree());
		json.put("name", jsonTemp);
		
		Person userPerson = user.getPerson();
		if (userPerson == null)
			return json.toString();

		if (userPerson.getBirthdate() != null) {
			json.put("birthdate", dateFormatter.format(userPerson.getBirthdate()));
			json.put("birthdateEstimated", userPerson.getBirthdateEstimated());
		}

		json.put("gender", WebUtil.escapeQuotes(userPerson.getGender()));		
		
		jsonTempArray = new JSONArray();
		for (PersonAddress address : userPerson.getAddresses()) {
			jsonTemp = new JSONObject();
			if (address.getPreferred())
				jsonTemp.put("preferred", true);
			jsonTemp.put("address1", address.getAddress1());
			jsonTemp.put("address2", address.getAddress2());
			jsonTemp.put("cityVillage", address.getCityVillage());
			jsonTemp.put("neighborhoodCell", address.getNeighborhoodCell());
			jsonTemp.put("region", address.getRegion());
			jsonTemp.put("subregion", address.getSubregion());
			jsonTemp.put("countyDistrict", address.getCountyDistrict());
			jsonTemp.put("stateProvince", address.getStateProvince());
			jsonTemp.put("country", address.getCountry());
			jsonTempArray.put(jsonTemp);
		}
		json.put("addressList", jsonTempArray);

		return json.toString();
	}
	
	/**
	 * Create user from JSON representation
	 * 
	 * @param xml JSON-encoded user
	 * @return User objected created from details in JSON
	 */
	public static User decode(String xml) {
		throw new RuntimeException("User decoding not yet implemented");
	}
	
}
