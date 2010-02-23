package org.openmrs.module.restmodule.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.restmodule.JsonObs;
import org.openmrs.module.restmodule.RestUtil;
import org.openmrs.module.restmodule.XmlObs;

/**
 * Provides a simple RESTful access to patients
 */
public class ObsResource implements RestResource {

	/**
	 * Handle all requests to this resource
	 * 
	 */
	public void handleRequest(Operation operation, OutputType outputType,
			String restRequest, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		switch (operation) {

		case GET:
			// check if the request has the required parameters
			if (request.getParameter("pId") == null
					|| request.getParameter("cId") == null
					|| request.getParameter("pId").equals("")
					|| request.getParameter("cId").equals("")) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			try {

				Collection<Patient> patientList = Context.getPatientService()
						.getPatientsByIdentifier(request.getParameter("pId"),
								false);
				// if exactly one patient with a matching identifier is found
				if (patientList != null && patientList.size() == 1) {
					Concept concept = Context.getConceptService().getConcept(
							Integer.valueOf(request.getParameter("cId")));

					Collection<Obs> obsList = Context.getObsService()
							.getObservationsByPersonAndConcept(
									patientList.iterator().next(), concept);
					printObsList(out, outputType, obsList);
				}
				// if multiple patients are found
				else if (patientList != null && patientList.size() > 1) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				}
				// print empty list since the patient couldn't be found or If
				// this prevents parser errors e.g for xml the parser would
				// indicate that no element was found
				else {
					printObsList(out, outputType, new HashSet<Obs>());
				}

			} catch (NumberFormatException nfe) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			} catch (Exception e) {
				response
						.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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

	/**
	 * Auto generated method comment
	 * 
	 * @param out
	 * @param outputType
	 * @param obsList
	 */
	public static void printObsList(PrintWriter out, OutputType outputType,
			Collection<Obs> obsList) {
		if (outputType == OutputType.XML) {
			out.print("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.print("<obsList version=\"" + RestServlet.obsListXmlVersion
					+ "\">");
			int i = 0;
			int max = RestUtil.getMaxResults();
			for (Obs obs : obsList) {
				out.print(XmlObs.encode(obs));
				i++;
				if (max > 0 && i >= max)
					break; // if max set, abort before exceeding
			}
			out.print("</obsList>");
		} else if (outputType == OutputType.JSON) {
			out.print("[");
			int i = 0;
			int max = RestUtil.getMaxResults();
			for (Obs obs : obsList) {
				if (i != 0)
					out.print(",");
				out.print(JsonObs.encode(obs));
				i++;
				if (max > 0 && i >= max)
					break; // if max set, abort before exceeding
			}
			out.print("]");
		}
	}
}
