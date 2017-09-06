package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultController {
	
	@RequestMapping(value="/accessDenied")
	public ModelAndView accessDenied(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("accessDenied");
		return modelAndView;
	}
}
