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
package org.openmrs.module.restmodule.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.restmodule.JsonUser;
import org.openmrs.module.restmodule.RestUtil;
import org.openmrs.module.restmodule.XmlUser;

/**
 * Provides a simple RESTful access to users
 */
public class UserResource implements RestResource {

	/**
	 * Handle all requests to this resource
	 */
	public void handleRequest(Operation operation, OutputType outputType,
			String restRequest, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		switch (operation) {

		case GET:
			List<User> userList = new ArrayList<User>();
			userList.add(Context.getUserService()
					.getUserByUsername(restRequest));
			printUserList(out, outputType, userList);

			break;

		case POST:
		case PUT:
		case DELETE:
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			return;
		}

	}

	/**
	 * Auto generated method comment
	 * 
	 * @param out
	 * @param outputType
	 * @param patientList
	 */
	public static void printUserList(PrintWriter out, OutputType outputType,
			Collection<User> userList) {
		if (outputType == OutputType.XML) {
			out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.print("<userList version=\""
					+ RestServlet.PATIENT_LIST_XML_VERSION + "\">");
			int i = 0;
			int max = RestUtil.getMaxResults();
			for (User user : userList) {
				out.print(XmlUser.encode(user));
				i++;
				if (max > 0 && i >= max)
					break; // if max set, abort before exceeding
			}
			out.print("</userList>");
		} else if (outputType == OutputType.JSON) {
			out.print("[");
			int i = 0;
			int max = RestUtil.getMaxResults();
			for (User user : userList) {
				if (i != 0)
					out.print(",");
				out.print(JsonUser.encode(user));
				i++;
				if (max > 0 && i >= max)
					break; // if max set, abort before exceeding
			}
			out.print("]");
		}
	}
}
