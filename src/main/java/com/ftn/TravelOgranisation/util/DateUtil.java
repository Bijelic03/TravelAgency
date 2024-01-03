package com.ftn.TravelOgranisation.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtil {

	public static LocalDate stringToDate(String datumString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return LocalDate.parse(datumString, formatter);
	}



	public static LocalDateTime stringToLocalDateTime(String localDateTimeString) {
		if (localDateTimeString == null || localDateTimeString.equalsIgnoreCase("null")) {
			return null;
		}

		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			return LocalDateTime.parse(localDateTimeString, formatter);
		} catch (DateTimeParseException e) {
			System.err.println("Nije moguće konvertovati string u LocalDateTime: " + localDateTimeString);
			e.printStackTrace();
			return null;
		}
	}

}
