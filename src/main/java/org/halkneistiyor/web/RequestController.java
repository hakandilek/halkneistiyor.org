package org.halkneistiyor.web;

import org.halkneistiyor.social.web.SignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestController {

	@RequestMapping("/request/new")
	public String requestNew() {
		return "requestNew";
	}

	@RequestMapping("/request/popular")
	public String requestPopular(Model model) {
		model.addAttribute("authenticated", SignInUtils.isAuthenticated());
		model.addAttribute("authUser", SignInUtils.authenticatedUser());
		return "requestPopular";
	}

	@RequestMapping("/request/recent")
	public String requestRecent() {
		return "requestRecent";
	}

}
