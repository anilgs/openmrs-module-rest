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

import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.User;
import org.openmrs.test.Verifies;

/**
 * Tests the XmlUser class
 */
public class XmlUserTest {

	/**
	 * @see {@link XmlUser#encode(User)}
	 */
	@Test
	@Verifies(value = "should return a valid XML document", method = "encode(User)")
	public void encode_shouldReturnAValidXMLDocument() throws Exception {
		User user = new User();

		user.setId(1);
		user.setUuid("3261245c-39d4-4b82-b6b6-bc567b8d9e46");
		user.setDescription("he is a maniac");
		user.setUsername("bwhite");
		user.setSystemId("1-9");

		Person person = new Person();
		person.setGender("M");
		// person.setBirthdate(new Date());
		person.setBirthdateEstimated(false);

		PersonAddress address = new PersonAddress();
		address.setAddress1("123 Dirt Road");
		address.setCityVillage("Indianapolis");
		address.setCountry("USA");
		address.setCountyDistrict("Marion");
		address.setPreferred(true);
		person.addAddress(address);

		user.setPerson(person);

		System.out.println(XmlUser.encode(user));
	}
}