package com.github.xabgesagtx.fatlining.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * Home and login pages
 *
 */
@RequestMapping("")
@Controller
public class HomeController {

	@GetMapping("")
	public String home() {
		return "redirect:/measurements";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String loginPost() {
		return "redirect:/";
	}
	
}
