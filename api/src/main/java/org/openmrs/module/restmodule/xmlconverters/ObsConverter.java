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
package org.openmrs.module.restmodule.xmlconverters;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.web.WebUtil;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Convert an Obs to XML
 */
public class ObsConverter implements Converter {

	public boolean canConvert(Class clazz) {
		return clazz.equals(Obs.class);
	}

	/**
	 * All dates are reported in YYYY-MM-DD format
	 */
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Obs obs = (Obs) value;

		writer.addAttribute("uuid", obs.getUuid());
		writer.addAttribute("patientId", nullSafeString(obs.getPersonId()));
		if (obs.getObsDatetime() != null)
			writer.addAttribute("obsDatetime",
					dateFormatter.format(obs.getObsDatetime()));
		if (obs.getAccessionNumber() != null)
			writer.addAttribute("accessionNumber",
					nullSafeString(obs.getAccessionNumber()));
		writer.addAttribute("isObsGrouping",
				nullSafeString(obs.isObsGrouping()));
		writer.addAttribute("isComplex", nullSafeString(obs.isComplex()));

		Concept concept = obs.getConcept();
		addOptionalElementWithIdAttribute(writer, "concept", concept
				.getConceptId().toString(), concept.getDisplayString());

		addOptionalElement(writer, "valueNumeric",
				nullSafeString(obs.getValueNumeric()));
		addOptionalElement(writer, "valueText", obs.getValueText());
		addOptionalElement(writer, "valueComplex", obs.getValueComplex());
		addOptionalElement(writer, "valueModifier", obs.getValueModifier());
		addOptionalElement(writer, "valueGroupId",
				nullSafeString(obs.getValueGroupId()));

		addOptionalElement(
				writer,
				"dateStarted",
				(obs.getDateStarted() != null) ? dateFormatter.format(obs
						.getDateStarted()) : "");
		addOptionalElement(
				writer,
				"dateStopped",
				(obs.getDateStopped() != null) ? dateFormatter.format(obs
						.getDateStopped()) : "");
		addOptionalElement(
				writer,
				"valueDatetime",
				(obs.getValueDatetime() != null) ? dateFormatter.format(obs
						.getValueDatetime()) : "");

		if (obs.getEncounter() != null) {
			writer.startNode("encounter");
			writer.addAttribute("id", nullSafeString(obs.getEncounter()
					.getEncounterId()));
			writer.addAttribute("type", obs.getEncounter().getEncounterType()
					.getName());
			writer.endNode();
		}

		if (obs.getValueCoded() != null) {
			Concept valueCoded = obs.getValueCoded();
			addOptionalElementWithIdAttribute(writer, "valueCoded", valueCoded
					.getConceptId().toString(), valueCoded.getDisplayString());
		}
		if (obs.getValueCodedName() != null) {
			ConceptName valueCodedName = obs.getValueCodedName();
			addOptionalElementWithIdAttribute(writer, "valueCodedName",
					valueCodedName.getConceptNameId().toString(),
					valueCodedName.getName());
		}
		if (obs.getValueDrug() != null) {
			Drug valueDrug = obs.getValueDrug();
			addOptionalElementWithIdAttribute(writer, "valueDrug", valueDrug
					.getDrugId().toString(), valueDrug.getName());
		}
		if (obs.getOrder() != null) {
			Order order = obs.getOrder();
			addOptionalElementWithIdAttribute(writer, "order", order
					.getOrderId().toString(), order.getInstructions());
		}

		addOptionalElement(writer, "comment",
				WebUtil.escapeQuotes(obs.getComment()));

		if (obs.getLocation() != null) {
			Location location = obs.getLocation();
			writer.startNode("location");
			writer.addAttribute("id", location.getLocationId().toString());
			addOptionalElement(writer, "address1", location.getAddress1());
			addOptionalElement(writer, "address2", location.getAddress2());
			addOptionalElement(writer, "cityVillage", location.getCityVillage());
			addOptionalElement(writer, "neighborhoodCell",
					location.getNeighborhoodCell());
			addOptionalElement(writer, "region", location.getRegion());
			addOptionalElement(writer, "subRegion", location.getSubregion());
			addOptionalElement(writer, "countyDistrict",
					location.getCountyDistrict());
			addOptionalElement(writer, "stateProvince",
					location.getStateProvince());
			addOptionalElement(writer, "country", location.getCountry());
			writer.endNode();
		}

		if (obs.getGroupMembers() != null && obs.getGroupMembers().size() > 0) {
			Set<Obs> groupMembers = obs.getGroupMembers();
			writer.startNode("obsMemberList");
			for (Obs member : groupMembers)
				addOptionalElementWithIdAttribute(writer, "member", member
						.getObsId().toString(),
						member.getValueAsString(Locale.ENGLISH));
			writer.endNode();
		}
	}

	private static String nullSafeString(Object o) {
		if (o != null)
			return o.toString();
		return "";
	}

	/**
	 * Convenience method for rendering XML elements
	 * 
	 * @param writer
	 *            XML output writer
	 * @param nodeName
	 *            name of node for element
	 * @param value
	 *            the value for the element. if null, then nothing is added to
	 *            the output buffer
	 */
	private void addOptionalElement(HierarchicalStreamWriter writer,
			String nodeName, String value) {
		if (value != null) {
			writer.startNode(nodeName);
			writer.setValue(value);
			writer.endNode();
		}
	}

	private void addOptionalElementWithIdAttribute(
			HierarchicalStreamWriter writer, String nodeName, String id,
			String value) {
		if (value != null) {
			writer.startNode(nodeName);
			writer.addAttribute("id", id);
			writer.setValue(value);
			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return null;
	}

}