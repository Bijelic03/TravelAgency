package com.ftn.TravelOrganisation.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;


import com.ftn.TravelOrganisation.controller.LoginController;
import com.ftn.TravelOrganisation.model.Korisnik;

@Component
public class InitHttpSessionListener implements HttpSessionListener {

	/** kod koji se izvrsava po kreiranju sesije */
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("Inicijalizacija sesisje HttpSessionListener...");
//		
//		//pri kreiranju sesije inicijalizujemo je ili radimo neke dodatne aktivnosti
		Korisnik prijavljeniKorisnik = null;
		HttpSession session  = arg0.getSession();
		session.setAttribute(LoginController.PRIJAVLJENI_KORISNIK, prijavljeniKorisnik);
		
//		
	}
	
	/** kod koji se izvrsava po brisanju sesije */
	public void sessionDestroyed(HttpSessionEvent arg0) {
		System.out.println("Brisanje sesisje HttpSessionListener...");
		
	}

}


