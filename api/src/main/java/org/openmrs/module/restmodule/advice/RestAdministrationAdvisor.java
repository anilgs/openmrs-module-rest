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
package org.openmrs.module.restmodule.advice;

import java.lang.reflect.Method;

import org.openmrs.module.restmodule.RestUtil;
import org.springframework.aop.AfterReturningAdvice;

/**
 * This class is intended to wrap around the org.openmrs.api.AdministrationService.
 */
public class RestAdministrationAdvisor implements AfterReturningAdvice {

	/**
	 * This method is called after every method in the associated service
	 * If a non get method contains the string "GlobalPropert", the 
	 * RestModule's property cache is updated
	 */
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		String methodName = method.getName();

		if (!methodName.startsWith("get") && methodName.contains("GlobalPropert")) {
			try {
				RestUtil.updateGlobalProperties();
			}
			catch (Throwable t) {
				// pass
			}
		}
		
	}
	
}