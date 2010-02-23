package org.openmrs.module.restmodule;

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

/**
 * Facilitates the encoding and decoding of
 * <code>org.openmrs.Obs</org> objects to/from
 * XML.
 */
// TODO Add unit tests for the methods in this class
public class XmlObs {

	/**
	 * All dates are reported in YYYY-MM-DD format
	 */
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * Convert a obs object into XML
	 * 
	 * @param obs
	 *            object to marshal into XML
	 * @return XML version of obs
	 */
	public static String encode(Obs obs) {

		StringBuffer xml = new StringBuffer();

		xml.append("<obs uuid=\""
				+ obs.getUuid()
				+ "\" "
				+ "patientId=\""
				+ obs.getPersonId()
				+ "\" "
				+ "obsDatetime=\""
				+ dateFormatter.format(obs.getObsDatetime())
				+ "\" "
				+ ((obs.getAccessionNumber() != null) ? "accessionNumber=\""
						+ obs.getAccessionNumber() + "\" " : "")
				+ "isObsGrouping=\"" + obs.isObsGrouping() + "\" "
				+ "isComplex=\"" + obs.isComplex() + "\">");

		Concept concept = obs.getConcept();
		addOptionalElementWithIdAttribute(xml, "concept", concept
				.getConceptId().toString(), concept.getDisplayString());

		addOptionalElement(xml, "valueNumeric", obs.getValueNumeric()
				.toString());
		addOptionalElement(xml, "valueText", obs.getValueText());
		addOptionalElement(xml, "valueComplex", obs.getValueComplex());
		addOptionalElement(xml, "valueModifier", obs.getValueModifier());
		addOptionalElement(xml, "valueGroupId",
				(obs.getValueGroupId() != null) ? obs.getValueGroupId()
						.toString() : "");
		addOptionalElement(xml, "dateStarted",
				(obs.getDateStarted() != null) ? dateFormatter.format(obs
						.getDateStarted()) : "");
		addOptionalElement(xml, "dateStopped",
				(obs.getDateStopped() != null) ? dateFormatter.format(obs
						.getDateStopped()) : "");
		addOptionalElement(xml, "valueDatetime",
				(obs.getValueDatetime() != null) ? dateFormatter.format(obs
						.getValueDatetime()) : "");

		if (obs.getEncounter() != null)
			xml
					.append("<encounter id=\""
							+ obs.getEncounter().getEncounterId()
							+ "\""
							+ ((obs.getEncounter().getEncounterType() != null) ? " type=\""
									+ obs.getEncounter().getEncounterType()
											.getName() + "\""
									: "") + " />");

		if (obs.getValueCoded() != null) {
			Concept valueCoded = obs.getValueCoded();
			addOptionalElementWithIdAttribute(xml, "valueCoded", valueCoded
					.getConceptId().toString(), valueCoded.getDisplayString());
		}
		if (obs.getValueCodedName() != null) {
			ConceptName valueCodedName = obs.getValueCodedName();
			addOptionalElementWithIdAttribute(xml, "valueCodedName",
					valueCodedName.getConceptNameId().toString(),
					valueCodedName.getName());
		}
		if (obs.getValueDrug() != null) {
			Drug valueDrug = obs.getValueDrug();
			addOptionalElementWithIdAttribute(xml, "valueDrug", valueDrug
					.getDrugId().toString(), valueDrug.getName());
		}
		if (obs.getOrder() != null) {
			Order order = obs.getOrder();
			addOptionalElementWithIdAttribute(xml, "order", order.getOrderId()
					.toString(), order.getInstructions());
		}

		addOptionalElement(xml, "comment", WebUtil.escapeQuotes(obs
				.getComment()));

		if (obs.getLocation() != null) {
			Location location = obs.getLocation();
			xml.append("<location id=\"" + location.getLocationId() + "\">");
			addOptionalElement(xml, "address1", location.getAddress1());
			addOptionalElement(xml, "address2", location.getAddress2());
			addOptionalElement(xml, "cityVillage", location.getCityVillage());
			addOptionalElement(xml, "neighborhoodCell", location
					.getNeighborhoodCell());
			addOptionalElement(xml, "region", location.getRegion());
			addOptionalElement(xml, "subRegion", location.getSubregion());
			addOptionalElement(xml, "countyDistrict", location
					.getCountyDistrict());
			addOptionalElement(xml, "stateProvince", location
					.getStateProvince());
			addOptionalElement(xml, "country", location.getCountry());
			xml.append("</location>");
		}

		if (obs.getGroupMembers() != null && obs.getGroupMembers().size() > 0) {

			Set<Obs> groupMembers = obs.getGroupMembers();
			xml.append("<obsMemberList>");
			for (Obs member : groupMembers) {
				addOptionalElementWithIdAttribute(xml, "member", member
						.getObsId().toString(), member
						.getValueAsString(Locale.ENGLISH));
				xml.append("</member>");
			}
			xml.append("</obsMemberList>");
		}

		xml.append("</obs>");

		return xml.toString();
	}

	/**
	 * Convenience method for rendering XML elements
	 * 
	 * @param xml
	 *            buffer for XML output
	 * @param tag
	 *            name of tag to put in the body of element
	 * @param value
	 *            the value for the element. if null, then nothing is added to
	 *            the output buffer
	 */
	private static void addOptionalElement(StringBuffer xml, String tag,
			String value) {
		if (value == null || value.equals(""))
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
	 * Convenience method for rendering XML elements that have a single
	 * attribute for id
	 * 
	 * @param xml
	 *            buffer for XML output
	 * @param tag
	 *            name of tag for element
	 * @param attribValue
	 *            the value for the attribute of this tag
	 * @param value
	 *            the value for the element. if null, then nothing is added to
	 *            the output buffer
	 */
	private static void addOptionalElementWithIdAttribute(StringBuffer xml,
			String tag, String attribValue, String value) {

		xml.append("<");
		xml.append(tag);
		xml.append(" id=\"" + attribValue + "\">");
		xml.append((value == null) ? "" : value);
		xml.append("</");
		xml.append(tag);
		xml.append(">");
	}

	/**
	 * Create obs from XML representation
	 * 
	 * @param xml
	 *            XML-encoded obs
	 * @return Obs objected created from details in XML
	 */
	public static Obs decode(String xml) {
		// TODO add implementation code
		throw new RuntimeException("Obs decoding not yet implemented");
	}

}
