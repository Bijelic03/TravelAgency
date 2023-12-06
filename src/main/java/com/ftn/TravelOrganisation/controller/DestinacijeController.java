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

import com.ftn.TravelOrganisation.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.service.DestinacijeService;





@Controller
@RequestMapping("destinacije")
public class DestinacijeController {
	
	private final String bURL;

	
	private final DestinacijeService destinacijeService;
	 
	@Autowired
	public DestinacijeController(ServletContext servletContext, DestinacijeService destinacijeService) {
		this.destinacijeService = destinacijeService;
		this.bURL = servletContext.getContextPath();
	}





	@GetMapping
	@ResponseBody
	public void sveDestinacije(ServletResponse response) throws IOException {

		List<Destinacija> listaDestinacija = destinacijeService.findAll();
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
				+ "	<table border=\"2\">\r\n"
				+ "		<tr border=\"1\">\r\n"
				+ "			<th>Grad</th>\r\n"
				+ "			<th>Drzava</th>\r\n"
				+ "			<th>Kontinent</th>\r\n"
				+ "		</tr>\r\n");
				for(int i = 0; i < listaDestinacija.size(); i++) {
					out.write( "		<tr border=\"1\">\r\n"
							+ "			<td>" +listaDestinacija.get(i).getGrad()+"</td>\r\n"
							+ "			<td>"+listaDestinacija.get(i).getDrzava()+"</td>\r\n"
							+ "			<td>"+listaDestinacija.get(i).getKontinent()+"</td>\r\n"
							+ "			<td><form method=\"POST\" action=\"destinacije/izmeni\"><Input name=\"id\" type=\"hidden\" value=" + listaDestinacija.get(i).getId() + ">"
									+ "</input><input type=\"submit\" value=\"izmeni\"></input></form></td>\r\n"
							+ "			<td><form method=\"POST\" action=\"destinacije/delete\"><Input name=\"id\"  type=\"hidden\" value=" + listaDestinacija.get(i).getId() + ">"
									+ "</input><input type=\"submit\" value=\"obrisi\"></input></form></td>\r\n"
							+ "		</tr>\r\n");
				}
				out.write(
				 "	</table>\r\n"
				+ " <a href=\"destinacije/dodaj\"><button>Dodaj</button></a>"
				+ "\r\n"
				+ "</body>\r\n"
				+ "\r\n"
				+ "</html>\r\n"
				+ "");
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
		destinacijeService.save(destinacija);
		response.sendRedirect(bURL+ "/destinacije");
	}
	
	@PostMapping("/edit")
	public void editDestinacju(@ModelAttribute Destinacija destinacija, HttpServletResponse response) throws IOException {
		destinacijeService.update(destinacija);
		response.sendRedirect(bURL+ "/destinacije");
	}
	
	@PostMapping(value="/delete")
	public void delete(@RequestParam Long id, HttpServletResponse response) throws IOException {		
		Destinacija deleted = destinacijeService.delete(id);
		response.sendRedirect(bURL+"/destinacije");
	}

	@PostMapping("/izmeni")
	@ResponseBody
	public void izmeniDestinaciju(HttpServletResponse response, Long id) throws IOException {

		//Destinacije destinacije = (Destinacije) memorijaAplikacije.get(DestinacijeService.DESTINACIJE_KEY);

		Destinacija destinacija = destinacijeService.returnOne(id);

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
