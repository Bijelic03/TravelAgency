package com.ftn.TravelOrganisation.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.ftn.TravelOrganisation.bean.SecondConfiguration.ApplicationMemory;
import com.ftn.TravelOrganisation.controller.DestinacijeController;
import com.ftn.TravelOrganisation.model.Destinacija;
import com.ftn.TravelOrganisation.service.DestinacijeService;



@Service
public class DestinacijeServiceImpl implements DestinacijeService {
	
	public static final String DESTINACIJE_KEY = "destinacije";
	


}
