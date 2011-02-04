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

import org.openmrs.User;
import org.openmrs.module.restmodule.xmlconverters.UserConverter;

import com.thoughtworks.xstream.XStream;

/**
 * Facilitates the encoding and decoding of
 * <code>org.openmrs.User</org> objects to/from
 * XML.
 */
public class XmlUser {
	
	/**
	 * Convert a user object into XML
	 * 
	 * @param user
	 *            object to marshal into XML
	 * @return XML version of user
	 * @should return a valid XML document
	 */
	public static String encode(User user) {
		XStream xstream = new XStream();
		xstream.registerConverter(new UserConverter());
		xstream.alias("user", User.class);
		return xstream.toXML(user);		
	}

	/**
	 * Create user from XML representation
	 * 
	 * @param xml
	 *            XML-encoded user
	 * @return User objected created from details in XML
	 */
	public static User decode(String xml) {
		throw new RuntimeException("User decoding not yet implemented");
	}

}
