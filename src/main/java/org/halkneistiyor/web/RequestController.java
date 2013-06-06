package org.halkneistiyor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestController {

	@RequestMapping("/request/new")
	public String requestNew() {
		return "requestNew";
	}

	@RequestMapping("/request/popular")
	public String requestPopular() {
		return "requestPopular";
	}

	@RequestMapping("/request/recent")
	public String requestRecent() {
		return "requestRecent";
	}

}
