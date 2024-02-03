package com.ftn.TravelOrganisation.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@Configuration
public class SecondConfiguration implements WebMvcConfigurer{

	@Bean(name= {"memorijaAplikacije"}, 
			initMethod="init", destroyMethod="destroy")
	public MemorijaAplikacije getMemorijaAplikacije() {
		return new MemorijaAplikacije();
	}
	
	public class MemorijaAplikacije extends HashMap {
		
		@Override
		public String toString() {
			return "MemorijaAplikacije"+this.hashCode();
		}
		
		public void init() {
			//inicijalizacija

		}
		
		public void destroy() {
			//brisanje
			System.out.println("destroy method called");
		}
	}
	//definisanje bean messageSource koji Spring po defaultu koristi razrešavanje poruka
	@Bean(name= {"messageSource"})
    public ResourceBundleMessageSource messageSource() {

    	ResourceBundleMessageSource source = new ResourceBundleMessageSource();
       	//postavljanje diretorijuma koji sadrži poruke/prefiks naziva property datoteke
        source.setBasenames("messages/messages");
        //ukoliko se ne postoji poruka za kluč ispiši samo ključ
        source.setUseCodeAsDefaultMessage(true);
        source.setDefaultEncoding("UTF-8");
        //postavljanje default lokalizacije na nivou aplikacije
//        source.setDefaultLocale(Locale.forLanguageTag("sr"));
        source.setDefaultLocale(Locale.ENGLISH);
        return source;
    }
	
	//LocaleResolver određuju lokalizaciju na osnovu podataka iz HTTP zahteva
	//SessionLocaleResolver - određuje lokalizaciju i skladišti je u HttpSession klijenta
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    //postavljanje default lokalizacije
	    slr.setDefaultLocale(Locale.forLanguageTag("sr"));
	    return slr;
	}
	
	//Interceptor that allows for changing the current locale on every request, 
	//via a configurable request parameter (default parameter name: "locale").
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("locale");
	    return lci;
	}
	
	//Restracija presretača se postiže dodavanjem presretača u InterceptorRegistry i  
	//implementacijom WebMvcConfigurer interfejs
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean
    public ConnectionManager connectionManager() {
		return new ConnectionManager();
	}
	
	public final class ConnectionManager {
		
		@Value("${spring.datasource.driverClassName}")
		private String driverClassName;
		
		@Value("${spring.datasource.url}")
		private String url; 
		
		@Value("${spring.datasource.username}")
		private String username; 
		
		@Value("${spring.datasource.password}")
		private String password; 
		
		private ConnectionManager() {
			super();
		}
		
		private Connection conn = null;	
	    
		public Connection getConnection() {
			if (conn == null) {
				// ucitavanje MySQL drajvera
				try {
					Class.forName(driverClassName);
					// konekcija
					conn = DriverManager.getConnection(
							url, username, password);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return conn;
		}
		
		public void closeConnection(){
			try {
				if(conn != null)
					conn.close();
					conn=null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
