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

import java.util.EnumSet;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;

import org.halkneistiyor.datamodel.SocialUser;
import org.halkneistiyor.datamodel.SocialUserManager;
import org.halkneistiyor.datamodel.UserRole;
import org.halkneistiyor.web.message.Message;
import org.halkneistiyor.web.message.MessageType;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import com.google.appengine.api.users.UserService;

@Controller
public class SignupController {

	private final SocialUserManager socialUserManager;
	
	private final UserService userService;

	@Inject
	public SignupController(SocialUserManager socialUserManager, UserService userService) {
		this.socialUserManager = socialUserManager;
		this.userService = userService;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public SignupForm signupForm(WebRequest request) {
		Connection<?> connection = ProviderSignInUtils.getConnection(request);
		if (connection != null) {
			String txt = "Your " + StringUtils.capitalize(connection.getKey().getProviderId())
					+ " account is not associated with an account. If you're new, please sign up.";
			Message message = new Message(MessageType.INFO, txt);
			request.setAttribute("message", message, WebRequest.SCOPE_REQUEST);
			return SignupForm.fromProviderUser(connection.fetchUserProfile());
		} else {
			return new SignupForm();
		}
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signup(@Valid SignupForm form, BindingResult formBinding, WebRequest request) {
		if (formBinding.hasErrors()) {
			return null;
		}
		SocialUser su = createUser(form, formBinding);
		if (su != null) {
			SignInUtils.signin(su.getUserId());
			ProviderSignInUtils.handlePostSignUp(su.getUserId(), request);
			return "redirect:/";
		}
		return null;
	}

	// internal helpers

	private SocialUser createUser(SignupForm form, BindingResult formBinding) {
		SocialUser su = new SocialUser();
		su.setEmail(form.getEmail());
		su.setFirstName(form.getFirstName());
		su.setLastName(form.getLastName());
		su.setEnabled(true);
		Set<UserRole> roles = EnumSet.of(UserRole.USER);
		if (userService.isUserAdmin()) {
			roles.add(UserRole.ADMIN);
		}
		su.setRoles(roles);

		socialUserManager.registerUser(su);
		return su;
	}

}
