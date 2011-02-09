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

import org.openmrs.Obs;
import org.openmrs.module.restmodule.xmlconverters.ObsConverter;

import com.thoughtworks.xstream.XStream;

/**
 * Facilitates the encoding and decoding of
 * <code>org.openmrs.Obs</org> objects to/from
 * XML.
 */
// TODO Add unit tests for the methods in this class
public class XmlObs {

	/**
	 * Convert a obs object into XML
	 * 
	 * @param obs
	 *            object to marshal into XML
	 * @return XML version of obs
	 */
	public static String encode(Obs obs) {
		XStream xstream = new XStream();
		xstream.registerConverter(new ObsConverter());
		xstream.alias("obs", Obs.class);
		return xstream.toXML(obs);
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
