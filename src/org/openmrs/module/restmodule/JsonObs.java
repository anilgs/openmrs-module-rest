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
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openmrs.Obs;
import org.openmrs.web.WebUtil;
import org.springframework.util.StringUtils;

/**
 * 
 * Facilitates the encoding of <code>org.openmrs.Obs</org> objects to/from JSON
 */
// TODO Add unit tests for the methods in this class
public class JsonObs {

	/**
	 * All dates are reported in YYYY-MM-DD format
	 */
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * Convert a obs object into JSON
	 * 
	 * @param obs
	 *            object to marshal into JSON
	 * @return JSON String version of obs
	 */

	public static String encode(Obs obs) {
		// this will hold the marshalled object that can be output as json text
		JSONObject json = new JSONObject();
		// holds temporary jsonObjects data
		JSONObject jsonTemp = null;

		json.put("uuid", obs.getUuid());
		json.put("patientId", obs.getPersonId());

		if (obs.getObsDatetime() != null)
			json.put("obsDatetime", dateFormatter.format(obs.getObsDatetime()));
		addNonEmptyStringAsProperty(json, "accessionNumber", obs
				.getAccessionNumber());
		json.put("isObsGrouping", obs.isObsGrouping());
		json.put("isComplex", obs.isComplex());
		json.putOpt("valueNumeric", obs.getValueNumeric());
		addNonEmptyStringAsProperty(json, "valueText", obs.getValueText());
		addNonEmptyStringAsProperty(json, "valueComplex", obs.getValueComplex());
		addNonEmptyStringAsProperty(json, "valueModifier", obs
				.getValueModifier());
		json.putOpt("valueGroupId", obs.getValueGroupId());

		if (obs.getDateStarted() != null)
			json.put("dateStarted", dateFormatter.format(obs.getDateStarted()));
		if (obs.getDateStopped() != null)
			json.put("dateStopped", dateFormatter.format(obs.getDateStopped()));

		// first convert this concept to a json object
		jsonTemp = new JSONObject();
		jsonTemp.put("id", obs.getConcept().getConceptId());
		addNonEmptyStringAsProperty(jsonTemp, "name", obs.getConcept()
				.getDisplayString());

		// add the converted concept object to the main json object
		json.put("concept", jsonTemp);

		if (obs.getEncounter() != null) {
			jsonTemp = new JSONObject();// refresh
			jsonTemp.put("id", obs.getEncounter().getEncounterId());
			if (obs.getEncounter().getEncounterType() != null)
				addNonEmptyStringAsProperty(jsonTemp, "type", obs
						.getEncounter().getEncounterType().getName());
			json.put("encounter", jsonTemp);
		}

		if (obs.getValueCoded() != null) {
			jsonTemp = new JSONObject();
			jsonTemp.put("id", obs.getValueCoded().getConceptId());
			addNonEmptyStringAsProperty(jsonTemp, "name", obs.getValueCoded()
					.getDisplayString());
			json.put("valueCoded", jsonTemp);
		}

		if (obs.getValueCodedName() != null) {
			jsonTemp = new JSONObject();
			jsonTemp.put("id", obs.getValueCodedName().getConceptNameId());
			addNonEmptyStringAsProperty(jsonTemp, "name", obs
					.getValueCodedName().getName());
			json.put("valueCodedName", jsonTemp);
		}

		if (obs.getValueDrug() != null) {
			jsonTemp = new JSONObject();
			jsonTemp.put("id", obs.getValueDrug().getDrugId());
			addNonEmptyStringAsProperty(jsonTemp, "name", obs.getValueDrug()
					.getName());
			json.put("valueDrug", jsonTemp);
		}

		if (obs.getOrder() != null) {
			jsonTemp = new JSONObject();
			jsonTemp.put("id", obs.getOrder().getOrderId());
			addNonEmptyStringAsProperty(jsonTemp, "instructions", obs
					.getOrder().getInstructions());
			json.put("order", jsonTemp);
		}

		addNonEmptyStringAsProperty(json, "comment", WebUtil.escapeQuotes(obs
				.getComment()));

		if (obs.getLocation() != null) {
			jsonTemp = new JSONObject();
			jsonTemp.put("id", obs.getLocation().getLocationId());
			addNonEmptyStringAsProperty(jsonTemp, "address1", obs.getLocation()
					.getAddress1());
			addNonEmptyStringAsProperty(jsonTemp, "address2", obs.getLocation()
					.getAddress2());
			addNonEmptyStringAsProperty(jsonTemp, "cityVillage", obs
					.getLocation().getCityVillage());
			addNonEmptyStringAsProperty(jsonTemp, "neighborhoodCell", obs
					.getLocation().getNeighborhoodCell());
			addNonEmptyStringAsProperty(jsonTemp, "region", obs.getLocation()
					.getRegion());
			addNonEmptyStringAsProperty(jsonTemp, "subRegion", obs
					.getLocation().getSubregion());
			addNonEmptyStringAsProperty(jsonTemp, "countyDistrict", obs
					.getLocation().getCountyDistrict());
			addNonEmptyStringAsProperty(jsonTemp, "stateProvince", obs
					.getLocation().getStateProvince());
			addNonEmptyStringAsProperty(jsonTemp, "country", obs.getLocation()
					.getCountry());

			json.put("location", jsonTemp);
		}

		if (obs.getGroupMembers() != null && obs.getGroupMembers().size() > 0) {

			Set<Obs> groupMembers = obs.getGroupMembers();
			JSONArray jsonArray = new JSONArray();
			for (Obs member : groupMembers) {
				jsonTemp = new JSONObject();
				jsonTemp.put("memberId", member.getObsId());
				addNonEmptyStringAsProperty(jsonTemp, "value", member
						.getValueAsString(Locale.ENGLISH));
				jsonArray.put(jsonTemp);
			}

			json.put("obsMemberList", jsonArray);

		}
		// output the json object as json text, the parameter value formats the
		// output with some indentations to the factor of 2 spaces for each
		// nested property
		return json.toString(2);
	}

	/**
	 * Convenience method that adds a property only if its key and value are not
	 * empty else it doesn't add it, note that the putOpt() method of the
	 * jsonObject adds a property even when its key or value is an empty string
	 * as long as none of them is null so this method adds the extra feature of
	 * ignoring a property if either its key or value is an empty string.
	 * 
	 * @param jObject
	 *            The Json Object you wish to add to the key and value
	 * @param key
	 *            The value of the key for the property you wish to add
	 * @param stringToAdd
	 *            The value of the string you wish to add to the jsonObject
	 * @param xml
	 *            JSON-encoded obs
	 * 
	 */
	private static void addNonEmptyStringAsProperty(JSONObject jObject,
			String key, String stringToAdd) {

		// donot add the property if the key or value is null or empty
		if (!StringUtils.hasText(key) || !StringUtils.hasText(stringToAdd))
			return;

		jObject.put(key, stringToAdd);

	}

	/**
	 * Create obs from JSON representation
	 * 
	 * @param xml
	 *            JSON-encoded obs
	 * @return Obs objected created from details in JSON
	 */
	public static Obs decode(String xml) {
		// TODO Add implementation code
		throw new RuntimeException("Obs decoding not yet implemented");
	}

}
