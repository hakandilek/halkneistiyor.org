/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.halkneistiyor.social.web;

import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.social.connect.CustomAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class SignInUtils {
	
	public static void signin(SocialUser user) {
		System.out.println("signing in: " + user);
		SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(user));	
	}

}
