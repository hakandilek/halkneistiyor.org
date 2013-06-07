package org.halkneistiyor.security.web;

import java.util.EnumSet;
import java.util.Set;

import javax.validation.Valid;

import org.halkneistiyor.security.UserAuthentication;
import org.halkneistiyor.security.UserRegistry;
import org.halkneistiyor.security.model.SocialUser;
import org.halkneistiyor.security.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.users.UserServiceFactory;

@Controller
public class UserRegistrationController {

	@Autowired
	private UserRegistry registry;

	@RequestMapping(value = "/security/register", method = RequestMethod.POST)
	public String register(@Valid UserRegistrationForm form,
			BindingResult result) {
		if (result.hasErrors()) {
			return null;
		}

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		SocialUser currentUser = (SocialUser) authentication.getPrincipal();
		Set<UserRole> roles = EnumSet.of(UserRole.USER);

		if (UserServiceFactory.getUserService().isUserAdmin()) {
			roles.add(UserRole.ADMIN);
		}

		String key = currentUser.getKey();
		String nickname = currentUser.getNickname();
		String email = currentUser.getEmail();
		String firstname = form.getFirstname();
		String lastname = form.getLastname();
		boolean enabled = true;
		SocialUser u = new SocialUser();
		u.setKey(key);
		u.setEmail(email);
		u.setFirstname(firstname);
		u.setLastname(lastname);
		u.setNickname(nickname);
		u.setEnabled(enabled);

		registry.registerUser(u);

		// Update the context with the full authentication
		context.setAuthentication(new UserAuthentication(u, authentication
				.getDetails()));

		return "redirect:/";
	}

}
