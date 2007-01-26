package org.openmrs.module.restmodule.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.restmodule.RestUtil;
import org.openmrs.module.restmodule.XmlPatient;

/**
 * Provides RESTful access to the patient search facilities of OpenMRS
 * 
 * @author Burke Mamlin
 * @version 1.0
 */
public class FindPatientResource implements RestResource {

	/**
	 * Handle all requests to this patient search resource
	 */
	public void handleRequest(int operation, String restRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		switch (operation) {

		case GET:
			List<Patient> patientList = Context.getPatientService()
					.findPatients(restRequest, false);

			out.print("<patientList>");
			int i = 0;
			int max = RestUtil.getMaxResults();
			for (Patient patient : patientList) {
				out.print(XmlPatient.encode(patient));
				i++;
				if (max > 0 && i >= max)
					break; // if max set, abort before exceeding
			}
			out.print("</patientList>");
			break;

		case POST:
		case PUT:
		case DELETE:
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			return;
		}

	}

}
