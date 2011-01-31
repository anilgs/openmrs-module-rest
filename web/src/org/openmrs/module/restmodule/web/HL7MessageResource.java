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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Service;
import org.openmrs.hl7.HL7Source;

/**
 * Provides RESTful access to the hl7 message facilities of OpenMRS
 */
public class HL7MessageResource implements RestResource {
	
	private static final Log log = LogFactory.getLog(HL7MessageResource.class);
	
	/**
	 * Private Constants to Ease Maintenance
	 */
	private final String MESSAGE = "message";
	private final String SOURCE = "source";
	
	/**
	 * Handle all requests to this HL7Message resource
	 */
	public void handleRequest(Operation operation, OutputType outputType,
			String restRequest, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String message = request.getParameter(MESSAGE);
		String source = request.getParameter(SOURCE);
		
		switch (operation) {
		case POST:		
			
			if (message == null || message.equals("")) {
				log.error("Missing a 'message' parameter");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);				
				return;
			}
			if (source == null || source.equals("")) {
				log.error("Missing a 'source' parameter");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);				
				return;
			}
			
			try {
				HL7Service hl7Service = Context.getHL7Service();
				HL7InQueue hl7Queue = new HL7InQueue();
				HL7Source hl7Source = hl7Service.getHL7SourceByName(source);
				
				if(hl7Source == null){
					log.error("HL7Source object not found with a name of: '" + source + "'");
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);				
					return;
				}
				
				hl7Queue.setHL7Source(hl7Source);
				hl7Queue.setHL7Data(message);
				hl7Service.saveHL7InQueue(hl7Queue);

			} catch (Exception e) {
				log.error("Error occurred while saving hl7 in queue", e);
				response
						.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			break;

		case GET:
		case PUT:
		case DELETE:
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			return;
		}

	}
}
