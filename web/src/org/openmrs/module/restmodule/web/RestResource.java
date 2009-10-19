package org.openmrs.module.restmodule.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents a RESTful resource (e.g., patient) that can be accessed through
 * RESTful calls
 */
public interface RestResource {

	// Rest Operations
        public enum Operation {GET, POST, PUT, DELETE};
        
    // Rest output content type options
        public enum OutputType {XML, JSON};
    
	/**
	 * Used to pass request to the given resource
	 * 
	 * @param operation
	 *            REST operation (e.g., GET, POST, PUT, DELETE)
	 * @param outputType type of output (e.g. xml, json)
	 * @param restRequest
	 *            fragment of request URL beyond resource reference
	 * @param request
	 *            HTTP request
	 * @param response
	 *            HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void handleRequest(Operation operation, OutputType outputType, String restRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;

}
