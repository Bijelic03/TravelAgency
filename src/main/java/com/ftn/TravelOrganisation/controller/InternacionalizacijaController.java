package com.ftn.TravelOrganisation.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

@Controller
public class InternacionalizacijaController {

	  @Autowired
	    private LocaleResolver localeResolver;

	    @GetMapping("/TravelOrganisation/change-language")
	    public String changeLanguage(@RequestParam(name = "locales") String locale,
	                                 HttpServletRequest request,
	                                 HttpServletResponse response) {
	        localeResolver.setLocale(request, response, new Locale(locale));
	        return "redirect:/"; // Redirect back to the same page or any other page
	    }

}


