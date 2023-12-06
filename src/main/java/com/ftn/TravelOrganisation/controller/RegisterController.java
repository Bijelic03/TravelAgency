package com.ftn.TravelOrganisation.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.service.RegisterService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
@RequestMapping("register")
public class RegisterController {

	
	private final String bURL;

	
	private final RegisterService registerService;
	 
	@Autowired
	public RegisterController(ServletContext servletContext, RegisterService registerService) {
		this.registerService = registerService;
		this.bURL = servletContext.getContextPath();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
	    registrar.setUseIsoFormat(true);
	    registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	    // Use ConversionService to register formatters
	    FormattingConversionService conversionService = (FormattingConversionService) binder.getConversionService();
	    registrar.registerFormatters(conversionService);
	}




	
	@GetMapping
	public String showRegistrationForm(Model model) {
	    model.addAttribute("korisnik", new Korisnik());
	    model.addAttribute("userAlreadyExists", false);
	    return "register";
	}

	@PostMapping
	public String register(@ModelAttribute("korisnik") Korisnik korisnik, Model model) {

	    if (!registerService.alreadyRegistered(korisnik.getKorisnickoIme())) {
	        registerService.register(korisnik);
	        return "redirect:/register";
	    } else {
	    	System.out.println("vec postoji taj user");
	        model.addAttribute("userAlreadyExists", true);
	        return "redirect:/register";
	    }
	}

	

}
