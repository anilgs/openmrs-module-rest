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
import org.openmrs.module.restmodule.xmlconverters.PatientConverter;

import com.thoughtworks.xstream.XStream;

/**
 * Facilitates the encoding and decoding of
 * <code>org.openmrs.Patient</org> objects to/from
 * XML.
 */
public class XmlPatient {

	/**
	 * Convert a patient object into XML
	 * 
	 * @param patient
	 *            object to marshal into XML
	 * @return XML version of patient
	 */
	public static String encode(Patient patient) {
		XStream xstream = new XStream();
		xstream.registerConverter(new PatientConverter());
		xstream.alias("patient", Patient.class);
		return xstream.toXML(patient);	
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
