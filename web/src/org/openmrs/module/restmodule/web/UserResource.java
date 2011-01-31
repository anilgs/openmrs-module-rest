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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.restmodule.JsonUser;
import org.openmrs.module.restmodule.XmlUser;

public class UserResource implements RestResource {

	public void handleRequest(Operation operation, OutputType outputType,
			String restRequest, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		switch (operation) {

		case GET:
			User user = Context.getUserService().getUser(
					Integer.valueOf(restRequest));

			try {
				printUsertList(out, outputType, user);
			} catch (NumberFormatException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			break;

		case POST:
		case PUT:
		case DELETE:
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			return;
		}

	}

	private void printUsertList(PrintWriter out, OutputType outputType,
			User user) {
		if (outputType == OutputType.XML) {
			out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.print("<userList version=\""
					+ RestServlet.PATIENT_LIST_XML_VERSION + "\">");

			out.print(XmlUser.encode(user));

			out.print("</userList>");
		} else if (outputType == OutputType.JSON) {
			out.print("[");

			out.print(JsonUser.encode(user));

			out.print("]");
		}

	}

}
