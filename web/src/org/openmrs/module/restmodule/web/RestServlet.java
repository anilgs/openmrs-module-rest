package org.openmrs.module.restmodule.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.APIAuthenticationException;
import org.openmrs.module.restmodule.RestUtil;
import org.openmrs.module.restmodule.web.RestResource.Operation;
import org.openmrs.module.restmodule.web.RestResource.OutputType;

/**
 * Provides a simple RESTful API to the repository. Useful for meeting simple
 * data needs over HTTP. Authentication is performed using HTTP BASIC
 * Authentication. Global properties are used to restrict access by client IP
 * address and to set a maximum number of results to be returned.
 */
public class RestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6644606499605233047L;

	/**
	 * Name for servlet within the servlet mapping (follows "/servletModule/" in
	 * URL)
	 */
	public static final String SERVLET_NAME = "api";
	public static final String SERVLET_NAME_JSON = "json";

	/**
	 * Internally held list of resources. Currently hardcoded.
	 */
	private static Hashtable<String, RestResource> resources = new Hashtable<String, RestResource>();
	static {
		resources.put("patient", new PatientResource());
		resources.put("findPatient", new FindPatientResource());
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handleRequest(Operation.GET, request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handleRequest(Operation.POST, request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handleRequest(Operation.PUT, request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		handleRequest(Operation.DELETE, request, response);
	}

	/**
	 * Handle all requests to the API -- i.e., authenticate to the API, restrict
	 * access based on client's IP address, parse the request URL, and pass
	 * control to the appropriate resource.
	 * 
	 * @param operation
	 *            HTTP operation being performed (e.g., GET, POST, PUT, DELETE)
	 * @param request
	 *            HTTP request
	 * @param response
	 *            HTTP response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void handleRequest(Operation operation, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Implement BASIC Authentication and restrict by client IP address
		String auth = request.getHeader("Authorization");
		String remoteAddress = request.getRemoteAddr();
		if (!RestUtil.allowUser(auth)
				|| !RestUtil.allowRemoteAddress(remoteAddress)) {
			// Not allowed
			response.setHeader("WWW-Authenticate",
					"BASIC realm=\"OpenMRS Rest API\"");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		// Parse the request URL, removing the rest module name along with the
		// mapping to this servlet
		String url = request.getRequestURI();
		int pos = url.indexOf("/" + RestUtil.MODULE_ID + "/")
				+ RestUtil.MODULE_ID.length() + 2;
		String target = url.substring(pos);
		
		OutputType outputType = null;
		
		// Remove "api/" from URL (gets us to this servlet)
		if (target.startsWith(SERVLET_NAME + "/")) {
			target = target.substring(4);
			outputType = OutputType.XML;
		}
		else if (target.startsWith(SERVLET_NAME_JSON + "/")) {
			target = target.substring(5);
			outputType = OutputType.JSON;
		}

		// If we have a matching resource, let it handle the request
		for (String resourceName : resources.keySet()) {
			if (target.startsWith(resourceName + "/")) {
				response.setHeader("Content-type", "text/xml");
				String restRequest = URLDecoder.decode(target
						.substring(resourceName.length() + 1), "UTF-8");
				try {
					resources.get(resourceName).handleRequest(operation, outputType,
							restRequest, request, response);
				} catch (APIAuthenticationException e) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
				}
				return;
			}
		}
		
		// If no matching resources were found, return an error
		response.sendError(HttpServletResponse.SC_BAD_REQUEST);

	}

}
