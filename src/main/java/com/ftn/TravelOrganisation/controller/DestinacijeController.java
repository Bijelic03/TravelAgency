package com.ftn.TravelOrganisation.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import com.ftn.TravelOrganisation.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.repository.DestinacijaRepository;
import com.ftn.TravelOrganisation.service.DestinacijeService;





@Controller
@RequestMapping("destinacije")
public class DestinacijeController {
	
	private final String bURL;

	
	private final DestinacijeService destinacijeService;
	 
	private final DestinacijaRepository destinacijaRepository;
	
	@Autowired
	public DestinacijeController(ServletContext servletContext, DestinacijeService destinacijeService, DestinacijaRepository destinacijaRepository) {
		this.destinacijeService = destinacijeService;
		this.destinacijaRepository = destinacijaRepository;
		this.bURL = servletContext.getContextPath();
	}





	@GetMapping
	@ResponseBody
	public ModelAndView sveDestinacije() {
	    ModelAndView rezultat = new ModelAndView("destinacije");
	    List<Destinacija> listaDestinacija = destinacijaRepository.findAll();
	    rezultat.addObject("listaDestinacija", listaDestinacija); // Dodajemo listu pod imenom "listaDestinacija"
	    return rezultat;
	}



	@GetMapping("/dodaj")
	@ResponseBody
	public void dodajDestinaciju(HttpServletResponse response) throws IOException {
	    PrintWriter out = response.getWriter();

	    out.write("<!DOCTYPE html>\r\n"
	            + "<html>\r\n"
	            + "\r\n"
	            + "<head>\r\n"
	            + "    <meta charset=\"UTF-8\" />\r\n"
	            + "    <base href=\"/TravelOrganisation/\" />\r\n"
	            + "    <title>Sve knjige</title>\r\n"
	            + "\r\n"
	            + "</head>\r\n"
	            + "\r\n"
	            + "<body>\r\n"
	            + "	\r\n"
	            + "	<form action=\"destinacije/add\" method=\"POST\">\r\n"
	            + "		<label>Grad</label>\r\n"
	            + "		<input name=\"grad\"></input>\r\n"
	            + "		<label>Drzava</label>\r\n"
	            + "		<input name=\"drzava\"></input>\r\n"
	            + "		<label>Kontinent</label>\r\n"
	            + "		<input name=\"kontinent\"></input>\r\n"
	            + "     <input type=\"submit\" value=\"Dodaj\"></input>"
	            + "	</form>\r\n"
	            + "\r\n"
	            + "</body>\r\n"
	            + "\r\n"
	            + "</html>\r\n");
	}

	@PostMapping("/add")
	public void addDestinacju(@ModelAttribute Destinacija destinacija, HttpServletResponse response) throws IOException {
		destinacijaRepository.save(destinacija);
		response.sendRedirect(bURL+ "/destinacije");
	}
	
	@PostMapping("/edit")
	public void editDestinacju(@ModelAttribute Destinacija destinacija, HttpServletResponse response) throws IOException {
		destinacijaRepository.update(destinacija);
		response.sendRedirect(bURL+ "/destinacije");
	}
	
	@PostMapping(value="/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {		
		destinacijaRepository.delete(id);
		response.sendRedirect(bURL+"/destinacije");
	}

	@PostMapping("/izmeni")
	@ResponseBody
	public void izmeniDestinaciju(HttpServletResponse response, Long id) throws IOException {

		//Destinacije destinacije = (Destinacije) memorijaAplikacije.get(DestinacijeService.DESTINACIJE_KEY);

		Destinacija destinacija = destinacijaRepository.findOne(id);

		PrintWriter out = response.getWriter();

		out.write("<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "\r\n"
				+ "<head>\r\n"
				+ "    <meta charset=\"UTF-8\" />\r\n"
				+ "    <base href=\"/TravelOrganisation/\" />\r\n"
				+ "    <title>Sve knjige</title>\r\n"
				+ "\r\n"
				+ "</head>\r\n"
				+ "\r\n"
				+ "<body>\r\n"
				+ "	\r\n"
				+ "	<form action=\"destinacije/edit\" method=\"POST\">\r\n"
				+ "		<input type=\"hidden\" name=\"id\" value="+ destinacija.getId() + " ></input>\r\n"
				+ "		<label>Grad</label>\r\n"
				+ "		<input value="+ destinacija.getGrad() + " name=\"grad\"></input>\r\n"
				+ "		<label>Drzava</label>\r\n"
				+ "		<input value="+ destinacija.getDrzava() + " name=\"drzava\"></input>\r\n"
				+ "		<label>Kontinent</label>\r\n"
				+ "		<input value="+ destinacija.getKontinent() + " name=\"kontinent\"></input>\r\n"
			    + "     <input type=\"submit\" value=\"Izmeni\"></input>"

				
				+ "	</form>\r\n"
				+ "\r\n"
				+ "</body>\r\n"
				+ "\r\n"
				+ "</html>\r\n"
				+ "");

	}







}
