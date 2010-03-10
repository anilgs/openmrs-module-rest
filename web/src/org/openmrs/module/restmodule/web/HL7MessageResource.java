package org.openmrs.module.restmodule.web; 
	 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Service;
import org.openmrs.hl7.HL7Source;

/**
 * Provides RESTful access to the HL7Message facilities of OpenMRS
 */
public class HL7MessageResource implements RestResource {
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

		switch (operation) {
		case PUT:		
			
			if (request.getParameter(MESSAGE) == null
					|| request.getParameter(MESSAGE).equals("")
					|| request.getParameter(SOURCE) == null
					|| request.getParameter(SOURCE).equals("")) {				
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);				
				return;
			}
			
			String message = request.getParameter(MESSAGE);
			String source = request.getParameter(SOURCE);
			
			try {
				HL7Service hl7Service = Context.getHL7Service();
				HL7InQueue hl7Queue = new HL7InQueue();
				HL7Source hl7Source = hl7Service.getHL7SourceByName(source);
				
				if(hl7Source == null){
					response.sendError(HttpServletResponse.SC_BAD_REQUEST);				
					return;
				}
				
				hl7Queue.setHL7Source(hl7Source);
				hl7Queue.setHL7Data(message);
				hl7Service.saveHL7InQueue(hl7Queue);

			} catch (Exception e) {
				response
						.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

			break;

		case GET:
		case POST:
		case DELETE:
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			return;
		}

	}
}
