package com.ftn.TravelOrganisation.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ftn.TravelOrganisation.model.Korisnik;
import com.ftn.TravelOrganisation.service.RegisterService;

@Controller
public class LoginController {

	public static final String PRIJAVLJENI_KORISNIK = "prijavljeni_korisnik";

	@Autowired
	RegisterService registerService;

	@GetMapping("/login")
	public String showLoginForm(HttpSession session) {
	    return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam String korisnickoIme, @RequestParam String sifra, HttpSession session, HttpServletRequest request, Model model) {
	    session.removeAttribute(PRIJAVLJENI_KORISNIK);
	    session = request.getSession(true);

	    Korisnik prijavljeniKorisnik = registerService.login(korisnickoIme, sifra);

	    if (prijavljeniKorisnik != null) {
	        session.setAttribute(PRIJAVLJENI_KORISNIK, prijavljeniKorisnik);
	        return "redirect:/";
	    } else {
	        model.addAttribute("loginError", "Korisnik ne postoji ili su uneti pogrešni podaci.");
	        return "login"; // Zamijenajte "your-login-page" imenom vaše login stranice.
	    }
	}


	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.removeAttribute(PRIJAVLJENI_KORISNIK);
	    return "redirect:/";
	}


}
