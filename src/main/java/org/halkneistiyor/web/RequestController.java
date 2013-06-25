package org.halkneistiyor.web;

import org.halkneistiyor.social.web.SignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestController {

	@RequestMapping("/talepler/yeni_talep")
	public String requestNew() {
		return "requestNew";
	}

	@RequestMapping("/talepler/populer")
	public String requestPopular(Model model) {
		model.addAttribute("authenticated", SignInUtils.isAuthenticated());
		model.addAttribute("authUser", SignInUtils.authenticatedUser());
		return "requestPopular";
	}

	@RequestMapping("/talepler/yeniler")
	public String requestRecent() {
		return "requestRecent";
	}

}
