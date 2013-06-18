package org.halkneistiyor.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RequestController {

	@RequestMapping("/talepler/yeni_talep")
	public String requestNew() {
		return "requestNew";
	}

	@RequestMapping("/talepler/populer")
	public String requestPopular() {
		return "requestPopular";
	}

	@RequestMapping("/talepler/yeniler")
	public String requestRecent() {
		return "requestRecent";
	}

}
