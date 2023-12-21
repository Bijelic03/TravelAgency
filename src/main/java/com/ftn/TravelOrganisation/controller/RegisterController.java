package com.ftn.TravelOrganisation.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.ftn.TravelOrganisation.repository.KorisnikRepository;
import com.ftn.TravelOrganisation.service.RegisterService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
@RequestMapping("register")
public class RegisterController {

	public static final String PRIJAVLJENI_KORISNIK = "prijavljeni_korisnik";

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
	    model.addAttribute("showModal", false);
	    return "register";
	}


	@PostMapping
	public String register(@ModelAttribute("korisnik") Korisnik korisnik, Model model, HttpSession session, HttpServletRequest request) {
	    if (!registerService.alreadyRegistered(korisnik.getKorisnickoIme())) {
	        registerService.register(korisnik);
	        session = request.getSession(true);
	        session.setAttribute(PRIJAVLJENI_KORISNIK, korisnik);

	        return "redirect:/";
	    } else {
	        System.out.println("Već postoji korisnik sa tim korisničkim imenom.");
	        model.addAttribute("userAlreadyExists", true);
	        model.addAttribute("showModal", true);
	        return "register";
	    }
	}



	

}
