package com.uma.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home() {

		return "homePage"; // This should match the Thymeleaf template name, e.g., home.html
	}

	@GetMapping("/home")
	public String homePage(@RequestParam String role, Model model) {
		switch (role.toLowerCase()) {
		case "user":
			return "login";
		case "admin":
			return "adminLogin";
		default:
			model.addAttribute("error", "url is incorrect click on any button");
			return "homePage";
		}
	}

}
